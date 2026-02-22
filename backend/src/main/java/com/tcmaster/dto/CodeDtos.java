package com.tcmaster.dto;

import com.tcmaster.entity.TestCode;
import com.tcmaster.enums.TestCodeLanguage;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CodeDtos {

    public record CodeSaveRequest(
            @NotNull TestCodeLanguage language,
            @NotBlank String codeContent
    ) {}

    public record CodeResponse(
            Long id,
            Long testCaseId,
            TestCodeLanguage language,
            String codeContent,
            Integer version
    ) {
        public static CodeResponse from(TestCode code) {
            return new CodeResponse(
                    code.getId(),
                    code.getTestCase().getId(),
                    code.getLanguage(),
                    code.getCodeContent(),
                    code.getVersion());
        }
    }
}
