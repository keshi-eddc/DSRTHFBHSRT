package com.eddc.johnsoncrawler.mapper.shanXi;

import com.eddc.johnsoncrawler.model.shanXi.ShanxiDiscussPrice;

public interface ShanxiDiscussPriceMapper {
    int insert(ShanxiDiscussPrice record);

    int insertSelective(ShanxiDiscussPrice record);

    ShanxiDiscussPrice selectNewDataByAccountForiDiscussPrice(String accout);

}