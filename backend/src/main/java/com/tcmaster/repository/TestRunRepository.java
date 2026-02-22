package com.tcmaster.repository;

import com.tcmaster.entity.TestRun;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestRunRepository extends JpaRepository<TestRun, Long> {
    List<TestRun> findByProjectIdOrderByIdDesc(Long projectId);
}
