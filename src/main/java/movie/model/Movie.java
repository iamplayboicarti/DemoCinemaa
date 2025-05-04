package movie.model;



import java.util.Date;

public class Movie {
    private int id;
    private String title;
    private String image;
    private Date showDate;
    private String status;

    public Movie(int id, String title, String image, Date showDate, String status) {
        this.id = id;
        this.title = title;
        this.image = image;
        this.showDate = showDate;
        this.status = status;
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getImage() { return image; }
    public Date getShowDate() { return showDate; }
    public String getStatus() { return status; }
}