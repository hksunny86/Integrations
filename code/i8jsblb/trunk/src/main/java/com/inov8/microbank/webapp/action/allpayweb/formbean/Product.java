package com.inov8.microbank.webapp.action.allpayweb.formbean;

/**
 * Created by Yasir Shabbir on 9/7/2016.
 */
public class Product
{

    private String name;
    private String url;
    private int categoryId;

    public Product(String name){
        this.name = name;
    }

    public Product() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Product product = (Product) o;

        return !(name != null ? !name.equals(product.name) : product.name != null);

    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }
}
