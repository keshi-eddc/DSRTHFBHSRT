package com.eddc.johnsoncrawler.service.service;

import java.util.Map;

public interface CrawlerService {
    /*****陕西数据抓取*****/
    /*挂网目录*/
    void getShanxiCatalogueData(Map<String, String> params);

    /*抓取议价信息，采购管理下，价格确认，确认状态选择未确认，查询出未确认的产品*/
    void getShanxiDiscussPriceData(Map<String, String> params);

}
