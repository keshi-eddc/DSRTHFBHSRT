package com.eddc.johnsoncrawler.service.shanXi.ShanxiDiscussPrice;

import com.eddc.johnsoncrawler.model.shanXi.ShanxiDiscussPrice;

public interface ShanxiDiscussPriceService {
    // 增
    int insertShanxiDiscussPrice(ShanxiDiscussPrice shanxiDiscussPrice);

    // 删
    // 改
    // 查
    ShanxiDiscussPrice selectNewOneDateByAccountForDiscussPrice(String account);
}
