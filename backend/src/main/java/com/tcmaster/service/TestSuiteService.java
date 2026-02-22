package com.tcmaster.service;

import com.tcmaster.dto.SuiteDtos;
import com.tcmaster.entity.Project;
import com.tcmaster.entity.TestSuite;
import com.tcmaster.repository.ProjectRepository;
import com.tcmaster.repository.TestSuiteRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class TestSuiteService {

    private final TestSuiteRepository testSuiteRepository;
    private final ProjectRepository projectRepository;

    public TestSuiteService(TestSuiteRepository testSuiteRepository, ProjectRepository projectRepository) {
        this.testSuiteRepository = testSuiteRepository;
        this.projectRepository = projectRepository;
    }

    public List<SuiteDtos.SuiteResponse> getSuites(Long projectId) {
        return testSuiteRepository.findByProjectIdOrderByOrderIndexAscIdAsc(projectId)
                .stream()
                .map(SuiteDtos.SuiteResponse::from)
                .toList();
    }

    public SuiteDtos.SuiteResponse create(Long projectId, SuiteDtos.SuiteRequest request) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Project not found"));
        TestSuite suite = new TestSuite();
        suite.setProject(project);
        suite.setParent(request.parentId() == null ? null : findSuite(request.parentId()));
        suite.setName(request.name());
        suite.setDescription(request.description());
        suite.setOrderIndex(request.orderIndex() == null ? 0 : request.orderIndex());
        return SuiteDtos.SuiteResponse.from(testSuiteRepository.save(suite));
    }

    public SuiteDtos.SuiteResponse update(Long id, SuiteDtos.SuiteRequest request) {
        TestSuite suite = findSuite(id);
        suite.setName(request.name());
        suite.setDescription(request.description());
        if (request.orderIndex() != null) {
            suite.setOrderIndex(request.orderIndex());
        }
        if (request.parentId() != null || suite.getParent() != null) {
            suite.setParent(request.parentId() == null ? null : findSuite(request.parentId()));
        }
        return SuiteDtos.SuiteResponse.from(testSuiteRepository.save(suite));
    }

    public SuiteDtos.SuiteResponse move(Long id, SuiteDtos.SuiteMoveRequest request) {
        TestSuite suite = findSuite(id);
        suite.setParent(request.parentId() == null ? null : findSuite(request.parentId()));
        if (request.orderIndex() != null) {
            suite.setOrderIndex(request.orderIndex());
        }
        return SuiteDtos.SuiteResponse.from(testSuiteRepository.save(suite));
    }

    public void delete(Long id) {
        testSuiteRepository.deleteById(id);
    }

    private TestSuite findSuite(Long id) {
        return testSuiteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Suite not found"));
    }
}
