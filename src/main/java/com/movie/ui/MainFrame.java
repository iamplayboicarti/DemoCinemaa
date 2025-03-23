package main.java.com.movie.ui;

import main.java.com.movie.db.Database;
import main.java.com.movie.model.Movie;
import main.java.com.movie.util.CalendarUtil;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class MainFrame extends JFrame {
    public MainFrame() {
        setTitle("🎬 Movie Ticket Booking");
        setSize(900, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // 🔹 Header
        JLabel headerLabel = new JLabel("🎥 Đặt Vé Xem Phim", JLabel.CENTER);
        headerLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        headerLabel.setForeground(Color.WHITE);
        headerLabel.setOpaque(true);
        headerLabel.setBackground(new Color(30, 30, 30));
        headerLabel.setPreferredSize(new Dimension(900, 50)); // Sử dụng giá trị cố định
        add(headerLabel, BorderLayout.NORTH);

        // 🔹 Menu Bar
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("☰ Menu");
        menu.setFont(new Font("SansSerif", Font.BOLD, 14));
        menuBar.add(menu);
        setJMenuBar(menuBar);

        // 🔹 Tabs
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("SansSerif", Font.PLAIN, 14));
        tabbedPane.addTab("🎬 Phim", new MoviePanel());
        tabbedPane.addTab("📅 Lịch Chiếu", createSchedulePanel());
        add(tabbedPane, BorderLayout.CENTER);

        setVisible(true);
    }
    private JPanel createSchedulePanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // 🔹 Định dạng ngày + giờ tiếng Việt
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, dd MMMM yyyy", new Locale("vi", "VN"));
        JComboBox<String> dateBox = new JComboBox<>();

        for (int i = 0; i < 7; i++) {
            Date date = CalendarUtil.getDate(i);
            String randomTime = generateRandomTime();
            dateBox.addItem(dateFormat.format(date) + " - " + randomTime);
        }

        // 🔹 Movie List Panel
        JPanel movieList = new JPanel(new GridLayout(0, 3, 10, 10));
        JScrollPane scrollPane = new JScrollPane(movieList);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

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

        panel.add(dateBox, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createMovieCard(Movie movie) {
        JPanel cardPanel = new JPanel(new BorderLayout());
        cardPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1, true));
        cardPanel.setPreferredSize(new Dimension(180, 280)); // Kích thước giống MoviePanel

        // 🔹 Load hình ảnh phim
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
        Image img = icon.getImage().getScaledInstance(180, 200, Image.SCALE_SMOOTH); // Kích thước ảnh giống MoviePanel
        JLabel imageLabel = new JLabel(new ImageIcon(img));
        imageLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0)); // Loại bỏ padding thừa
        imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // Căn giữa ảnh

        // 🔹 Hiển thị tiêu đề phim + số ghế trống
        int emptySeats = (int) Database.getSeatsByMovieId(movie.getId()).stream()
                .filter(s -> s.getStatus().equals("empty")).count();
        JLabel titleLabel = new JLabel("<html><center>" + movie.getTitle() +
                "<br>(" + emptySeats + " ghế trống)</center></html>", JLabel.CENTER);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0)); // Padding nhỏ cho tiêu đề

        // 🔹 Bắt sự kiện click vào phim
        cardPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                new SeatPanel(movie.getId());
            }
        });

        // 🔹 Bố trí giống MoviePanel, loại bỏ khoảng trống thừa
        cardPanel.add(titleLabel, BorderLayout.NORTH);
        cardPanel.add(imageLabel, BorderLayout.CENTER);
        cardPanel.setMaximumSize(new Dimension(180, 280)); // Đảm bảo không vượt kích thước

        return cardPanel;
    }

    // 🔹 Hàm tạo giờ giả lập (10:00 - 23:00)
    private String generateRandomTime() {
        Random rand = new Random();
        int hour = rand.nextInt(14) + 10; // Random từ 10 đến 23
        int minute = rand.nextInt(6) * 10; // Random phút: 00, 10, 20, 30, 40, 50
        return String.format("%02d:%02d", hour, minute);
    }
}