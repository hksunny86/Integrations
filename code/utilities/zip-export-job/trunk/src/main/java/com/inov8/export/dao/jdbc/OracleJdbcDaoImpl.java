package com.inov8.export.dao.jdbc;

import com.inov8.export.dao.OracleJdbcDao;
import com.inov8.framework.common.model.ExportInfoModel;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.map.ObjectMapper;
import org.extremecomponents.table.core.TableConstants;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.engine.SessionFactoryImplementor;
import org.hibernate.engine.SessionImplementor;
import org.hibernate.impl.CriteriaImpl;
import org.hibernate.loader.OuterJoinLoader;
import org.hibernate.loader.criteria.CriteriaLoader;
import org.hibernate.loader.criteria.CriteriaQueryTranslator;
import org.hibernate.persister.entity.OuterJoinLoadable;
import org.hibernate.type.Type;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.orm.hibernate3.SessionFactoryUtils;

import javax.persistence.Column;
import javax.sql.DataSource;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Date;

/**
 * Created by NaseerUl on 9/3/2016.
 */
public class OracleJdbcDaoImpl implements OracleJdbcDao, ApplicationContextAware
{
    private static final Log LOGGER = LogFactory.getLog(OracleJdbcDaoImpl.class);

    private SessionFactory sessionFactory;

    private JdbcTemplate jdbcTemplate;

    public OracleJdbcDaoImpl()
    {
        super();
    }

    @Override
    public SqlRowSet queryForRowSet(ExportInfoModel exportInfoModel) throws Exception
    {
        DetachedCriteria detachedCriteria = exportInfoModel.getDetachedCriteria();
        Session session = SessionFactoryUtils.getSession(sessionFactory,true);
        Connection con = session.connection();
        
        // query for Row Set
        setColumnsProjection(exportInfoModel, session);
        String rowSetsql = toSql(detachedCriteria.getExecutableCriteria(session));
        //query for Total Row Set
        setSumsProjection(exportInfoModel, session);
        final String totalRowSetsql = toSql(detachedCriteria.getExecutableCriteria(session));
        
        Long appUserId =  exportInfoModel.getAppUserId();
        String currentDate = "to_date('" + DateFormatUtils.format(new Date(),"yyyy-MM-dd HH:mm:ss") + "','yyyy-MM-dd HH24:mi:ss')";
        
        String fromDate = null;
        String toDate=null;
        
        if(null!=exportInfoModel.getPackageCall()){
        	 fromDate = "to_date('" + DateFormatUtils.format(exportInfoModel.getFromDate(),"yyyy-MM-dd HH:mm:ss") + "','yyyy-MM-dd HH24:mi:ss')";
             toDate = "to_date('" + DateFormatUtils.format(exportInfoModel.getToDate(),"yyyy-MM-dd HH:mm:ss") + "','yyyy-MM-dd HH24:mi:ss')";
        }

        if (exportInfoModel.getReportId() != null && (exportInfoModel.getReportId().longValue() == 10L || exportInfoModel.getReportId().longValue() == 11L)){
            LOGGER.info("Report ID: " + exportInfoModel.getReportId() + ". Going to add decryption function to Card_no Column");
            rowSetsql = rowSetsql.replace("this_.CARD_NO", "P_ENCRYPT.DECRYPT_CARD_NO(this_.CARD_NO)");

        }

        LOGGER.info("***INSERTING NEW EXPORT REQUEST***"+rowSetsql);
        SessionFactoryUtils.releaseSession(session,sessionFactory);
        
        final String exportRequestInsertSql = "INSERT INTO EXPORT_REQUEST (EXPORT_REQUEST_ID,CREATED_ON,UPDATED_ON,FROM_DATE,TO_DATE,REPORT_ID,EXPORT_VIEW,QUERY,QUERY_ROWS,PACKAGE_CALL,DATE_TYPE,STATUS_ID,USER_NAME,EMAIL,CREATED_BY,UPDATED_BY,VERSION_NO) VALUES(EXPORT_REQUEST_SEQ.NEXTVAL,"+currentDate+","+currentDate+","+fromDate+","+toDate+",?,?,?,?,?,?,?,?,?,?,?,?)";
        
        int i = jdbcTemplate.update(exportRequestInsertSql, new Object[]{exportInfoModel.getReportId(),exportInfoModel.getView(),rowSetsql,totalRowSetsql,exportInfoModel.getPackageCall(),exportInfoModel.getDateType(),1L,exportInfoModel.getUsername(),exportInfoModel.getEmail(),appUserId,appUserId,0});
        
        LOGGER.info(exportRequestInsertSql);
        
        /// Updating Report MetaData
        
        String reportMetaData = getMetaDataJson(exportInfoModel);
        Clob reportMetaDataClob = con.createClob();
        reportMetaDataClob.setString(1, reportMetaData);
        
        final String checkMetaDataSql = "SELECT REPORTS_METADATA_ID FROM REPORTS_METADATA WHERE REPORT_ID =?";
        SqlRowSet rs =  jdbcTemplate.queryForRowSet(checkMetaDataSql,exportInfoModel.getReportId());
        
        if(rs.next())
        {
        	final String reportMetaDataUpdate = "UPDATE REPORTS_METADATA SET  METADATA =? WHERE REPORT_ID=?";
             LOGGER.info(reportMetaDataUpdate); 
            
             PreparedStatement pst = session.connection().prepareStatement(reportMetaDataUpdate);
             pst.setClob(1, reportMetaDataClob);
             pst.setLong(2, exportInfoModel.getReportId());
             pst.executeUpdate();
             pst.close();
             
        }
        else
        {
        	 final String reportMetaDataInsert = "INSERT INTO REPORTS_METADATA (REPORTS_METADATA_ID,CREATED_ON,UPDATED_ON,METADATA,REPORT_ID,CREATED_BY,UPDATED_BY,VERSION_NO) VALUES(REPORTS_METADATA_SEQ.NEXTVAL,"+currentDate+","+currentDate+",?,"+exportInfoModel.getReportId()+","+appUserId+","+appUserId+",0)";
             LOGGER.info(reportMetaDataInsert); 
             
             PreparedStatement pst = session.connection().prepareStatement(reportMetaDataInsert);
             pst.setClob(1, reportMetaDataClob);
             pst.executeUpdate();
             pst.close();
 
        }
     
        return null;
    }

