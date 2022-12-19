package com.inov8.jsblconsumer.model;

import java.util.ArrayList;

public class SupplierModel {

	private String name;
	private ArrayList<ProductModel> productList;

	public SupplierModel(String name, ArrayList<ProductModel> productList) {
		super();
		this.name = name;
		this.productList = productList;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<ProductModel> getPrd() {
		return productList;
	}

	public void setPrd(ArrayList<ProductModel> prd) {
		this.productList = prd;
	}

}
