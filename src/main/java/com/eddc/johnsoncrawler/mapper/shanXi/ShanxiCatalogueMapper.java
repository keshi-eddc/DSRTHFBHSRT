package com.eddc.johnsoncrawler.mapper.shanXi;

import com.eddc.johnsoncrawler.model.shanXi.ShanxiCatalogue;

public interface ShanxiCatalogueMapper {
    int insert(ShanxiCatalogue record);

    int insertSelective(ShanxiCatalogue record);

    ShanxiCatalogue selectNewData();

    ShanxiCatalogue selectNewDataByAccount(String account);
}