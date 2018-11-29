package com.eddc.johnsoncrawler.service.heNan.service.impl;


import com.eddc.johnsoncrawler.model.heNan.Johnson_henan_RelationDistribution_list;
import com.eddc.johnsoncrawler.service.heNan.Johnson_henan_RelationDistribution.Johnson_henan_RelationDistribution_service;
import com.eddc.johnsoncrawler.service.heNan.service.HeNanCrawlerService;
import com.eddc.johnsoncrawler.utils.http.HttpClientUtil;
import com.eddc.johnsoncrawler.utils.http.request.MultiPartFormRequest;
import com.eddc.johnsoncrawler.utils.http.request.RequestMethod;
import com.eddc.johnsoncrawler.utils.http.response.Response;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Map;

@Service(value = "HeNanCrawlerService")
public class HeNanCrawlerServiceImpl implements HeNanCrawlerService {
    Logger logger = LogManager.getLogger(HeNanCrawlerServiceImpl.class.getName());

    @Autowired
    private Johnson_henan_RelationDistribution_service johnson_henan_relationDistribution_service;


    @Override
    public String getPostContent(String url, Map<String, String> headParms) {
        logger.info("- 获得页面");
        String content = "";
        if (StringUtils.isNotEmpty(url)) {
            try {
                MultiPartFormRequest request = new MultiPartFormRequest(url, RequestMethod.POST);
//                Request request = new Request(url, RequestMethod.GET);
                request.addHeaders(headParms);
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


    @Override
    public void extraOnePageContent(String content, String account, String isInsert) {
        logger.info("- 解析页面");
        //检查页面是否为空
        if (StringUtils.isNotEmpty(content)) {
            //检查页面是否正常
            if (content.contains("mainlist")) {
                try {

                    Document doc = Jsoup.parse(content);
                    logger.info("网页标题：" + doc.title());
                    //页码
                    Elements pageGuideseles = doc.select("div#ctl00_ContentPlaceHolder1_AspNetPager1 > div:nth-child(1)");
                    String pageGuideStr = "";
                    if (pageGuideseles != null && pageGuideseles.size() > 0) {
                        pageGuideStr = pageGuideseles.text();
                    }

                    Elements lineseles = doc.select("div> table.mainlist > tbody > tr");
                    if (lineseles != null && lineseles.size() > 0) {
                        logger.info("- 表有：" + lineseles.size() + " 行");
                        //表头
                        Elements headLineles = lineseles.get(0).select("th");
                        int project_name_position = 0;
                        int product_code_position = 0;
                        int category_position = 0;
                        int category_one_position = 0;
                        int category_two_position = 0;
                        int directory_name_position = 0;
                        int registration_certificate_product_name_position = 0;
                        int brand_position = 0;
                        int specifications_position = 0;
                        int product_model_position = 0;
                        int unit_position = 0;
                        int production_enterprise_position = 0;
                        int remarks_position = 0;
                        int purchase_price_limit_position = 0;
                        int bid_winner_position = 0;
                        int confirm_status_position = 0;
                        int agent_position = 0;
                        int no_deal_position = 0;
                        if (headLineles != null && headLineles.size() > 0) {
                            for (int headi = 0; headi < headLineles.size(); headi++) {
                                Element headele = headLineles.get(headi);
                                String headStr = headele.text();
//                                logger.info("表头：" + headStr);
                                if (headStr.equals("项目名称")) {
                                    project_name_position = headi;
                                } else if (headStr.equals("产品代码")) {
                                    product_code_position = headi;
                                } else if (headStr.equals("大类")) {
                                    category_position = headi;
                                } else if (headStr.equals("一级分类")) {
                                    category_one_position = headi;
                                } else if (headStr.equals("二级分类")) {
                                    category_two_position = headi;
                                } else if (headStr.equals("目录名称")) {
                                    directory_name_position = headi;
                                } else if (headStr.equals("注册证产品名称")) {
                                    registration_certificate_product_name_position = headi;
                                } else if (headStr.equals("品牌")) {
                                    brand_position = headi;
                                } else if (headStr.equals("规格")) {
                                    specifications_position = headi;
                                } else if (headStr.equals("型号")) {
                                    product_model_position = headi;
                                } else if (headStr.equals("单位")) {
                                    unit_position = headi;
                                } else if (headStr.equals("生产企业")) {
                                    production_enterprise_position = headi;
                                } else if (headStr.equals("备注")) {
                                    remarks_position = headi;
                                } else if (headStr.equals("采购限价")) {
                                    purchase_price_limit_position = headi;
                                } else if (headStr.equals("中标人(及其委托的代理人)")) {
                                    bid_winner_position = headi;
                                } else if (headStr.equals("确认状态")) {
                                    confirm_status_position = headi;
                                } else if (headStr.equals("中标人维护其委托的代理人")) {
                                    agent_position = headi;
                                } else if (headStr.equals("未发生交易")) {
                                    no_deal_position = headi;
                                }
                            }
                        }
                        //表内容
                        for (int i = 1; i < lineseles.size(); i++) {
                            Johnson_henan_RelationDistribution_list johnson_henan_relationDistribution_list = new Johnson_henan_RelationDistribution_list();
//                            logger.info("============== " + i);
                            Element linele = lineseles.get(i);
                            Elements lineTDseles = linele.select("td");
                            if (lineTDseles != null && lineTDseles.size() > 0) {
                                for (Element lineTDsele : lineTDseles) {
//                                    logger.info(lineTDsele.text());
                                }
                                try {
                                    String project_name = lineTDseles.get(project_name_position).text();
                                    if (StringUtils.isNotBlank(project_name) && !project_name.equals(" ")) {
                                        johnson_henan_relationDistribution_list.setProjectName(project_name);
                                    }
                                    String product_code = lineTDseles.get(product_code_position).text();
                                    if (StringUtils.isNotBlank(product_code) && !product_code.equals(" ")) {
                                        johnson_henan_relationDistribution_list.setProductCode(product_code);
                                    }
                                    String category = lineTDseles.get(category_position).text();
                                    if (StringUtils.isNotBlank(category) && !category.equals(" ")) {
                                        johnson_henan_relationDistribution_list.setCategory(category);
                                    }
                                    String category_one = lineTDseles.get(category_one_position).text();
                                    if (StringUtils.isNotBlank(category_one) && !category_one.equals(" ")) {
                                        johnson_henan_relationDistribution_list.setCategoryOne(category_one);
                                    }
                                    String category_two = lineTDseles.get(category_two_position).text();
                                    if (StringUtils.isNotBlank(category_two) && !category_two.equals(" ")) {
                                        johnson_henan_relationDistribution_list.setCategoryTwo(category_two);
                                    }
                                    String directory_name = lineTDseles.get(directory_name_position).text();
                                    if (StringUtils.isNotBlank(directory_name) && !directory_name.equals(" ")) {
                                        johnson_henan_relationDistribution_list.setDirectoryName(directory_name);
                                    }
                                    String registration_certificate_product_name = lineTDseles.get(registration_certificate_product_name_position).text();
                                    if (StringUtils.isNotBlank(registration_certificate_product_name) && !registration_certificate_product_name.equals(" ")) {
                                        johnson_henan_relationDistribution_list.setRegistrationCertificateProductName(registration_certificate_product_name);
                                    }
                                    String brand = lineTDseles.get(brand_position).text();
                                    if (StringUtils.isNotBlank(brand) && !brand.equals(" ")) {
                                        johnson_henan_relationDistribution_list.setBrand(brand);
                                    }
                                    String specifications = lineTDseles.get(specifications_position).text();
                                    if (StringUtils.isNotBlank(specifications) && !specifications.equals(" ")) {
                                        johnson_henan_relationDistribution_list.setSpecifications(specifications);
                                    }
                                    String product_model = lineTDseles.get(product_model_position).text();
                                    if (StringUtils.isNotBlank(product_model) && !product_model.equals(" ")) {
                                        johnson_henan_relationDistribution_list.setProductModel(product_model);
                                    }
                                    String unit = lineTDseles.get(unit_position).text();
                                    if (StringUtils.isNotBlank(unit) && !unit.equals(" ")) {
                                        johnson_henan_relationDistribution_list.setUnit(unit);
                                    }
                                    String production_enterprise = lineTDseles.get(production_enterprise_position).text();
                                    if (StringUtils.isNotBlank(production_enterprise) && !production_enterprise.equals(" ")) {
                                        johnson_henan_relationDistribution_list.setProductionEnterprise(production_enterprise);
                                    }
                                    String remarks = lineTDseles.get(remarks_position).text();
                                    if (StringUtils.isNotEmpty(remarks) && !remarks.equals(" ")) {
                                        johnson_henan_relationDistribution_list.setRemarks(lineTDseles.get(remarks_position).text());
                                    }
                                    String purchasePriceLimit = lineTDseles.get(purchase_price_limit_position).text();
                                    if (StringUtils.isNotBlank(purchasePriceLimit) && !purchasePriceLimit.equals(" ")) {
                                        johnson_henan_relationDistribution_list.setPurchasePriceLimit(Double.valueOf(purchasePriceLimit));
                                    }
                                    String bid_winner = lineTDseles.get(bid_winner_position).text();
                                    if (StringUtils.isNotBlank(bid_winner) && !bid_winner.equals(" ")) {
                                        johnson_henan_relationDistribution_list.setBidWinner(bid_winner);
                                    }
                                    String confirm_status = lineTDseles.get(confirm_status_position).text();
                                    if (StringUtils.isNotBlank(confirm_status) && !confirm_status.equals(" ")) {
                                        johnson_henan_relationDistribution_list.setConfirmStatus(confirm_status);
                                    }
                                    String agent = lineTDseles.get(agent_position).text();
                                    if (StringUtils.isNotBlank(agent) && !agent.equals(" ")) {
                                        johnson_henan_relationDistribution_list.setAgent(agent);
                                    }
                                    String no_deal = lineTDseles.get(no_deal_position).text();
                                    if (StringUtils.isNotBlank(no_deal) && !no_deal.equals(" ")) {
                                        johnson_henan_relationDistribution_list.setNoDeal(no_deal);
                                    }
                                    johnson_henan_relationDistribution_list.setAccount(account);
                                    johnson_henan_relationDistribution_list.setInsertTime(new Timestamp(Calendar.getInstance().getTime().getTime()));
                                    johnson_henan_relationDistribution_list.setUpdateTime(new Timestamp(Calendar.getInstance().getTime().getTime()));

                                    if (isInsert.equals("yes")) {
                                        johnson_henan_relationDistribution_service.addJohnson_henan_RelationDistribution_list(johnson_henan_relationDistribution_list);
                                        logger.info("- 插入数据库,第：" + i + " 条：" + johnson_henan_relationDistribution_list.toString());
                                    } else {
                                        logger.info("- 测试 ,不插入数据库,第：" + i + " 条：" + johnson_henan_relationDistribution_list.toString());
                                    }
                                } catch (Exception e) {
                                    logger.error("入库异常");
                                    e.printStackTrace();
                                }
                            }
                        }
                        logger.info("页码导航：" + pageGuideStr);

                    } else {
                        logger.info("表有0行");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.error("！！！解析异常");
                }
            } else {
                logger.error("！！！异常页面");
            }
        } else {
            logger.error("！！！空页面，不解析");
        }

    }
}
