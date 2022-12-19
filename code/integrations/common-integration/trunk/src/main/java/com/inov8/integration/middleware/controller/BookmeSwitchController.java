package com.inov8.integration.middleware.controller;

import com.inov8.integration.vo.BookmeMiddlewareVO;

import java.io.Serializable;


public interface BookmeSwitchController extends Serializable {

    BookmeMiddlewareVO getMovieCityList(BookmeMiddlewareVO middlewareVO) throws RuntimeException;

    BookmeMiddlewareVO getPlayingMovies(BookmeMiddlewareVO middlewareVO) throws RuntimeException;

    BookmeMiddlewareVO getMovieShows(BookmeMiddlewareVO middlewareVO) throws RuntimeException;

    BookmeMiddlewareVO getMovieDetailWithShows(BookmeMiddlewareVO middlewareVO) throws RuntimeException;

    BookmeMiddlewareVO saveCinemaBooking(BookmeMiddlewareVO middlewareVO) throws RuntimeException;

    BookmeMiddlewareVO getTransportServices(BookmeMiddlewareVO middlewareVO) throws RuntimeException;

    BookmeMiddlewareVO getDepartureCityList(BookmeMiddlewareVO middlewareVO) throws RuntimeException;

    BookmeMiddlewareVO getDestinationCityList(BookmeMiddlewareVO middlewareVO) throws RuntimeException;

    BookmeMiddlewareVO getTransportInformation(BookmeMiddlewareVO middlewareVO) throws RuntimeException;

    BookmeMiddlewareVO getTransportBillingInfo(BookmeMiddlewareVO middlewareVO) throws RuntimeException;

    BookmeMiddlewareVO saveTransportOrder(BookmeMiddlewareVO middlewareVO) throws RuntimeException;

    BookmeMiddlewareVO orderInquiry(BookmeMiddlewareVO middlewareVO) throws RuntimeException;

    BookmeMiddlewareVO updateOrderStatus(BookmeMiddlewareVO middlewareVO) throws RuntimeException;


}
