package main.java.com.movie.ui;

import main.java.com.movie.db.Database;
import main.java.com.movie.model.Movie;
import main.java.com.movie.util.CalendarUtil;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.Date;

import java.util.List;


public class MainFrame extends JFrame {
    public MainFrame() {
        setTitle("Movie Ticket Booking");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Menu");
        menuBar.add(menu);
        setJMenuBar(menuBar);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Movies", new MoviePanel());
        tabbedPane.addTab("Schedule", createSchedulePanel());
        add(tabbedPane);

        setVisible(true);
    }

    private JPanel createSchedulePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JComboBox<String> dateBox = new JComboBox<>();
        for (int i = 0; i < 7; i++) {
            Date date = CalendarUtil.getDate(i);
            dateBox.addItem(date.toString());
        }
        dateBox.addActionListener(e -> {
            Date selectedDate = CalendarUtil.getDate(dateBox.getSelectedIndex());
            JPanel movieList = new JPanel(new GridLayout(0, 3));
            List<Movie> movies = Database.getMoviesByDate(selectedDate);
            for (Movie movie : movies) {
                int emptySeats = (int) Database.getSeatsByMovieId(movie.getId()).stream().filter(s -> s.getStatus().equals("empty")).count();
                String imagePath = "src/resources/images/" + movie.getImage();
                File imageFile = new File(imagePath);
                ImageIcon icon;
                if (imageFile.exists()) {
                    icon = new ImageIcon(imagePath);
                } else {
                    icon = new ImageIcon();
                    System.out.println("Không tìm thấy hình: " + imagePath);
                }
                JLabel label = new JLabel(movie.getTitle() + " (" + emptySeats + " ghế trống)", icon, JLabel.CENTER);
                label.addMouseListener(new java.awt.event.MouseAdapter() {
                    public void mouseClicked(java.awt.event.MouseEvent evt) {
                        new SeatPanel(movie.getId());
                    }
                });
                movieList.add(label);
            }
            panel.removeAll();
            panel.add(dateBox, BorderLayout.NORTH);
            panel.add(movieList, BorderLayout.CENTER);
            panel.revalidate();
            panel.repaint();
        });
        panel.add(dateBox, BorderLayout.NORTH);
        return panel;
    }
}