/**
 * 
 */
package com.inov8.microbank.server.service.portal.dayendsettlement;

import java.util.List;

import com.inov8.microbank.common.vo.dayendsettlement.DayEndSettlementVo;

/**
 * @author NaseerUl
 *
 */
public interface DayEndSettlementManager
{
	List<DayEndSettlementVo> searchDayEndSettlementFiles();
}
