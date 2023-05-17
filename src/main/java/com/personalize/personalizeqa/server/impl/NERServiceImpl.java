package com.personalize.personalizeqa.server.impl;

import com.personalize.personalizeqa.modelNer.NERClient;
import com.personalize.personalizeqa.server.NERService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NERServiceImpl implements NERService {
    @Autowired
    private NERClient nerClient;
    @Override
    public String qa(String text) {
        String data = nerClient.getNERPost(text);
        return data;
    }
}
