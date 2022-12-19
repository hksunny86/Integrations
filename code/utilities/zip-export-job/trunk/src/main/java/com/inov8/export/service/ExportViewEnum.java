package com.inov8.export.service;

import com.inov8.export.model.ExportInformationModel;
import com.inov8.export.model.ExportRequestModel;
import com.inov8.export.view.CsvView;
import com.inov8.export.view.CustomXlsView;
import com.inov8.export.view.CustomXlsxView;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by NaseerUl on 9/9/2016.
 */
public enum ExportViewEnum
{
    CSV
    {
        @Override
        public String export(ExportRequestModel exportRequestModel, ExportInformationModel exportInfoModel, ResultSet rowSet, ResultSet totalsRowSet, Connection connection) throws Exception
        {
            CsvView csvView = new CsvView();
            return csvView.export(exportRequestModel,exportInfoModel,rowSet,totalsRowSet);
        }
    },
    XLS
    {
        @Override
        public String export(ExportRequestModel exportRequestModel, ExportInformationModel exportInfoModel, ResultSet rowSet, ResultSet totalsRowSet, Connection connection) throws Exception
        {
            CustomXlsView customXlsView = new CustomXlsView();
            return customXlsView.export(exportRequestModel,exportInfoModel,rowSet,totalsRowSet, connection);
        }
    },
    XLSX
    {
        @Override
        public String export(ExportRequestModel exportRequestModel, ExportInformationModel exportInfoModel, ResultSet rowSet, ResultSet totalsRowSet, Connection connection) throws Exception
        {
            CustomXlsxView customXlsxView = new CustomXlsxView();
            return customXlsxView.export(exportRequestModel,exportInfoModel,rowSet,totalsRowSet, connection);
        }
    };

    ExportViewEnum()
    {

    }
    /**
     * Exports the data and returns exPorted file path
     * @throws SQLException
     */
    public abstract String export(ExportRequestModel exportRequestModel, ExportInformationModel exportInfoModel, ResultSet rowSet, ResultSet totalsRowSet, Connection connection) throws Exception;
}
