package com.tcmaster.repository;

import com.tcmaster.entity.TestCode;
import com.tcmaster.enums.TestCodeLanguage;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestCodeRepository extends JpaRepository<TestCode, Long> {
    List<TestCode> findByTestCaseIdOrderByUpdatedAtDesc(Long testCaseId);
    Optional<TestCode> findTopByTestCaseIdAndLanguageOrderByVersionDesc(Long testCaseId, TestCodeLanguage language);
}
