package com.inov8.microbank.common.model;

import com.inov8.framework.common.model.BasePersistableModel;

import javax.persistence.*;

/**
 * Created by Attique on 11/5/2018.
 */


@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "APICITY_seq",sequenceName = "APICITY_seq", allocationSize=1)
@Table(name = "API_CITY")
public class ApiCityModel extends BasePersistableModel{

    private Long cityId;
    private String name;

    /**
     * Default constructor.
     */
    public ApiCityModel() {
    }

    /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
    @javax.persistence.Transient
    public Long getPrimaryKey() {
        return getCityId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
    @javax.persistence.Transient
    public void setPrimaryKey(Long primaryKey) {
        setCityId(primaryKey);
    }

    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    {
        String primaryKeyFieldName = "cityId";
        return primaryKeyFieldName;
    }

    @javax.persistence.Transient
    public String getPrimaryKeyParameter() {
        String parameters = "";
        parameters += "&cityId=" + getCityId();
        return parameters;
    }

    /**
     * Returns the value of the <code>cityId</code> property.
     *
     */
    @Column(name = "CITY_ID" , nullable = false )
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="APICITY_seq")
    public Long getCityId() {
        return cityId;
    }

    /**
     * Sets the value of the <code>cityId</code> property.
     *
     * @param cityId the value for the <code>cityId</code> property
     *
     */

    public void setCityId(Long cityId) {
        this.cityId = cityId;
    }

    /**
     * Returns the value of the <code>name</code> property.
     *
     */
    @Column(name = "NAME" , nullable = false , length=50 )
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
