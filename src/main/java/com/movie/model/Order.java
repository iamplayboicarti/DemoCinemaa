package main.java.com.movie.model;


import java.util.Date;

public class Order {
    private int id;
    private int movieId;
    private String seatIds;
    private String snackItems;
    private double totalPrice;
    private String paymentMethod;
    private Date orderDate;

    public Order(int id, int movieId, String seatIds, String snackItems, double totalPrice, String paymentMethod, Date orderDate) {
        this.id = id;
        this.movieId = movieId;
        this.seatIds = seatIds;
        this.snackItems = snackItems;
        this.totalPrice = totalPrice;
        this.paymentMethod = paymentMethod;
        this.orderDate = orderDate;
    }
}