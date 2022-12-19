package com.inov8.integration.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.inov8.integration.middleware.bookme.pdu.*;
import com.inov8.microbank.server.service.switchmodule.IntegrationMessageVO;
import com.inov8.microbank.server.service.switchmodule.iris.model.CustomerAccount;

import java.util.Collections;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BookmeMiddlewareVO implements IntegrationMessageVO {

    private static final long serialVersionUID = -7259889967771862968L;

    private String responseCode;
    private String responseDescription;
    private String microbankTransactionCode;
    private String merchantId;

    private String orderId;
    private String statusId;

    private String cityId;
    private String movieId;
    private String bookingDate;

    private String cinemaId;
    private String showId;
    private String seats;
    private String name;
    private String email;
    private String phone;
    private String city;
    private String houseNo;
    private String block;
    private String streetNo;
    private String saveAddress;
    private String area;
    private String bookingReferenceNo;
    private String address;
    private String showType;
    private String bookingType;
    private FlexiFares flexiFares;

    public FlexiFares getFlexiFares() {
        return flexiFares;
    }

    public void setFlexiFares(FlexiFares flexiFares) {
        this.flexiFares = flexiFares;
    }

    private BookMeCinemaTicketSeatPlan bookMeCinemaTicketSeatPlan;
    private String sessionId;
    private String userId;
    private String paymentMethod;
    private String deviceTypeId;
    private String accountNumber;
    private String transactionAmount;
    private String pin;
    private String chargesType;
    private Movie movie;
    private String accountTitle;
    private String eventId;
    private String date;
    private String tickets;
    private String cnic;
    private String amount;
    private String departureTime;
    private String arrivalTime;
    private String scheduleId;
    private String routeId;
    private String latitude;
    private String longitude;
    private String ticketPrice;
    private String total_price;
    private BookMeEventBooking bookMeEventBooking;
    private SeatInfo seatInfo;
    private String transactionCharges;
    private String totalAmount;
    private String codCharges;
    private String eticketCharges;
    private String priceId;

    private String cod;

    public String getCod() {
        return cod;
    }

    public void setCod(String cod) {
        this.cod = cod;
    }

    private String paymentType;

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getCodCharges() {
        return codCharges;
    }

    public void setCodCharges(String codCharges) {
        this.codCharges = codCharges;
    }

    public String getEticketCharges() {
        return eticketCharges;
    }

    public void setEticketCharges(String eticketCharges) {
        this.eticketCharges = eticketCharges;
    }

    public String getTransactionCharges() {
        return transactionCharges;
    }

    public void setTransactionCharges(String transactionCharges) {
        this.transactionCharges = transactionCharges;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BookMeEventBooking getBookMeEventBooking() {
        return bookMeEventBooking;
    }

    public void setBookMeEventBooking(BookMeEventBooking bookMeEventBooking) {
        this.bookMeEventBooking = bookMeEventBooking;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String event_id) {
        this.eventId = event_id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTickets() {
        return tickets;
    }

    public void setTickets(String tickets) {
        this.tickets = tickets;
    }

    public String getCnic() {
        return cnic;
    }

    public void setCnic(String cnic) {
        this.cnic = cnic;
    }

    public String getAccountTitle() {
        return accountTitle;
    }

    public void setAccountTitle(String accountTitle) {
        this.accountTitle = accountTitle;
    }

    // Transport API
    private String serviceId;
    private String timeId;
    private String destinationCityId;
    private List<TransportService> transportServiceList;
    private List<DepartureCity> departureCityList;
    private List<DestinationCity> destinationCityList;
    private List<TransportTime> transportTimeList;
    private TransportBillingInfo transportBillingInfo;
    private TransportBooking transportBooking;

    private List<MovieCity> movieCityList;
    private List<Movie> movieList;
    private CinemaBooking cinemaBooking;

    private OrderDetailVO orderDetailVO;


    private List<Cinema> cinemaList;
    private List<ShowDates> showDateList;
    private List<ShowTimes> showTimeList;
    private List<EventCities> eventCitiesList;
    private BookMeEvents bookMeEvents;

    @JsonProperty("seat_numbers_male")
    private String seat_numbers_male;

    @JsonProperty("seat_numbers_female")
    private String seat_numbers_female;

    @JsonProperty("seat_numbers")
    private String seat_numbers;

    public String getSeat_numbers() {
        return seat_numbers;
    }

    public void setSeat_numbers(String seat_numbers) {
        this.seat_numbers = seat_numbers;
    }

    public String getSeat_numbers_male() {
        return seat_numbers_male;
    }

    public void setSeat_numbers_male(String seat_numbers_male) {
        this.seat_numbers_male = seat_numbers_male;
    }

    public String getSeat_numbers_female() {
        return seat_numbers_female;
    }

    public void setSeat_numbers_female(String seat_numbers_female) {
        this.seat_numbers_female = seat_numbers_female;
    }

    public BookMeEvents getBookMeEvents() {
        return bookMeEvents;
    }

    public void setBookMeEvents(BookMeEvents bookMeEvents) {
        this.bookMeEvents = bookMeEvents;
    }

    public List<EventCities> getEventCitiesList() {
        return eventCitiesList;
    }

    public void setEventCitiesList(List<EventCities> eventCitiesList) {
        this.eventCitiesList = eventCitiesList;
    }

    private String productId;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    private String sharedSession;

    public String getSharedSession() {
        return sharedSession;
    }

    public void setSharedSession(String sharedSession) {
        this.sharedSession = sharedSession;
    }

    @Override
    public String getPaymentGatewayCode() {
        return null;
    }

    @Override
    public void setPaymentGatewayCode(String code) {

    }

    public String getPriceId() {
        return priceId;
    }

    public void setPriceId(String priceId) {
        this.priceId = priceId;
    }

    @Override
    public String getMicrobankTransactionCode() {
        return microbankTransactionCode;
    }

    @Override
    public void setMicrobankTransactionCode(String transactionCode) {
        this.microbankTransactionCode = transactionCode;
    }

    @Override
    public String getSystemTraceAuditNumber() {
        return null;
    }

    @Override
    public String getTransmissionDateAndTime() {
        return null;
    }

    @Override
    public String getMessageAsEdi() {
        return null;
    }

    @Override
    public String getRetrievalReferenceNumber() {
        return null;
    }

    @Override
    public List<CustomerAccount> getCustomerAccounts() {
        return null;
    }

    @Override
    public String getSecureVerificationData() {
        return null;
    }

    @Override
    public void setSecureVerificationData(String secureVerificationData) {

    }

    public BookMeCinemaTicketSeatPlan getBookMeCinemaTicketSeatPlan() {
        return bookMeCinemaTicketSeatPlan;
    }

    public void setBookMeCinemaTicketSeatPlan(
            BookMeCinemaTicketSeatPlan bookMeCinemaTicketSeatPlan) {
        this.bookMeCinemaTicketSeatPlan = bookMeCinemaTicketSeatPlan;
    }


    @Override
    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    @Override
    public String getIvrChannelStatus() {
        return null;
    }

    @Override
    public String getMobileChannelStatus() {
        return null;
    }

    @Override
    public long getTimeOutInterval() {
        return 0;
    }

    @Override
    public void setTimeOutInterval(long timeOut) {

    }

    public String getResponseDescription() {
        return responseDescription;
    }

    public void setResponseDescription(String responseDescription) {
        this.responseDescription = responseDescription;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public String getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(String bookingDate) {
        this.bookingDate = bookingDate;
    }

    public String getCinemaId() {
        return cinemaId;
    }

    public void setCinemaId(String cinemaId) {
        this.cinemaId = cinemaId;
    }

    public String getShowId() {
        return showId;
    }

    public void setShowId(String showId) {
        this.showId = showId;
    }

    public String getSeats() {
        return seats;
    }

    public void setSeats(String seats) {
        this.seats = seats;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHouseNo() {
        return houseNo;
    }

    public void setHouseNo(String houseNo) {
        this.houseNo = houseNo;
    }

    public String getBlock() {
        return block;
    }

    public void setBlock(String block) {
        this.block = block;
    }

    public String getStreetNo() {
        return streetNo;
    }

    public void setStreetNo(String streetNo) {
        this.streetNo = streetNo;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getBookingReferenceNo() {
        return bookingReferenceNo;
    }

    public void setBookingReferenceNo(String bookingReferenceNo) {
        this.bookingReferenceNo = bookingReferenceNo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getShowType() {
        return showType;
    }

    public void setShowType(String showType) {
        this.showType = showType;
    }

    public String getBookingType() {
        return bookingType;
    }

    public void setBookingType(String bookingType) {
        this.bookingType = bookingType;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getTimeId() {
        return timeId;
    }

    public void setTimeId(String timeId) {
        this.timeId = timeId;
    }

    public String getDestinationCityId() {
        return destinationCityId;
    }

    public void setDestinationCityId(String destinationCityId) {
        this.destinationCityId = destinationCityId;
    }

    public List<DepartureCity> getDepartureCityList() {
        return departureCityList;
    }

    public void setDepartureCityList(List<DepartureCity> departureCityList) {
        this.departureCityList = departureCityList;
    }

    public List<DestinationCity> getDestinationCityList() {
        return destinationCityList;
    }

    public void setDestinationCityList(List<DestinationCity> destinationCityList) {
        this.destinationCityList = destinationCityList;
    }

    public List<TransportTime> getTransportTimeList() {
        return transportTimeList;
    }

    public void setTransportTimeList(List<TransportTime> transportTimeList) {
        this.transportTimeList = transportTimeList;
    }

    public TransportBillingInfo getTransportBillingInfo() {
        return transportBillingInfo;
    }

    public void setTransportBillingInfo(TransportBillingInfo transportBillingInfo) {
        this.transportBillingInfo = transportBillingInfo;
    }

    public TransportBooking getTransportBooking() {
        return transportBooking;
    }

    public void setTransportBooking(TransportBooking transportBooking) {
        this.transportBooking = transportBooking;
    }

    public List<MovieCity> getMovieCityList() {
        return movieCityList;
    }

    public void setMovieCityList(List<MovieCity> movieCityList) {
        this.movieCityList = movieCityList;
    }

    public List<Movie> getMovieList() {
        return movieList;
    }

    public void setMovieList(List<Movie> movieList) {
        this.movieList = movieList;
    }

    public CinemaBooking getCinemaBooking() {
        return cinemaBooking;
    }

    public void setCinemaBooking(CinemaBooking cinemaBooking) {
        this.cinemaBooking = cinemaBooking;
    }

    public OrderDetailVO getOrderDetailVO() {
        return orderDetailVO;
    }

    public void setOrderDetailVO(OrderDetailVO orderDetailVO) {
        this.orderDetailVO = orderDetailVO;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public List<TransportService> getTransportServiceList() {
        return transportServiceList;
    }

    public void setTransportServiceList(List<TransportService> transportServiceList) {
        this.transportServiceList = transportServiceList;
    }

    public String getStatusId() {
        return statusId;
    }

    public void setStatusId(String statusId) {
        this.statusId = statusId;
    }

    public List<Cinema> getCinemaList() {
        return cinemaList;
    }

    public void setCinemaList(List<Cinema> cinemaList) {
        this.cinemaList = cinemaList;
    }

    public List<ShowDates> getShowDateList() {
        return showDateList;
    }

    public void setShowDateList(List<ShowDates> showDateList) {
        if(showDateList != null)
            Collections.sort(showDateList, new DatesComparator());
        this.showDateList = showDateList;
    }

    public List<ShowTimes> getShowTimeList() {
        return showTimeList;
    }

    public void setShowTimeList(List<ShowTimes> showTimeList) {
        if(showTimeList != null)
            Collections.sort(showTimeList, new TimesComparator());
        this.showTimeList = showTimeList;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getUserId() {
        return userId;
    }

    public String getSaveAddress() {
        return saveAddress;
    }

    public void setSaveAddress(String saveAddress) {
        this.saveAddress = saveAddress;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getDeviceTypeId() {
        return deviceTypeId;
    }

    public void setDeviceTypeId(String deviceTypeId) {
        this.deviceTypeId = deviceTypeId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(String transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    @Override
    public String toString() {
        return "BookmeMiddlewareVO{" +
                "responseCode='" + responseCode + '\'' +
                ", responseDescription='" + responseDescription + '\'' +
                ", microbankTransactionCode='" + microbankTransactionCode + '\'' +
                ", merchantId='" + merchantId + '\'' +
                ", orderId='" + orderId + '\'' +
                ", statusId='" + statusId + '\'' +
                ", cityId='" + cityId + '\'' +
                ", movieId='" + movieId + '\'' +
                ", bookingDate='" + bookingDate + '\'' +
                ", cinemaId='" + cinemaId + '\'' +
                ", showId='" + showId + '\'' +
                ", seats='" + seats + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", city='" + city + '\'' +
                ", houseNo='" + houseNo + '\'' +
                ", block='" + block + '\'' +
                ", streetNo='" + streetNo + '\'' +
                ", saveAddress=" + saveAddress +
                ", area='" + area + '\'' +
                ", bookingReferenceNo='" + bookingReferenceNo + '\'' +
                ", address='" + address + '\'' +
                ", showType='" + showType + '\'' +
                ", bookingType='" + bookingType + '\'' +
                ", bookMeCinemaTicketSeatPlan=" + bookMeCinemaTicketSeatPlan +
                ", sessionId='" + sessionId + '\'' +
                ", userId='" + userId + '\'' +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", deviceTypeId='" + deviceTypeId + '\'' +
                ", accountNumber='" + accountNumber + '\'' +
                ", transactionAmount='" + transactionAmount + '\'' +
                ", pin='" + pin + '\'' +
                ", chargesType='" + chargesType + '\'' +
                ", movie=" + movie +
                ", accountTitle='" + accountTitle + '\'' +
                ", eventId='" + eventId + '\'' +
                ", date='" + date + '\'' +
                ", tickets='" + tickets + '\'' +
                ", cnic='" + cnic + '\'' +
                ", amount='" + amount + '\'' +
                ", departureTime='" + departureTime + '\'' +
                ", arrivalTime='" + arrivalTime + '\'' +
                ", scheduleId='" + scheduleId + '\'' +
                ", routeId='" + routeId + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                ", ticketPrice='" + ticketPrice + '\'' +
                ", total_price='" + total_price + '\'' +
                ", bookMeEventBooking=" + bookMeEventBooking +
                ", seatInfo=" + seatInfo +
                ", serviceId='" + serviceId + '\'' +
                ", timeId='" + timeId + '\'' +
                ", destinationCityId='" + destinationCityId + '\'' +
                ", transportServiceList=" + transportServiceList +
                ", departureCityList=" + departureCityList +
                ", destinationCityList=" + destinationCityList +
                ", transportTimeList=" + transportTimeList +
                ", transportBillingInfo=" + transportBillingInfo +
                ", transportBooking=" + transportBooking +
                ", movieCityList=" + movieCityList +
                ", movieList=" + movieList +
                ", cinemaBooking=" + cinemaBooking +
                ", orderDetailVO=" + orderDetailVO +
                ", cinemaList=" + cinemaList +
                ", showDateList=" + showDateList +
                ", showTimeList=" + showTimeList +
                ", eventCitiesList=" + eventCitiesList +
                ", bookMeEvents=" + bookMeEvents +
                ", seat_numbers_male='" + seat_numbers_male + '\'' +
                ", seat_numbers_female='" + seat_numbers_female + '\'' +
                ", seat_numbers='" + seat_numbers + '\'' +
                ", productId='" + productId + '\'' +
                ", sharedSession='" + sharedSession + '\'' +
                '}';
    }

    /**
     * @return the chargesType
     */
    public String getChargesType() {
        return chargesType;
    }

    /**
     * @param chargesType the chargesType to set
     */
    public void setChargesType(String chargesType) {
        this.chargesType = chargesType;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public String getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(String scheduleId) {
        this.scheduleId = scheduleId;
    }

    public String getRouteId() {
        return routeId;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketPrice(String ticketPrice) {
        this.ticketPrice = ticketPrice;
    }

    public String getTotal_price() {
        return total_price;
    }

    public void setTotal_price(String total_price) {
        this.total_price = total_price;
    }

    public SeatInfo getSeatInfo() {
        return seatInfo;
    }

    public void setSeatInfo(SeatInfo seatInfo) {
        this.seatInfo = seatInfo;
    }
}
