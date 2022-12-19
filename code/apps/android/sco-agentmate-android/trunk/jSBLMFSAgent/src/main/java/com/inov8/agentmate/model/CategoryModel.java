package com.inov8.agentmate.model;

import java.io.Serializable;
import java.util.ArrayList;

public class CategoryModel implements Serializable {
	private static final long serialVersionUID = 1L;
	private String id;
	private String name;
	private String icon;
	private String isProduct;
	private ArrayList<ProductModel> productList;
	private ArrayList<CategoryModel> categoryList;
	private ArrayList<SupplierModel> supplierList;
	private CategoryModel parentCategory;
	
	public CategoryModel(String id, String name, String icon, String isProduct,
			ArrayList<ProductModel> productList, ArrayList<CategoryModel> categoryList, ArrayList<SupplierModel> supplierList) {
		super();
		this.id = id;
		this.name = name;
		this.icon = icon;
		this.isProduct = isProduct;
		this.productList = productList;
		this.categoryList = categoryList;
		this.supplierList = supplierList;
	}

	public CategoryModel(String id, String name,
			String icon,String isProduct, CategoryModel prevCat) {
		super();
		this.id = id;
		this.name = name;
		this.icon = icon;
		this.isProduct = isProduct;

		this.setParentCategory(prevCat);
	}

	
	public void addCategory(CategoryModel category) {
		if (this.categoryList == null) {
			this.categoryList = new ArrayList<CategoryModel>();
		}		
		this.categoryList.add(category);
	}
	
	public void addProduct(ProductModel product) {
		if (this.productList == null) {
			this.productList = new ArrayList<ProductModel>();
		}		
		this.productList.add(product);
	}
	
	public void addSupplier(SupplierModel supplier) {
		if (this.supplierList == null) {
			this.supplierList = new ArrayList<SupplierModel>();
		}		
		this.supplierList.add(supplier);
	}
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getIsProduct() {
		return isProduct;
	}

	public void setIsProduct(String isProduct) {
		this.isProduct = isProduct;
	}
	
	public ArrayList<ProductModel> getProductList() {
		return productList;
	}

	public void setProductList(ArrayList<ProductModel> productList) {
		this.productList = productList;
	}

	public ArrayList<CategoryModel> getCategoryList() {
		return categoryList;
	}

	public void setCategoryList(ArrayList<CategoryModel> categoryList) {
		this.categoryList = categoryList;
	}

	public ArrayList<SupplierModel> getSupplierList() {
		return supplierList;
	}

	public void setSupplierList(ArrayList<SupplierModel> supplierList) {
		this.supplierList = supplierList;
	}

	public CategoryModel getParentCategory() {
		return parentCategory;
	}

	public void setParentCategory(CategoryModel parentCategory) {
		this.parentCategory = parentCategory;
	}
}