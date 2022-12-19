package com.inov8.integration.vo.careem.response;
import com.inov8.integration.vo.careem.helper.*;


/**
 * Created by Muhammad Ali on 12/21/2016.
 */
public class CareemProductsResponseVO extends CareemResponseVO {
    private Products[] products;

    public Products[] getProducts() {
        return products;
    }

    public void setProducts(Products[] products) {
        this.products = products;
    }

    static class Products {
        private int product_id;
        private float capacity;
        private String display_name;
        private float display_order;
        private boolean availibility_now;
        private boolean availibility_later;
        private String image;
        private float minimum_time_to_book;
        private float maximum_time_to_cancel_now;
        private float maximum_time_to_cancel_later;
        private price_details price_details;

        public int getProduct_id() {
            return product_id;
        }

        public void setProduct_id(int product_id) {
            this.product_id = product_id;
        }

        public float getCapacity() {
            return capacity;
        }

        public void setCapacity(float capacity) {
            this.capacity = capacity;
        }

        public String getDisplay_name() {
            return display_name;
        }

        public void setDisplay_name(String display_name) {
            this.display_name = display_name;
        }

        public float getDisplay_order() {
            return display_order;
        }

        public void setDisplay_order(float display_order) {
            this.display_order = display_order;
        }

        public boolean isAvailibility_now() {
            return availibility_now;
        }

        public void setAvailibility_now(boolean availibility_now) {
            this.availibility_now = availibility_now;
        }

        public boolean isAvailibility_later() {
            return availibility_later;
        }

        public void setAvailibility_later(boolean availibility_later) {
            this.availibility_later = availibility_later;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public float getMinimum_time_to_book() {
            return minimum_time_to_book;
        }

        public void setMinimum_time_to_book(float minimum_time_to_book) {
            this.minimum_time_to_book = minimum_time_to_book;
        }

        public float getMaximum_time_to_cancel_now() {
            return maximum_time_to_cancel_now;
        }

        public void setMaximum_time_to_cancel_now(float maximum_time_to_cancel_now) {
            this.maximum_time_to_cancel_now = maximum_time_to_cancel_now;
        }

        public float getMaximum_time_to_cancel_later() {
            return maximum_time_to_cancel_later;
        }

        public void setMaximum_time_to_cancel_later(float maximum_time_to_cancel_later) {
            this.maximum_time_to_cancel_later = maximum_time_to_cancel_later;
        }

        public price_details getPrice_details() {
            return price_details;
        }

        public void setPrice_details(price_details price_details) {
            this.price_details = price_details;
        }
    }
}
