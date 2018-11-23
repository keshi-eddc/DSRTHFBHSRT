package com.eddc.johnsoncrawler;

import com.eddc.johnsoncrawler.service.service.CrawlerService;
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
    CrawlerService crawlerService;

    @Test
    public void getShanxiDataTest() {
        Map<String, String> params = new HashMap<>();
        //申报企业名称
        params.put("comname", "强生");
        //登录后的cookie
        params.put("Cookie", "ASP.NET_SessionId=2bmdrmyiqcwofsn03td05jx0");

        crawlerService.getShanxiCatalogueData(params);
    }


}