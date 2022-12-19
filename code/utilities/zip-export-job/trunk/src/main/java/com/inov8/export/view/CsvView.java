package com.inov8.export.view;

import com.inov8.export.common.EncryptionUtil;
import com.inov8.export.model.ExportInformationModel;
import com.inov8.export.model.ExportRequestModel;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.validator.GenericValidator;

import java.io.BufferedWriter;
import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.sql.ResultSet;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by NaseerUl on 9/19/2016.
 */
public class CsvView
{
    public CsvView()
    {
        super();
    }

    public String export(ExportRequestModel exportRequestModel, ExportInformationModel exportInfoModel, ResultSet rowSet, ResultSet totalsRowSet) throws Exception
    {
        Set<Integer> escapeCommasColumnsIndexesSet = prepareEscapeCommasColumnsIndexesSet(exportInfoModel.getEscapeCommasColumnsIndexes());
        Set<Integer> balanceColumnsIndexesSet = prepareColumnsIndexesSet(exportInfoModel.getBalaceCellColumnsIndexes());
        Set<Integer> accountNoColumnsIndexesSet = prepareColumnsIndexesSet(exportInfoModel.getAccountCellColumnsIndexes());
        Set<Integer> cnicColumnsIndexesSet = prepareColumnsIndexesSet(exportInfoModel.getCnicCellColumnsIndexes());
        Set<Integer> dobColumnsIndexesSet = prepareColumnsIndexesSet(exportInfoModel.getDobCellColumnsIndexes());

        
        final int columnCount = rowSet.getMetaData().getColumnCount();
        StringBuilder csvRowBuilder = new StringBuilder();
        File zipReportsDir = new File(exportInfoModel.getExportDirPath()+ File.separator+exportRequestModel.getUsername());
        
        Path path = Paths.get(exportInfoModel.getExportDirPath()+ File.separator+exportRequestModel.getUsername());

		boolean pathExists = Files.exists(path,new LinkOption[]{ LinkOption.NOFOLLOW_LINKS});
		
		if(!pathExists)
			Files.createDirectory(path);
        
        
        String csvFileName = exportInfoModel.getExportFileName()+"_"+ DateFormatUtils.format(new Date(),"yyyyMMdd_HHmmss")+"_CSV.csv";
        File file = new File(zipReportsDir, csvFileName);
        try (BufferedWriter writer = Files.newBufferedWriter(file.toPath(),
                Charset.defaultCharset(), StandardOpenOption.CREATE_NEW))
        {
            writer.write(exportInfoModel.getColumnsTitles());
            writer.newLine();
            if(CollectionUtils.isNotEmpty(escapeCommasColumnsIndexesSet) || CollectionUtils.isNotEmpty(balanceColumnsIndexesSet) || CollectionUtils.isNotEmpty(accountNoColumnsIndexesSet)) {
                while(rowSet.next()) {
                    for(int idx = 1; idx <= columnCount; idx++) {
                        Object val = rowSet.getObject(idx);
                        if(val != null) {
                            
                        	String value = val.toString();
                        	
                        	if(null!=escapeCommasColumnsIndexesSet && escapeCommasColumnsIndexesSet.contains(idx)) {
                                if(!value.trim().isEmpty()) {
                                    csvRowBuilder.append(value.replaceAll(",", ";"));
                                }
                            }
                            else if(null!=dobColumnsIndexesSet && dobColumnsIndexesSet.contains(idx)) {
                                if(!value.trim().isEmpty()) {
                                    csvRowBuilder.append(EncryptionUtil.decryptPin(value));
                                }
                            }
                            else if(null!=balanceColumnsIndexesSet && balanceColumnsIndexesSet.contains(idx)) {
                                if(!value.trim().isEmpty()) {
                                    csvRowBuilder.append(EncryptionUtil.decryptPin(value));
                                }

                            }else if(null!=accountNoColumnsIndexesSet && accountNoColumnsIndexesSet.contains(idx)) {
                                if(!value.trim().isEmpty()) {
                                    csvRowBuilder.append(EncryptionUtil.decryptAccountNo(value));
                                }
                            }
                            else if(null!=cnicColumnsIndexesSet && cnicColumnsIndexesSet.contains(idx)) {
                                if(!value.trim().isEmpty()) {
                                    csvRowBuilder.append(EncryptionUtil.decryptPin(value));
                                }
                            }
                            else 
                            {
                            	if (StringUtils.contains(value,"<br/>")) {
                            		value = StringUtils.replace(value, "<br/>"," ");
                     	        }
                            	
                                csvRowBuilder.append(value);
                            }
                        }
                        csvRowBuilder.append(',');
                    }
                    if(csvRowBuilder.length() > 0) {
                        csvRowBuilder.deleteCharAt(csvRowBuilder.length() - 1);
                    }
                    writer.write(csvRowBuilder.toString());
                    writer.newLine();
                    csvRowBuilder.setLength(0);
                }
            }
            else
            {
                while(rowSet.next()) {
                    for(int idx = 1; idx <= columnCount; idx++) {
                        Object val = rowSet.getObject(idx);
                        if(val != null) {
                            csvRowBuilder.append(val);
                        }
                        csvRowBuilder.append(',');
                    }
                    csvRowBuilder.deleteCharAt(csvRowBuilder.length() - 1);
                    writer.write(csvRowBuilder.toString());
                    writer.newLine();
                    csvRowBuilder.setLength(0);
                }
            }

            if(totalsRowSet != null && totalsRowSet.next())
            {
                csvRowBuilder.append("Total:");
                for(int idx = 1; idx <= columnCount; idx++)
                {
                    csvRowBuilder.append(totalsRowSet.getString(idx));
                    csvRowBuilder.append(',');
                }
                if(csvRowBuilder.length() > 0)
                {
                    csvRowBuilder.deleteCharAt(csvRowBuilder.length()-1);
                }
                writer.write(csvRowBuilder.toString());//append totals row
            }
        } catch(Exception ex) {
            ex.printStackTrace();
        }
        return file.getPath();
    }

    private Set<Integer> prepareEscapeCommasColumnsIndexesSet(String escapeCommasColumnsIndexes)
    {
        Set<Integer> escapeCommasColumnsIndexesSet = null;
        if(!GenericValidator.isBlankOrNull(escapeCommasColumnsIndexes))
        {
            String[] columnsIndexes = escapeCommasColumnsIndexes.split(",");
            escapeCommasColumnsIndexesSet = new HashSet<>(columnsIndexes.length);
            for(String columnIndex :columnsIndexes)
            {
                escapeCommasColumnsIndexesSet.add(Integer.valueOf(columnIndex));
            }
        }
        return escapeCommasColumnsIndexesSet;
    }

    private Set<Integer> prepareColumnsIndexesSet(String columnsIndexesStr) {
        Set<Integer> columnsIndexesSet = null;
        if (!GenericValidator.isBlankOrNull(columnsIndexesStr)) {
            String[] columnsIndexes = columnsIndexesStr.split(",");
            columnsIndexesSet = new HashSet<>(columnsIndexes.length);
            for (String columnIndex : columnsIndexes) {
                columnsIndexesSet.add(Integer.valueOf(columnIndex));
            }
        }
        return columnsIndexesSet;
    }
}
