package com.inov8.export.service;

import com.inov8.export.vo.PricePlanDetailVO;
import com.inov8.export.vo.SharePlanDetailVO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by atieq.rehman on 3/27/2018.
 */
public class ProductProfileManager {

    private Connection connection;

    public ProductProfileManager(Connection connection) {
        this.connection = connection;
    }
    
    public Map<Long, List<PricePlanDetailVO>> loadPricePlanDetail(List<Long> idList) {

        PreparedStatement statement = null;
        List<PricePlanDetailVO> vos = new ArrayList<>();
        try {
            statement = connection.prepareStatement("SELECT PRICE_PLAN_ID, TITLE, PLAN_TYPE_NAME, USAGE_TYPE_NAME, " +
                    "ACTIVATION_FROM, SKIP_MAX_TX_AMOUNT, ACTIVE_STR, SLAB_FROM, SLAB_TO, MIN_VALUE, MAX_VALUE, FIXED_STR, INCLUSIVE_STR, FEE " +
                    "FROM PRICE_PLAN_DETAIL_VIEW WHERE PRICE_PLAN_ID IN (" + concatIds(idList) + ") " +
                    "ORDER BY PRICE_PLAN_ID, SLAB_FROM");
            ResultSet set = statement.executeQuery();

            while (set.next()) {
                int col = 1;
                vos.add(new PricePlanDetailVO(
                        set.getBigDecimal(col++).longValue(),
                        set.getString(col++),
                        set.getString(col++),
                        set.getString(col++),
                        set.getBigDecimal(col++),
                        set.getBigDecimal(col++),
                        set.getString(col++),
                        set.getBigDecimal(col++),
                        set.getBigDecimal(col++),
                        set.getBigDecimal(col++),
                        set.getBigDecimal(col++),
                        set.getString(col++),
                        set.getString(col++),
                        set.getBigDecimal(col)
                ));
            }

            statement.clearParameters();
            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Map<Long, List<PricePlanDetailVO>> grouped = new LinkedHashMap<>();

        for (PricePlanDetailVO entry : vos) {
            Long pricePlanId = entry.getPricePlanId();

            List<PricePlanDetailVO> exists = grouped.get(pricePlanId);
            if (exists == null) {
                List<PricePlanDetailVO> e = new ArrayList<>();
                e.add(entry);

                grouped.put(pricePlanId, e);
            } else {
                exists.add(entry);
            }
        }
        return grouped;
    }

    public Map<Long, Map<Long, List<SharePlanDetailVO>>> loadSharePlanDetail(List<Long> idList) {

        PreparedStatement statement = null;
        List<SharePlanDetailVO> vos = new ArrayList<>();
        try {

            String SHARE_PLAN_DETAIL = "SELECT SPDV.SHARE_PLAN_ID, SPDV.TITLE, SPDV.PLAN_TYPE_NAME, SPDV.USAGE_TYPE_NAME, " +
                    "SPDV.SERVICE_NAME, SPDV.PRODUCT_NAME, SPDV.ACTIVE_STR, " +
                    "SPDV.SHARE_PLAN_DETAIL_ID, SPDV.SLAB_FROM, SPDV.SLAB_TO, " +
                    "SPSS.SHARE_PLAN_SH_SHARE_ID, CS.COMMISSION_STAKEHOLDER_ID AS STAKEHOLDER_ID,CS.NAME AS STAKEHOLDER_NAME, SPSS.SHARE_VALUE,  " +
                    "SPSS.IS_WHT_APPLICABLE " +
                    "FROM SHARE_PLAN_SH_SHARE SPSS, COMMISSION_STAKEHOLDER CS, SHARE_PLAN_DETAIL_VIEW SPDV " +
                    "WHERE SPDV.SHARE_PLAN_ID IN (?) " +
                    "AND SPSS.SHARE_PLAN_DETAIL_ID = SPDV.SHARE_PLAN_DETAIL_ID " +
                    "AND SPSS.STAKEHOLDER_ID = CS.COMMISSION_STAKEHOLDER_ID " +
                    "ORDER BY SPDV.TITLE ASC, SPDV.SLAB_FROM ASC, CS.NAME ASC";

            statement = connection.prepareStatement(SHARE_PLAN_DETAIL.replace("?", concatIds(idList)));
            ResultSet set = statement.executeQuery();
            while (set.next()) {
                int col = 1;
                vos.add(new SharePlanDetailVO(
                        set.getBigDecimal(col++).longValue(),
                        set.getString(col++),
                        set.getString(col++),
                        set.getString(col++),
                        set.getString(col++),
                        set.getString(col++),
                        set.getString(col++),
                        set.getBigDecimal(col++).longValue(),
                        set.getBigDecimal(col++),
                        set.getBigDecimal(col++),
                        set.getBigDecimal(col++).longValue(),
                        set.getBigDecimal(col++).longValue(),
                        set.getString(col++),
                        set.getBigDecimal(col++),
                        set.getBigDecimal(col)
                ));
            }

            statement.clearParameters();
            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Map<Long, List<SharePlanDetailVO>> grouped = new LinkedHashMap<>();

        for (SharePlanDetailVO entry : vos) {
            Long pricePlanId = entry.getSharePlanId();

            List<SharePlanDetailVO> exists = grouped.get(pricePlanId);
            if (exists == null) {
                List<SharePlanDetailVO> e = new ArrayList<>();
                e.add(entry);

                grouped.put(pricePlanId, e);
            } else {
                exists.add(entry);
            }
        }

        Map<Long, Map<Long, List<SharePlanDetailVO>>>  plans = new LinkedHashMap<>();

        for(Map.Entry<Long, List<SharePlanDetailVO>> p : grouped.entrySet()) {

            // new Map for slabs
            Map<Long, List<SharePlanDetailVO>> finalSlabs = new LinkedHashMap<>();

            List<SharePlanDetailVO> oldSlabs = grouped.get(p.getKey());
            for(SharePlanDetailVO vo: oldSlabs) {
                Long shareDetailId = vo.getSharePlanDetailId();

                List<SharePlanDetailVO> sh = finalSlabs.get(shareDetailId);
                if(sh == null) {
                    List<SharePlanDetailVO> d = new ArrayList<>();
                    d.add(vo);
                    finalSlabs.put(shareDetailId, d);
                }

                else {
                    sh.add(vo);
                }
            }

            plans.put(p.getKey(), finalSlabs);
        }

        return plans;
    }

    private String concatIds(List<Long> idList) {
        StringBuilder ids = new StringBuilder();
        int i = 1;
        int size = idList.size();
        for (Long id : idList) {
            ids.append(id);
            if (i != size)
                ids.append(",");

            i++;
        }

        return ids.toString();
    }

}
