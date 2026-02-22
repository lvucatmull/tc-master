package com.tcmaster.controller;

import com.tcmaster.dto.CaseDtos;
import com.tcmaster.dto.CodeDtos;
import com.tcmaster.service.TestCaseService;
import com.tcmaster.service.TestCodeService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.*;

@RestController
public class TestCaseController {

    private final TestCaseService testCaseService;
    private final TestCodeService testCodeService;

    public TestCaseController(TestCaseService testCaseService, TestCodeService testCodeService) {
        this.testCaseService = testCaseService;
        this.testCodeService = testCodeService;
    }

    @GetMapping("/api/suites/{id}/cases")
    public List<CaseDtos.CaseResponse> getCases(@PathVariable Long id) {
        return testCaseService.getBySuite(id);
    }

    @PostMapping("/api/suites/{id}/cases")
    public CaseDtos.CaseResponse createCase(@PathVariable Long id, @Valid @RequestBody CaseDtos.CaseRequest request) {
        return testCaseService.create(id, request);
    }

    @GetMapping("/api/cases/{id}")
    public CaseDtos.CaseResponse getCase(@PathVariable Long id) {
        return testCaseService.getById(id);
    }

    @PutMapping("/api/cases/{id}")
    public CaseDtos.CaseResponse updateCase(@PathVariable Long id, @Valid @RequestBody CaseDtos.CaseRequest request) {
        return testCaseService.update(id, request);
    }

    @DeleteMapping("/api/cases/{id}")
    public void deleteCase(@PathVariable Long id) {
        testCaseService.delete(id);
    }

    @PutMapping("/api/cases/{id}/code")
    public CodeDtos.CodeResponse saveCode(@PathVariable Long id, @Valid @RequestBody CodeDtos.CodeSaveRequest request) {
        return testCodeService.save(id, request);
    }

    @GetMapping("/api/cases/{id}/code")
    public List<CodeDtos.CodeResponse> getCodes(@PathVariable Long id) {
        return testCodeService.getLatestCodes(id);
    }
}
