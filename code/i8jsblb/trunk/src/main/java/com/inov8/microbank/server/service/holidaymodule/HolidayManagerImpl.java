package com.inov8.microbank.server.service.holidaymodule;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.inov8.microbank.common.model.HolidayModel;
import com.inov8.microbank.server.dao.holidaymodule.HolidayDAO;

public class HolidayManagerImpl implements HolidayManager {

	private final static Log logger = LogFactory.getLog(HolidayManagerImpl.class);
	
	private HolidayDAO holidayDAO;

	
	/* (non-Javadoc)
	 * @see com.inov8.microbank.server.service.holidaymodule.HolidayManager#getHolidayModelByDate(java.util.Date)
	 */
	public HolidayModel getHolidayModelByDate(Date date) {
		
		HolidayModel holidayModel = null;
		
		List<HolidayModel> customList = holidayDAO.getHolidayModelByDate(date);

		if(customList != null && !customList.isEmpty()) {
			
			holidayModel = customList.get(0);
		}
		
		return holidayModel;
	}
	
	public List<HolidayModel> getHolidayModelByRange(Calendar firstDate, Calendar endDate) {
		
		return holidayDAO.getHolidayModelByRange(firstDate, endDate);
	}
	
	public void saveUpdate(HolidayModel holidayModel) {
		
		holidayDAO.saveOrUpdate(holidayModel);
		logger.info(holidayModel);
	}	
	
	
	public void setHolidayDAO(HolidayDAO holidayDAO) {
		this.holidayDAO = holidayDAO;
	}
}
