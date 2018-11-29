package com.eddc.johnsoncrawler.model.heNan;

import java.util.Date;

public class Johnson_henan_RelationDistribution_list {
    private String projectName;

    private String productCode;

    private String category;

    private String categoryOne;

    private String categoryTwo;

    private String directoryName;

    private String registrationCertificateProductName;

    private String brand;

    private String specifications;

    private String productModel;

    private String unit;

    private String productionEnterprise;

    private String remarks;

    private Double purchasePriceLimit;

    private String bidWinner;

    private String confirmStatus;

    private String agent;

    private String noDeal;

    private String account;

    private Date insertTime;

    private Date updateTime;

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName == null ? null : projectName.trim();
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode == null ? null : productCode.trim();
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category == null ? null : category.trim();
    }

    public String getCategoryOne() {
        return categoryOne;
    }

    public void setCategoryOne(String categoryOne) {
        this.categoryOne = categoryOne == null ? null : categoryOne.trim();
    }

    public String getCategoryTwo() {
        return categoryTwo;
    }

    public void setCategoryTwo(String categoryTwo) {
        this.categoryTwo = categoryTwo == null ? null : categoryTwo.trim();
    }

    public String getDirectoryName() {
        return directoryName;
    }

    public void setDirectoryName(String directoryName) {
        this.directoryName = directoryName == null ? null : directoryName.trim();
    }

    public String getRegistrationCertificateProductName() {
        return registrationCertificateProductName;
    }

    public void setRegistrationCertificateProductName(String registrationCertificateProductName) {
        this.registrationCertificateProductName = registrationCertificateProductName == null ? null : registrationCertificateProductName.trim();
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand == null ? null : brand.trim();
    }

    public String getSpecifications() {
        return specifications;
    }

    public void setSpecifications(String specifications) {
        this.specifications = specifications == null ? null : specifications.trim();
    }

    public String getProductModel() {
        return productModel;
    }

    public void setProductModel(String productModel) {
        this.productModel = productModel == null ? null : productModel.trim();
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit == null ? null : unit.trim();
    }

    public String getProductionEnterprise() {
        return productionEnterprise;
    }

    public void setProductionEnterprise(String productionEnterprise) {
        this.productionEnterprise = productionEnterprise == null ? null : productionEnterprise.trim();
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks == null ? null : remarks.trim();
    }

    public Double getPurchasePriceLimit() {
        return purchasePriceLimit;
    }

    public void setPurchasePriceLimit(Double purchasePriceLimit) {
        this.purchasePriceLimit = purchasePriceLimit;
    }

    public String getBidWinner() {
        return bidWinner;
    }

    public void setBidWinner(String bidWinner) {
        this.bidWinner = bidWinner == null ? null : bidWinner.trim();
    }

    public String getConfirmStatus() {
        return confirmStatus;
    }

    public void setConfirmStatus(String confirmStatus) {
        this.confirmStatus = confirmStatus == null ? null : confirmStatus.trim();
    }

    public String getAgent() {
        return agent;
    }

    public void setAgent(String agent) {
        this.agent = agent == null ? null : agent.trim();
    }

    public String getNoDeal() {
        return noDeal;
    }

    public void setNoDeal(String noDeal) {
        this.noDeal = noDeal == null ? null : noDeal.trim();
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account == null ? null : account.trim();
    }

    public Date getInsertTime() {
        return insertTime;
    }

    public void setInsertTime(Date insertTime) {
        this.insertTime = insertTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        String show = "[" +
                " 项目名称:" + projectName +
                " ,产品代码:" + productCode +
                " ,大类:" + category +
                " ,一级分类:" + categoryOne +
                " ,二级分类:" + categoryTwo +
                " ,目录名称:" + directoryName +
                " ,注册证产品名称:" + registrationCertificateProductName +
                " ,品牌:" + brand +
                " ,规格:" + specifications +
                " ,型号:" + productModel +
                " ,单位:" + unit +
                " ,生产企业:" + productionEnterprise +
                " ,备注:" + remarks +
                " ,采购限价:" + purchasePriceLimit +
                " ,中标人(及其委托的代理人):" + bidWinner +
                " ,确认状态:" + confirmStatus +
                " ,中标人维护其委托的代理人:" + agent +
                " ,未发生交易:" + noDeal +
                " ,账号:" + account +
                " ,数据入库时间:" + insertTime +
                " ,数据更新时间:" + updateTime +
                " ]";
        return show;
    }
}