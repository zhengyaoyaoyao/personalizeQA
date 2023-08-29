package com.personalize.personalizeqa.server;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.personalize.personalizeqa.dto.DataSetListDTO;
import com.personalize.personalizeqa.entity.DataSet;
import com.personalize.personalizeqa.entity.R;
import com.personalize.personalizeqa.vo.DataListVO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface IDataSetService extends IService<DataSet> {
    R<DataSet> upload(String dataName, String dataSetUrl, String dataSetInfo,MultipartFile[] files);

    R<Page<DataSet>> findAll(Integer curPage,Integer maxPage,String keyword);

    R<DataSet> findById(String id);

    R<Boolean> deleteById(String id);

    R<DataSet> updateById(String id, String dataName, String dataUrl, String dataInfo);

    R<Boolean> uploadFilesById(String id, MultipartFile[] files);
    boolean updateFilesSize(String id,Long size);

    boolean isNotExist(String dataName);

    void download(HttpServletRequest request, HttpServletResponse response, String dataName,String id) throws Exception;

    DataListVO datasetList();
}
