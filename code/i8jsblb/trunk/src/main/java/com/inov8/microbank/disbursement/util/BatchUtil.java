package com.inov8.microbank.disbursement.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by AtieqRe on 2/15/2017.
 */
public class BatchUtil {
    public static int DB_FETCH_SIZE = 1000;
    public static int DB_BATCH_SIZE = 1000;
    public static int DB_COMMIT_SIZE = 1000;
    public static final int FIXED_THREAD_POOL =15;
    public static final int TOTAL_SEQUENCES =12;
    public static final String ENC_ZERO = "0";
    public static final String ENC_DATE_01_01_1970 = "01/01/1970";

    private static Properties prop;
    static {
        try (InputStream is = BatchUtil.class.getClassLoader().getResourceAsStream("applicationJDBC.properties")) {
            prop = new Properties();
            if(is != null)
                prop.load(is);
        }

        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getProperty(String key){
        return prop.getProperty(key);
    }

    public static String getDbUsername() {
        return prop.getProperty("datasource.username");
    }

    public static String getDbPassword() {
        return prop.getProperty("datasource.password");
    }

    public static String getDbURL() {
        return prop.getProperty("datasource.url");
    }

    public static int getDbCommitSize() {
        return getIntValue("db.commit.size", DB_COMMIT_SIZE);
    }

    public static int getDbBatchSize() {
        return getIntValue("db.batch.size", DB_BATCH_SIZE);
    }

    public static int getDbFetchSize() {
        return getIntValue("db.fetch.size", DB_FETCH_SIZE);
    }

    private static int getIntValue(String key, int defaultVal) {
        String str = prop.getProperty(key);
        if(str != null && str.trim().length() != 0) {
            try{
                defaultVal = Integer.parseInt(str);
            }
            catch (Exception e) {
            }
        }

        return defaultVal;
    }
}
