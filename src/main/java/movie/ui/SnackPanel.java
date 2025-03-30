package movie.ui;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SnackPanel extends JFrame {
    private final Map<String, Integer> selectedItems = new HashMap<>();
    private final Map<String, Integer> prices = new HashMap<>();
    private final JLabel totalLabel;
    private final JLabel totalInWords;
    private double totalPrice = 0;

    public SnackPanel(int movieId, List<String> selectedSeats) {
        setTitle("Buy Snacks");
        setSize(600, 500);
        setLocationRelativeTo(null);

        prices.put("Coca", 20000); prices.put("Pepsi", 20000); prices.put("Mirinda", 20000);
        prices.put("Phomai", 30000); prices.put("Bơ", 30000); prices.put("Mix", 30000);
        prices.put("Dâu", 30000); prices.put("Socola", 30000);
        prices.put("1 Bắp 2 Nước", 70000); prices.put("1 Nước 2 Bắp", 70000); prices.put("2 Bắp 1 Nước", 70000);

        JPanel snackPanel = new JPanel(new GridLayout(0, 3, 10, 10));
        String[] items = {"Coca", "Pepsi", "Mirinda", "Phomai", "Bơ", "Mix", "Dâu", "Socola", "1 Bắp 2 Nước", "1 Nước 2 Bắp", "2 Bắp 1 Nước"};
        for (String item : items) {
            snackPanel.add(createItemPanel(item));
        }

        JScrollPane scrollPane = new JScrollPane(snackPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel bottomPanel = new JPanel(new BorderLayout());
        totalLabel = new JLabel("Tổng tiền: 0 VNĐ");
        totalLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        totalLabel.setFont(new Font("Arial", Font.BOLD, 14));

        totalInWords = new JLabel("(Không đồng)");
        totalInWords.setHorizontalAlignment(SwingConstants.RIGHT);
        totalInWords.setFont(new Font("Arial", Font.ITALIC, 12));

        JButton payButton = new JButton("Thanh toán");
        payButton.addActionListener(e -> processPayment(movieId, selectedSeats));

        JPanel totalPanel = new JPanel(new GridLayout(2, 1));
        totalPanel.add(totalLabel);
        totalPanel.add(totalInWords);

        bottomPanel.add(totalPanel, BorderLayout.CENTER);
        bottomPanel.add(payButton, BorderLayout.EAST);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
        setVisible(true);
    }

    private JPanel createItemPanel(String name) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY), name, TitledBorder.CENTER, TitledBorder.TOP));

        JLabel priceLabel = new JLabel(prices.get(name) + " VNĐ");
        priceLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        priceLabel.setFont(new Font("Arial", Font.BOLD, 12));

        JPanel quantityPanel = new JPanel();
        JButton minus = new JButton("-");
        JTextField quantity = new JTextField("0", 2);
        quantity.setHorizontalAlignment(JTextField.CENTER);
        JButton plus = new JButton("+");

        minus.addActionListener(e -> updateQuantity(name, quantity, -1));
        plus.addActionListener(e -> updateQuantity(name, quantity, 1));

        quantityPanel.add(minus);
        quantityPanel.add(quantity);
        quantityPanel.add(plus);

        panel.add(priceLabel, BorderLayout.NORTH);
        panel.add(quantityPanel, BorderLayout.SOUTH);
        return panel;
    }

    private void updateQuantity(String name, JTextField quantityField, int delta) {
        int current = Integer.parseInt(quantityField.getText());
        int newValue = Math.max(0, current + delta);
        quantityField.setText(String.valueOf(newValue));
        selectedItems.put(name, newValue);
        updateTotal();
    }

    private void updateTotal() {
        totalPrice = selectedItems.entrySet().stream().mapToDouble(e -> e.getValue() * prices.get(e.getKey())).sum();
        totalLabel.setText("Tổng tiền: " + String.format("%,d", (int) totalPrice) + " VNĐ");
        totalInWords.setText("(" + convertNumberToWords((int) totalPrice) + ")");
    }

    private void processPayment(int movieId, List<String> selectedSeats) {
        double seatPrice = selectedSeats.size() * 50000;
        totalPrice += seatPrice;
        StringBuilder snackItems = new StringBuilder();
        for (Map.Entry<String, Integer> entry : selectedItems.entrySet()) {
            if (entry.getValue() > 0) {
                snackItems.append(entry.getKey()).append(":").append(entry.getValue()).append(",");
            }
        }
        new PaymentPanel(movieId, selectedSeats, snackItems.toString(), totalPrice);
        dispose();
    }

    private String convertNumberToWords(int num) {
        if (num == 0) return "Không đồng";
        String[] units = {"", "một", "hai", "ba", "bốn", "năm", "sáu", "bảy", "tám", "chín"};
        String[] tens = {"", "mười", "hai mươi", "ba mươi", "bốn mươi", "năm mươi", "sáu mươi", "bảy mươi", "tám mươi", "chín mươi"};
        String[] thousands = {"", "nghìn", "triệu", "tỷ"};
        StringBuilder result = new StringBuilder();
        int index = 0;
        while (num > 0) {
            int part = num % 1000;
            if (part > 0) {
                StringBuilder section = new StringBuilder();
                int hundreds = part / 100;
                int remainder = part % 100;
                if (hundreds > 0) {
                    section.append(units[hundreds]).append(" trăm ");
                }
                if (remainder > 0) {
                    if (remainder < 10) section.append(units[remainder]);
                    else section.append(tens[remainder / 10]).append(" ").append(units[remainder % 10]);
                }
                //section.append(" ").append(thousands[index]).append(" ");
                section.append(thousands[index]).append(" ");
                result.insert(0, section);
            }
            num /= 1000;
            index++;
        }
        return result.toString().trim();
    }
}