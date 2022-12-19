package com.inov8.microbank.common.util;

import java.util.ArrayList;
import java.util.List;

import org.extremecomponents.table.bean.Column;
import org.extremecomponents.table.calc.Calc;
import org.extremecomponents.table.core.TableModel;

import com.inov8.microbank.common.model.portal.agentreportsmodule.AgentTransactionSummaryViewModel;

public class DebitCreditTotalCalc implements Calc{

	@SuppressWarnings("unchecked")
	@Override
	public Number getCalcResult(TableModel tableModel, Column column) {
		Double result = 0.0d;
		if(tableModel.getCurrentRowBean() instanceof AgentTransactionSummaryViewModel){
			List<AgentTransactionSummaryViewModel> arr = (ArrayList<AgentTransactionSummaryViewModel>) tableModel.getCollectionOfPageBeans();
			for(AgentTransactionSummaryViewModel model : arr){
				result = result+model.getBankCreditAmountSum()+model.getBankDebitAmountSum();
			}
		}
		return result;
	}

}
