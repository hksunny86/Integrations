package com.inov8.microbank.server.service.dailyjob;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.log4j.Logger;

import com.inov8.framework.common.model.BasePersistableModel;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.server.dao.framework.GenericDAO;
import com.inov8.microbank.common.model.AgentMonthlyTxStatsViewModel;
import com.inov8.microbank.common.model.messagemodule.SmsMessage;
import com.inov8.microbank.common.util.MessageUtil;
import com.inov8.microbank.common.util.SmsSender;

/** 
 * Created By    : NOT Naseer Ullah <br>
 * Creation Date : Dec 20, 2013 12:48:51 PM<p>
 * Purpose       : <p>
 * Updated By    : <br>
 * Updated Date  : <br>
 * Comments      : <br>
 */
public class AgentMonthlyTxStatsScheduler
{
    private static final Logger LOGGER = Logger.getLogger( AgentMonthlyTxStatsScheduler.class );

    private GenericDAO genericDao;
    private SmsSender smsSender;

    public AgentMonthlyTxStatsScheduler()
    {
    }

    public void init()
    {
        LOGGER.info("*********** Executing AgentMonthlyTxStatsScheduler ***********");
        try
        {
            CustomList<BasePersistableModel> customList = genericDao.findAll( AgentMonthlyTxStatsViewModel.class );
            if( customList != null && customList.getResultsetList() != null && customList.getResultsetList().size() > 0 ) {
                Calendar calendar = GregorianCalendar.getInstance();
                calendar.add( Calendar.MONTH, -1 );
                String date = DateFormatUtils.format( calendar.getTimeInMillis(), "MMM" );

                String fromMobileNo = MessageUtil.getMessage( "agentTxStats.sms.fromMobileNo" );

                List<BasePersistableModel> list = customList.getResultsetList();
                long start = System.currentTimeMillis();
                for( BasePersistableModel basePersistableModel : list )
                {
                    AgentMonthlyTxStatsViewModel agentDailyTxStatsViewModel = (AgentMonthlyTxStatsViewModel) basePersistableModel;

                    SmsMessage smsMessage = new SmsMessage(null, null );
					smsMessage.setFrom( fromMobileNo );
                    smsMessage.setMobileNo( agentDailyTxStatsViewModel.getMobileNo() );                        

                    Object[] args = {date,
                                     agentDailyTxStatsViewModel.getP2pSent(), agentDailyTxStatsViewModel.getP2pSentAmount(), agentDailyTxStatsViewModel.getP2pSentCom(),
                                     agentDailyTxStatsViewModel.getP2pRecieved(), agentDailyTxStatsViewModel.getP2pRecAmount(), agentDailyTxStatsViewModel.getP2pRecCom(),
                                     agentDailyTxStatsViewModel.getBillPayment(), agentDailyTxStatsViewModel.getBillPaymentAmount(), agentDailyTxStatsViewModel.getBillPaymentCom()};

                    String message = MessageUtil.getMessage( "agentMonthlyTxStats.sms.text", args );
                    smsMessage.setMessageText( message );
                    smsSender.send( smsMessage );
                }
                long end = System.currentTimeMillis();
                LOGGER.info( "Time(ms) taken to push " + list.size() +" SMS to JMS Queue: " + (end-start) );
            }
            else
            {
                LOGGER.info("No monthly agent transaction stats found.");
            }
            
        }
        catch( Exception e ) 
        {
            LOGGER.error( e.getMessage(), e );
        }
    }

    public void setGenericDao( GenericDAO genericDao )
    {
        this.genericDao = genericDao;
    }

    public void setSmsSender( SmsSender smsSender )
    {
        this.smsSender = smsSender;
    }

}
