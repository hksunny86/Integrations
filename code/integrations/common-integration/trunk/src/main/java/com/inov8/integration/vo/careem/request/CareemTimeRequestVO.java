package com.inov8.integration.vo.careem.request;

import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * Created by Muhammad Ali on 12/21/2016.
 */
public class CareemTimeRequestVO implements Serializable {
    private static final long serialVersionUID = 2003476737933146539L;

    @Pattern(regexp = "^(\\+|-)?(?:90(?:(?:\\.0{1,6})?)|(?:[0-9]|[1-8][0-9])(?:(?:\\.[0-9]{1,6})?))$" ,message = "Latitude is not correct")
    private String start_latitude;
    @Pattern(regexp = "^(\\+|-)?(?:180(?:(?:\\.0{1,6})?)|(?:[0-9]|[1-9][0-9]|1[0-7][0-9])(?:(?:\\.[0-9]{1,6})?))$" ,message = "Longitude is not correct")
    private String start_longitude;
    @Min(value = 2 ,message = "product id cannot be this low")
    private int product_id;

    public float getStart_latitude() {
        return Float.parseFloat(start_latitude);
    }

    public void setStart_latitude(String start_latitude) {
        this.start_latitude = start_latitude;
    }

    public float getStart_longitude() {
        return Float.parseFloat(start_longitude);
    }

    public void setStart_longitude(String start_longitude) {
        this.start_longitude = start_longitude;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }
}
