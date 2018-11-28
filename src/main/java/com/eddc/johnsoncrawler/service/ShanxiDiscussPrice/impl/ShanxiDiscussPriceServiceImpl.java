package com.eddc.johnsoncrawler.service.ShanxiDiscussPrice.impl;

import com.eddc.johnsoncrawler.mapper.ShanxiDiscussPriceMapper;
import com.eddc.johnsoncrawler.model.ShanxiDiscussPrice;
import com.eddc.johnsoncrawler.service.ShanxiDiscussPrice.ShanxiDiscussPriceService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service(value = "ShanxiDiscussPriceService")
public class ShanxiDiscussPriceServiceImpl implements ShanxiDiscussPriceService {

    @Resource
    ShanxiDiscussPriceMapper shanxiDiscussPriceMapper;

    @Override
    public int insertShanxiDiscussPrice(ShanxiDiscussPrice shanxiDiscussPrice) {
        return shanxiDiscussPriceMapper.insertSelective(shanxiDiscussPrice);
    }

    @Override
    public ShanxiDiscussPrice selectNewOneDateByAccountForDiscussPrice(String account) {
        return shanxiDiscussPriceMapper.selectNewDataByAccountForiDiscussPrice(account);
    }
}
