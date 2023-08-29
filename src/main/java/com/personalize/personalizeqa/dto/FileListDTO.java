package com.personalize.personalizeqa.dto;

import com.personalize.personalizeqa.entity.File;
import lombok.Data;

import java.util.List;

@Data
public class FileListDTO {
    private List<File> datas;
}
