package movie.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.text.NumberFormat;
import java.util.*;
import java.util.List;


public class SnackPanel extends JFrame {
    // Constants
    private static final int DEFAULT_SPACING = 12;
    private static final String CURRENCY = "VNĐ";
    private static final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private static final Color ACCENT_COLOR = new Color(231, 76, 60);
    private static final Color BACKGROUND_COLOR = new Color(245, 245, 245);
    private static final Font REGULAR_FONT = new Font("Arial", Font.PLAIN, 14);

    private final Map<String, JTextField> quantityFields = new HashMap<>();
    private final Map<String, Integer> selectedItems = new HashMap<>();
    private final Map<String, Integer> prices = new HashMap<>();
    private  JLabel totalLabel;
    private  JLabel totalInWords;
    private final NumberFormat currencyFormatter = NumberFormat.getNumberInstance(Locale.US);
    private double totalPrice = 0;

    private final Map<String, List<String>> categories = new HashMap<>();

    public SnackPanel(int movieId, List<String> selectedSeats) {
        setTitle("Chọn đồ ăn");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setMinimumSize(new Dimension(800, 600));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(BACKGROUND_COLOR);

        initializePricesAndCategories();

        JPanel mainContainer = new JPanel(new BorderLayout());
        mainContainer.setBackground(BACKGROUND_COLOR);

        JPanel contentPanel = new JPanel(new BorderLayout(DEFAULT_SPACING, DEFAULT_SPACING));
        contentPanel.setBorder(new EmptyBorder(DEFAULT_SPACING, DEFAULT_SPACING, DEFAULT_SPACING, DEFAULT_SPACING));
        contentPanel.setBackground(BACKGROUND_COLOR);

        JPanel titlePanel = createTitlePanel();
        contentPanel.add(titlePanel, BorderLayout.NORTH);

        JTabbedPane categoryTabs = createCategoryTabs();
        contentPanel.add(categoryTabs, BorderLayout.CENTER);

        JPanel summaryPanel = createSummaryPanel(movieId, selectedSeats);
        contentPanel.add(summaryPanel, BorderLayout.SOUTH);

        JPanel centeredPanel = new JPanel();
        centeredPanel.setLayout(new BoxLayout(centeredPanel, BoxLayout.Y_AXIS));
        centeredPanel.setBackground(BACKGROUND_COLOR);

        JPanel fixedWidthPanel = new JPanel(new BorderLayout());
        fixedWidthPanel.setMaximumSize(new Dimension(1000, 2000));
        fixedWidthPanel.setPreferredSize(new Dimension(1000, contentPanel.getPreferredSize().height));
        fixedWidthPanel.setBackground(BACKGROUND_COLOR);
        fixedWidthPanel.add(contentPanel, BorderLayout.CENTER);

        centeredPanel.add(Box.createVerticalStrut(20));
        centeredPanel.add(fixedWidthPanel);
        centeredPanel.add(Box.createVerticalGlue());

        mainContainer.add(centeredPanel, BorderLayout.CENTER);

        setContentPane(mainContainer);
        setVisible(true);
    }

    private JPanel createSummaryPanel(int movieId, List<String> selectedSeats) {
        JPanel summaryPanel = new JPanel(new BorderLayout(DEFAULT_SPACING, DEFAULT_SPACING));
        summaryPanel.setBackground(Color.WHITE);
        summaryPanel.setBorder(new CompoundBorder(
                new LineBorder(Color.LIGHT_GRAY, 1),
                new EmptyBorder(DEFAULT_SPACING, DEFAULT_SPACING, DEFAULT_SPACING, DEFAULT_SPACING)
        ));

        JPanel totalPricePanel = new JPanel(new GridLayout(2, 1, 5, 2));
        totalPricePanel.setBackground(Color.WHITE);

        totalLabel = new JLabel("0 " + CURRENCY);
        totalLabel.setFont(new Font("Arial", Font.BOLD, 18));
        totalLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        totalLabel.setForeground(PRIMARY_COLOR);

        totalInWords = new JLabel("(Không đồng)");
        totalInWords.setFont(new Font("Arial", Font.ITALIC, 14));
        totalInWords.setHorizontalAlignment(SwingConstants.RIGHT);

        totalPricePanel.add(new JLabel("Tổng tiền:"));
        totalPricePanel.add(totalLabel);

        JPanel totalWordsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        totalWordsPanel.setBackground(Color.WHITE);
        totalWordsPanel.add(totalInWords);

        JButton checkoutButton = createStyledButton("Thanh toán", ACCENT_COLOR);
        checkoutButton.setPreferredSize(new Dimension(150, 50)); // Larger button for fullscreen
        checkoutButton.setFont(new Font("Arial", Font.BOLD, 16)); // Larger font
        checkoutButton.addActionListener(e -> processPayment(movieId, selectedSeats));

        summaryPanel.add(totalPricePanel, BorderLayout.CENTER);
        summaryPanel.add(totalWordsPanel, BorderLayout.SOUTH);
        summaryPanel.add(checkoutButton, BorderLayout.EAST);

        return summaryPanel;
    }

