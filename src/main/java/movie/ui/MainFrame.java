package movie.ui;

import movie.db.Database;
import movie.model.Movie;
import movie.util.CalendarUtil;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class MainFrame extends JFrame {
    // UI Colors
    private static final Color HEADER_COLOR = new  Color(41, 128, 185);
    private static final Color BACKGROUND_COLOR = new Color(245, 245, 245);
    private static final Color CARD_BORDER_COLOR = new Color(200, 200, 200);

    public MainFrame() {
        setTitle("🎬 Movie Ticket Booking");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(BACKGROUND_COLOR);

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(HEADER_COLOR);
        headerPanel.setBorder(new EmptyBorder(15, 20, 15, 20));

        JLabel headerLabel = new JLabel("🎥 HỆ THỐNG ĐẶT VÉ XEM PHIM", JLabel.CENTER);
        headerLabel.setFont(new Font("SansSerif", Font.BOLD, 28));
        headerLabel.setForeground(Color.WHITE);
        headerPanel.add(headerLabel, BorderLayout.CENTER);

        add(headerPanel, BorderLayout.NORTH);

        JMenuBar menuBar = new JMenuBar();
        menuBar.setBorder(new EmptyBorder(5, 10, 5, 10));

        JMenu menu = new JMenu("☰ Menu");
        menu.setFont(new Font("SansSerif", Font.BOLD, 16));

        JMenuItem homeItem = new JMenuItem("Trang chủ");
        homeItem.setFont(new Font("SansSerif", Font.PLAIN, 14));

        JMenuItem accountItem = new JMenuItem("Tài khoản");
        accountItem.setFont(new Font("SansSerif", Font.PLAIN, 14));

        JMenuItem settingsItem = new JMenuItem("Cài đặt");
        settingsItem.setFont(new Font("SansSerif", Font.PLAIN, 14));

        menu.add(homeItem);
        menu.add(accountItem);
        menu.add(settingsItem);

        menuBar.add(menu);
        setJMenuBar(menuBar);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("SansSerif", Font.BOLD, 16));
        tabbedPane.addTab("🎬 Phim", createMoviePanel());
        tabbedPane.addTab("📅 Lịch Chiếu", createSchedulePanel());

        JPanel contentWrapper = new JPanel(new BorderLayout());
        contentWrapper.setBorder(new EmptyBorder(10, 10, 10, 10));
        contentWrapper.setBackground(BACKGROUND_COLOR);
        contentWrapper.add(tabbedPane, BorderLayout.CENTER);

        add(contentWrapper, BorderLayout.CENTER);

        setVisible(true);
    }

    private JPanel createMoviePanel() {
        return new MoviePanel();
    }

    private JPanel createSchedulePanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 20)); // Add some spacing
        panel.setBackground(BACKGROUND_COLOR);

        JPanel datePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        datePanel.setBackground(BACKGROUND_COLOR);
        datePanel.setBorder(new EmptyBorder(10, 0, 20, 0));

        JLabel dateLabel = new JLabel("Chọn ngày xem phim: ");
        dateLabel.setFont(new Font("SansSerif", Font.BOLD, 16));

        // 🔹 Định dạng ngày + giờ tiếng Việt
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, dd MMMM yyyy", new Locale("vi", "VN"));
        JComboBox<String> dateBox = new JComboBox<>();
        dateBox.setFont(new Font("SansSerif", Font.PLAIN, 16));
        dateBox.setPreferredSize(new Dimension(400, 40));

        for (int i = 0; i < 7; i++) {
            Date date = CalendarUtil.getDate(i);
            String randomTime = generateRandomTime();
            dateBox.addItem(dateFormat.format(date) + " - " + randomTime);
        }

        datePanel.add(dateLabel);
        datePanel.add(dateBox);

        JPanel centeringPanel = new JPanel(new GridBagLayout());
        centeringPanel.setBackground(BACKGROUND_COLOR);

        JPanel movieListContainer = new JPanel(new BorderLayout());
        movieListContainer.setBackground(BACKGROUND_COLOR);
        movieListContainer.setBorder(new EmptyBorder(0, 20, 20, 20));

        JPanel movieList = new JPanel(new GridLayout(0, 4, 25, 25));
        movieList.setBackground(BACKGROUND_COLOR);

        movieList.setPreferredSize(new Dimension(
                Math.min(1600, Toolkit.getDefaultToolkit().getScreenSize().width - 100), 800));

        JScrollPane scrollPane = new JScrollPane(movieList);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        scrollPane.getVerticalScrollBar().setUnitIncrement(20); // Smoother scrolling
        scrollPane.setBackground(BACKGROUND_COLOR);

        movieListContainer.add(scrollPane, BorderLayout.CENTER);
        centeringPanel.add(movieListContainer);

        dateBox.addActionListener(e -> {
            Date selectedDate = CalendarUtil.getDate(dateBox.getSelectedIndex());
            movieList.removeAll();
            List<Movie> movies = Database.getMoviesByDate(selectedDate);

            if (movies != null) { // Kiểm tra null
                for (Movie movie : movies) {
                    movieList.add(createMovieCard(movie));
                }
            }

            movieList.revalidate();
            movieList.repaint();
        });

        // 🔹 Load dữ liệu ngày đầu tiên
        dateBox.setSelectedIndex(0);
        dateBox.getActionListeners()[0].actionPerformed(null);

        panel.add(datePanel, BorderLayout.NORTH);
        panel.add(centeringPanel, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createMovieCard(Movie movie) {
        JPanel cardPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();

                // Add shadow effect
                int shadowGap = 5;
                int shadowOffset = 4;
                g2d.setColor(new Color(0, 0, 0, 50));
                g2d.fillRoundRect(shadowOffset, shadowOffset,
                        getWidth() - shadowGap - shadowOffset,
                        getHeight() - shadowGap - shadowOffset,
                        10, 10);

                g2d.setColor(Color.WHITE);
                g2d.fillRoundRect(0, 0, getWidth() - shadowGap,
                        getHeight() - shadowGap, 10, 10);

                g2d.dispose();
            }
        };

        cardPanel.setLayout(new BorderLayout());
        cardPanel.setPreferredSize(new Dimension(280, 380)); // Larger for fullscreen
        cardPanel.setOpaque(false);
        cardPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // 🔹 Load hình ảnh phim - larger for fullscreen
        String imagePath = "src/resources/images/" + movie.getImage();
        ImageIcon icon;
        try {
            icon = new ImageIcon(imagePath);
            if (icon.getImageLoadStatus() != MediaTracker.COMPLETE) {
                icon = new ImageIcon("src/resources/images/default.jpg"); // Hình mặc định nếu lỗi
            }
        } catch (Exception e) {
            icon = new ImageIcon("src/resources/images/default.jpg"); // Hình mặc định nếu lỗi
        }
        Image img = icon.getImage().getScaledInstance(260, 300, Image.SCALE_SMOOTH);
        JLabel imageLabel = new JLabel(new ImageIcon(img));
        imageLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // 🔹 Hiển thị tiêu đề phim + số ghế trống - improved styling
        int emptySeats = (int) Database.getSeatsByMovieId(movie.getId()).stream()
                .filter(s -> s.getStatus().equals("empty")).count();

        JPanel infoPanel = new JPanel(new BorderLayout(5, 5));
        infoPanel.setOpaque(false);
        infoPanel.setBorder(new EmptyBorder(10, 5, 5, 5));

        JLabel titleLabel = new JLabel(movie.getTitle());
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        titleLabel.setForeground(new Color(33, 33, 33));

        JLabel seatsLabel = new JLabel(emptySeats + " ghế trống");
        seatsLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        seatsLabel.setForeground(new Color(117, 117, 117));

        infoPanel.add(titleLabel, BorderLayout.NORTH);
        infoPanel.add(seatsLabel, BorderLayout.CENTER);

        // Add a styled button for booking
        JButton bookButton = createStylishButton("Đặt vé", new Color(231, 76, 60), Color.WHITE);
        bookButton.addActionListener(e -> new SeatPanel(movie.getId()));

        // 🔹 Bố trí các phần tử với khoảng cách phù hợp
        cardPanel.add(infoPanel, BorderLayout.NORTH);
        cardPanel.add(imageLabel, BorderLayout.CENTER);
        cardPanel.add(bookButton, BorderLayout.SOUTH);

        return cardPanel;
    }

    private JButton createStylishButton(String text, Color bgColor, Color textColor) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Fill button with gradient
                GradientPaint gp = new GradientPaint(
                        0, 0, bgColor,
                        0, getHeight(), bgColor.darker());
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);

                // Draw text
                FontMetrics fm = g2.getFontMetrics();
                Rectangle textRect = new Rectangle(0, 0, getWidth(), getHeight());
                String buttonText = getText();
                g2.setColor(textColor);
                g2.setFont(getFont());
                int x = (textRect.width - fm.stringWidth(buttonText)) / 2;
                int y = (textRect.height - fm.getHeight()) / 2 + fm.getAscent();
                g2.drawString(buttonText, x, y);
                g2.dispose();
            }
        };

        button.setFont(new Font("SansSerif", Font.BOLD, 15));
        button.setForeground(textColor);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(0, 45)); // Taller for fullscreen

        return button;
    }

    // 🔹 Hàm tạo giờ giả lập (10:00 - 23:00)
    private String generateRandomTime() {
        Random rand = new Random();
        int hour = rand.nextInt(14) + 10; // Random từ 10 đến 23
        int minute = rand.nextInt(6) * 10; // Random phút: 00, 10, 20, 30, 40, 50
        return String.format("%02d:%02d", hour, minute);
    }
}