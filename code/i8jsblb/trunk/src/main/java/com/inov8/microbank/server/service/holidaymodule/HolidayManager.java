package com.inov8.microbank.server.service.holidaymodule;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.inov8.microbank.common.model.HolidayModel;

public interface HolidayManager {

	public HolidayModel getHolidayModelByDate(Date date);
	public List<HolidayModel> getHolidayModelByRange(Calendar firstDate, Calendar endDate);
	public void saveUpdate(HolidayModel holidayModel);
}