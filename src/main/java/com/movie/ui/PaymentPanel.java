package main.java.com.movie.ui;



import main.java.com.movie.db.Database;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.List;


public class PaymentPanel extends JFrame {
    private static final DecimalFormat df = new DecimalFormat("#,###");

    public PaymentPanel(int movieId, List<String> selectedSeats, String snackItems, double totalPrice) {
        setTitle("Payment");
        setSize(400, 300);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(3, 1));
        JRadioButton cash = new JRadioButton("Thanh toán tiền mặt");
        JRadioButton transfer = new JRadioButton("Thanh toán chuyển khoản (Momo, Ngân hàng)");
        ButtonGroup group = new ButtonGroup();
        group.add(cash);
        group.add(transfer);

        JButton confirm = new JButton("Xác nhận");
        confirm.addActionListener(e -> {
            String formattedPrice = df.format(totalPrice) + " VNĐ";
            if (cash.isSelected()) {
                for (String seat : selectedSeats) {
                    Database.updateSeatStatus(movieId, seat, "booked");
                }
                Database.saveOrder(movieId, String.join(",", selectedSeats), snackItems, totalPrice, "cash");
                JOptionPane.showMessageDialog(this, "Bạn đã đặt vé thành công!\nGhế: " + selectedSeats + "\nSnacks: " + snackItems + "\nTổng tiền: " + formattedPrice);
                dispose();
            } else if (transfer.isSelected()) {
                String amount = JOptionPane.showInputDialog("Nhập số tiền:");
                if (Double.parseDouble(amount) >= totalPrice) {
                    for (String seat : selectedSeats) {
                        Database.updateSeatStatus(movieId, seat, "booked");
                    }
                    Database.saveOrder(movieId, String.join(",", selectedSeats), snackItems, totalPrice, "transfer");
                    JOptionPane.showMessageDialog(this, "Bạn đã đặt vé thành công!\nGhế: " + selectedSeats + "\nSnacks: " + snackItems + "\nTổng tiền: " + formattedPrice);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Tiền bạn nhập không đủ!");
                }
            }
        });

        panel.add(cash);
        panel.add(transfer);
        panel.add(confirm);
        add(panel);
        setVisible(true);
    }
}