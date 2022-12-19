package com.inov8.microbank.webapp.action.common;

import java.util.Comparator;

import com.inov8.microbank.common.vo.BulkFilerNonFilerVO;

public class UpdateFilerInvalidRecordsComparator implements Comparator<BulkFilerNonFilerVO> {

	@Override
	public int compare(BulkFilerNonFilerVO o1, BulkFilerNonFilerVO o2) {
		return o1.getIsValid().compareTo(o2.getIsValid());
	}

}
