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
        //抓取批次
        params.put("sync_number", "2018");
        crawlerService.getShanxiCatalogueData(params);
    }


}
