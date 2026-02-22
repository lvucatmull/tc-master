package com.tcmaster.repository;

import com.tcmaster.entity.TestCase;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestCaseRepository extends JpaRepository<TestCase, Long> {
    List<TestCase> findBySuiteIdOrderByIdAsc(Long suiteId);
    long countBySuiteProjectId(Long projectId);
    Optional<TestCase> findByTcId(String tcId);
}
