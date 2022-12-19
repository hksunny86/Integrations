package com.inov8.integration.middleware.bookme.pdu;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Movie implements Serializable {

    private static final long serialVersionUID = -4201139098743989054L;

    @JsonProperty("movie_id")
    private String movieId;
    @JsonProperty("imdb_id")
    private String imdbId;
    @JsonProperty("title")
    private String title;
    @JsonProperty("genre")
    private String genre;
    @JsonProperty("language")
    private String language;
    @JsonProperty("director")
    private String director;
    @JsonProperty("producer")
    private String producer;
    @JsonProperty("release_date")
    private String releaseDate;
    @JsonProperty("music_director")
    private String musicDirector;
    @JsonProperty("country")
    private String country;
    @JsonProperty("cast")
    private String cast;
    @JsonProperty("synopsis")
    private String synopsis;
    @JsonProperty("details")
    private String details;
    @JsonProperty("ranking")
    private String rank;
    @JsonProperty("length")
    private String length;
    @JsonProperty("trailer_link")
    private String trailerLink;
    @JsonProperty("thumbnail")
    private String thumbnail;
    @JsonProperty("date")
    private String date;
    @JsonProperty("booking_type")
    private String bookingType;
    @JsonProperty("points")
    private String points;
    @JsonProperty("update_date")
    private String updateDate;
    @JsonProperty("close_date")
    private String closeDate;
    @JsonProperty("status")
    private String status;
    @JsonProperty("shows")
    private List<ShowDetail> shows;


    @Override
    public String toString() {
        return "Movie{" +
                "movieId='" + movieId + '\'' +
                ", imdbId='" + imdbId + '\'' +
                ", title='" + title + '\'' +
                ", genre='" + genre + '\'' +
                ", language='" + language + '\'' +
                ", director='" + director + '\'' +
                ", producer='" + producer + '\'' +
                ", releaseDate='" + releaseDate + '\'' +
                ", musicDirector='" + musicDirector + '\'' +
                ", country='" + country + '\'' +
                ", cast='" + cast + '\'' +
                ", synopsis='" + synopsis + '\'' +
                ", details='" + details + '\'' +
                ", rank='" + rank + '\'' +
                ", length='" + length + '\'' +
                ", trailerLink='" + trailerLink + '\'' +
                ", thumbnail='" + thumbnail + '\'' +
                ", date='" + date + '\'' +
                ", bookingType='" + bookingType + '\'' +
                ", points='" + points + '\'' +
                ", updateDate='" + updateDate + '\'' +
                ", closeDate='" + closeDate + '\'' +
                ", status='" + status + '\'' +
                ", shows=" + shows +
                '}';
    }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public String getImdbId() {
        return imdbId;
    }

    public void setImdbId(String imdbId) {
        this.imdbId = imdbId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getProducer() {
        return producer;
    }

    public void setProducer(String producer) {
        this.producer = producer;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getMusicDirector() {
        return musicDirector;
    }

    public void setMusicDirector(String musicDirector) {
        this.musicDirector = musicDirector;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCast() {
        return cast;
    }

    public void setCast(String cast) {
        this.cast = cast;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getTrailerLink() {
        return trailerLink;
    }

    public void setTrailerLink(String trailerLink) {
        this.trailerLink = trailerLink;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getBookingType() {
        return bookingType;
    }

    public void setBookingType(String bookingType) {
        this.bookingType = bookingType;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public String getCloseDate() {
        return closeDate;
    }

    public void setCloseDate(String closeDate) {
        this.closeDate = closeDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<ShowDetail> getShows() {
        return shows;
    }

    public void setShows(List<ShowDetail> shows) {
        this.shows = shows;
    }
}
