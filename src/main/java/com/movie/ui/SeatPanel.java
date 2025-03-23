package main.java.com.movie.ui;

import main.java.com.movie.db.Database;
import main.java.com.movie.model.Seat;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class SeatPanel extends JFrame {
    private List<String> selectedSeats = new ArrayList<>();
    private int movieId;
    private JPanel seatGrid;

    // Bảng màu mới
    private static final Color BOOKED_COLOR = new Color(255, 0, 0);       // Red
    private static final Color NORMAL_COLOR = new Color(186, 85, 211);    // Medium Orchid (tím hồng)
    private static final Color VIP_COLOR = new Color(255, 182, 193);      // Light Pink (hồng nhạt)
    private static final Color SWEETBOX_COLOR = new Color(255, 140, 0);   // Dark Orange (cam)
    private static final Color SELECTED_COLOR = new Color(50, 205, 50);   // Lime Green (xanh lá cây)
    private static final Color SCREEN_COLOR = new Color(50, 50, 50);      // Dark gray
    private static final Color BACKGROUND_COLOR = new Color(245, 245, 245); // Light gray

    public SeatPanel(int movieId) {
        this.movieId = movieId;
        setTitle("Chọn Ghế Ngồi");
        setSize(700, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(BACKGROUND_COLOR);

        initializeUI();
        setVisible(true);
    }

    private void initializeUI() {
        List<Seat> seats = Database.getSeatsByMovieId(movieId);
        Map<String, Seat> seatMap = seats.stream().collect(Collectors.toMap(Seat::getSeatNumber, seat -> seat));

        // Sử dụng GridBagLayout thay vì GridLayout
        seatGrid = new JPanel(new GridBagLayout());
        seatGrid.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        seatGrid.setBackground(BACKGROUND_COLOR);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Khoảng cách giữa các ghế

        for (int i = 0; i < 56; i++) { // 7 hàng x 8 cột
            String seatNum = (char) ('A' + i / 8) + "" + (i % 8 + 1);
            Seat seat = seatMap.getOrDefault(seatNum, new Seat(0, movieId, seatNum, "empty", false));

            // Tính toán vị trí
            gbc.gridx = i % 8; // Cột
            gbc.gridy = i / 8; // Hàng

            JButton seatButton = createSeatButton(seat, seatNum);

            // Điều chỉnh kích thước ô lưới cho ghế Sweetbox
            if (seatNum.startsWith("G")) {
                gbc.gridwidth = 1; // Sweetbox chiếm 1 cột nhưng lớn hơn
                gbc.gridheight = 1;
            } else {
                gbc.gridwidth = 1;
                gbc.gridheight = 1;
            }

            seatGrid.add(seatButton, gbc);
        }

        add(createScreenPanel(), BorderLayout.NORTH);
        add(seatGrid, BorderLayout.CENTER);
        add(createControlPanel(), BorderLayout.SOUTH);
    }

    private JButton createSeatButton(Seat seat, String seatNum) {
        JButton seatButton = new JButton(seatNum) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if ("booked".equals(seat.getStatus())) {
                    g.setColor(Color.BLACK);
                    g.drawLine(0, 0, getWidth(), getHeight()); // Gạch chéo
                    g.drawLine(getWidth(), 0, 0, getHeight());
                }
            }
        };
        seatButton.setBackground(getSeatColor(seat, seatNum));

        // Điều chỉnh kích thước và font dựa trên loại ghế
        if (seatNum.startsWith("G")) { // Ghế Sweetbox (hàng G)
            seatButton.setPreferredSize(new Dimension(80, 50)); // Gấp đôi kích thước ghế thường
            seatButton.setFont(new Font("Arial", Font.PLAIN, 14)); // Tăng cỡ chữ
        } else { // Ghế thường và VIP
            seatButton.setPreferredSize(new Dimension(40, 25)); // Kích thước mặc định
            seatButton.setFont(new Font("Arial", Font.PLAIN, 10));
        }

        seatButton.setFocusPainted(false);
        seatButton.setToolTipText(getSeatTooltip(seat));

        seatButton.addActionListener(e -> {
            if ("empty".equals(seat.getStatus())) {
                if (selectedSeats.contains(seatNum)) {
                    // Bỏ chọn ghế
                    int confirm = JOptionPane.showConfirmDialog(this, "Bạn muốn bỏ chọn ghế " + seatNum + "?",
                            "Xác nhận", JOptionPane.OK_CANCEL_OPTION);
                    if (confirm == JOptionPane.OK_OPTION) {
                        selectedSeats.remove(seatNum);
                        seatButton.setBackground(getSeatColor(seat, seatNum)); // Trả lại màu gốc
                        seatButton.setToolTipText(getSeatTooltip(seat));
                        seatButton.repaint();
                    }
                } else {
                    // Chọn ghế
                    int confirm = JOptionPane.showConfirmDialog(this, "Xác nhận đặt ghế " + seatNum + "?",
                            "Xác nhận", JOptionPane.OK_CANCEL_OPTION);
                    if (confirm == JOptionPane.OK_OPTION) {
                        selectedSeats.add(seatNum);
                        seatButton.setBackground(SELECTED_COLOR);
                        seatButton.setToolTipText("Ghế bạn đã chọn");
                        seatButton.repaint();
                    }
                }
            }
        });

        return seatButton;
    }

    private Color getSeatColor(Seat seat, String seatNum) {
        if ("booked".equals(seat.getStatus())) {
            return BOOKED_COLOR;
        } else if (selectedSeats.contains(seatNum)) {
            return SELECTED_COLOR;
        } else if (seatNum.startsWith("A") || seatNum.startsWith("B") || seatNum.startsWith("C")) {
            return NORMAL_COLOR; // 3 hàng đầu
        } else if (seatNum.startsWith("D") || seatNum.startsWith("E") || seatNum.startsWith("F")) {
            return VIP_COLOR;    // 3 hàng giữa
        } else {
            return SWEETBOX_COLOR; // Hàng cuối (G)
        }
    }

    private String getSeatTooltip(Seat seat) {
        if ("booked".equals(seat.getStatus())) {
            return "Ghế đã đặt";
        } else if (selectedSeats.contains(seat.getSeatNumber())) {
            return "Ghế bạn đã chọn";
        } else if (seat.getSeatNumber().startsWith("A") || seat.getSeatNumber().startsWith("B") || seat.getSeatNumber().startsWith("C")) {
            return "Ghế thường";
        } else if (seat.getSeatNumber().startsWith("D") || seat.getSeatNumber().startsWith("E") || seat.getSeatNumber().startsWith("F")) {
            return "Ghế VIP";
        } else {
            return "Sweetbox";
        }
    }

    private JPanel createScreenPanel() {
        JPanel screenPanel = new JPanel();
        screenPanel.setBackground(SCREEN_COLOR);
        screenPanel.setPreferredSize(new Dimension(0, 40));
        screenPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

        JLabel screenLabel = new JLabel("MÀN HÌNH", SwingConstants.CENTER);
        screenLabel.setForeground(Color.WHITE);
        screenLabel.setFont(new Font("Arial", Font.BOLD, 14));
        screenPanel.add(screenLabel);

        return screenPanel;
    }

    private JPanel createControlPanel() {
        JPanel controlPanel = new JPanel(new BorderLayout());
        controlPanel.setBackground(BACKGROUND_COLOR);
        controlPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Legend chia thành 2 hàng (3 + 2)
        JPanel legendPanel = new JPanel(new GridLayout(2, 3, 20, 5));
        legendPanel.setBackground(BACKGROUND_COLOR);
        legendPanel.add(createLegendItem("Ghế đã đặt", BOOKED_COLOR, true));
        legendPanel.add(createLegendItem("Ghế thường", NORMAL_COLOR, false));
        legendPanel.add(createLegendItem("Ghế VIP", VIP_COLOR, false));
        legendPanel.add(createLegendItem("Sweetbox", SWEETBOX_COLOR, false));
        legendPanel.add(createLegendItem("Đã chọn", SELECTED_COLOR, false));

        JButton proceedButton = new JButton("Tiếp tục");
        proceedButton.setBackground(new Color(30, 144, 255));
        proceedButton.setForeground(Color.WHITE);
        proceedButton.setFocusPainted(false);
        proceedButton.setPreferredSize(new Dimension(100, 30));
        proceedButton.addActionListener(e -> {
            if (!selectedSeats.isEmpty()) {
                new SnackPanel(movieId, selectedSeats);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn ít nhất một ghế!",
                        "Thông báo", JOptionPane.WARNING_MESSAGE);
            }
        });

        controlPanel.add(legendPanel, BorderLayout.CENTER);
        controlPanel.add(proceedButton, BorderLayout.EAST);
        return controlPanel;
    }

    private JPanel createLegendItem(String text, Color color, boolean hasCross) {
        JPanel itemPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        itemPanel.setBackground(BACKGROUND_COLOR);

        JLabel colorBox = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (hasCross) {
                    g.setColor(Color.BLACK);
                    g.drawLine(0, 0, getWidth(), getHeight());
                    g.drawLine(getWidth(), 0, 0, getHeight());
                }
            }
        };
        colorBox.setOpaque(true);
        colorBox.setBackground(color);
        colorBox.setPreferredSize(new Dimension(15, 15));

        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.PLAIN, 12));

        itemPanel.add(colorBox);
        itemPanel.add(label);
        return itemPanel;
    }
}