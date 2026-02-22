package com.tcmaster.service;

import com.tcmaster.dto.CodeDtos;
import com.tcmaster.entity.TestCase;
import com.tcmaster.entity.TestCode;
import com.tcmaster.repository.TestCaseRepository;
import com.tcmaster.repository.TestCodeRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class TestCodeService {

    private final TestCodeRepository testCodeRepository;
    private final TestCaseRepository testCaseRepository;

    public TestCodeService(TestCodeRepository testCodeRepository, TestCaseRepository testCaseRepository) {
        this.testCodeRepository = testCodeRepository;
        this.testCaseRepository = testCaseRepository;
    }

    public CodeDtos.CodeResponse save(Long caseId, CodeDtos.CodeSaveRequest request) {
        TestCase testCase = testCaseRepository.findById(caseId)
                .orElseThrow(() -> new IllegalArgumentException("Test case not found"));

        int nextVersion = testCodeRepository.findTopByTestCaseIdAndLanguageOrderByVersionDesc(caseId, request.language())
                .map(code -> code.getVersion() + 1)
                .orElse(1);

        TestCode code = new TestCode();
        code.setTestCase(testCase);
        code.setLanguage(request.language());
        code.setCodeContent(request.codeContent());
        code.setVersion(nextVersion);
        code.setUpdatedAt(LocalDateTime.now());

        return CodeDtos.CodeResponse.from(testCodeRepository.save(code));
    }

    public List<CodeDtos.CodeResponse> getLatestCodes(Long caseId) {
        return testCodeRepository.findByTestCaseIdOrderByUpdatedAtDesc(caseId)
                .stream()
                .map(CodeDtos.CodeResponse::from)
                .toList();
    }
}
