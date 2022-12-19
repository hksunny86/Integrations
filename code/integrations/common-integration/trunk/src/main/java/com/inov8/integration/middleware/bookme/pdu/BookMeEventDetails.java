package com.inov8.integration.middleware.bookme.pdu;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BookMeEventDetails implements Serializable {


    @JsonProperty("easypay_handling_charges_per_pass")
    private String easypayHandlingChargesPass;

    @JsonProperty("handling_charges_per_ticket")
    private String handlingChargesTicket;

    @JsonProperty("easypay_handling_charges")
    private String easypayHandlingCharges;

    private String cod;

    private Prices[] prices;

    @JsonProperty("event_id")
    private String eventId;

    @JsonProperty("cities")
    private EventDetailsCities[] eventCitiesList;

    private String title;

    private String duration;

    private String note;

    private String venue;

    private String detail;

    private String thumb;

    private String banner;
    @JsonProperty("background_img")
    private String backgroundImg;

    @JsonProperty("handling_charges")
    private String handlingCharges;

    @JsonProperty("event_dates")
    private String eventDates[];

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public EventDetailsCities[] getEventCitiesList() {
        return eventCitiesList;
    }

    public void setEventCitiesList(EventDetailsCities[] eventCitiesList) {
        this.eventCitiesList = eventCitiesList;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String[] getEventDates() {
        return eventDates;
    }

    public void setEventDates(String[] eventDates) {
        this.eventDates = eventDates;
    }

    public String getEasypayHandlingChargesPass() {
        return easypayHandlingChargesPass;
    }

    public void setEasypayHandlingChargesPass(String easypayHandlingChargesPass) {
        this.easypayHandlingChargesPass = easypayHandlingChargesPass;
    }

    public String getHandlingChargesTicket() {
        return handlingChargesTicket;
    }

    public void setHandlingChargesTicket(String handlingChargesTicket) {
        this.handlingChargesTicket = handlingChargesTicket;
    }

    public String getEasypayHandlingCharges() {
        return easypayHandlingCharges;
    }

    public void setEasypayHandlingCharges(String easypayHandlingCharges) {
        this.easypayHandlingCharges = easypayHandlingCharges;
    }

    public String getCod() {
        return cod;
    }

    public void setCod(String cod) {
        this.cod = cod;
    }

    public Prices[] getPrices() {
        return prices;
    }

    public void setPrices(Prices[] prices) {
        this.prices = prices;
    }

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    public String getBackgroundImg() {
        return backgroundImg;
    }

    public void setBackgroundImg(String backgroundImg) {
        this.backgroundImg = backgroundImg;
    }

    public String getHandlingCharges() {
        return handlingCharges;
    }

    public void setHandlingCharges(String handlingCharges) {
        this.handlingCharges = handlingCharges;
    }
}
