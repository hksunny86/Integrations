package com.inov8.microbank.common.util;

import org.extremecomponents.table.bean.Column;
import org.extremecomponents.table.calc.Calc;
import org.extremecomponents.table.core.TableModel;

public class CustomCalc implements Calc{

	@Override
	public Number getCalcResult(TableModel tableModel, Column column) {
		Double result = 0.0d;
		if(column != null && column.getValue() != null && column.getValue() instanceof Double){
			result = (Double)column.getValue();
		}
		return result;
	}

}
