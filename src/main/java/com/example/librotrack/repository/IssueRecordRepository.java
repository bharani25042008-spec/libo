package com.example.librotrack.repository;

import com.example.librotrack.entity.IssueRecord;
import com.example.librotrack.entity.Student;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IssueRecordRepository extends JpaRepository<IssueRecord, Long> {

    List<IssueRecord> findByStudentAndReturnDateIsNull(Student student);

    List<IssueRecord> findByReturnDateIsNull();
}
