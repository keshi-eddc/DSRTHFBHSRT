package com.eddc.johnsoncrawler.service.shanXi.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.eddc.johnsoncrawler.model.shanXi.ShanxiCatalogue;
import com.eddc.johnsoncrawler.model.shanXi.ShanxiDiscussPrice;
import com.eddc.johnsoncrawler.service.shanXi.ShanxiDiscussPrice.ShanxiDiscussPriceService;
import com.eddc.johnsoncrawler.service.shanXi.Shanxicatalogue.ShanxicatalogueService;
import com.eddc.johnsoncrawler.service.shanXi.service.ShanXiCrawlerService;
import com.eddc.johnsoncrawler.utils.http.HttpClientUtil;
import com.eddc.johnsoncrawler.utils.http.request.MultiPartFormRequest;
import com.eddc.johnsoncrawler.utils.http.request.RequestMethod;
import com.eddc.johnsoncrawler.utils.http.response.Response;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

@Service(value = "ShanXiCrawlerService")
public class ShanXiCrawlerServiceImpl implements ShanXiCrawlerService {

    Logger logger = LogManager.getLogger(ShanXiCrawlerServiceImpl.class.getName());

    @Autowired
    ShanxicatalogueService shanxicatalogueService;

    @Autowired
    ShanxiDiscussPriceService shanxiDiscussPriceService;


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
        //参数-抓取批次
        //本次抓取批次
        String sync_number = getSyncNumber(params.get("account"));
        params.put("sync_number", sync_number);
        logger.info("- 抓取批次：" + sync_number);
        //登录后的cookie
        String cookie = params.get("Cookie");
        Map<String, String> headParams = new HashMap<>();
        headParams.put("Cookie", cookie);

        int page = 0;
        //已抓取数量
        int crawledNum = 0;
        //每页显示数量
        int rows = 30;
        Boolean isNotLast = true;

        while (isNotLast) {
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
                String content = getPostContent(url, headParams, null);
                System.out.println("content：" + content);
                //解析页面
                List<ShanxiCatalogue> shanxicatalogueList = extraShanxiCatalogueData(content);
                if (shanxicatalogueList.size() > 0) {
                    for (int i = 0; i < shanxicatalogueList.size(); i++) {
                        ShanxiCatalogue shanxicatalogueModel = shanxicatalogueList.get(i);
                        //设置属性
//                            19	账号	account
                        shanxicatalogueModel.setAccount(params.get("account"));
//                            20	批次	sync_number
                        shanxicatalogueModel.setSyncNumber(params.get("sync_number"));
//                            21	搜索词	search_keyword
                        shanxicatalogueModel.setSearchKeyword(params.get("comname"));
                        //数据插入数据库
                        shanxicatalogueService.insertShanxicatalogue(shanxicatalogueModel);
                        crawledNum++;
                    }
                    //判断最后一页 1
                    if (shanxicatalogueList.size() < rows) {
                        logger.info("- 第:" + page + " 的数据量是：" + shanxicatalogueList.size() + ",小于" + rows + "/页，即为最后一页。");
                        isNotLast = false;
                    }

                } else {
                    //判断最后一页 2
                    //content：{"total":43,"records":1290,"rows":[]} shanxicatalogueList.size() = 0
                    logger.info("- 最后一页:" + page + " 翻页完成。");
                    isNotLast = false;
                }
                //判断最后一页 3
                int totalPageNum = getShanxiCatalogueDataAlPageNum(content);
                int allRecordsNum = getShanxiCatalogueDataAllRecordsNum(content);
                logger.info("- 已经访问及解析第：" + page + " 页/" + totalPageNum + " ,url:" + url);
                if (totalPageNum != 0) {
                    if (page >= totalPageNum) {
                        isNotLast = false;
                        logger.info("- 最后一页:" + page + " 翻页完成。");
                    }
                } else if (allRecordsNum != 0) { //判断最后一页 4
                    if (crawledNum >= allRecordsNum) {
                        isNotLast = false;
                        logger.info("- 最后一页:" + page + " 翻页完成。");
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
                logger.error("！！！翻页是出现异常");
            }
        }
        logger.info("- 抓取完成。共抓了：" + page + " 页,数量：" + crawledNum);
    }

