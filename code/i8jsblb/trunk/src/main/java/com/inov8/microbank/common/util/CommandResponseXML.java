package com.inov8.microbank.common.util;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "param" })
@XmlRootElement(name = "params")
public class CommandResponseXML {


    @XmlElement(required = true)
    protected List<Param> param;

    public List<Param> getParamList() {
        if (param == null) {
            param = new ArrayList<Param>();
        }
        return this.param;
    }

    public static class Param {

        protected String name;
        protected String value;

        public String getName() {
            return name;
        }

        @XmlAttribute
        public void setName(String name) {
            this.name = name;
        }

        public String getValue() {
            return value;
        }

        @XmlValue
        public void setValue(String value) {
            this.value = value;
        }

    }
}
