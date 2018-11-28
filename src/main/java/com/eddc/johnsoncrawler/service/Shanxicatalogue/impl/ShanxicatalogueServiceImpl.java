package com.eddc.johnsoncrawler.service.Shanxicatalogue.impl;

import com.eddc.johnsoncrawler.mapper.ShanxiCatalogueMapper;
import com.eddc.johnsoncrawler.model.ShanxiCatalogue;
import com.eddc.johnsoncrawler.service.Shanxicatalogue.ShanxicatalogueService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service(value = "ShanxicatalogueService")
public class ShanxicatalogueServiceImpl implements ShanxicatalogueService {

    @Resource
    ShanxiCatalogueMapper shanxicatalogueMapper;

    @Override
    public int insertShanxicatalogue(ShanxiCatalogue shanxicatalogue) {
        return shanxicatalogueMapper.insertSelective(shanxicatalogue);
    }

    @Override
    public ShanxiCatalogue selectNewOneDate() {
        return shanxicatalogueMapper.selectNewData();
    }

    @Override
    public ShanxiCatalogue selectNewOneDateByAccount(String account) {
        return shanxicatalogueMapper.selectNewDataByAccount(account);
    }
}
