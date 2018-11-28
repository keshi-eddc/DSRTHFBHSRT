package com.eddc.johnsoncrawler.service.Shanxicatalogue;

import com.eddc.johnsoncrawler.model.ShanxiCatalogue;

public interface ShanxicatalogueService {

    //增
    int insertShanxicatalogue(ShanxiCatalogue shanxicatalogue);

    //删

    //改

    //查

    ShanxiCatalogue selectNewOneDate();

    ShanxiCatalogue selectNewOneDateByAccount(String account);


}
