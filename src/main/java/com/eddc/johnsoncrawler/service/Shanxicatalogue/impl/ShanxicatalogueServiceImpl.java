package com.eddc.johnsoncrawler.service.Shanxicatalogue.impl;

import com.eddc.johnsoncrawler.mapper.ShanxicatalogueMapper;
import com.eddc.johnsoncrawler.model.Shanxicatalogue;
import com.eddc.johnsoncrawler.service.Shanxicatalogue.ShanxicatalogueService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service(value = "ShanxicatalogueService")
public class ShanxicatalogueServiceImpl implements ShanxicatalogueService {

    @Resource
    ShanxicatalogueMapper shanxicatalogueMapper;

    @Override
    public int insertShanxicatalogue(Shanxicatalogue shanxicatalogue) {
        return shanxicatalogueMapper.insertSelective(shanxicatalogue);
    }

    @Override
    public Shanxicatalogue selectNewOneDate() {
        return shanxicatalogueMapper.selectNewData();
    }

    @Override
    public Shanxicatalogue selectNewOneDateByAccount(String account) {
        return shanxicatalogueMapper.selectNewDataByAccount(account);
    }
}
