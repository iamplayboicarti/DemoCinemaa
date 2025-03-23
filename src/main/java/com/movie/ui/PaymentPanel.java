package main.java.com.movie.ui;

import main.java.com.movie.db.Database;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.List;

public class PaymentPanel extends JFrame {
    private static final DecimalFormat df = new DecimalFormat("#,### VNĐ");

    public PaymentPanel(int movieId, List<String> selectedSeats, String snackItems, double totalPrice) {
        setTitle("Xác nhận thanh toán");
        setSize(450, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Phần thông tin đặt vé
        JPanel infoPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        infoPanel.setBorder(BorderFactory.createTitledBorder("Thông tin đặt vé"));

        JLabel seatLabel = new JLabel("Ghế: " + String.join(", ", selectedSeats));
        JLabel snackLabel = new JLabel("Snacks: " + (snackItems.isEmpty() ? "Không có" : snackItems));
        JLabel totalLabel = new JLabel("Tổng tiền: " + df.format(totalPrice));

        infoPanel.add(seatLabel);
        infoPanel.add(snackLabel);
        infoPanel.add(totalLabel);

        // Phần chọn phương thức thanh toán
        JPanel paymentPanel = new JPanel(new GridLayout(2, 1));
        paymentPanel.setBorder(BorderFactory.createTitledBorder("Phương thức thanh toán"));

        JRadioButton cash = new JRadioButton("Tiền mặt");
        JRadioButton transfer = new JRadioButton("Chuyển khoản (Momo, ngân hàng)");
        ButtonGroup group = new ButtonGroup();
        group.add(cash);
        group.add(transfer);

        paymentPanel.add(cash);
        paymentPanel.add(transfer);

        // Nút xác nhận
        JButton confirmButton = new JButton("Xác nhận thanh toán");
        confirmButton.setFont(new Font("Arial", Font.BOLD, 14));
        confirmButton.setFocusPainted(false);
        confirmButton.setBackground(new Color(72, 133, 237));
        confirmButton.setForeground(Color.WHITE);
        confirmButton.setPreferredSize(new Dimension(200, 40));

        confirmButton.addActionListener(e -> handlePayment(cash, transfer, movieId, selectedSeats, snackItems, totalPrice));

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(confirmButton);

        mainPanel.add(infoPanel, BorderLayout.NORTH);
        mainPanel.add(paymentPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
        setVisible(true);
    }

    private void handlePayment(JRadioButton cash, JRadioButton transfer, int movieId, List<String> selectedSeats, String snackItems, double totalPrice) {
        String formattedPrice = df.format(totalPrice);
        if (cash.isSelected()) {
            processBooking(movieId, selectedSeats, snackItems, totalPrice, "cash");
            JOptionPane.showMessageDialog(this, "Thanh toán thành công!\nGhế: " + selectedSeats + "\nSnacks: " + snackItems + "\nTổng tiền: " + formattedPrice);
            dispose();
        } else if (transfer.isSelected()) {
            String amount = JOptionPane.showInputDialog(this, "Nhập số tiền thanh toán:", "Chuyển khoản", JOptionPane.QUESTION_MESSAGE);
            if (amount != null && !amount.trim().isEmpty()) {
                try {
                    double enteredAmount = Double.parseDouble(amount);
                    if (enteredAmount >= totalPrice) {
                        processBooking(movieId, selectedSeats, snackItems, totalPrice, "transfer");
                        JOptionPane.showMessageDialog(this, "Thanh toán thành công!\nGhế: " + selectedSeats + "\nSnacks: " + snackItems + "\nTổng tiền: " + formattedPrice);
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(this, "Số tiền nhập không đủ! Vui lòng thử lại.");
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Vui lòng nhập số tiền hợp lệ.");
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn phương thức thanh toán.");
        }
    }

    private void processBooking(int movieId, List<String> selectedSeats, String snackItems, double totalPrice, String paymentMethod) {
        for (String seat : selectedSeats) {
            Database.updateSeatStatus(movieId, seat, "booked");
        }
        Database.saveOrder(movieId, String.join(",", selectedSeats), snackItems, totalPrice, paymentMethod);
    }
}
