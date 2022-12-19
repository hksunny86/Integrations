package com.inov8.export.model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.util.Map;

/**
 * Created by Hassan Javaid on 5/30/2016.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class ExportInformationModel
{
    private String view;
    private String username;
    private String email;
    private Long appUserId;
    private Long reportId;
    private String exportDirPath;
    private String exportFileName;
    private String columnsProps;
    private String columnsTitles;
    private String propsFormats;
    private String escapeCommasColumnsIndexes;
    private String balaceCellColumnsIndexes;
    private String cnicCellColumnsIndexes;
    private String accountCellColumnsIndexes;
    private String dobCellColumnsIndexes;

    private Map<String,String> propsFormatsMap;

    public ExportInformationModel()
    {
        super();
    }

    public String getView()
    {
        return view;
    }

    public void setView(String view)
    {
        this.view = view;
    }

    public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getExportDirPath()
    {
        return exportDirPath;
    }

    public void setExportDirPath(String exportDirPath)
    {
        this.exportDirPath = exportDirPath;
    }

    public String getExportFileName()
    {
        return exportFileName;
    }

    public void setExportFileName(String exportFileName)
    {
        this.exportFileName = exportFileName;
    }

    public String getColumnsProps()
    {
        return columnsProps;
    }

    public void setColumnsProps(String columnsProps)
    {
        this.columnsProps = columnsProps;
    }

    public String getColumnsTitles()
    {
        return columnsTitles;
    }

    public void setColumnsTitles(String columnsTitles)
    {
        this.columnsTitles = columnsTitles;
    }

    public String getPropsFormats()
    {
        return propsFormats;
    }

    public void setPropsFormats(String propsFormats)
    {
        this.propsFormats = propsFormats;
    }

    public String getEscapeCommasColumnsIndexes()
    {
        return escapeCommasColumnsIndexes;
    }

    public void setEscapeCommasColumnsIndexes(String escapeCommasColumnsIndexes)
    {
        this.escapeCommasColumnsIndexes = escapeCommasColumnsIndexes;
    }

    public String getBalaceCellColumnsIndexes() {
		return balaceCellColumnsIndexes;
	}

	public void setBalaceCellColumnsIndexes(String balaceCellColumnsIndexes) {
		this.balaceCellColumnsIndexes = balaceCellColumnsIndexes;
	}

	public String getAccountCellColumnsIndexes() {
		return accountCellColumnsIndexes;
	}

	public void setAccountCellColumnsIndexes(String accountCellColumnsIndexes) {
		this.accountCellColumnsIndexes = accountCellColumnsIndexes;
	}

	public Map<String, String> getPropsFormatsMap()
    {
        return propsFormatsMap;
    }

    public void setPropsFormatsMap(Map<String, String> propsFormatsMap)
    {
        this.propsFormatsMap = propsFormatsMap;
    }

    public String getFormat(String property)
    {
        if(null!=propsFormatsMap)
        	return propsFormatsMap.get(property);
        else 
        	return null;
    }

	public Long getAppUserId() {
		return appUserId;
	}

	public void setAppUserId(Long appUserId) {
		this.appUserId = appUserId;
	}

	public Long getReportId() {
		return reportId;
	}

	public void setReportId(Long reportId) {
		this.reportId = reportId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

    public String getCnicCellColumnsIndexes() {
        return cnicCellColumnsIndexes;
    }

    public void setCnicCellColumnsIndexes(String cnicCellColumnsIndexes) {
        this.cnicCellColumnsIndexes = cnicCellColumnsIndexes;
    }

    public String getDobCellColumnsIndexes() {
        return dobCellColumnsIndexes;
    }

    public void setDobCellColumnsIndexes(String dobCellColumnsIndexes) {
        this.dobCellColumnsIndexes = dobCellColumnsIndexes;
    }
}
