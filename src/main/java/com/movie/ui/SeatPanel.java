package main.java.com.movie.ui;


import main.java.com.movie.db.Database;
import main.java.com.movie.model.Seat;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class SeatPanel extends JFrame {
    private List<String> selectedSeats = new ArrayList<>();
    private int movieId;

    public SeatPanel(int movieId) {
        this.movieId = movieId;
        setTitle("Select Seats");
        setSize(600, 400);
        setLocationRelativeTo(null);

        JPanel seatGrid = new JPanel(new GridLayout(6, 8));
        List<Seat> seats = Database.getSeatsByMovieId(movieId);
        for (int i = 0; i < 48; i++) {
            String seatNum = (char) ('A' + i / 8) + "" + (i % 8 + 1);
            JButton seatButton = new JButton(seatNum);
            Seat seat = seats.stream().filter(s -> s.getSeatNumber().equals(seatNum)).findFirst().orElse(new Seat(0, movieId, seatNum, "empty", false));

            if (seat.getStatus().equals("empty")) {
                seatButton.setBackground(seat.isCouple() ? Color.PINK : Color.GREEN);
            } else if (seat.getStatus().equals("booked")) {
                seatButton.setBackground(Color.YELLOW);
            } else if (seat.getStatus().equals("occupied")) {
                seatButton.setBackground(Color.RED);
            }

            seatButton.addActionListener(e -> {
                if (seat.getStatus().equals("empty")) {
                    int confirm = JOptionPane.showConfirmDialog(this, "Bạn đặt ghế " + seatNum + "?", "Xác nhận", JOptionPane.OK_CANCEL_OPTION);
                    if (confirm == JOptionPane.OK_OPTION) {
                        selectedSeats.add(seatNum);
                        Database.updateSeatStatus(movieId, seatNum, "booked");
                        seatButton.setBackground(Color.YELLOW);
                    }
                }
            });
            seatGrid.add(seatButton);
        }

        JPanel screen = new JPanel();
        screen.setBackground(Color.GRAY);
        screen.setPreferredSize(new Dimension(500, 20));
        JLabel screenLabel = new JLabel("Màn hình", SwingConstants.CENTER);
        screenLabel.setForeground(Color.WHITE);
        screen.add(screenLabel);

        JButton proceedButton = new JButton("Tiếp tục");
        proceedButton.addActionListener(e -> {
            if (!selectedSeats.isEmpty()) {
                new SnackPanel(movieId, selectedSeats);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn ít nhất một ghế!");
            }
        });

        // Chú thích với nền màu tương ứng
        JPanel legendPanel = new JPanel(new GridLayout(1, 4));

        JLabel emptyLabel = new JLabel("Ghế trống", SwingConstants.CENTER);
        emptyLabel.setOpaque(true);
        emptyLabel.setBackground(Color.GREEN);
        emptyLabel.setForeground(Color.BLACK);

        JLabel occupiedLabel = new JLabel("Ghế có người", SwingConstants.CENTER);
        occupiedLabel.setOpaque(true);
        occupiedLabel.setBackground(Color.RED);
        occupiedLabel.setForeground(Color.WHITE);

        JLabel bookedLabel = new JLabel("Ghế đã đặt", SwingConstants.CENTER);
        bookedLabel.setOpaque(true);
        bookedLabel.setBackground(Color.YELLOW);
        bookedLabel.setForeground(Color.BLACK);

        JLabel coupleLabel = new JLabel("Ghế đôi", SwingConstants.CENTER);
        coupleLabel.setOpaque(true);
        coupleLabel.setBackground(Color.PINK);
        coupleLabel.setForeground(Color.BLACK);

        legendPanel.add(emptyLabel);
        legendPanel.add(occupiedLabel);
        legendPanel.add(bookedLabel);
        legendPanel.add(coupleLabel);

        add(screen, BorderLayout.NORTH);
        add(seatGrid, BorderLayout.CENTER);
        add(legendPanel, BorderLayout.SOUTH);
        add(proceedButton, BorderLayout.EAST);
        setVisible(true);
    }
}