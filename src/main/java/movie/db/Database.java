package movie.db;

import movie.model.Movie;
import movie.model.Seat;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

public class Database {
    private static final String URL = "jdbc:mysql://localhost:3306/movie_db";
    private static final String USER = "root";
    private static final String PASSWORD = "yui4727"; // Thay đổi theo MySQL của bạn

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static List<Movie> getMoviesByStatus(String status) {
        List<Movie> movies = new ArrayList<>();
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM movies WHERE status = ?")) {
            stmt.setString(1, status);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                movies.add(new Movie(rs.getInt("id"), rs.getString("title"), rs.getString("image"),
                        rs.getDate("show_date"), rs.getString("status")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return movies;
    }

    public static List<Movie> getMoviesByDate(Date date) {
        List<Movie> movies = new ArrayList<>();
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM movies WHERE show_date = ?")) {
            stmt.setDate(1, new java.sql.Date(date.getTime()));
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                movies.add(new Movie(rs.getInt("id"), rs.getString("title"), rs.getString("image"),
                        rs.getDate("show_date"), rs.getString("status")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return movies;
    }

    public static List<Seat> getSeatsByMovieId(int movieId) {
        List<Seat> seats = new ArrayList<>();
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM seats WHERE movie_id = ?")) {
            stmt.setInt(1, movieId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                seats.add(new Seat(rs.getInt("id"), rs.getInt("movie_id"), rs.getString("seat_number"),
                        rs.getString("status"), rs.getBoolean("is_couple")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return seats;
    }

    public static void updateSeatStatus(int movieId, String seatNumber, String status) {
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement("UPDATE seats SET status = ? WHERE movie_id = ? AND seat_number = ?")) {
            stmt.setString(1, status);
            stmt.setInt(2, movieId);
            stmt.setString(3, seatNumber);
            int rowsAffected = stmt.executeUpdate();
            System.out.println("Cập nhật ghế " + seatNumber + " cho movie_id " + movieId + ": " + rowsAffected + " dòng affected");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void saveOrder(int movieId, String seatIds, String snackItems, double totalPrice, String paymentMethod) {
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "INSERT INTO orders (movie_id, seat_ids, snack_items, total_price, payment_method, order_date) VALUES (?, ?, ?, ?, ?, NOW())")) {
            stmt.setInt(1, movieId);
            stmt.setString(2, seatIds);
            stmt.setString(3, snackItems);
            stmt.setDouble(4, totalPrice);
            stmt.setString(5, paymentMethod);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

// Add these methods to your existing Database class

    public static int saveOrder(int movieId, String seatIds, String snackItems, double totalPrice, String paymentMethod, String email) {
        int orderId = 0;
        try {
            Connection conn = getConnection();
            String sql = "INSERT INTO orders (movie_id, seat_ids, snack_items, total_price, payment_method, email, order_date) VALUES (?, ?, ?, ?, ?, ?, NOW())";
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, movieId);
            stmt.setString(2, seatIds);
            stmt.setString(3, snackItems);
            stmt.setDouble(4, totalPrice);
            stmt.setString(5, paymentMethod);
            stmt.setString(6, email);
            stmt.executeUpdate();

            // Get the generated order ID
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                orderId = rs.getInt(1);
            }

            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orderId;
    }

    public static String getMovieTitle(int movieId) {
        String title = "";
        try {
            Connection conn = getConnection();
            String sql = "SELECT title FROM movies WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, movieId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                title = rs.getString("title");
            }

            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return title;
    }

    public static String getMovieShowtime(int movieId) {
        String showtime = "";
        try {
            Connection conn = getConnection();
            String sql = "SELECT show_date FROM movies WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, movieId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                java.util.Date date = rs.getDate("show_date");
                if (date != null) {
                    showtime = new java.text.SimpleDateFormat("dd/MM/yyyy").format(date);
                }
            }

            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return showtime;
    }
}