    private String getMetaDataJson(ExportInfoModel exportInfoModel) throws Exception {
    	ObjectMapper mapper = new ObjectMapper();
  
	    String modelJsonString = null;
	    try 
	    {
	    	exportInfoModel.setUsername(null);
	    	exportInfoModel.setEmail(null);
	    	exportInfoModel.setView(null);
	    	exportInfoModel.setAppUserId(null);
	    	modelJsonString = mapper.writeValueAsString(exportInfoModel);
	    } catch (IOException e) {
	         e.printStackTrace();																																																																																																																																																																															
	         throw new Exception("ExportInfoModel Json Parsing Exception");
	    }
		return modelJsonString;
	}

	@Override
    public SqlRowSet queryForTotalsRowSet(ExportInfoModel exportInfoModel)
    {
        DetachedCriteria detachedCriteria = exportInfoModel.getDetachedCriteria();
        Session session = SessionFactoryUtils.getSession(sessionFactory,true);
        setSumsProjection(exportInfoModel, session);
        final String sql = toSql(detachedCriteria.getExecutableCriteria(session));
        SessionFactoryUtils.releaseSession(session,sessionFactory);
        
        
        return jdbcTemplate.queryForRowSet(sql);
    }

    private void setColumnsProjection(ExportInfoModel exportInfoModel, Session session)
    {
        DetachedCriteria detachedCriteria = exportInfoModel.getDetachedCriteria();
        detachedCriteria.setProjection(null);//reset column projections
        detachedCriteria.setResultTransformer(Criteria.ROOT_ENTITY);

        CriteriaImpl c = (CriteriaImpl) exportInfoModel.getDetachedCriteria().getExecutableCriteria(session);

        ProjectionList projectionList = Projections.projectionList();
        try {
            for( String property : exportInfoModel.getColumnsProps().split(",") )
            {
                String format = exportInfoModel.getFormat(property);
                if(format != null)
                {
                    Class<? extends Object> persistentClass = Class.forName(c.getEntityOrClassName());
                    final PropertyDescriptor propertyDescriptor = new PropertyDescriptor(property, persistentClass);
                    Column columnAnnotation = propertyDescriptor.getReadMethod().getAnnotation(Column.class);
                    if(columnAnnotation != null)
                    {
                        String columnName = columnAnnotation.name();
                        String sqlProjection = null;
                        if(TableConstants.CURRENCY.equals(format))
                        {
                            sqlProjection = "to_char("+columnName+",'FM999999999990.00') AS "+columnName;
                        }
                        else if(TableConstants.DATE.equals(format))
                        {
                            sqlProjection = "to_char("+columnName+",'dd/MM/yyyy HH24:mi') AS "+columnName;
                        }
                        projectionList.add(Projections.sqlProjection(sqlProjection, new String[] {columnName}, new Type[]{Hibernate.STRING}) );
                    }
                }
                else
                {
                    projectionList.add(Projections.property(property));
                }

            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }

        detachedCriteria.setProjection(projectionList);
    }

    private void setSumsProjection(ExportInfoModel exportInfoModel, Session session)
    {
        DetachedCriteria detachedCriteria = exportInfoModel.getDetachedCriteria();
        detachedCriteria.setProjection(null);//reset column projections
        detachedCriteria.setResultTransformer(Criteria.ROOT_ENTITY);

        CriteriaImpl c = (CriteriaImpl) exportInfoModel.getDetachedCriteria().getExecutableCriteria(session);

        ProjectionList projectionList = Projections.projectionList();
        try {
            for( String property : exportInfoModel.getColumnsProps().split(",") )
            {
                String format = exportInfoModel.getFormat(property);
                Class<? extends Object> persistentClass = Class.forName(c.getEntityOrClassName());
                final PropertyDescriptor propertyDescriptor = new PropertyDescriptor(property, persistentClass);
                Column columnAnnotation = propertyDescriptor.getReadMethod().getAnnotation(Column.class);
                if(columnAnnotation != null)
                {
                    String columnName = columnAnnotation.name();
                    String sqlProjection = null;
                    if(format != null && TableConstants.CURRENCY.equals(format))
                    {
                        sqlProjection = "to_char(sum(NVL("+columnName+",0)),'FM999999999990.00') AS "+columnName;
                    }
                    else
                    {
                        sqlProjection = "' ' AS "+columnName;
                    }
                    projectionList.add(Projections.sqlProjection(sqlProjection, new String[] {columnName}, new Type[]{Hibernate.STRING}) );
                }

            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }

        detachedCriteria.setProjection(projectionList);
    }

    public static String toSql(Criteria criteria){
        String sql="";
        Object[] parameters;
        try{
            CriteriaImpl c = (CriteriaImpl) criteria;
            SessionImplementor s = c.getSession();
            SessionFactoryImplementor factory = s.getFactory();
            String[] implementors = factory.getImplementors( c.getEntityOrClassName() );
            OuterJoinLoadable outerJoinLoadable = (OuterJoinLoadable)factory.getEntityPersister(implementors[0]);
            CriteriaLoader loader = new CriteriaLoader(outerJoinLoadable, factory, c, implementors[0], s.getEnabledFilters());
            Field f = OuterJoinLoader.class.getDeclaredField("sql");
            f.setAccessible(true);
            sql = (String)f.get(loader);
            Field fp = CriteriaLoader.class.getDeclaredField("translator");
            fp.setAccessible(true);
            CriteriaQueryTranslator translator = (CriteriaQueryTranslator) fp.get(loader);
            parameters = translator.getQueryParameters().getPositionalParameterValues();
        }
        catch(Exception e){
            throw new RuntimeException(e);
        }
        if (sql != null)
        {
            //int fromPosition = sql.indexOf(" from ");
            //sql = "\nSELECT * " + sql.substring(fromPosition);

            if (parameters != null && parameters.length > 0)
            {
                for (Object val : parameters)
                {
                    String value = "%";
                    if (val instanceof Boolean)
                    {
                        value = ((Boolean) val) ? "1" : "0";
                    }
                    else if (val instanceof String)
                    {
                        value = "'" + val + "'";
                    }
                    else if (val instanceof Number)
                    {
                        value = val.toString();
                    }
                    else if (val instanceof Class)
                    {
                        value = "'" + ((Class) val).getCanonicalName() + "'";
                    }
                    else if (val instanceof Date)
                    {
                        value = "to_date('" + DateFormatUtils.format((Date) val,"yyyy-MM-dd HH:mm:ss") + "','yyyy-MM-dd HH24:mi:ss')";
                    }
                    else if (val instanceof Enum)
                    {
                        value = "" + ((Enum) val).ordinal();
                    }
                    else
                    {
                        value = val.toString();
                    }
                    sql = StringUtils.replaceOnce(sql,"?",value);
                }
            }
        }
        return sql.replaceAll("left outer join", "\nleft outer join").replaceAll(
                " and ", "\nand ").replaceAll(" on ", "\non ").replaceAll("<>",
                "!=").replaceAll("<", " < ").replaceAll(">", " > ");
    }

    public void setSessionFactory(SessionFactory sessionFactory)
    {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException
    {
        DataSource dataSource;
        String dataSourceBeanId = "reportsDataSource";
        if(applicationContext.containsBean(dataSourceBeanId)) {
            dataSource = applicationContext.getBean(dataSourceBeanId, DataSource.class);
        } else {
            dataSourceBeanId = "dataSource";
            if(applicationContext.containsBean(dataSourceBeanId)) {
                dataSource = applicationContext.getBean(dataSourceBeanId, DataSource.class);
            } else {
                throw new BeanInitializationException("At least one bean of subtype of java.sql.DataSource with id reportsDataSource/dataSource is required");
            }
        }
        LOGGER.info("Reports Data Source Bean id is " + dataSourceBeanId);
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public static void main(String[] args) {
        String sql = "select this_.district as y0_, this_.TEHSIL as y1_, this_.FULL_NAME as y2_, this_.USER_ID as y3_, this_.NIC as y4_, this_.MOBILE_NO as y5_, this_.CARD_NO as y6_, this_.CUSTOMER_ACCOUNT_TYPE as y7_, this_.FULL_ADDRESS as y8_, to_char(DOB,'dd/MM/yyyy HH24:mi') AS DOB, this_.SEGMENT as y10_, this_.SERVICE_OP_NAME as y11_, this_.account_opened_by as y12_, to_char(account_opening_date,'dd/MM/yyyy HH24:mi') AS account_opening_date, this_.REGISTRATION_STATE as y14_, this_.ACCOUNT_ACTIVATED_BY as y15_, this_.ACCOUNT_ACTIVATED_BY_ID as y16_, this_.ACCOUNT_STATE_NAME as y17_, to_char(ACCOUNT_ACTIVATED_ON,'dd/MM/yyyy HH24:mi') AS ACCOUNT_ACTIVATED_ON, this_.IS_ACCOUNT_ENABLED as y19_, this_.IS_ACCOUNT_LOCKED as y20_, this_.IS_ACCOUNT_CLOSED as y21_, this_.IS_CREDENTIALS_EXPIRED as y22_, this_.ACCOUNT_ACTIVATION_CHANNEL as y23_, this_.NADRA_SESSION_ID as y24_, this_.TERMINAL_ID as y25_ from USER_INFO_LIST_VIEW this_ where (1=1) and this_.account_opening_date > =to_date('2014-05-05 00:00:00','yyyy-MM-dd HH24:mi:ss') order by this_.APP_USER_ID asc";
        sql = sql.replace("this_.CARD_NO", "P_ENCRYPT.DECRYPT_CARD_NO(this_.CARD_NO)");
        System.out.println(sql);

        String sql2 = "select this_.CARD_CATEGORY as y0_, this_.CARD_TYPE as y1_, this_.CARD_NO as y2_, this_.ACCOUNT_NO as y3_, to_char(ISSUANCE_DATE,'dd/MM/yyyy HH24:mi') AS ISSUANCE_DATE, to_char(EXPIRY_DATE,'dd/MM/yyyy HH24:mi') AS EXPIRY_DATE, this_.APP_USER_TYPE as y6_, this_.APP_USER_NAME as y7_, this_.MOBILE_NO as y8_, this_.SEGMENT as y9_, this_.CARD_STATUS as y10_, this_.CARD_STATE as y11_ from DEBIT_CARD_VIEW this_ where (1=1) and this_.ISSUANCE_DATE > =to_date('2016-05-05 00:00:00','yyyy-MM-dd HH24:mi:ss') order by this_.CREATED_ON desc";
        sql2 = sql2.replace("this_.CARD_NO", "P_ENCRYPT.DECRYPT_CARD_NO(this_.CARD_NO)");

        System.out.println(sql2);


    }
}
