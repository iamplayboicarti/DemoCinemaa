package main.java.com.movie.model;



public class Seat {
    private int id;
    private int movieId;
    private String seatNumber;
    private String status;
    private boolean isCouple;

    public Seat(int id, int movieId, String seatNumber, String status, boolean isCouple) {
        this.id = id;
        this.movieId = movieId;
        this.seatNumber = seatNumber;
        this.status = status;
        this.isCouple = isCouple;
    }

    public int getId() { return id; }
    public int getMovieId() { return movieId; }
    public String getSeatNumber() { return seatNumber; }
    public String getStatus() { return status; }
    public boolean isCouple() { return isCouple; }
}