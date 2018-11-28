package com.eddc.johnsoncrawler.service.ShanxiDiscussPrice;

import com.eddc.johnsoncrawler.model.ShanxiDiscussPrice;

public interface ShanxiDiscussPriceService {
    // 增
    int insertShanxiDiscussPrice(ShanxiDiscussPrice shanxiDiscussPrice);

    // 删
    // 改
    // 查
    ShanxiDiscussPrice selectNewOneDateByAccountForDiscussPrice(String account);
}
