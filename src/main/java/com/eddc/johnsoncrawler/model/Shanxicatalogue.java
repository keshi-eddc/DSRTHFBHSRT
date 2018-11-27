package com.eddc.johnsoncrawler.model;

import java.util.Date;

public class Shanxicatalogue {
    private Integer procureCatalogId;

    private String gtypeName;

    private String sortName;

    private String catalogueType;

    private String gpartName;

    private String gpartregOutlookc;

    private String gpartregModel;

    private String gpartOrigin;

    private String gpartminpUnit;

    private Integer codeCount;

    private String comName;

    private String comNameSc;

    private String regcardNm;

    private String regcardDed;

    private String procureCataState;

    private Double gpartsPrice;

    private String syncNumber;

    private String searchKeyword;

    private String account;

    private Date insertTime;

    private Date updateTime;

    public Integer getProcureCatalogId() {
        return procureCatalogId;
    }

    public void setProcureCatalogId(Integer procureCatalogId) {
        this.procureCatalogId = procureCatalogId;
    }

    public String getGtypeName() {
        return gtypeName;
    }

    public void setGtypeName(String gtypeName) {
        this.gtypeName = gtypeName == null ? null : gtypeName.trim();
    }

    public String getSortName() {
        return sortName;
    }

    public void setSortName(String sortName) {
        this.sortName = sortName == null ? null : sortName.trim();
    }

    public String getCatalogueType() {
        return catalogueType;
    }

    public void setCatalogueType(String catalogueType) {
        this.catalogueType = catalogueType == null ? null : catalogueType.trim();
    }

    public String getGpartName() {
        return gpartName;
    }

    public void setGpartName(String gpartName) {
        this.gpartName = gpartName == null ? null : gpartName.trim();
    }

    public String getGpartregOutlookc() {
        return gpartregOutlookc;
    }

    public void setGpartregOutlookc(String gpartregOutlookc) {
        this.gpartregOutlookc = gpartregOutlookc == null ? null : gpartregOutlookc.trim();
    }

    public String getGpartregModel() {
        return gpartregModel;
    }

    public void setGpartregModel(String gpartregModel) {
        this.gpartregModel = gpartregModel == null ? null : gpartregModel.trim();
    }

    public String getGpartOrigin() {
        return gpartOrigin;
    }

    public void setGpartOrigin(String gpartOrigin) {
        this.gpartOrigin = gpartOrigin == null ? null : gpartOrigin.trim();
    }

    public String getGpartminpUnit() {
        return gpartminpUnit;
    }

    public void setGpartminpUnit(String gpartminpUnit) {
        this.gpartminpUnit = gpartminpUnit == null ? null : gpartminpUnit.trim();
    }

    public Integer getCodeCount() {
        return codeCount;
    }

    public void setCodeCount(Integer codeCount) {
        this.codeCount = codeCount;
    }

    public String getComName() {
        return comName;
    }

    public void setComName(String comName) {
        this.comName = comName == null ? null : comName.trim();
    }

    public String getComNameSc() {
        return comNameSc;
    }

    public void setComNameSc(String comNameSc) {
        this.comNameSc = comNameSc == null ? null : comNameSc.trim();
    }

    public String getRegcardNm() {
        return regcardNm;
    }

    public void setRegcardNm(String regcardNm) {
        this.regcardNm = regcardNm == null ? null : regcardNm.trim();
    }

    public String getRegcardDed() {
        return regcardDed;
    }

    public void setRegcardDed(String regcardDed) {
        this.regcardDed = regcardDed == null ? null : regcardDed.trim();
    }

    public String getProcureCataState() {
        return procureCataState;
    }

    public void setProcureCataState(String procureCataState) {
        this.procureCataState = procureCataState == null ? null : procureCataState.trim();
    }

    public Double getGpartsPrice() {
        return gpartsPrice;
    }

    public void setGpartsPrice(Double gpartsPrice) {
        this.gpartsPrice = gpartsPrice;
    }

    public String getSyncNumber() {
        return syncNumber;
    }

    public void setSyncNumber(String syncNumber) {
        this.syncNumber = syncNumber == null ? null : syncNumber.trim();
    }

    public String getSearchKeyword() {
        return searchKeyword;
    }

    public void setSearchKeyword(String searchKeyword) {
        this.searchKeyword = searchKeyword == null ? null : searchKeyword.trim();
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
}