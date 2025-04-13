package movie.ui;

import movie.db.Database;
import movie.util.EmailSender;
import movie.util.PDFTicketGenerator;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.List;
import java.util.regex.Pattern;

public class PaymentPanel extends JFrame {
    // Constants
    private static final int DEFAULT_SPACING = 12;
    private static final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private static final Color ACCENT_COLOR = new Color(231, 76, 60);
    private static final Color BACKGROUND_COLOR = new Color(245, 245, 245);
    private static final Font TITLE_FONT = new Font("Arial", Font.BOLD, 16);
    private static final Font REGULAR_FONT = new Font("Arial", Font.PLAIN, 14);
    private static final Font PRICE_FONT = new Font("Arial", Font.BOLD, 14);

    private static final DecimalFormat df = new DecimalFormat("#,### VNĐ");
    // regex cho email
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$"
    );
    private static final Pattern NAME_PATTERN = Pattern.compile("^[\\p{L} .'-]+$");

    public PaymentPanel(int movieId, List<String> selectedSeats, String snackItems, double totalPrice) {
        setTitle("Xác nhận thanh toán");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainContainer = new JPanel(new BorderLayout());
        mainContainer.setBackground(BACKGROUND_COLOR);

        JPanel headerPanel = createHeaderPanel();

        JPanel contentPanel = new JPanel(new BorderLayout(DEFAULT_SPACING, DEFAULT_SPACING));
        contentPanel.setBorder(new EmptyBorder(DEFAULT_SPACING, DEFAULT_SPACING, DEFAULT_SPACING, DEFAULT_SPACING));
        contentPanel.setBackground(BACKGROUND_COLOR);

        JPanel wrapperPanel = new JPanel(new BorderLayout());
        wrapperPanel.setBackground(BACKGROUND_COLOR);

        mainContainer.add(wrapperPanel, BorderLayout.CENTER);

        JPanel orderSummaryPanel = createOrderSummaryPanel(selectedSeats, snackItems, totalPrice);

        JPanel paymentMethodPanel = new JPanel(new BorderLayout(DEFAULT_SPACING, DEFAULT_SPACING));
        paymentMethodPanel.setBackground(Color.WHITE);
        paymentMethodPanel.setBorder(new CompoundBorder(
                new LineBorder(Color.LIGHT_GRAY, 1),
                new EmptyBorder(DEFAULT_SPACING, DEFAULT_SPACING, DEFAULT_SPACING, DEFAULT_SPACING)
        ));

        JPanel userInfoPanel = createUserInfoPanel();

        JPanel methodsPanel = createPaymentMethodsPanel();

        paymentMethodPanel.add(new JLabel("Thông tin thanh toán", JLabel.LEFT), BorderLayout.NORTH);
        paymentMethodPanel.add(userInfoPanel, BorderLayout.CENTER);
        paymentMethodPanel.add(methodsPanel, BorderLayout.SOUTH);

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        actionPanel.setBackground(BACKGROUND_COLOR);

        JButton cancelButton = createStyledButton("Hủy", new Color(150, 150, 150));
        cancelButton.addActionListener(e -> dispose());

        JButton confirmButton = createStyledButton("Xác nhận thanh toán", ACCENT_COLOR);
        confirmButton.addActionListener(e -> {
            JTextField nameField = (JTextField) ((JPanel) userInfoPanel.getComponent(0)).getComponent(1);
            JTextField emailField = (JTextField) ((JPanel) userInfoPanel.getComponent(1)).getComponent(1);
            JRadioButton cash = (JRadioButton) methodsPanel.getComponent(0);
            JRadioButton transfer = (JRadioButton) methodsPanel.getComponent(1);
            handlePayment(nameField, emailField, cash, transfer, movieId, selectedSeats, snackItems, totalPrice);
        });

        actionPanel.add(cancelButton);
        actionPanel.add(Box.createHorizontalStrut(10));
        actionPanel.add(confirmButton);

        contentPanel.add(orderSummaryPanel, BorderLayout.NORTH);
        contentPanel.add(paymentMethodPanel, BorderLayout.CENTER);
        contentPanel.add(actionPanel, BorderLayout.SOUTH);

        mainContainer.add(headerPanel, BorderLayout.NORTH);

        JPanel centeredPanel = new JPanel();
        centeredPanel.setBackground(BACKGROUND_COLOR);
        centeredPanel.setLayout(new BoxLayout(centeredPanel, BoxLayout.Y_AXIS));

        JPanel fixedWidthPanel = new JPanel();
        fixedWidthPanel.setLayout(new BorderLayout());
        fixedWidthPanel.setMaximumSize(new Dimension(600, 2000));
        fixedWidthPanel.setPreferredSize(new Dimension(600, contentPanel.getPreferredSize().height));
        fixedWidthPanel.add(contentPanel, BorderLayout.CENTER);

        centeredPanel.add(Box.createVerticalStrut(20));
        centeredPanel.add(fixedWidthPanel);
        centeredPanel.add(Box.createVerticalGlue());

        wrapperPanel.add(centeredPanel, BorderLayout.CENTER);

        setContentPane(mainContainer);
        setVisible(true);
    }

    private JPanel createUserInfoPanel() {
        JPanel userInfoPanel = new JPanel(new GridLayout(2, 1, 5, 10));
        userInfoPanel.setBackground(Color.WHITE);

        JPanel namePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        namePanel.setBackground(Color.WHITE);
        JLabel nameLabel = new JLabel("Họ tên:");
        nameLabel.setFont(REGULAR_FONT);
        nameLabel.setPreferredSize(new Dimension(120, 25));

        JTextField nameField = new JTextField(15);
        nameField.setFont(REGULAR_FONT);
        nameField.setPreferredSize(new Dimension(200, 25));

        namePanel.add(nameLabel);
        namePanel.add(nameField);

        JPanel emailPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        emailPanel.setBackground(Color.WHITE);
        JLabel emailLabel = new JLabel("Email để nhận vé:");
        emailLabel.setFont(REGULAR_FONT);
        emailLabel.setPreferredSize(new Dimension(120, 25));

        JTextField emailField = new JTextField(15);
        emailField.setFont(REGULAR_FONT);
        emailField.setPreferredSize(new Dimension(200, 25));

        emailPanel.add(emailLabel);
        emailPanel.add(emailField);

        userInfoPanel.add(namePanel);
        userInfoPanel.add(emailPanel);

        return userInfoPanel;
    }

    private JPanel createPaymentMethodsPanel() {
        JPanel methodsPanel = new JPanel(new GridLayout(2, 1, 5, 10));
        methodsPanel.setBackground(Color.WHITE);
        methodsPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                "Phương thức thanh toán",
                0,
                0,
                REGULAR_FONT
        ));

        JRadioButton cash = createStyledRadioButton("Tiền mặt", true);
        JRadioButton transfer = createStyledRadioButton("Chuyển khoản (Momo, ngân hàng)", false);

        ButtonGroup group = new ButtonGroup();
        group.add(cash);
        group.add(transfer);

        methodsPanel.add(cash);
        methodsPanel.add(transfer);

        return methodsPanel;
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(PRIMARY_COLOR);
        panel.setBorder(new EmptyBorder(DEFAULT_SPACING, DEFAULT_SPACING, DEFAULT_SPACING, DEFAULT_SPACING));

        JLabel titleLabel = new JLabel("Xác nhận và thanh toán");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);

        panel.add(titleLabel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createOrderSummaryPanel(List<String> selectedSeats, String snackItems, double totalPrice) {
        JPanel panel = new JPanel(new GridLayout(0, 1, 5, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new CompoundBorder(
                new LineBorder(Color.LIGHT_GRAY, 1),
                new EmptyBorder(DEFAULT_SPACING, DEFAULT_SPACING, DEFAULT_SPACING, DEFAULT_SPACING)
        ));

        JLabel summaryTitle = new JLabel("Tóm tắt đơn hàng", JLabel.LEFT);
        summaryTitle.setFont(TITLE_FONT);
        summaryTitle.setForeground(PRIMARY_COLOR);

        JPanel seatsPanel = createInfoRow("Ghế:", String.join(", ", selectedSeats));

        String snacksDisplay = snackItems.isEmpty() ? "Không có" : snackItems;
        JPanel snacksPanel = createInfoRow("Đồ ăn:", snacksDisplay);

        JPanel pricePanel = createInfoRow("Tổng tiền:", df.format(totalPrice));
        pricePanel.setFont(PRICE_FONT);

        panel.add(summaryTitle);
        panel.add(Box.createVerticalStrut(5));
        panel.add(seatsPanel);
        panel.add(snacksPanel);
        panel.add(new JSeparator());
        panel.add(pricePanel);

        return panel;
    }

    private JPanel createInfoRow(String label, String value) {
        JPanel panel = new JPanel(new BorderLayout(10, 0));
        panel.setBackground(Color.WHITE);

        JLabel labelComponent = new JLabel(label);
        labelComponent.setFont(REGULAR_FONT);
        labelComponent.setForeground(new Color(100, 100, 100));

        JLabel valueComponent = new JLabel(value);
        valueComponent.setFont(REGULAR_FONT);

        panel.add(labelComponent, BorderLayout.WEST);
        panel.add(valueComponent, BorderLayout.CENTER);

        return panel;
    }

    private JRadioButton createStyledRadioButton(String text, boolean selected) {
        JRadioButton button = new JRadioButton(text, selected);
        button.setFont(REGULAR_FONT);
        button.setBackground(Color.WHITE);
        button.setFocusPainted(false);
        return button;
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(REGULAR_FONT);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setContentAreaFilled(true);
        button.setPreferredSize(new Dimension(150, 40));
        button.setMargin(new Insets(8, 15, 8, 15));

        return button;
    }

    private void handlePayment(JTextField nameField, JTextField emailField, JRadioButton cash, JRadioButton transfer,
                               int movieId, List<String> selectedSeats, String snackItems, double totalPrice) {
        String name = nameField.getText().trim();
        if (name.isEmpty() || !NAME_PATTERN.matcher(name).matches()) {
            showErrorDialog("Vui lòng nhập họ tên hợp lệ.");
            return;
        }

        String email = emailField.getText().trim();
        if (email.isEmpty() || !EMAIL_PATTERN.matcher(email).matches()) {
            showErrorDialog("Vui lòng nhập địa chỉ email hợp lệ.");
            return;
        }

        String formattedPrice = df.format(totalPrice);
        if (cash.isSelected()) {
            processBooking(movieId, selectedSeats, snackItems, totalPrice, "cash", name, email);
            showSuccessDialog(selectedSeats, snackItems, formattedPrice, name, email);
            dispose();
        } else if (transfer.isSelected()) {
            showTransferDialog(movieId, selectedSeats, snackItems, totalPrice, formattedPrice, name, email);
        }
    }

    private void showErrorDialog(String message) {
        JOptionPane.showMessageDialog(
                this,
                message,
                "Lỗi",
                JOptionPane.ERROR_MESSAGE
        );
    }

    private void showSuccessDialog(List<String> selectedSeats, String snackItems, String formattedPrice, String name, String email) {
        JOptionPane.showMessageDialog(
                this,
                "Thanh toán thành công!\n\n" +
                        "Khách hàng: " + name + "\n" +
                        "Ghế: " + String.join(", ", selectedSeats) + "\n" +
                        "Đồ ăn: " + (snackItems.isEmpty() ? "Không có" : snackItems) + "\n" +
                        "Tổng tiền: " + formattedPrice + "\n\n" +
                        "Thông tin vé đã được gửi đến email: " + email,
                "Thanh toán hoàn tất",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void showTransferDialog(int movieId, List<String> selectedSeats, String snackItems,
                                    double totalPrice, String formattedPrice, String name, String email) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel infoLabel = new JLabel("<html>Số tài khoản: <b>19038217892173</b><br>"
                + "Ngân hàng: <b>VietcomBank</b><br>"
                + "Tên người nhận: <b>Rạp Chiếu Phim</b><br>"
                + "Nội dung chuyển khoản: <b>Thanh toan ve phim " + name + " " + email + "</b></html>");
        infoLabel.setFont(REGULAR_FONT);

        JLabel amountLabel = new JLabel("Số tiền thanh toán:");
        amountLabel.setFont(REGULAR_FONT);

        JTextField amountField = new JTextField(formattedPrice.replace(" VNĐ", ""));
        amountField.setFont(REGULAR_FONT);

        JPanel inputPanel = new JPanel(new BorderLayout(5, 5));
        inputPanel.add(amountLabel, BorderLayout.NORTH);
        inputPanel.add(amountField, BorderLayout.CENTER);

        panel.add(infoLabel, BorderLayout.NORTH);
        panel.add(inputPanel, BorderLayout.CENTER);

        int result = JOptionPane.showConfirmDialog(
                this,
                panel,
                "Thông tin chuyển khoản",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.INFORMATION_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {
            try {
                String amountText = amountField.getText().replaceAll("[^\\d.]", "");
                double enteredAmount = Double.parseDouble(amountText);
                if (enteredAmount >= totalPrice) {
                    processBooking(movieId, selectedSeats, snackItems, totalPrice, "transfer", name, email);
                    showSuccessDialog(selectedSeats, snackItems, formattedPrice, name, email);
                    dispose();
                } else {
                    showErrorDialog("Số tiền nhập không đủ! Vui lòng thử lại.");
                }
            } catch (NumberFormatException ex) {
                showErrorDialog("Vui lòng nhập số tiền hợp lệ.");
            }
        }
    }

    private void processBooking(int movieId, List<String> selectedSeats, String snackItems,
                                double totalPrice, String paymentMethod, String name, String email) {
        for (String seat : selectedSeats) {
            Database.updateSeatStatus(movieId, seat, "booked");
        }

        int orderId = Database.saveOrder(movieId, String.join(",", selectedSeats), snackItems, totalPrice, paymentMethod, email);

        try {
            String pdfPath = null;
            try {
                pdfPath = PDFTicketGenerator.generateTicket(orderId, movieId, selectedSeats, snackItems, totalPrice, name, email);
                System.out.println("PDF ticket generated successfully: " + pdfPath);
            } catch (Exception e) {
                System.err.println("Failed to generate PDF ticket: " + e.getMessage());
                e.printStackTrace();
                JOptionPane.showMessageDialog(this,
                        "Đặt vé thành công, vé đã được gửi qua email, nhưng không thể tạo file PDF.",
                        "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            }

            sendTicketEmail(orderId, movieId, selectedSeats, snackItems, totalPrice, name, email, pdfPath);

        } catch (Exception e) {
            System.err.println("Failed to send email: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Đặt vé thành công, nhưng không thể gửi email. Vui lòng kiểm tra thông tin vé của bạn.",
                    "Cảnh báo", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void sendTicketEmail(int orderId, int movieId, List<String> selectedSeats,
                                 String snackItems, double totalPrice, String name, String email, String pdfPath) {
        try {
            String movieTitle = Database.getMovieTitle(movieId);
            String showDate = Database.getMovieShowtime(movieId);

            String subject = "Xác nhận đặt vé xem phim: " + (movieTitle != null ? movieTitle : "") + " - " + name;

            StringBuilder content = new StringBuilder();
            content.append("<html><body style='font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto;'>");
            content.append("<div style='background-color: #2980b9; color: white; padding: 20px; text-align: center;'>");
            content.append("<h1 style='margin: 0;'>Vé Xem Phim</h1>");
            content.append("</div>");
            content.append("<div style='padding: 20px; background-color: #f9f9f9;'>");
            content.append("<h2 style='color: #2980b9;'>Thông tin vé xem phim</h2>");
            content.append("<div style='background-color: white; border-radius: 5px; padding: 15px; margin-bottom: 20px;'>");
            content.append("<p><b>Mã đơn hàng:</b> ").append(orderId).append("</p>");
            content.append("<p><b>Khách hàng:</b> ").append(name).append("</p>");

            if (movieTitle != null && !movieTitle.isEmpty()) {
                content.append("<p><b>Phim:</b> ").append(movieTitle).append("</p>");
            }

            if (showDate != null && !showDate.isEmpty()) {
                content.append("<p><b>Ngày chiếu:</b> ").append(showDate).append("</p>");
            }

            content.append("<p><b>Ghế:</b> ").append(String.join(", ", selectedSeats)).append("</p>");

            if (snackItems != null && !snackItems.isEmpty() && !snackItems.equals("Không có")) {
                content.append("<p><b>Đồ ăn:</b> ").append(snackItems).append("</p>");
            } else {
                content.append("<p><b>Đồ ăn:</b> Không có</p>");
            }

            content.append("<p><b>Tổng tiền:</b> ").append(df.format(totalPrice)).append("</p>");
            content.append("</div>");
            content.append("<p style='background-color: #e3f2fd; padding: 15px; border-radius: 5px;'>");
            content.append("Cảm ơn bạn đã đặt vé! Vui lòng đến trước suất chiếu 15 phút để kiểm tra vé.");
            content.append("</p>");
            content.append("<p>Vé PDF đã được tạo và đính kèm cùng với email này.</p>");
            content.append("</div>");
            content.append("<div style='background-color: #34495e; color: white; padding: 15px; text-align: center;'>");
            content.append("<p>© 2025 Rạp Chiếu Phim.</p>");
            content.append("</div>");
            content.append("</body></html>");

            EmailSender.sendEmail(email, subject, content.toString(), pdfPath);
        } catch (Exception e) {
            throw new RuntimeException("Không thể gửi email: " + e.getMessage(), e);
        }
    }
}