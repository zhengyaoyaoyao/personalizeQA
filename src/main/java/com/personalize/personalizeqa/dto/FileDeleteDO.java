package com.personalize.personalizeqa.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FileDeleteDO {
    private String id;
    private String fileName;
    private String relativePath;
    private String url;
    private String folder;
}
