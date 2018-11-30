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

    /*
     * 陕西省数据抓取说明
     * 前提： ie浏览器登录http://hcjy.sxsyxcg.com/ ，获得登录后的cookie
     * 抓取： 1.挂网目录2.议价信息
     * 频率： 每周三次（周一、周三、周五上午10点）
     * */

    //1.挂网目录
    @Test
    public void getShanxiCatalogueDataTest() {
        Map<String, String> params = new HashMap<>();
        //申报企业名称
        params.put("comname", "强生");
        //登录后的cookie
        params.put("Cookie", "ASP.NET_SessionId=vgqcebgctqsikf5ifnm05bul");
        //当前的账号名称
        params.put("account", "强生上海");
//        params.put("account", "迈思强");
        shanXiCrawlerService.getShanxiCatalogueData(params);
    }

    //2.议价信息
    @Test
    public void getShanxiDiscussPriceDataTest() {
        Map<String, String> params = new HashMap<>();
        //申报企业名称
        params.put("comname", "强生");
        //登录后的cookie
        params.put("Cookie", "ASP.NET_SessionId=vgqcebgctqsikf5ifnm05bul");
        //当前的账号名称
        params.put("account", "强生上海");

        shanXiCrawlerService.getShanxiDiscussPriceData(params);
    }

}
