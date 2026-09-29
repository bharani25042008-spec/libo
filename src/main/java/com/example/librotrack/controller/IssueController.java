package com.example.librotrack.controller;

import com.example.librotrack.entity.IssueRecord;
import com.example.librotrack.service.IssueService;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class IssueController {

    private final IssueService issueService;

    public IssueController(IssueService issueService) {
        this.issueService = issueService;
    }

    @PostMapping("/issues")
    public ResponseEntity<IssueRecord> issueBook(@RequestBody Map<String, Long> request) {
        Long bookId = request.get("bookId");
        Long studentId = request.get("studentId");

        if (bookId == null || studentId == null) {
            throw new IllegalArgumentException("bookId and studentId are required");
        }

        IssueRecord issueRecord = issueService.issueBook(bookId, studentId);
        return new ResponseEntity<>(issueRecord, HttpStatus.CREATED);
    }

    @PostMapping("/issues/{id}/return")
    public IssueRecord returnBook(@PathVariable Long id) {
        return issueService.returnBook(id);
    }

    @GetMapping("/issues/student/{studentId}")
    public List<IssueRecord> getStudentActiveIssues(@PathVariable Long studentId) {
        return issueService.getStudentActiveIssues(studentId);
    }

    @GetMapping("/issues/active")
    public List<IssueRecord> getActiveIssues() {
        return issueService.getActiveIssues();
    }
}
