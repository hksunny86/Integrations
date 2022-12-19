package com.inov8.integration.middleware.bookme.pdu;

/**
 * Created by Shoaib on 7/5/2017.
 */
public class Prices {

    private String id;

    private String inclusive;

    private String price_type;

    private String price;

    private String discount;

    public String getInclusive ()
    {
        return inclusive;
    }

    public void setInclusive (String inclusive)
    {
        this.inclusive = inclusive;
    }

    public String getPrice_type ()
    {
        return price_type;
    }

    public void setPrice_type (String price_type)
    {
        this.price_type = price_type;
    }

    public String getPrice ()
    {
        return price;
    }

    public void setPrice (String price)
    {
        this.price = price;
    }

    public String getDiscount ()
    {
        return discount;
    }

    public void setDiscount (String discount)
    {
        this.discount = discount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [inclusive = "+inclusive+", price_type = "+price_type+", price = "+price+", discount = "+discount+"]";
    }
}
