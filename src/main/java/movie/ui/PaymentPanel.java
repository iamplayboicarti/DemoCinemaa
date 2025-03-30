package movie.ui;

import movie.db.Database;
import movie.util.EmailSender;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.List;
import java.util.regex.Pattern;

public class PaymentPanel extends JFrame {
    private static final DecimalFormat df = new DecimalFormat("#,### VNĐ");
    // regex email
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$"
    );

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
        JPanel paymentPanel = new JPanel(new GridLayout(3, 1));
        paymentPanel.setBorder(BorderFactory.createTitledBorder("Phương thức thanh toán"));

        // Email field
        JPanel emailPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel emailLabel = new JLabel("Email: ");
        JTextField emailField = new JTextField(20);
        emailPanel.add(emailLabel);
        emailPanel.add(emailField);

        JRadioButton cash = new JRadioButton("Tiền mặt");
        JRadioButton transfer = new JRadioButton("Chuyển khoản (Momo, ngân hàng)");
        ButtonGroup group = new ButtonGroup();
        group.add(cash);
        group.add(transfer);

        paymentPanel.add(emailPanel);
        paymentPanel.add(cash);
        paymentPanel.add(transfer);

        // Nút xác nhận
        JButton confirmButton = new JButton("Xác nhận thanh toán");
        confirmButton.setFont(new Font("Arial", Font.BOLD, 14));
        confirmButton.setFocusPainted(false);
        confirmButton.setBackground(new Color(72, 133, 237));
        confirmButton.setForeground(Color.WHITE);
        confirmButton.setPreferredSize(new Dimension(200, 40));

        confirmButton.addActionListener(e -> handlePayment(emailField, cash, transfer, movieId, selectedSeats, snackItems, totalPrice));

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(confirmButton);

        mainPanel.add(infoPanel, BorderLayout.NORTH);
        mainPanel.add(paymentPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
        setVisible(true);
    }

    private void handlePayment(JTextField emailField, JRadioButton cash, JRadioButton transfer,
                               int movieId, List<String> selectedSeats, String snackItems, double totalPrice) {
        // Validate email
        String email = emailField.getText().trim();
        if (email.isEmpty() || !EMAIL_PATTERN.matcher(email).matches()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập địa chỉ email hợp lệ.",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String formattedPrice = df.format(totalPrice);
        if (cash.isSelected()) {
            processBooking(movieId, selectedSeats, snackItems, totalPrice, "cash", email);
            JOptionPane.showMessageDialog(this, "Thanh toán thành công!\nGhế: " + selectedSeats +
                    "\nSnacks: " + snackItems + "\nTổng tiền: " + formattedPrice +
                    "\nThông tin vé đã được gửi đến email: " + email);
            dispose();
        } else if (transfer.isSelected()) {
            String amount = JOptionPane.showInputDialog(this, "Nhập số tiền thanh toán:", "Chuyển khoản", JOptionPane.QUESTION_MESSAGE);
            if (amount != null && !amount.trim().isEmpty()) {
                try {
                    double enteredAmount = Double.parseDouble(amount);
                    if (enteredAmount >= totalPrice) {
                        processBooking(movieId, selectedSeats, snackItems, totalPrice, "transfer", email);
                        JOptionPane.showMessageDialog(this, "Thanh toán thành công!\nGhế: " + selectedSeats +
                                "\nSnacks: " + snackItems + "\nTổng tiền: " + formattedPrice +
                                "\nThông tin vé đã được gửi đến email: " + email);
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

    private void processBooking(int movieId, List<String> selectedSeats, String snackItems,
                                double totalPrice, String paymentMethod, String email) {
        for (String seat : selectedSeats) {
            Database.updateSeatStatus(movieId, seat, "booked");
        }

        // Save order with email
        int orderId = Database.saveOrder(movieId, String.join(",", selectedSeats), snackItems, totalPrice, paymentMethod, email);

        // Send email with ticket information
        try {
            sendTicketEmail(orderId, movieId, selectedSeats, snackItems, totalPrice, email);
        } catch (Exception e) {
            // Log error but don't prevent booking from completing
            System.err.println("Failed to send email: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Đặt vé thành công, nhưng không thể gửi email. Vui lòng kiểm tra thông tin vé của bạn.",
                    "Cảnh báo", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void sendTicketEmail(int orderId, int movieId, List<String> selectedSeats,
                                 String snackItems, double totalPrice, String email) {
        try {
            // Get movie details from database
            String movieTitle = Database.getMovieTitle(movieId);
            String showDate = Database.getMovieShowtime(movieId);

            // Create email content
            String subject = "Xác nhận đặt vé xem phim: " + (movieTitle != null ? movieTitle : "");

            StringBuilder content = new StringBuilder();
            content.append("<html><body>");
            content.append("<h2>Thông tin vé xem phim</h2>");
            content.append("<p><b>Mã đơn hàng:</b> ").append(orderId).append("</p>");

            if (movieTitle != null && !movieTitle.isEmpty()) {
                content.append("<p><b>Phim:</b> ").append(movieTitle).append("</p>");
            }

            if (showDate != null && !showDate.isEmpty()) {
                content.append("<p><b>Ngày chiếu:</b> ").append(showDate).append("</p>");
            }

            content.append("<p><b>Ghế:</b> ").append(String.join(", ", selectedSeats)).append("</p>");

            if (snackItems != null && !snackItems.isEmpty() && !snackItems.equals("Không có")) {
                content.append("<p><b>Snacks:</b> ").append(snackItems).append("</p>");
            } else {
                content.append("<p><b>Snacks:</b> Không có</p>");
            }

            content.append("<p><b>Tổng tiền:</b> ").append(df.format(totalPrice)).append("</p>");
            content.append("<p>Cảm ơn bạn đã đặt vé! Vui lòng đến trước suất chiếu 15 phút để nhận vé.</p>");
            content.append("</body></html>");

            // Send email
            EmailSender.sendEmail(email, subject, content.toString());
        } catch (Exception e) {
            throw new RuntimeException("Không thể gửi email: " + e.getMessage(), e);
        }
    }
}