package com.personalize.personalizeqa.server;

import com.baomidou.mybatisplus.extension.service.IService;
import com.personalize.personalizeqa.dto.DataSetListDTO;
import com.personalize.personalizeqa.entity.DataSet;
import com.personalize.personalizeqa.entity.R;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IDataSetService extends IService<DataSet> {
    R<DataSet> upload(String dataName, String dataSetUrl, String dataSetInfo,MultipartFile[] files);

    R<DataSetListDTO> findAll();

    R<DataSet> findById(String id);
}
