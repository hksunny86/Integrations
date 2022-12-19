package com.inov8.export.service;

import com.inov8.export.model.ExportRequestModel;
import com.inov8.export.view.PricePlanDetailXlsView;
import com.inov8.export.view.SharePlanDetailXlsView;
import com.inov8.export.vo.PricePlanDetailVO;
import com.inov8.export.vo.SharePlanDetailVO;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Workbook;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

/**
 * Created by atieq.rehman on 3/28/2018.
 */
public class MultiSheetViewFactory {
    private static final Logger LOGGER = Logger.getLogger(MultiSheetViewFactory.class);

    private ProductProfileManager productProfileManager;

    public MultiSheetViewFactory(Connection connection) {
        this.productProfileManager = new ProductProfileManager(connection);
    }

    public void generateDetailSheets(ExportRequestModel exportRequestModel, Workbook workbook, Map<String, CellStyle> styles, List<Long> ids) {

        Long reportId = exportRequestModel.getReportId();
        String viewType = exportRequestModel.getReportView();
        if (!(viewType.toUpperCase().equals("XLS") || viewType.toUpperCase().equals("XLSX"))) {
            return;
        }

        if (reportId == 48) {
            Map<Long, List<PricePlanDetailVO>> list = productProfileManager.loadPricePlanDetail(ids);
            if (list == null || list.isEmpty()) {
                LOGGER.info("No details found against given ids of Price Plans");
                return;
            }

            PricePlanDetailXlsView view = new PricePlanDetailXlsView();
            view.generateDetailSheets(workbook, styles, list);
        }

        else if (reportId == 49) {
            Map<Long, Map<Long, List<SharePlanDetailVO>>> list = productProfileManager.loadSharePlanDetail(ids);
            if (list == null || list.isEmpty()) {
                LOGGER.info("No details found against given ids of Share Plans");
                return;
            }

            SharePlanDetailXlsView view = new SharePlanDetailXlsView();
            view.generateDetailSheets(workbook, styles, list);
        }
    }
}
