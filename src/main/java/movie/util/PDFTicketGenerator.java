package movie.util;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.awt.Desktop;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Element;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.Barcode;
import com.itextpdf.text.pdf.Barcode128;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.Font;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPCell;

import movie.db.Database;

/**
 * Utility class to generate PDF tickets for movie bookings with Vietnamese support
 */
public class PDFTicketGenerator {
    private static final DecimalFormat df = new DecimalFormat("#,### VNĐ");

    // Define common colors
    private static final com.itextpdf.text.BaseColor PRIMARY_COLOR = new com.itextpdf.text.BaseColor(41, 128, 185);
    private static final com.itextpdf.text.BaseColor ACCENT_COLOR = new com.itextpdf.text.BaseColor(231, 76, 60);
    private static final com.itextpdf.text.BaseColor LIGHT_GRAY = new com.itextpdf.text.BaseColor(245, 245, 245);

    /**
     * Generates a PDF ticket for a movie booking with Vietnamese support
     *
     * @param orderId Order ID from the database
     * @param movieId Movie ID
     * @param selectedSeats List of selected seat names
     * @param snackItems Snack items as a comma-separated string
     * @param totalPrice Total price of the booking
     * @param customerName Customer's name for display in the ticket
     * @param email Customer email
     * @return Path to the generated PDF file
     * @throws Exception If PDF generation fails
     */
    public static String generateTicket(int orderId, int movieId, List<String> selectedSeats,
                                        String snackItems, double totalPrice, String customerName, String email) throws Exception {
        // Get movie details from database
        String movieTitle = Database.getMovieTitle(movieId);
        String showDate = Database.getMovieShowtime(movieId);

        // Create directory for tickets if it doesn't exist
        File ticketsDir = new File("tickets");
        if (!ticketsDir.exists()) {
            ticketsDir.mkdir();
        }

        // Create file name with timestamp to avoid duplicates
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String timestamp = dateFormat.format(new Date());
        String fileName = "tickets/ticket_" + orderId + "_" + timestamp + ".pdf";

        // Create PDF document
        Document document = new Document(PageSize.A5, 36, 36, 54, 36); // Use A5 size for ticket-like appearance
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(fileName));
        document.open();

        // Setup Vietnamese font
        BaseFont vietnameseFont = getVietnameseFont();
        Font headerFont = new Font(vietnameseFont, 20, Font.BOLD, PRIMARY_COLOR);
        Font titleFont = new Font(vietnameseFont, 16, Font.BOLD, PRIMARY_COLOR);
        Font boldFont = new Font(vietnameseFont, 12, Font.BOLD);
        Font normalFont = new Font(vietnameseFont, 12, Font.NORMAL);
        Font smallFont = new Font(vietnameseFont, 9, Font.ITALIC);
        Font emphasisFont = new Font(vietnameseFont, 12, Font.BOLD, ACCENT_COLOR);

        // Add ticket header
        Paragraph header = new Paragraph("VÉ XEM PHIM", headerFont);
        header.setAlignment(Element.ALIGN_CENTER);
        document.add(header);

        // Add subtitle with cinema name
        Paragraph cinemaName = new Paragraph("CINEMA STAR", new Font(vietnameseFont, 14, Font.BOLD));
        cinemaName.setAlignment(Element.ALIGN_CENTER);
        document.add(cinemaName);
        document.add(Chunk.NEWLINE);

        try {
            // Add a horizontal line
            PdfPTable line = new PdfPTable(1);
            line.setWidthPercentage(100);
            PdfPCell cell = new PdfPCell();
            cell.setBorderWidth(1f);
            cell.setBorderColor(PRIMARY_COLOR);
            cell.setFixedHeight(2f);
            cell.setBorder(Rectangle.BOTTOM);
            line.addCell(cell);
            document.add(line);
            document.add(Chunk.NEWLINE);
        } catch (Exception e) {
            System.err.println("Could not add line: " + e.getMessage());
        }

        // Add movie title with emphasis
        if (movieTitle != null && !movieTitle.isEmpty()) {
            Paragraph movie = new Paragraph("Phim: ", boldFont);
            movie.add(new Chunk(movieTitle, titleFont));
            document.add(movie);
            document.add(Chunk.NEWLINE);
        }

        // Create a table for ticket details
        PdfPTable detailsTable = new PdfPTable(2);
        detailsTable.setWidthPercentage(100);
        try {
            detailsTable.setWidths(new float[]{1, 3});
        } catch (Exception e) {
            System.err.println("Could not set table widths: " + e.getMessage());
        }

        // Add customer name to the ticket
        addTableRow(detailsTable, "Khách hàng:", customerName, boldFont, normalFont);

        // Add ticket details
        addTableRow(detailsTable, "Mã đơn hàng:", String.valueOf(orderId), boldFont, normalFont);
        addTableRow(detailsTable, "Ngày chiếu:", (showDate != null ? showDate : "N/A"), boldFont, normalFont);
        addTableRow(detailsTable, "Ghế:", String.join(", ", selectedSeats), boldFont, emphasisFont);

        String snacksText = (snackItems != null && !snackItems.isEmpty() && !snackItems.equals("Không có")) ?
                snackItems : "Không có";
        addTableRow(detailsTable, "Đồ ăn:", snacksText, boldFont, normalFont);

        addTableRow(detailsTable, "Tổng tiền:", df.format(totalPrice), boldFont, emphasisFont);
        addTableRow(detailsTable, "Email:", email, boldFont, normalFont);

