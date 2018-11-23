package com.eddc.johnsoncrawler.service.service.impl;

import com.eddc.johnsoncrawler.service.service.CrawlerService;
import com.eddc.johnsoncrawler.utils.http.HttpClientUtil;
import com.eddc.johnsoncrawler.utils.http.request.MultiPartFormRequest;
import com.eddc.johnsoncrawler.utils.http.request.RequestMethod;
import com.eddc.johnsoncrawler.utils.http.response.Response;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

@Service(value = "CrawlerService")
public class CrawlerServiceImpl implements CrawlerService {

    Logger logger = LogManager.getLogger(CrawlerServiceImpl.class.getName());

    @Override
    public void getShanxiCatalogueData(Map<String, String> params) {
        String comname = "";
        //参数检查
        if (params.containsKey("comname")) {
            comname = params.get("comname");
            if (StringUtils.isNotBlank(comname)) {
                logger.info("- 开始获取陕西省 挂网目录 ：" + comname);
            } else {
                logger.error("！！！请检查参数 comname （申报企业名称）,不能为空。");
                return;
            }
        } else {
            logger.error("！！！请检查参数 comname （申报企业名称）");
            return;
        }
        try {
            comname = URLEncoder.encode(comname, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //参数检查
        String cookie = params.get("Cookie");

        Map<String, String> headParams = new HashMap<>();
        headParams.put("Cookie", cookie);

        int page = 0;
        int totalPage = 1;
        //每页显示数量
        int rows = 30;
        Boolean isNotLast = true;
        do {
            page++;
            try {
                /*
                 * 参数说明
                 * COMNAME 申报企业名称
                 * rows 每页显示数量
                 * page 页数
                 * */
                String url = "http://hcjy.sxsyxcg.com" +
                        "/HSNN/CM/Trade/Web/Controller/TradeCatalogController/QueryTradeCatalogGPart.HSNN?" +
                        "&COMNAME=" + comname +
                        "&PAGEINFO=1" +
                        "&_search=false" +
                        "&nd=" +
                        "&rows=" + rows +
                        "&page=" + page +
                        "&sidx=" +
                        "&sord=asc";
                logger.info("- 即将访问第：" + page + " 页,url:" + url);
                String content = getPostContent(url, headParams, null);
                System.out.println("content：" + content);


                if (page >= totalPage) {
                    isNotLast = false;
                }
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("！！！翻页是出现异常");
            }
        } while (isNotLast);
        logger.info("- 抓取完成。共抓了：" + page + " 页");
    }


    /**
     * @param content
     * @methodName extraShanxiCatalogueData
     * @description 解析陕西省 挂网目录返回的json
     * @author keshi
     * @date 2018年11月23日 15:28
     */
    public void extraShanxiCatalogueData(String content) {
        //检验要解析的内容
        if (StringUtils.isNotBlank(content)) {
            //检验是否是正常页面
            if (content.contains("PROCURECATALOGID")) {
//                JSONArray jsonArray = JSONArray.parse(content);

            } else {
                logger.error("！！！异常页面，不进行解析");
            }
        } else {
            logger.error("！！！要解析的页面为空，不进行解析。请检查");
        }
    }

    public String getPostContent(String url, Map<String, String> headParams, Map<String, Object> fromParams) {
//        logger.info("- 获得页面");
        String content = "";
        if (StringUtils.isNotEmpty(url)) {
            try {
                MultiPartFormRequest request = new MultiPartFormRequest(url, RequestMethod.POST);
//                Request request = new Request(url, RequestMethod.GET);
                request.addHeaders(headParams);
                request.addParts(fromParams);
                Response response = HttpClientUtil.doRequest(request);
                int code = response.getCode();
                if (code == 200) {
                    content = response.getResponseText();
                    if (StringUtils.isNotBlank(content)) {
                        logger.info("- 页面正常");
                    } else {
                        logger.error("返回空");
                    }
                } else {
                    logger.error("请求错误：" + code);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return content;
    }
}
