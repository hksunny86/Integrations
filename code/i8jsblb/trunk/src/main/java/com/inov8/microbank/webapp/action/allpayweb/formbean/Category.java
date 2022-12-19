package com.inov8.microbank.webapp.action.allpayweb.formbean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by Yasir Shabbir on 9/7/2016.
 */
public class Category implements Serializable, Comparable<Category> {
    private int categoryId;
    private String name;
    private int parentCategoryId;
    private int sequenceNumber;
    private List<Product> productList;


    private List<Category> childCategory;

    public Category() {
    }

    public Category(int categoryId) {
        this.categoryId = categoryId;
    }


    @Override
    public int hashCode() {
        return Objects.hash(categoryId);
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getParentCategoryId() {
        return parentCategoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public void setParentCategoryId(int parentCategoryId) {
        this.parentCategoryId = parentCategoryId;
    }

    public List<Category> getChildCategory() {
        return childCategory;
    }

    public void setChildCategory(List<Category> childCategory) {
        this.childCategory = childCategory;
    }

    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }

    public Integer getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(int sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public void addChildCategory(Category category) {
        if (this.childCategory == null) {
            this.childCategory = new ArrayList<>();
        }

        this.childCategory.add(category);
    }

    public void addProduct(Product product) {
        if (this.productList == null) {
            this.productList = new ArrayList<>();
        }

        this.productList.add(product);
    }

    @Override
    public int compareTo(Category o) {
        return this.getSequenceNumber().compareTo(o.getSequenceNumber());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return categoryId == category.categoryId;
    }


}


