package com.eddc.johnsoncrawler.mapper;

import com.eddc.johnsoncrawler.model.ShanxiCatalogue;

public interface ShanxiCatalogueMapper {
    int insert(ShanxiCatalogue record);

    int insertSelective(ShanxiCatalogue record);

    ShanxiCatalogue selectNewData();


    ShanxiCatalogue selectNewDataByAccount(String account);

}