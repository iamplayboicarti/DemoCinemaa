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
        movieTabs.addTab("Showing", createMovieList("showing"));
        movieTabs.addTab("Ongoing", createMovieList("ongoing"));
        movieTabs.addTab("Upcoming", createMovieList("upcoming"));
        add(movieTabs, BorderLayout.CENTER);
    }

    private JPanel createMovieList(String status) {
        JPanel panel = new JPanel(new GridLayout(0, 3));
        List<Movie> movies = Database.getMoviesByStatus(status);
        for (Movie movie : movies) {
            int emptySeats = (int) Database.getSeatsByMovieId(movie.getId()).stream().filter(s -> s.getStatus().equals("empty")).count();
            JLabel label = new JLabel(movie.getTitle() + " (" + emptySeats + " ghế trống)",
                    new ImageIcon("src/resources/images/" + movie.getImage()), JLabel.CENTER);
            label.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    new SeatPanel(movie.getId());
                }
            });
            panel.add(label);
        }
        return panel;
    }
}