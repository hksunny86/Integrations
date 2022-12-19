package com.inov8.integration.vo.careem.response;

/**
 * Created by Muhammad Ali on 12/21/2016.
 */
public class CareemTimeResponseVO extends CareemResponseVO {
    private Times[] times;

    public Times[] getTimes() {
        return times;
    }

    public void setTimes(Times[] times) {
        this.times = times;
    }

    static class Times {
        private int product_id;
        private long eta;
        private String display_name;

        public int getProduct_id() {
            return product_id;
        }

        public void setProduct_id(int product_id) {
            this.product_id = product_id;
        }

        public long getEta() {
            return eta;
        }

        public void setEta(long eta) {
            this.eta = eta;
        }

        public String getDisplay_name() {
            return display_name;
        }

        public void setDisplay_name(String display_name) {
            this.display_name = display_name;
        }
    }
}