    private void initializePricesAndCategories() {
        prices.put("Coca-Cola", 20000);
        prices.put("Pepsi", 20000);
        prices.put("Mirinda", 20000);
        prices.put("Sprite", 20000);
        prices.put("Nước suối", 15000);

        prices.put("Bắp vị phô mai", 30000);
        prices.put("Bắp vị bơ", 30000);
        prices.put("Bắp vị hỗn hợp", 30000);
        prices.put("Bắp vị dâu", 30000);
        prices.put("Bắp vị socola", 30000);

        prices.put("1 Bắp & 2 Nước", 70000);
        prices.put("2 Bắp & 1 Nước", 70000);
        prices.put("2 Bắp & 2 Nước", 90000);

        categories.put("Nước uống", Arrays.asList("Coca-Cola", "Pepsi", "Mirinda", "Sprite", "Nước suối"));
        categories.put("Bắp rang", Arrays.asList("Bắp vị phô mai", "Bắp vị bơ", "Bắp vị hỗn hợp",
                "Bắp vị dâu", "Bắp vị socola"));
        categories.put("Combo ưu đãi", Arrays.asList("1 Bắp & 2 Nước", "2 Bắp & 1 Nước",
                "2 Bắp & 2 Nước"));
    }

    private JPanel createTitlePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(PRIMARY_COLOR);
        panel.setBorder(new EmptyBorder(DEFAULT_SPACING, DEFAULT_SPACING, DEFAULT_SPACING, DEFAULT_SPACING));

