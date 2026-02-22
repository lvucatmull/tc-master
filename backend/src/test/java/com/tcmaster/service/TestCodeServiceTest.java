package com.tcmaster.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.tcmaster.dto.CodeDtos;
import com.tcmaster.entity.TestCase;
import com.tcmaster.entity.TestCode;
import com.tcmaster.enums.TestCodeLanguage;
import com.tcmaster.repository.TestCaseRepository;
import com.tcmaster.repository.TestCodeRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TestCodeServiceTest {

    @Mock
    private TestCodeRepository testCodeRepository;
    @Mock
    private TestCaseRepository testCaseRepository;

    private TestCodeService testCodeService;

    @BeforeEach
    void setUp() {
        testCodeService = new TestCodeService(testCodeRepository, testCaseRepository);
    }

    @Test
    void save_incrementsVersionFromLatest() {
        TestCase testCase = new TestCase();
        when(testCaseRepository.findById(1L)).thenReturn(Optional.of(testCase));

        TestCode latest = new TestCode();
        latest.setVersion(3);
        when(testCodeRepository.findTopByTestCaseIdAndLanguageOrderByVersionDesc(1L, TestCodeLanguage.TS))
                .thenReturn(Optional.of(latest));

        when(testCodeRepository.save(any(TestCode.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CodeDtos.CodeResponse response = testCodeService.save(
                1L,
                new CodeDtos.CodeSaveRequest(TestCodeLanguage.TS, "console.log('test')")
        );

        assertEquals(4, response.version());
        assertEquals(TestCodeLanguage.TS, response.language());
    }
}
