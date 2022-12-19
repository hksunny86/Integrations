package com.inov8.microbank.common.util;

import java.util.Comparator;

import com.inov8.microbank.common.model.productmodule.ProductListViewModel;

public class ProductsComparator implements Comparator<ProductListViewModel>{

	public int compare(ProductListViewModel madel1, ProductListViewModel madel2) {
		return madel1.getSequenceNo().compareTo(madel2.getSequenceNo());
	}

}
