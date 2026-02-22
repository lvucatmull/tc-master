package com.tcmaster.repository;

import com.tcmaster.entity.TestSuite;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestSuiteRepository extends JpaRepository<TestSuite, Long> {
    List<TestSuite> findByProjectIdOrderByOrderIndexAscIdAsc(Long projectId);
}
