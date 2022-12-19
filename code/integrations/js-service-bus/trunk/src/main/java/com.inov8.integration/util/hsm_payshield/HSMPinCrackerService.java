package com.inov8.integration.util.hsm_payshield;

import com.inov8.hsm.controller.IPayShieldSwitchController;
import com.inov8.integration.config.PropertyReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.remoting.httpinvoker.HttpInvokerProxyFactoryBean;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by inov8 on 9/17/2018.
 */
@Service
public class HSMPinCrackerService {

    private static Logger logger = LoggerFactory.getLogger(HSMPinCrackerService.class.getSimpleName());
    @Autowired(required = false)
    @Qualifier("HSMPayShieldJdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    private static IPayShieldSwitchController inov8PayShieldController1 = null;
    private static IPayShieldSwitchController inov8PayShieldController2 = null;
    private static IPayShieldSwitchController bankPayShieldController = null;

    @PostConstruct
    private void init() {
        if (PropertyReader.getProperty("service.status") != null && !PropertyReader.getProperty("service.status").equals("") &&
                PropertyReader.getProperty("service.status").equals("start")) {
            run();
        }
    }

    private void run() {
        try {
            logger.info("PIN cracker service - STARTED");
            inov8PayShieldController1 = getFromProxy(PropertyReader.getProperty("inov8.hsm-payshield.url1"));
            inov8PayShieldController2 = getFromProxy(PropertyReader.getProperty("inov8.hsm-payshield.url2"));
            bankPayShieldController = getFromProxy(PropertyReader.getProperty("bank.hsm-payshield.url"));

            jdbcTemplate.query(PropertyReader.getProperty("accountInfo.select.sql"), new ResultSetExtractor<String>() {

                public String extractData(ResultSet rs) throws SQLException, DataAccessException {

                    int threadCount = Integer.parseInt(PropertyReader.getProperty("thread.count"));
                    ExecutorService executor = Executors.newFixedThreadPool(threadCount);
                    int threadCounter = 1;
                    boolean hsm1or2 = true;
                    while (rs.next()) {
                        HSMPinCrackerThread hsmPinCrackerThread;
                        if (hsm1or2) {
                            hsmPinCrackerThread = new HSMPinCrackerThread(jdbcTemplate, inov8PayShieldController1, bankPayShieldController);
                            hsm1or2 = false;
                        } else {
                            hsmPinCrackerThread = new HSMPinCrackerThread(jdbcTemplate, inov8PayShieldController2, bankPayShieldController);
                            hsm1or2 = true;
                        }
                        hsmPinCrackerThread.setName("Thread-" + threadCounter);
                        hsmPinCrackerThread.setAccountInfoId(rs.getString("ACCOUNT_INFO_ID"));
                        hsmPinCrackerThread.setCustomerId(rs.getString("CUSTOMER_ID"));
                        hsmPinCrackerThread.setPan(rs.getString("PAN"));
                        hsmPinCrackerThread.setPin(rs.getString("PIN"));
                        hsmPinCrackerThread.setPvv(rs.getString("PVV"));

                        executor.execute(hsmPinCrackerThread);
                        //logger.info("Total threads added in executor service are " + threadCounter);
                        /*if (threadCounter == 10) {
                            break;
                        }*/
                        threadCounter++;
                    }

                    executor.shutdown();
                    while (!executor.isTerminated()) {
                    }

                    return "";
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        logger.info("PIN cracker service - STOPPED");
    }

    public static IPayShieldSwitchController getFromContext() {
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        IPayShieldSwitchController inov8PayShieldController = (IPayShieldSwitchController) context.getBean("middlewareswitch");
        return inov8PayShieldController;
    }

    public static IPayShieldSwitchController getFromProxy(String serverUrl) throws Exception {
        HttpInvokerProxyFactoryBean httpInvokerProxyFactoryBean = new HttpInvokerProxyFactoryBean();
        httpInvokerProxyFactoryBean.setServiceInterface(IPayShieldSwitchController.class);
        httpInvokerProxyFactoryBean.setServiceUrl(serverUrl);
        httpInvokerProxyFactoryBean.afterPropertiesSet();

        IPayShieldSwitchController inov8PayShieldController = (IPayShieldSwitchController) httpInvokerProxyFactoryBean.getObject();
        return inov8PayShieldController;
    }
}
