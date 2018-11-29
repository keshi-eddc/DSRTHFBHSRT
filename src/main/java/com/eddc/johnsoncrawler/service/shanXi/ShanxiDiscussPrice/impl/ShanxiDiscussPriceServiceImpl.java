package com.eddc.johnsoncrawler.service.shanXi.ShanxiDiscussPrice.impl;

import com.eddc.johnsoncrawler.mapper.shanXi.ShanxiDiscussPriceMapper;
import com.eddc.johnsoncrawler.model.shanXi.ShanxiDiscussPrice;
import com.eddc.johnsoncrawler.service.shanXi.ShanxiDiscussPrice.ShanxiDiscussPriceService;
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
