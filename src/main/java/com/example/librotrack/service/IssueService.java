package com.example.librotrack.service;

import com.example.librotrack.entity.Book;
import com.example.librotrack.entity.IssueRecord;
import com.example.librotrack.entity.Student;
import com.example.librotrack.exception.AlreadyReturnedException;
import com.example.librotrack.exception.NoCopiesAvailableException;
import com.example.librotrack.exception.ResourceNotFoundException;
import com.example.librotrack.repository.BookRepository;
import com.example.librotrack.repository.IssueRecordRepository;
import com.example.librotrack.repository.StudentRepository;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class IssueService {

    private static final double FINE_PER_DAY = 10;

    private final IssueRecordRepository issueRecordRepository;
    private final BookRepository bookRepository;
    private final StudentRepository studentRepository;

    public IssueService(IssueRecordRepository issueRecordRepository,
                       BookRepository bookRepository,
                       StudentRepository studentRepository) {
        this.issueRecordRepository = issueRecordRepository;
        this.bookRepository = bookRepository;
        this.studentRepository = studentRepository;
    }

    public IssueRecord issueBook(Long bookId, Long studentId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + bookId));

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + studentId));

        if (book.getAvailableCopies() <= 0) {
            throw new NoCopiesAvailableException("No copies of this book are currently available.");
        }

        IssueRecord issueRecord = new IssueRecord();
        issueRecord.setBook(book);
        issueRecord.setStudent(student);

        LocalDate issueDate = LocalDate.now();
        LocalDate dueDate = issueDate.plusDays(14);

        issueRecord.setIssueDate(issueDate);
        issueRecord.setDueDate(dueDate);
        issueRecord.setReturnDate(null);
        issueRecord.setFine(0);

        book.setAvailableCopies(book.getAvailableCopies() - 1);
        bookRepository.save(book);

        return issueRecordRepository.save(issueRecord);
    }

    public IssueRecord returnBook(Long issueId) {
        IssueRecord issueRecord = issueRecordRepository.findById(issueId)
                .orElseThrow(() -> new ResourceNotFoundException("Issue record not found with id: " + issueId));

        if (issueRecord.getReturnDate() != null) {
            throw new AlreadyReturnedException("This book has already been returned.");
        }

        LocalDate returnDate = LocalDate.now();
        issueRecord.setReturnDate(returnDate);

        long overdueDays = 0;
        if (returnDate.isAfter(issueRecord.getDueDate())) {
            overdueDays = ChronoUnit.DAYS.between(issueRecord.getDueDate(), returnDate);
        }

        double fine = overdueDays * FINE_PER_DAY;
        issueRecord.setFine(fine);

        Book book = issueRecord.getBook();
        book.setAvailableCopies(book.getAvailableCopies() + 1);
        bookRepository.save(book);

        return issueRecordRepository.save(issueRecord);
    }

    public List<IssueRecord> getStudentActiveIssues(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + studentId));
        return issueRecordRepository.findByStudentAndReturnDateIsNull(student);
    }

    public List<IssueRecord> getActiveIssues() {
        return issueRecordRepository.findByReturnDateIsNull();
    }
}
