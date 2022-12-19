package com.inov8.microbank.server.dao.holidaymodule;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.common.model.HolidayModel;

public interface HolidayDAO extends BaseDAO<HolidayModel, Long> {
	
	public List<HolidayModel> getHolidayModelByDate(Date date);
	public List<HolidayModel> getHolidayModelByRange(Calendar firstDate, Calendar endDate);
}