package com.inov8.integration.loyalty;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.List;

/**
 * Created by bkr on 7/18/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class LoyaltyVO implements Serializable{


    private String responseCode;
    private String responseDescription;
    private String sessionid;
    private String sharedSession;
    private String totalCount;
    public String getSharedSession() {
        return sharedSession;
    }

    public void setSharedSession(String sharedSession) {
        this.sharedSession = sharedSession;
    }

    private List<CategoryVO> categoryVOs;
    private List<Merchants> merchants;
    private Merchants merchant;
    private List<AllPointsVO> allPointsVO;
    private List<DetailPointsVO> detailPointsVO;
    private List<CreateCardVO> createCardVO;
    private List<MerchantLoyaltySetupVO> merchantLoyaltySetups;
    private MerchantLoyaltySetupVO merchantLoyaltySetup;
    private List<MiniStatement> miniStatements;
    private String query;
    private String title;
    private String name;
    private String tags;
    private String address;
    private String category;

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseDescription() {
        return responseDescription;
    }

    public void setResponseDescription(String responseDescription) {
        this.responseDescription = responseDescription;
    }


    public List<AllPointsVO> getAllPointsVO() {
        return allPointsVO;
    }

    public void setAllPointsVO(List<AllPointsVO> allPointsVO) {
        this.allPointsVO = allPointsVO;
    }

    public List<DetailPointsVO> getDetailPointsVO() {
        return detailPointsVO;
    }

    public void setDetailPointsVO(List<DetailPointsVO> detailPointsVO) {
        this.detailPointsVO = detailPointsVO;
    }

    public List<CreateCardVO> getCreateCardVO() {
        return createCardVO;
    }

    public void setCreateCardVO(List<CreateCardVO> createCardVO) {
        this.createCardVO = createCardVO;
    }

    public Merchants getMerchant() {
        return merchant;
    }

    public void setMerchant(Merchants merchant) {
        this.merchant = merchant;
    }

    public List<Merchants> getMerchants() {
        return merchants;
    }

    public void setMerchants(List<Merchants> merchants) {
        this.merchants = merchants;
    }


    public String getSessionid() {
        return sessionid;
    }

    public void setSessionid(String sessionid) {
        this.sessionid = sessionid;
    }

    public List<CategoryVO> getCategoryVOs() {
        return categoryVOs;
    }

    public void setCategoryVOs(List<CategoryVO> categoryVOs) {
        this.categoryVOs = categoryVOs;
    }

    public List<MerchantLoyaltySetupVO> getMerchantLoyaltySetups() {
        return merchantLoyaltySetups;
    }

    public void setMerchantLoyaltySetups(List<MerchantLoyaltySetupVO> merchantLoyaltySetups) {
        this.merchantLoyaltySetups = merchantLoyaltySetups;
    }

    public MerchantLoyaltySetupVO getMerchantLoyaltySetup() {
        return merchantLoyaltySetup;
    }

    public void setMerchantLoyaltySetup(MerchantLoyaltySetupVO merchantLoyaltySetup) {
        this.merchantLoyaltySetup = merchantLoyaltySetup;
    }

    public List<MiniStatement> getMiniStatements() {
        return miniStatements;
    }

    public void setMiniStatements(List<MiniStatement> miniStatements) {
        this.miniStatements = miniStatements;
    }

    public String getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(String totalCount) {
        this.totalCount = totalCount;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
