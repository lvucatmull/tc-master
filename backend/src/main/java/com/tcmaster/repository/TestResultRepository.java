package com.tcmaster.repository;

import com.tcmaster.entity.TestResult;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestResultRepository extends JpaRepository<TestResult, Long> {
    List<TestResult> findByTestRunIdOrderByIdAsc(Long testRunId);
}
