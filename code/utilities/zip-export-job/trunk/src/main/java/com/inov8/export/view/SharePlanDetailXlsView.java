package com.inov8.export.view;

import com.inov8.export.vo.PricePlanDetailVO;
import com.inov8.export.vo.SharePlanDetailVO;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.extremecomponents.table.view.ExportViewUtils;

import java.util.*;

/**
 * Created by atieq.rehman on 3/28/2018.
 */
public class SharePlanDetailXlsView extends XlsView {

    public void generateDetailSheets(Workbook workbook, Map<String, CellStyle> styles, Map<Long, Map<Long, List<SharePlanDetailVO>>> data) {

        for (Map.Entry<Long, Map<Long, List<SharePlanDetailVO>>> e : data.entrySet()) {
            Map<Long, List<SharePlanDetailVO>> plans = data.get(e.getKey());

            if (plans == null || plans.values().isEmpty())
                continue;

            SharePlanDetailVO header = (SharePlanDetailVO) ((List) plans.values().toArray()[0]).get(0);
            Sheet sheet = workbook.createSheet(header.getTitle());
            createHeader(sheet, styles, header);

            int rowNum = 8;
            for (Map.Entry<Long, List<SharePlanDetailVO>> slab : plans.entrySet()) {
                List<SharePlanDetailVO> shares = plans.get(slab.getKey());

                if(shares == null || shares.isEmpty())
                    continue;

                Row row = sheet.createRow(rowNum);
                int cellNum = 1;
                createCell(sheet, row, styles.get("numericStyle"), 0, cellNum++, shares.get(0).getSlabFrom());
                createCell(sheet, row, styles.get("numericStyle"), 0, cellNum++, shares.get(0).getSlabTo());

                int shCell = cellNum + 2;
                for (SharePlanDetailVO sh : shares) {
                    if(sh.getShare() > 0) {
                        createCell(sheet, row, styles.get("textStyle"), 1, shCell++, sh.getStakeholderName());
                        createCell(sheet, row, styles.get("numericStyle"), 0, shCell++, sh.getShare());
                        createCell(sheet, row, styles.get("textStyle"), 1, shCell++, sh.getWhtApplicable());

                        shCell++;
                    }
                }

                rowNum++;
            }
        }
    }

    void createHeader(Sheet sheet, Map<String, CellStyle> styles, SharePlanDetailVO vo) {

        Row row = sheet.createRow(1);
        createCell(sheet, row, styles.get("textStyle"), 1, 1, "Title");
        createCell(sheet, row, styles.get("textStyle"), 1, 2, vo.getTitle());
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 2, 7));

        row = sheet.createRow(3);
        createCell(sheet, row, styles.get("textStyle"), 1, 1, "Plan Type");
        createCell(sheet, row, styles.get("textStyle"), 1, 2, vo.getPlanType());
        sheet.addMergedRegion(new CellRangeAddress(3, 3, 2, 3));

        createCell(sheet, row, styles.get("textStyle"), 1, 5, "Usage Type");
        createCell(sheet, row, styles.get("textStyle"), 1, 6, vo.getUsageType());

        row = sheet.createRow(5);
        createCell(sheet, row, styles.get("textStyle"), 1, 1, "Service");
        createCell(sheet, row, styles.get("textStyle"), 1, 2, vo.getServiceName());
        sheet.addMergedRegion(new CellRangeAddress(5, 5, 2, 4));

        createCell(sheet, row, styles.get("textStyle"), 1, 5, "Product");
        createCell(sheet, row, styles.get("textStyle"), 1, 6, vo.getProductName());
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 6, 7));

        Row stkRow = sheet.createRow(6);
        row = sheet.createRow(7);

        createCell(sheet, row, styles.get("titleStyle"), 1, 1, "Slab From");
        createCell(sheet, row, styles.get("titleStyle"), 1, 2, "Slab To");

        int cellNum = 4;
        Workbook workbook = sheet.getWorkbook();
        CellStyle titleStyle = styles.get("titleStyle");
        for(int i =1; i < 8; i++) {

            createCell(sheet, stkRow, titleStyle, 1, cellNum + i, "");
            createCell(sheet, stkRow, titleStyle, 1, cellNum + i, "");
            createCell(sheet, stkRow, titleStyle, 1, cellNum + i, "Stakeholder " + i);

            CellRangeAddress mergedCell = new CellRangeAddress(6, 6, cellNum + i, cellNum + i + 2);
            sheet.addMergedRegion(mergedCell);

            RegionUtil.setBorderTop(CellStyle.BORDER_THIN, mergedCell, sheet, workbook);
            RegionUtil.setBorderBottom(CellStyle.BORDER_THIN, mergedCell, sheet, workbook);
            RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, mergedCell, sheet, workbook);
            RegionUtil.setBorderRight(CellStyle.BORDER_THIN, mergedCell, sheet, workbook);

            createCell(sheet, row, titleStyle, 1, cellNum + i, "Name");
            createCell(sheet, row, titleStyle, 1, cellNum + i + 1, "Share(%)");
            createCell(sheet, row, titleStyle, 1, cellNum + i + 2, "WHT");
            cellNum +=3;
        }
    }
}
