package com.eddc.johnsoncrawler;

import com.eddc.johnsoncrawler.service.shanXi.Shanxicatalogue.ShanxicatalogueService;
import com.eddc.johnsoncrawler.service.shanXi.service.ShanXiCrawlerService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ShanxiTest {

    Logger logger = LogManager.getLogger(ShanxiTest.class.getName());

    @Autowired
    ShanXiCrawlerService shanXiCrawlerService;

    @Autowired
    ShanxicatalogueService shanxicatalogueService;

    //挂网目录
    @Test
    public void getShanxiDataTest() {
        Map<String, String> params = new HashMap<>();
        //申报企业名称
        params.put("comname", "强生");
        //登录后的cookie
        params.put("Cookie", "ASP.NET_SessionId=40v551gpm3xc034twlethn3e");
        //当前的账号名称
        params.put("account", "强生上海");
//        params.put("account", "迈思强");
        shanXiCrawlerService.getShanxiCatalogueData(params);
    }

    //议价信息
    @Test
    public void getShanxiDiscussPriceDataTest() {
        Map<String, String> params = new HashMap<>();
        //申报企业名称
        params.put("comname", "强生");
        //登录后的cookie
        params.put("Cookie", "ASP.NET_SessionId=40v551gpm3xc034twlethn3e");
        //当前的账号名称
        params.put("account", "强生上海");

        shanXiCrawlerService.getShanxiDiscussPriceData(params);
    }

}
