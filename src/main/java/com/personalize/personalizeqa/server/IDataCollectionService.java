package com.personalize.personalizeqa.server;

import com.personalize.personalizeqa.entity.R;

public interface IDataCollectionService {
    R<String> pingSource(String api);
    R<String> callSource(String id,String api,String mongodbSet);
}