        document.add(detailsTable);
        document.add(Chunk.NEWLINE);

        // Add barcode or QR code for ticket validation
        try {
            document.add(new Paragraph("Mã vé", new Font(vietnameseFont, 10, Font.BOLD)));

            Barcode128 barcode = new Barcode128();
            barcode.setCode("TIX" + orderId);
            barcode.setCodeType(Barcode.CODE128);
            Image barcodeImage = barcode.createImageWithBarcode(writer.getDirectContent(), null, null);
            barcodeImage.setAlignment(Element.ALIGN_CENTER);
            barcodeImage.scaleToFit(250, 50);
            document.add(barcodeImage);
        } catch (Exception e) {
            System.err.println("Could not generate barcode: " + e.getMessage());
        }

        // Add instruction text in a box
        try {
            PdfPTable noteTable = new PdfPTable(1);
            noteTable.setWidthPercentage(100);

            PdfPCell noteCell = new PdfPCell();
            noteCell.setBackgroundColor(LIGHT_GRAY);
            noteCell.setBorderColor(PRIMARY_COLOR);
            noteCell.setBorderWidth(1f);
            noteCell.setPadding(10f);

            Paragraph note = new Paragraph(
                    "Vui lòng đến trước suất chiếu 15 phút để kiểm tra vé. Cảm ơn quý khách!", normalFont);
            note.setAlignment(Element.ALIGN_CENTER);
            noteCell.addElement(note);

            noteTable.addCell(noteCell);
            document.add(Chunk.NEWLINE);
            document.add(noteTable);
        } catch (Exception e) {
            System.err.println("Could not add instruction box: " + e.getMessage());

            // Fallback to simple text
            document.add(Chunk.NEWLINE);
            Paragraph note = new Paragraph(
                    "Vui lòng đến trước suất chiếu 15 phút để kiểm tra vé. Cảm ơn quý khách!", normalFont);
            note.setAlignment(Element.ALIGN_CENTER);
            document.add(note);
        }

        // Add timestamp
        document.add(Chunk.NEWLINE);
        Paragraph footer = new Paragraph(
                "Vé tạo lúc: " + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()), smallFont);
        footer.setAlignment(Element.ALIGN_RIGHT);
        document.add(footer);

        // Close document
        document.close();

        System.out.println("PDF Ticket created successfully: " + fileName);

        // Optionally, open the PDF automatically
        try {
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(new File(fileName));
            }
        } catch (Exception e) {
            System.err.println("Could not open PDF automatically: " + e.getMessage());
        }

        return fileName;
    }

    /**
     * Helper method to add a row to the details table
     */
    private static void addTableRow(PdfPTable table, String label, String value, Font labelFont, Font valueFont) {
        PdfPCell labelCell = new PdfPCell(new Paragraph(label, labelFont));
        labelCell.setBorder(Rectangle.NO_BORDER);
        labelCell.setPaddingBottom(8f);

        PdfPCell valueCell = new PdfPCell(new Paragraph(value, valueFont));
        valueCell.setBorder(Rectangle.NO_BORDER);
        valueCell.setPaddingBottom(8f);

        table.addCell(labelCell);
        table.addCell(valueCell);
    }

    /**
     * Get a font that supports Vietnamese characters
     *
     * @return BaseFont that supports Vietnamese
     * @throws Exception If font creation fails
     */
    private static BaseFont getVietnameseFont() throws Exception {
        // Try to use a built-in font that supports Vietnamese
        try {
            // First, try to find Vietnamese font in the system
            File fontFile = null;

            // Common Vietnamese font locations on Windows
            String[] possibleFontPaths = {
                    "c:/windows/fonts/arial.ttf",
                    "c:/windows/fonts/times.ttf",
                    "c:/windows/fonts/tahoma.ttf",
                    // MacOS font paths
                    "/Library/Fonts/Arial Unicode.ttf",
                    "/System/Library/Fonts/Apple Color Emoji.ttc",
                    // Linux font paths
                    "/usr/share/fonts/truetype/liberation/LiberationSans-Regular.ttf",
                    "/usr/share/fonts/truetype/dejavu/DejaVuSans.ttf"
            };

            // Check for existence of fonts
            for (String path : possibleFontPaths) {
                File f = new File(path);
                if (f.exists()) {
                    fontFile = f;
                    break;
                }
            }

            // If a font file was found, use it
            if (fontFile != null) {
                return BaseFont.createFont(
                        fontFile.getAbsolutePath(),
                        BaseFont.IDENTITY_H,
                        BaseFont.EMBEDDED
                );
            }

            // Fall back to built-in fonts with Unicode support
            return BaseFont.createFont(
                    BaseFont.HELVETICA,
                    BaseFont.CP1252,
                    BaseFont.EMBEDDED
            );
        } catch (Exception e) {
            System.err.println("Error loading Vietnamese font: " + e.getMessage());
            // Fallback to standard font
            return BaseFont.createFont(
                    BaseFont.HELVETICA,
                    BaseFont.CP1252,
                    BaseFont.NOT_EMBEDDED
            );
        }
    }

    /**
     * Legacy method for backward compatibility
     */
    public static String generateTicket(int orderId, int movieId, List<String> selectedSeats,
                                        String snackItems, double totalPrice, String email) throws Exception {
        // Call the new method with an empty customer name for backward compatibility
        return generateTicket(orderId, movieId, selectedSeats, snackItems, totalPrice, "Không có tên", email);
    }
}