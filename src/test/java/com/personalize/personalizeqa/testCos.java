package com.personalize.personalizeqa;

import com.personalize.personalizeqa.storage.TencentCosAutoConfigure;
import com.qcloud.cos.COSClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Test;
@SpringBootTest
public class testCos {
    @Autowired
    private TencentCosAutoConfigure.TencentServiceImpl tencentService;
    @Test
    public void CosClient(){
//        COSClient client = tencentService.buildClient();
//        boolean b = client.doesBucketExist("personalized-loan-1422695558");
//        System.out.println(b);
    }
}
