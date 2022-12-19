package com.inov8.microbank.common.util;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;

import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by bkr on 11/15/2016.
 */
public class ExportLogoInReport {

    public static void appendLogoInXls(HSSFPatriarch patriarch, HSSFWorkbook wb, String filePath, int startCol){
        ByteArrayOutputStream akblLogoInBytes = new ByteArrayOutputStream();
        try {
            URL akblLogoUrl = new URL(MessageUtil.getPortalLink() + filePath);
            BufferedImage image = ImageIO.read(akblLogoUrl);
            ImageIO.write(image,"png",akblLogoInBytes);
        } catch (IOException e) {
            e.printStackTrace();
        }

        HSSFClientAnchor anchor = new HSSFClientAnchor(0,0,0,0,(short)startCol,(short)0,(short)(startCol+2),(short)4);
        int index = wb.addPicture(akblLogoInBytes.toByteArray(), HSSFWorkbook.PICTURE_TYPE_PNG);
        patriarch.createPicture(anchor,index);
    }
    
    public static void appendLogoInXls(Drawing patriarch, Workbook wb, String filePath, int startCol){
        ByteArrayOutputStream akblLogoInBytes = new ByteArrayOutputStream();
        try {
            URL akblLogoUrl = new URL(MessageUtil.getPortalLink() + filePath);
            BufferedImage image = ImageIO.read(akblLogoUrl);
            ImageIO.write(image,"png",akblLogoInBytes);
        } catch (IOException e) {
            e.printStackTrace();
        }

        XSSFClientAnchor anchor = new XSSFClientAnchor(0,0,0,0,(short)startCol,(short)0,(short)(startCol+2),(short)4);
        int index = wb.addPicture(akblLogoInBytes.toByteArray(), HSSFWorkbook.PICTURE_TYPE_PNG);
        patriarch.createPicture(anchor,index);
    }

    public static void appendLogoInPdf(StringBuffer xlsfo, String firstFilePath, String secondFilePath){

        URL firtLogoUrl = null;
        URL secondLogoUrl = null;
        try {
            firtLogoUrl = new URL(MessageUtil.getPortalLink() +firstFilePath);
            secondLogoUrl = new URL(MessageUtil.getPortalLink()+secondFilePath);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        xlsfo.append(" <fo:table-row> ");

        xlsfo.append(" <fo:table-cell > ");
        xlsfo.append(" <fo:block keep-together.within-page=\"always\" float=\"left\">");
        xlsfo.append("<fo:external-graphic src=\"url('"+firtLogoUrl+"')\" content-height=\"scale-to-fit\" height=\"2.00in\"  content-width=\"1.20in\" scaling=\"uniform\" scaling-method=\"auto\"/>");
        xlsfo.append("</fo:block>");
        xlsfo.append(" </fo:table-cell> ");

        xlsfo.append(" <fo:table-cell > ");
        xlsfo.append(" </fo:table-cell> ");

        xlsfo.append(" <fo:table-cell > ");
        xlsfo.append(" </fo:table-cell> ");

        xlsfo.append(" <fo:table-cell > ");
        xlsfo.append(" <fo:block keep-together.within-page=\"always\" float=\"left\">");
        xlsfo.append("<fo:external-graphic src=\"url('"+secondLogoUrl+"')\" content-height=\"scale-to-fit\" height=\"2.00in\"  content-width=\"1.20in\" scaling=\"uniform\" scaling-method=\"auto\"/>");
        xlsfo.append("</fo:block>");
        xlsfo.append(" </fo:table-cell> ");

        xlsfo.append(" </fo:table-row> ");
    }


}
