package com.example.librotrack.service;

import com.example.librotrack.entity.Book;
import com.example.librotrack.exception.ResourceNotFoundException;
import com.example.librotrack.repository.BookRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public Book createBook(Book book) {
        book.setAvailableCopies(book.getTotalCopies());
        return bookRepository.save(book);
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Book getBookById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));
    }

    public Book updateBook(Long id, Book bookDetails) {
        Book existingBook = getBookById(id);

        existingBook.setTitle(bookDetails.getTitle());
        existingBook.setAuthor(bookDetails.getAuthor());
        existingBook.setIsbn(bookDetails.getIsbn());
        existingBook.setCategory(bookDetails.getCategory());
        existingBook.setTotalCopies(bookDetails.getTotalCopies());

        if (bookDetails.getAvailableCopies() != null) {
            existingBook.setAvailableCopies(bookDetails.getAvailableCopies());
        } else {
            existingBook.setAvailableCopies(existingBook.getTotalCopies());
        }

        return bookRepository.save(existingBook);
    }

    public void deleteBook(Long id) {
        Book book = getBookById(id);
        bookRepository.delete(book);
    }

    public List<Book> searchBooks(String title, String author, String category) {
        if (title != null && !title.isBlank()) {
            return bookRepository.findByTitleContainingIgnoreCase(title);
        }
        if (author != null && !author.isBlank()) {
            return bookRepository.findByAuthorContainingIgnoreCase(author);
        }
        if (category != null && !category.isBlank()) {
            return bookRepository.findByCategoryContainingIgnoreCase(category);
        }
        return getAllBooks();
    }
}
