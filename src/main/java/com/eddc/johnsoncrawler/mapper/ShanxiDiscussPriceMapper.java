package com.eddc.johnsoncrawler.mapper;

import com.eddc.johnsoncrawler.model.ShanxiDiscussPrice;

public interface ShanxiDiscussPriceMapper {
    int insert(ShanxiDiscussPrice record);

    int insertSelective(ShanxiDiscussPrice record);

    ShanxiDiscussPrice selectNewDataByAccountForiDiscussPrice(String accout);
}