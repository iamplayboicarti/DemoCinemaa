package main.java.com.movie.ui;


import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SnackPanel extends JFrame {
    private Map<String, Integer> selectedItems = new HashMap<>();
    private Map<String, Integer> prices = new HashMap<>();

    public SnackPanel(int movieId, List<String> selectedSeats) {
        setTitle("Buy Snacks");
        setSize(600, 400);
        setLocationRelativeTo(null);

        JPanel snackPanel = new JPanel(new GridLayout(0, 3));
        prices.put("Coca", 20000); prices.put("Pepsi", 20000); prices.put("Mirinda", 20000);
        prices.put("Phomai", 30000); prices.put("Bơ", 30000); prices.put("Mix", 30000); prices.put("Dâu", 30000); prices.put("Socola", 30000);
        prices.put("1 Bắp 2 Nước", 70000); prices.put("1 Nước 2 Bắp", 70000); prices.put("2 Bắp 1 Nước", 70000);

        String[] drinks = {"Coca", "Pepsi", "Mirinda"};
        String[] popcorns = {"Phomai", "Bơ", "Mix", "Dâu", "Socola"};
        String[] combos = {"1 Bắp 2 Nước", "1 Nước 2 Bắp", "2 Bắp 1 Nước"};

        for (String drink : drinks) snackPanel.add(createItemPanel(drink));
        for (String popcorn : popcorns) snackPanel.add(createItemPanel(popcorn));
        for (String combo : combos) snackPanel.add(createItemPanel(combo));

        JButton payButton = new JButton("Thanh toán");
        payButton.addActionListener(e -> {
            double totalPrice = selectedSeats.size() * 50000; // Giá ghế mặc định 50k
            StringBuilder snackItems = new StringBuilder();
            for (Map.Entry<String, Integer> entry : selectedItems.entrySet()) {
                if (entry.getValue() > 0) {
                    totalPrice += entry.getValue() * prices.get(entry.getKey());
                    snackItems.append(entry.getKey()).append(":").append(entry.getValue()).append(",");
                }
            }
            new PaymentPanel(movieId, selectedSeats, snackItems.toString(), totalPrice);
            dispose();
        });

        add(snackPanel, BorderLayout.CENTER);
        add(payButton, BorderLayout.SOUTH);
        setVisible(true);
    }

    private JPanel createItemPanel(String name) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JLabel(name + " - " + prices.get(name)), BorderLayout.NORTH);
        JPanel quantityPanel = new JPanel();
        JButton minus = new JButton("-");
        JTextField quantity = new JTextField("0", 2);
        quantity.setHorizontalAlignment(JTextField.CENTER);
        JButton plus = new JButton("+");

        minus.addActionListener(e -> {
            int q = Integer.parseInt(quantity.getText());
            if (q > 0) {
                q--;
                quantity.setText(String.valueOf(q));
                selectedItems.put(name, q);
            }
        });
        plus.addActionListener(e -> {
            int q = Integer.parseInt(quantity.getText());
            q++;
            quantity.setText(String.valueOf(q));
            selectedItems.put(name, q);
        });

        quantityPanel.add(minus);
        quantityPanel.add(quantity);
        quantityPanel.add(plus);
        panel.add(quantityPanel, BorderLayout.CENTER);
        return panel;
    }
}