        JLabel titleLabel = new JLabel("Chọn món ăn và thức uống để thưởng thức khi xem phim");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);


        JPanel paddedPanel = new JPanel(new BorderLayout());
        paddedPanel.setBackground(PRIMARY_COLOR);
        paddedPanel.setBorder(new EmptyBorder(10, 0, 10, 0));
        paddedPanel.add(titleLabel, BorderLayout.NORTH);

        panel.add(paddedPanel, BorderLayout.CENTER);

        return panel;
    }

    private JTabbedPane createCategoryTabs() {
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Arial", Font.BOLD, 14));

        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

        for (Map.Entry<String, List<String>> category : categories.entrySet()) {
            String categoryName = category.getKey();
            List<String> items = category.getValue();

            JPanel categoryPanel = new JPanel();
            categoryPanel.setLayout(new GridLayout(0, 3, DEFAULT_SPACING * 2, DEFAULT_SPACING * 2)); // Increased spacing
            categoryPanel.setBorder(new EmptyBorder(DEFAULT_SPACING * 2, DEFAULT_SPACING * 2,
                    DEFAULT_SPACING * 2, DEFAULT_SPACING * 2));

            for (String item : items) {
                categoryPanel.add(createItemCard(item));
            }

            JScrollPane scrollPane = new JScrollPane(categoryPanel);
            scrollPane.setBorder(null);
            scrollPane.getVerticalScrollBar().setUnitIncrement(16);

            tabbedPane.addTab(categoryName, scrollPane);
        }

        return tabbedPane;
    }

    private JPanel createItemCard(String name) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout(10, 15));
        card.setBackground(Color.WHITE);
        card.setBorder(new CompoundBorder(
                new LineBorder(Color.LIGHT_GRAY, 1),
                new EmptyBorder(15, 15, 15, 15)
        ));

        JLabel nameLabel = new JLabel(name);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 18));
        nameLabel.setForeground(PRIMARY_COLOR);

        String formattedPrice = formatCurrency(prices.get(name));
        JLabel priceLabel = new JLabel(formattedPrice);
        priceLabel.setFont(new Font("Arial", Font.BOLD, 16));
        priceLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.add(nameLabel, BorderLayout.WEST);
        headerPanel.add(priceLabel, BorderLayout.EAST);

        JPanel quantityPanel = createQuantitySelector(name);

        card.add(headerPanel, BorderLayout.NORTH);
        card.add(quantityPanel, BorderLayout.SOUTH);

        return card;
    }

    private JPanel createQuantitySelector(String name) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel.setBackground(Color.WHITE);

        JButton minusButton = createStyledButton("-", new Color(230, 230, 230));
        minusButton.setPreferredSize(new Dimension(50, 40));
        minusButton.setFont(new Font("Arial", Font.BOLD, 18));

        JTextField quantityField = new JTextField("0", 2);
        quantityField.setHorizontalAlignment(JTextField.CENTER);
        quantityField.setFont(new Font("Arial", Font.BOLD, 16));
        quantityField.setEditable(false);
        quantityField.setPreferredSize(new Dimension(50, 40));
        quantityFields.put(name, quantityField);

        JButton plusButton = createStyledButton("+", PRIMARY_COLOR);
        plusButton.setPreferredSize(new Dimension(50, 40));
        plusButton.setFont(new Font("Arial", Font.BOLD, 18));

        minusButton.addActionListener(e -> updateQuantity(name, -1));
        plusButton.addActionListener(e -> updateQuantity(name, 1));

        panel.add(minusButton);
        panel.add(quantityField);
        panel.add(plusButton);

        return panel;
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(REGULAR_FONT);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true); // Make sure the background color is shown

        // Ensure the button retains its styling on all platforms
        button.setContentAreaFilled(true);

        // Optional: add a small margin to make the button text more visible
        button.setMargin(new Insets(5, 10, 5, 10));

        return button;
    }

    private void updateQuantity(String name, int delta) {
        JTextField field = quantityFields.get(name);
        int current = Integer.parseInt(field.getText());
        int newValue = Math.max(0, current + delta);

        field.setText(String.valueOf(newValue));
        selectedItems.put(name, newValue);
        updateTotal();
    }


    private void updateTotal() {
        totalPrice = selectedItems.entrySet().stream()
                .mapToDouble(e -> e.getValue() * prices.get(e.getKey()))
                .sum();

        totalLabel.setText(formatCurrency((int) totalPrice));
        totalInWords.setText("(" + convertNumberToWords((int) totalPrice) + ")");
    }


    private String formatCurrency(int amount) {
        return currencyFormatter.format(amount) + " " + CURRENCY;
    }

    private void processPayment(int movieId, List<String> selectedSeats) {
        double seatPrice = selectedSeats.size() * 50000;
        double grandTotal = totalPrice + seatPrice;

        StringBuilder snackItems = new StringBuilder();
        for (Map.Entry<String, Integer> entry : selectedItems.entrySet()) {
            if (entry.getValue() > 0) {
                snackItems.append(entry.getKey())
                        .append(":")
                        .append(entry.getValue())
                        .append(",");
            }
        }

        new PaymentPanel(movieId, selectedSeats, snackItems.toString(), grandTotal);
        dispose();
    }

    private String convertNumberToWords(int num) {
        if (num == 0) return "Không đồng";

        String[] units = {"", "một", "hai", "ba", "bốn", "năm", "sáu", "bảy", "tám", "chín"};
        String[] tens = {"", "mười", "hai mươi", "ba mươi", "bốn mươi", "năm mươi",
                "sáu mươi", "bảy mươi", "tám mươi", "chín mươi"};
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
                    if (remainder < 10) {
                        section.append(units[remainder]);
                    } else {
                        section.append(tens[remainder / 10]);
                        if (remainder % 10 > 0) {
                            section.append(" ").append(units[remainder % 10]);
                        }
                    }
                }

                section.append(" ").append(thousands[index]).append(" ");
                result.insert(0, section);
            }

            num /= 1000;
            index++;
        }

        return result.toString().trim() + " đồng";
    }
}