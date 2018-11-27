package com.eddc.johnsoncrawler.mapper;

import com.eddc.johnsoncrawler.model.Shanxicatalogue;

public interface ShanxicatalogueMapper {
    int insert(Shanxicatalogue record);

    int insertSelective(Shanxicatalogue record);

    Shanxicatalogue selectNewData();

    Shanxicatalogue selectNewDataByAccount(String account);

}