    /**
     * @param
     * @methodName getSyncNumber
     * @description 获得 抓取批次
     * @author keshi
     * @date 2018年11月27日 13:50
     */
    public String getSyncNumber(String account) {
        /*
         * 抓取批次 （用于标识一天内 第几次抓的）
         * 前提：更换账号的时候，要跟换USBkey,重新启动代码，
         * */
        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
        String dateStr = sf.format(new Date());
//        dateStr = "20181128";
        //本次抓取批次
        String sync_number = "";
        ShanxiCatalogue shanxicatalogueNewOne = shanxicatalogueService.selectNewOneDateByAccount(account);
        if (shanxicatalogueNewOne == null) {
            //数据库没有 数据，首次抓取
            sync_number = dateStr + "01";
        } else {
            String last_sync_number = shanxicatalogueNewOne.getSyncNumber();
            if (last_sync_number.contains(dateStr)) {
                //当天已经抓取过，批次加1
                long last_sync_number_num = Long.valueOf(last_sync_number);
                long sync_number_num = last_sync_number_num + 1;
                sync_number = String.valueOf(sync_number_num);
            } else {
                //当天第一次抓取
                sync_number = dateStr + "01";
            }
        }
        return sync_number;
    }

    /**
     * @param content
     * @methodName getShanxiCatalogueDataAlPageNum
     * @description 获得总页数
     * @author keshi
     * @date 2018年11月26日 17:18
     */
    public int getShanxiCatalogueDataAlPageNum(String content) {
        int totalPageNum = 0;
        //检验要解析的内容
        if (StringUtils.isNotBlank(content)) {
            //检验是否是正常页面
            if (content.contains("total")) {
                try {
                    JSONObject jo = JSONObject.parseObject(content);
                    if (jo.containsKey("total")) {
                        totalPageNum = jo.getIntValue("total");
//                        logger.info("totalPageNum：" + totalPageNum);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            logger.error("！！！要解析的页面为空，无法获得总页数。请检查");
        }
        return totalPageNum;
    }

    /**
     * @param content
     * @methodName getShanxiCatalogueDataAllRecordsNum
     * @description 获得所有的数据的个数
     * @author keshi
     * @date 2018年11月26日 17:20
     */
    public int getShanxiCatalogueDataAllRecordsNum(String content) {
        int allRecordsNum = 0;
        //检验要解析的内容
        if (StringUtils.isNotBlank(content)) {
            //检验是否是正常页面
            if (content.contains("records")) {
                try {
                    JSONObject jo = JSONObject.parseObject(content);
                    if (jo.containsKey("records")) {
                        allRecordsNum = jo.getIntValue("records");
//                        logger.info("allRecordsNum：" + allRecordsNum);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            logger.error("！！！要解析的页面为空，无法获得总页数。请检查");
        }
        return allRecordsNum;
    }

    /**
     * @param content
     * @methodName extraShanxiCatalogueData
     * @description 解析陕西省 挂网目录返回的json
     * @author keshi
     * @date 2018年11月23日 15:28
     */
    public List<ShanxiCatalogue> extraShanxiCatalogueData(String content) {
        List<ShanxiCatalogue> shanxicatalogueList = new ArrayList<>(100);
        //检验要解析的内容
        if (StringUtils.isNotBlank(content)) {
            //检验是否是正常页面
            if (content.contains("rows")) {
                JSONObject jo = JSONObject.parseObject(content);
                if (jo.containsKey("rows")) {
                    JSONArray joarr = jo.getJSONArray("rows");
                    if (joarr != null && joarr.size() > 0) {
//                        logger.info("- 本页数据有：" + joarr.size());
                        for (int i = 0; i < joarr.size(); i++) {
                            try {
                                JSONObject joLine = joarr.getJSONObject(i);
                                //模型对象
                                ShanxiCatalogue shanxicatalogueModel = new ShanxiCatalogue();
//                            1	组件编号	procure_catalog_id
                                shanxicatalogueModel.setProcureCatalogId(joLine.getString("PROCURECATALOGID"));
//                            2	大类名称	gtype_name
                                String gtype_name = joLine.getString("GTYPENAME");
                                shanxicatalogueModel.setGtypeName(gtype_name);
//                            3	目录分类	sort_name
                                String sort_name = joLine.getString("SORTNAME");
                                shanxicatalogueModel.setSortName(sort_name);
//                            5	组件名称	gpart_name
                                String gpart_name = joLine.getString("GPARTNAME");
                                shanxicatalogueModel.setGpartName(gpart_name);
//                            6	规格	gpartreg_outlookc
                                String gpartreg_outlookc = joLine.getString("GPARTREGOUTLOOKC");
                                shanxicatalogueModel.setGpartregOutlookc(gpartreg_outlookc);
//                            7	型号	gpartreg_model
                                String gpartreg_model = joLine.getString("GPARTREGMODEL");
                                shanxicatalogueModel.setGpartregModel(gpartreg_model);
//                            8	产地	gpart_origin
                                String gpart_origin = joLine.getString("GPARTORIGIN");
                                shanxicatalogueModel.setGpartOrigin(gpart_origin);
//                            9	单位	gpartminp_unit
                                String gpartminp_unit = joLine.getString("GPARTMINPUNIT");
                                shanxicatalogueModel.setGpartminpUnit(gpartminp_unit);
//                            10	code数量	code_count
                                int code_count = joLine.getIntValue("CODECOUNT");
                                shanxicatalogueModel.setCodeCount(code_count);
//                            11	申报企业名称	com_name
                                String com_name = joLine.getString("COMNAME");
                                shanxicatalogueModel.setComName(com_name);
//                            12	生产企业名称	com_name_sc
                                String com_name_sc = joLine.getString("COMNAME_SC");
                                shanxicatalogueModel.setComNameSc(com_name_sc);
//                            13	注册证编号	regcard_nm
                                String regcard_nm = joLine.getString("REGCARDNM");
                                shanxicatalogueModel.setRegcardNm(regcard_nm);
//                            14	注册证有效期	regcard_ded
                                String regcard_ded = joLine.getString("REGCARDDED");
                                shanxicatalogueModel.setRegcardDed(regcard_ded);
//                            15	状态	procure_cata_state
                                String procure_cata_state = joLine.getString("PROCURECATASTATE");
                                shanxicatalogueModel.setProcureCataState(procure_cata_state);
//                            16	限价	gparts_price
                                double gparts_price = joLine.getDoubleValue("GPARTSPRICE");
                                shanxicatalogueModel.setGpartsPrice(gparts_price);
                                //4	目录类别	catalogue_type
                                String catalogue_type = "";
                                if (gparts_price == 0) {
                                    catalogue_type = "备选采购";
                                } else {
                                    catalogue_type = "限价采购";
                                }
                                shanxicatalogueModel.setCatalogueType(catalogue_type);
//                            17	insert_time
                                shanxicatalogueModel.setInsertTime(new Timestamp(Calendar.getInstance().getTime().getTime()));
//                            18	update_time
                                shanxicatalogueModel.setUpdateTime(new Timestamp(Calendar.getInstance().getTime().getTime()));

                                //返回对象列表
                                shanxicatalogueList.add(shanxicatalogueModel);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                } else {
                    logger.error("！！！异常页面,没有rows，不进行解析");
                }
            } else {
                logger.error("！！！异常页面，不进行解析");
            }
        } else {
            logger.error("！！！要解析的页面为空，不进行解析。请检查");
        }
        return shanxicatalogueList;
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
//                        logger.info("- 页面正常");
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

    //----------陕西省 议价信息-------------
    @Override
    public void getShanxiDiscussPriceData(Map<String, String> params) {
        logger.info("- 开始获取 陕西省 议价信息");

        //参数-抓取批次
        String sync_number = getSyncNumberForDiscussPrice(params.get("account"));
        params.put("sync_number", sync_number);
        logger.info("- 抓取批次：" + sync_number);

        //登录后的cookie
        String cookie = params.get("Cookie");
        Map<String, String> headParams = new HashMap<>();
        headParams.put("Cookie", cookie);


        //翻页
        int page = 0;
        //已抓取数量
        int crawledNum = 0;
        //每页显示数量
        int rows = 30;
        Boolean isNotLast = true;

        while (isNotLast) {
            page++;
            try {
                /*
                 * 参数说明
                 * rows 每页显示数量
                 * page 页数
                 * CONFIRMSTATE=1 未确认
                 * */
                String url = "http://hcjy.sxsyxcg.com" +
                        "/HSNN/CM/Trade/Web/Controller/SXTradeController/QueryGpart.HSNN?PROCURECATALOGID=&GPARTNAME=&CONFIRMSTATE=1&COMNAME_SC=&REGCARDNM=" +
                        "&_search=false" +
                        "&nd=" +
                        "&rows=" + rows +
                        "&page=" + page +
                        "&sidx=" +
                        "&sord=asc";
                String content = getPostContent(url, headParams, null);
                logger.info("content：" + content);
                //解析页面
                List<ShanxiDiscussPrice> shanxiDiscussPriceList = extraShanxiDiscussPriceData(content, params);
                if (shanxiDiscussPriceList.size() > 0) {
                    for (int i = 0; i < shanxiDiscussPriceList.size(); i++) {
                        ShanxiDiscussPrice shanxiDiscussPriceModel = shanxiDiscussPriceList.get(i);
                        //设置属性
//                        17	insert_time
                        shanxiDiscussPriceModel.setInsertTime(new Timestamp(Calendar.getInstance().getTime().getTime()));
//                        18	update_time
                        shanxiDiscussPriceModel.setUpdateTime(new Timestamp(Calendar.getInstance().getTime().getTime()));
//                        19	账号	account
                        shanxiDiscussPriceModel.setAccount(params.get("account"));
//                        20	批次	sync_number
                        shanxiDiscussPriceModel.setSyncNumber(params.get("sync_number"));

                        //数据插入数据库
                        shanxiDiscussPriceService.insertShanxiDiscussPrice(shanxiDiscussPriceModel);
                        crawledNum++;
                    }
                } else {
                    //判断最后一页 2
                    //content：{"total":43,"records":1290,"rows":[]} shanxicatalogueList.size() = 0
                    logger.info("- 最后一页:" + page + " 翻页完成。");
                    isNotLast = false;
                }
                //判断最后一页 3
                int totalPageNum = getShanxiCatalogueDataAlPageNum(content);
                int allRecordsNum = getShanxiCatalogueDataAllRecordsNum(content);
                logger.info("- 已经访问及解析第：" + page + " 页/" + totalPageNum + " ,url:" + url);

                if (totalPageNum != 0) {
                    if (page >= totalPageNum) {
                        isNotLast = false;
                        logger.info("- 最后一页:" + page + " 翻页完成。");
                    }
                } else if (allRecordsNum != 0) { //判断最后一页 4
                    if (crawledNum >= allRecordsNum) {
                        isNotLast = false;
                        logger.info("- 最后一页:" + page + " 翻页完成。");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("！！！翻页是出现异常");
            }
        }
        logger.info("- 抓取完成。共抓了：" + page + " 页,数量：" + crawledNum);

    }


    public List<ShanxiDiscussPrice> getClickContent(String procureCatalogIdStr, Map<String, String> params) {
        List<ShanxiDiscussPrice> shanxiDiscussPriceList = new ArrayList<>();

        logger.info("           开始获取 点击  procure_catalog_id:" + procureCatalogIdStr + " 价格确认 按钮 的数据。");
        //登录后的cookie
        String cookie = params.get("Cookie");
        Map<String, String> headParams = new HashMap<>();
        headParams.put("Cookie", cookie);
        //参数检查 procureCatalogIdStr 一定不能为空
        if (StringUtils.isBlank(procureCatalogIdStr)) {
            logger.error("！！！在获得点击后的内容时，procureCatalogIdStr 参数 为空");
            return null;
        }
        //点击后有可能有多页，此处要进行翻页
        //翻页
        int page = 0;
        //已抓取数量
        int crawledNum = 0;
        //每页显示数量
        int rows = 30;
        Boolean isNotLast = true;

        while (isNotLast) {
            page++;
            try {
                /*
                 * 参数说明
                 * rows 每页显示数量
                 * page 页数
                 * */
                String url = "http://hcjy.sxsyxcg.com" +
                        "/HSNN/CM/Trade/Web/Controller/SXTradeController/QueryGPartForComCodeConfirm.HSNN?" +
                        "PROCURECATALOGID=" + procureCatalogIdStr +
                        "&_search=false" +
                        "&nd=" +
                        "&rows=" + rows +
                        "&page=" + page +
                        "&sidx=" +
                        "&sord=asc";
                String content = getPostContent(url, headParams, null);
                logger.info("           content：" + content);
                //解析页面
                List<ShanxiDiscussPrice> shanxiDiscussPriceClickList = extraShanxiDiscussPriceClickData(content);
                if (shanxiDiscussPriceClickList.size() > 0) {
                    for (int i = 0; i < shanxiDiscussPriceClickList.size(); i++) {
                        ShanxiDiscussPrice shanxiDiscussPriceClickModel = shanxiDiscussPriceClickList.get(i);
                        shanxiDiscussPriceList.add(shanxiDiscussPriceClickModel);
                        crawledNum++;
                    }
                } else {
                    //判断最后一页 2
                    //content：{"total":43,"records":1290,"rows":[]} shanxicatalogueList.size() = 0
//                    logger.info("- 最后一页:" + page + " 翻页完成。");
                    isNotLast = false;
                }
                //判断最后一页 3
                int totalPageNum = getShanxiCatalogueDataAlPageNum(content);
                int allRecordsNum = getShanxiCatalogueDataAllRecordsNum(content);
//                logger.info("- 已经访问及解析第：" + page + " 页/" + totalPageNum + " ,url:" + url);

                if (totalPageNum != 0) {
                    if (page >= totalPageNum) {
                        isNotLast = false;
//                        logger.info("- 最后一页:" + page + " 翻页完成。");
                    }
                } else if (allRecordsNum != 0) { //判断最后一页 4
                    if (crawledNum >= allRecordsNum) {
                        isNotLast = false;
//                        logger.info("- 最后一页:" + page + " 翻页完成。");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("！！！翻页是出现异常");
            }
        }
        logger.info("           " + procureCatalogIdStr + " 抓取完成。共抓了：" + page + " 页,数量：" + crawledNum);
        return shanxiDiscussPriceList;
    }

    /**
     * @param content
     * @methodName extraShanxiDiscussPriceData
     * @description 解析 议价信息
     * @author keshi
     * @date 2018年11月27日 17:16
     */
    public List<ShanxiDiscussPrice> extraShanxiDiscussPriceData(String content, Map<String, String> params) {
        List<ShanxiDiscussPrice> shanxiDiscussPriceList = new ArrayList<>(100);
        //检验要解析的内容
        if (StringUtils.isNotBlank(content)) {
            //检验是否是正常页面
            if (content.contains("rows")) {
                JSONObject jo = JSONObject.parseObject(content);
                if (jo.containsKey("rows")) {
                    JSONArray joarr = jo.getJSONArray("rows");
                    if (joarr != null && joarr.size() > 0) {
//                        logger.info("- 本页数据有：" + joarr.size());
                        for (int i = 0; i < joarr.size(); i++) {
                            try {
                                JSONObject joLine = joarr.getJSONObject(i);
                                //模型对象
                                ShanxiDiscussPrice shanxiDiscussPriceModel = new ShanxiDiscussPrice();
//                                1	procure_catalog_id	组件编号
                                shanxiDiscussPriceModel.setProcureCatalogId(joLine.getString("PROCURECATALOGID"));
//                                2	gpart_name	组件名称
                                shanxiDiscussPriceModel.setGpartName(joLine.getString("GPARTNAME"));
//                                3	gpartreg_outlookc	注册证规格
                                shanxiDiscussPriceModel.setGpartregOutlookc(joLine.getString("GPARTREGOUTLOOKC"));
//                                4	gpartreg_model	注册证型号
                                shanxiDiscussPriceModel.setGpartregModel(joLine.getString("GPARTREGMODEL"));
//                                5	gpart_origin	产地
                                shanxiDiscussPriceModel.setGpartOrigin(joLine.getString("GPARTORIGIN"));
//                                6	gpartminp_unit	单位
                                shanxiDiscussPriceModel.setGpartminpUnit(joLine.getString("GPARTUNIT"));
//                                7	code_count	dode数量
                                shanxiDiscussPriceModel.setCodeCount(joLine.getIntValue("CODECOUNT"));
//                                8	com_name_sc	生产企业
                                shanxiDiscussPriceModel.setComNameSc(joLine.getString("COMNAME_SC"));
//                                9	regcard_nm	注册证编号
                                shanxiDiscussPriceModel.setRegcardNm(joLine.getString("REGCARDNM"));
//                                10 confirm_state	确认状态
                                shanxiDiscussPriceModel.setConfirmState(joLine.getString("CONFIRMSTATE"));

                                //获取及解析，点击价格确认按钮 后的 数据
                                String procure_catalog_id_str = joLine.getString("PROCURECATALOGID");
                                //获取点击后的内容，返回 list
                                List<ShanxiDiscussPrice> clickDiscussPriceModelList = getClickContent(procure_catalog_id_str, params);
                                if (clickDiscussPriceModelList.size() > 0) {
                                    //点击后返回的对象数组大于0
                                    for (int j = 0; j < clickDiscussPriceModelList.size(); j++) {
                                        //点击后返回的对象
                                        ShanxiDiscussPrice clickDiscussPriceModel = clickDiscussPriceModelList.get(j);
//                                        1	procure_catalog_id	组件编号
                                        clickDiscussPriceModel.setProcureCatalogId(shanxiDiscussPriceModel.getProcureCatalogId());
//                                        2	gpart_name	组件名称
                                        clickDiscussPriceModel.setGpartName(shanxiDiscussPriceModel.getGpartName());
//                                        3	gpartreg_outlookc	注册证规格
                                        clickDiscussPriceModel.setGpartregOutlookc(shanxiDiscussPriceModel.getGpartregOutlookc());
//                                        4	gpartreg_model	注册证型号
                                        clickDiscussPriceModel.setGpartregModel(shanxiDiscussPriceModel.getGpartregModel());
//                                        5	gpart_origin	产地
                                        clickDiscussPriceModel.setGpartOrigin(shanxiDiscussPriceModel.getGpartOrigin());
//                                        6	gpartminp_unit	单位
                                        clickDiscussPriceModel.setGpartminpUnit(shanxiDiscussPriceModel.getGpartminpUnit());
//                                        7	code_count	dode数量
                                        clickDiscussPriceModel.setCodeCount(shanxiDiscussPriceModel.getCodeCount());
//                                        8	com_name_sc	生产企业
                                        clickDiscussPriceModel.setComNameSc(shanxiDiscussPriceModel.getComNameSc());
//                                        9	regcard_nm	注册证编号
                                        clickDiscussPriceModel.setRegcardNm(shanxiDiscussPriceModel.getRegcardNm());
//                                        10	confirm_state	确认状态
                                        clickDiscussPriceModel.setConfirmState(shanxiDiscussPriceModel.getConfirmState());
                                        //返回对象列表
                                        shanxiDiscussPriceList.add(clickDiscussPriceModel);
                                    }
                                } else {
                                    //点击后没有返回的数据，也要返回点击之前获得的对象
                                    //返回对象列表
                                    shanxiDiscussPriceList.add(shanxiDiscussPriceModel);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                } else {
                    logger.error("！！！异常页面,没有rows，不进行解析");
                }
            } else {
                logger.error("！！！异常页面，不进行解析");
            }
        } else {
            logger.error("！！！要解析的页面为空，不进行解析。请检查");
        }
        return shanxiDiscussPriceList;
    }

    /**
     * @param content
     * @methodName extraShanxiDiscussPriceData
     * @description 解析 议价信息
     * @author keshi
     * @date 2018年11月27日 17:16
     */
    public List<ShanxiDiscussPrice> extraShanxiDiscussPriceClickData(String content) {
        List<ShanxiDiscussPrice> shanxiDiscussPriceList = new ArrayList<>(100);
        //检验要解析的内容
        if (StringUtils.isNotBlank(content)) {
            //检验是否是正常页面
            if (content.contains("rows")) {
                JSONObject jo = JSONObject.parseObject(content);
                if (jo.containsKey("rows")) {
                    JSONArray joarr = jo.getJSONArray("rows");
                    if (joarr != null && joarr.size() > 0) {
//                        logger.info("- 本页数据有：" + joarr.size());
                        for (int i = 0; i < joarr.size(); i++) {
                            try {
                                JSONObject joLine = joarr.getJSONObject(i);
                                //模型对象
                                ShanxiDiscussPrice shanxiDiscussPriceModel = new ShanxiDiscussPrice();
//                                11	click_hos_name	医院	HOSNAME
                                shanxiDiscussPriceModel.setClickHosName(joLine.getString("HOSNAME"));
//                                12	click_gpart_price	价格	GPARTPRICE
                                shanxiDiscussPriceModel.setClickGpartPrice(joLine.getDoubleValue("GPARTPRICE"));
//                                13	click_confirm_state	提交状态	CONFIRMSTATE
                                shanxiDiscussPriceModel.setClickConfirmState(joLine.getString("CONFIRMSTATE"));
//                                14	click_edup_time	提交时间	EDUPTIME
                                shanxiDiscussPriceModel.setClickEdupTime(joLine.getString("EDUPTIME"));
//                                15	click_gpartreg_outlookc	产品规格	GPARTREGOUTLOOKC
                                shanxiDiscussPriceModel.setClickGpartregOutlookc(joLine.getString("GPARTREGOUTLOOKC"));
//                                16	click_gpartreg_model	产品型号	GPARTREGMODEL
                                shanxiDiscussPriceModel.setClickGpartregModel(joLine.getString("GPARTREGMODEL"));
                                //返回对象列表
                                shanxiDiscussPriceList.add(shanxiDiscussPriceModel);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                } else {
                    logger.error("！！！异常页面,没有rows，不进行解析");
                }
            } else {
                logger.error("！！！异常页面，不进行解析");
            }
        } else {
            logger.error("！！！要解析的页面为空，不进行解析。请检查");
        }
        return shanxiDiscussPriceList;
    }

    /**
     * @param
     * @methodName getSyncNumber
     * @description 获得 抓取批次
     * @author keshi
     * @date 2018年11月27日 13:50
     */
    public String getSyncNumberForDiscussPrice(String account) {

        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
        String dateStr = sf.format(new Date());
//        dateStr = "20181128";
        //本次抓取批次
        String sync_number = "";
        ShanxiDiscussPrice shanxiDiscussPrice = shanxiDiscussPriceService.selectNewOneDateByAccountForDiscussPrice(account);
        if (shanxiDiscussPrice == null) {
            //数据库没有 数据，首次抓取
            sync_number = dateStr + "01";
        } else {
            String last_sync_number = shanxiDiscussPrice.getSyncNumber();
            if (last_sync_number.contains(dateStr)) {
                //当天已经抓取过，批次加1
                long last_sync_number_num = Long.valueOf(last_sync_number);
                long sync_number_num = last_sync_number_num + 1;
                sync_number = String.valueOf(sync_number_num);
            } else {
                //当天第一次抓取
                sync_number = dateStr + "01";
            }
        }
        return sync_number;
    }

}
