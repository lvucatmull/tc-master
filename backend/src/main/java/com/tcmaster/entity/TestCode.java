package com.tcmaster.entity;

import com.tcmaster.enums.TestCodeLanguage;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "test_codes")
public class TestCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "test_case_id", nullable = false)
    private TestCase testCase;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TestCodeLanguage language;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String codeContent;

    @Column(nullable = false)
    private Integer version;

    @Column(nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    public Long getId() { return id; }
    public TestCase getTestCase() { return testCase; }
    public void setTestCase(TestCase testCase) { this.testCase = testCase; }
    public TestCodeLanguage getLanguage() { return language; }
    public void setLanguage(TestCodeLanguage language) { this.language = language; }
    public String getCodeContent() { return codeContent; }
    public void setCodeContent(String codeContent) { this.codeContent = codeContent; }
    public Integer getVersion() { return version; }
    public void setVersion(Integer version) { this.version = version; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
