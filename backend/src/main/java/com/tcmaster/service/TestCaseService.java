package com.tcmaster.service;

import com.tcmaster.dto.CaseDtos;
import com.tcmaster.entity.Project;
import com.tcmaster.entity.TestCase;
import com.tcmaster.entity.TestSuite;
import com.tcmaster.enums.TestCasePriority;
import com.tcmaster.enums.TestCaseStatus;
import com.tcmaster.repository.TestCaseRepository;
import com.tcmaster.repository.TestSuiteRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class TestCaseService {

    private final TestCaseRepository testCaseRepository;
    private final TestSuiteRepository testSuiteRepository;

    public TestCaseService(TestCaseRepository testCaseRepository, TestSuiteRepository testSuiteRepository) {
        this.testCaseRepository = testCaseRepository;
        this.testSuiteRepository = testSuiteRepository;
    }

    public List<CaseDtos.CaseResponse> getBySuite(Long suiteId) {
        return testCaseRepository.findBySuiteIdOrderByIdAsc(suiteId).stream().map(CaseDtos.CaseResponse::from).toList();
    }

    public CaseDtos.CaseResponse create(Long suiteId, CaseDtos.CaseRequest request) {
        TestSuite suite = testSuiteRepository.findById(suiteId)
                .orElseThrow(() -> new IllegalArgumentException("Suite not found"));
        TestCase testCase = new TestCase();
        applyRequest(testCase, request);
        testCase.setSuite(suite);
        testCase.setTcId(generateTcId(suite.getProject()));
        return CaseDtos.CaseResponse.from(testCaseRepository.save(testCase));
    }

    public CaseDtos.CaseResponse getById(Long caseId) {
        return CaseDtos.CaseResponse.from(findCase(caseId));
    }

    public CaseDtos.CaseResponse update(Long caseId, CaseDtos.CaseRequest request) {
        TestCase testCase = findCase(caseId);
        applyRequest(testCase, request);
        return CaseDtos.CaseResponse.from(testCaseRepository.save(testCase));
    }

    public void delete(Long caseId) {
        testCaseRepository.deleteById(caseId);
    }

    private void applyRequest(TestCase testCase, CaseDtos.CaseRequest request) {
        testCase.setTitle(request.title());
        testCase.setDescription(request.description());
        testCase.setPrecondition(request.precondition());
        testCase.setSteps(request.steps());
        testCase.setExpectedResult(request.expectedResult());
        testCase.setPriority(request.priority() == null ? TestCasePriority.MEDIUM : request.priority());
        testCase.setCategory(request.category());
        testCase.setTags(request.tags());
        testCase.setStatus(request.status() == null ? TestCaseStatus.ACTIVE : request.status());
    }

    private TestCase findCase(Long caseId) {
        return testCaseRepository.findById(caseId)
                .orElseThrow(() -> new IllegalArgumentException("Test case not found"));
    }

    private String generateTcId(Project project) {
        long sequence = testCaseRepository.countBySuiteProjectId(project.getId()) + 1;
        return "%s-%d".formatted(project.getCode(), sequence);
    }
}
