package com.inov8.integration.middleware.cameoo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Java class for ArrayOfCardData complex type.
 * <p>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;complexType name="ArrayOfCardData">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="CardData" type="{http://tempuri.org/}CardData" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfCardData", propOrder = {
        "cardData"
})
public class ArrayOfCardData {

    @XmlElement(name = "CardData", nillable = true)
    protected List<CardData> cardData;

    /**
     * Gets the value of the cardData property.
     * <p>
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the cardData property.
     * <p>
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCardData().add(newItem);
     * </pre>
     * <p>
     * <p>
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CardData }
     */
    public List<CardData> getCardData() {
        if (cardData == null) {
            cardData = new ArrayList<CardData>();
        }
        return this.cardData;
    }

}
