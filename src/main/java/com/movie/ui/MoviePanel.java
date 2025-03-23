package main.java.com.movie.ui;

import main.java.com.movie.db.Database;
import main.java.com.movie.model.Movie;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class MoviePanel extends JPanel {
    public MoviePanel() {
        setLayout(new BorderLayout());

        JTabbedPane movieTabs = new JTabbedPane();
        movieTabs.addTab("🎬 Đang chiếu", createMovieList("showing"));
        movieTabs.addTab("🎥 Sắp chiếu", createMovieList("ongoing"));
        movieTabs.addTab("🍿 Sắp ra mắt", createMovieList("upcoming"));

        add(movieTabs, BorderLayout.CENTER);
    }

    private JPanel createMovieList(String status) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 20)); // Căn trái + khoảng cách

        List<Movie> movies = Database.getMoviesByStatus(status);
        for (Movie movie : movies) {
            panel.add(createMovieCard(movie, status));
        }

        return panel;
    }

    private JPanel createMovieCard(Movie movie, String status) {
        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(new BorderLayout());
        cardPanel.setPreferredSize(new Dimension(180, 280));
        cardPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1, true));

        // Load ảnh phim (tự scale kích thước)
        ImageIcon icon = new ImageIcon("src/resources/images/" + movie.getImage());
        Image img = icon.getImage().getScaledInstance(180, 200, Image.SCALE_SMOOTH);
        JLabel imageLabel = new JLabel(new ImageIcon(img));

        // Lấy số ghế trống
        int emptySeats = (int) Database.getSeatsByMovieId(movie.getId()).stream()
                .filter(s -> s.getStatus().equals("empty")).count();
        JLabel titleLabel = new JLabel("<html><center>" + movie.getTitle() +
                "<br>(" + emptySeats + " ghế trống)</center></html>", JLabel.CENTER);

        // Chỉ hiển thị nút "Đặt vé" nếu phim đang chiếu
        JButton bookButton = new JButton("Đặt vé");
        bookButton.setFont(new Font("Arial", Font.BOLD, 12));
        bookButton.setBackground(Color.RED);
        bookButton.setForeground(Color.WHITE);
        bookButton.setFocusPainted(false);
        bookButton.addActionListener(e -> new SeatPanel(movie.getId()));

        if (!status.equals("showing")) {
            bookButton.setVisible(false);  // Ẩn nút nếu phim không phải "showing"
        }

        cardPanel.add(imageLabel, BorderLayout.CENTER);
        cardPanel.add(titleLabel, BorderLayout.NORTH);
        if (status.equals("showing")) {
            cardPanel.add(bookButton, BorderLayout.SOUTH);
        }

        return cardPanel;
    }
}