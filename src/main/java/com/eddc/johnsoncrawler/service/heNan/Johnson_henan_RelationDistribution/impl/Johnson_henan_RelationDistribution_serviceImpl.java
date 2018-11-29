package com.eddc.johnsoncrawler.service.heNan.Johnson_henan_RelationDistribution.impl;


import com.eddc.johnsoncrawler.mapper.heNan.Johnson_henan_RelationDistribution_listMapper;
import com.eddc.johnsoncrawler.model.heNan.Johnson_henan_RelationDistribution_list;
import com.eddc.johnsoncrawler.service.heNan.Johnson_henan_RelationDistribution.Johnson_henan_RelationDistribution_service;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service(value = "Johnson_henan_RelationDistribution_service")
public class Johnson_henan_RelationDistribution_serviceImpl implements Johnson_henan_RelationDistribution_service {

    @Resource
    Johnson_henan_RelationDistribution_listMapper johnson_henan_relationDistribution_listMapper;

    @Override
    public int addJohnson_henan_RelationDistribution_list(Johnson_henan_RelationDistribution_list johnson_henan_relationDistribution_list) {
        return johnson_henan_relationDistribution_listMapper.insertSelective(johnson_henan_relationDistribution_list);
    }
}
