package org.example.ovr.service;


import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import org.example.ovr.Entity.Bill;
import org.example.ovr.repository.BillRepository;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class BillService {

    private final BillRepository billRepository;

    // Base URL to serve PDF files (e.g., via a static resource mapping in Spring)
    private static final String BASE_PDF_URL = "http://localhost:8080/bills/";

    public BillService(BillRepository billRepository) {
        this.billRepository = billRepository;
    }

    // ===============================
    // Save Bill
    // ===============================
    public Bill saveBill(Bill bill) {
        return billRepository.save(bill);
    }

    // ===============================
    // Get All Bills
    // ===============================
    public List<Bill> getAllBills() {
        return billRepository.findAll();
    }

    // ===============================
    // Get Bills by Reservation ID
    // ===============================
    public List<Bill> getBillsByReservationId(Long reservationId) {
        return billRepository.findByReservationId(reservationId);
    }

    // ===============================
    // Delete Bill
    // ===============================
    public void deleteBill(Long id) {
        billRepository.deleteById(id);
    }

    // ===============================
    // Generate PDF and return file path
    // ===============================
    public Bill generateBillPdf(Bill bill) {
        try {
            String pdfDir = "bills/"; // relative to project root
            File dir = new File(pdfDir);
            if (!dir.exists()) { dir.mkdirs(); }

            String fileName = "Bill_" + bill.getId() + ".pdf";
            String filePath = pdfDir + fileName;

            PdfWriter writer = new PdfWriter(filePath);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

            // ===========================
            // Header
            // ===========================
            document.add(new Paragraph("************************************************************")
                    .setTextAlignment(TextAlignment.CENTER));
            document.add(new Paragraph("*************** OCEAN VIEW RESORT ****************")
                    .setBold().setFontSize(20).setTextAlignment(TextAlignment.CENTER));
            document.add(new Paragraph("************************************************************")
                    .setTextAlignment(TextAlignment.CENTER));
            document.add(new Paragraph("\n"));
            document.add(new Paragraph("################### GUEST INVOICE #####################")
                    .setBold().setTextAlignment(TextAlignment.CENTER));
            document.add(new Paragraph("\n"));

            // ===========================
            // Guest & Reservation Info
            // ===========================
            Table guestTable = new Table(UnitValue.createPercentArray(new float[]{3,3})).useAllAvailableWidth();
            guestTable.addCell(new Cell().add(new Paragraph("Invoice No:").setBold()));
            guestTable.addCell(new Cell().add(new Paragraph("INV-" + bill.getId())));
            guestTable.addCell(new Cell().add(new Paragraph("Reservation ID:").setBold()));
            guestTable.addCell(new Cell().add(new Paragraph(String.valueOf(bill.getReservationId()))));
            guestTable.addCell(new Cell().add(new Paragraph("Room Number:").setBold()));
            guestTable.addCell(new Cell().add(new Paragraph(String.valueOf(bill.getRoomNumber()))));
            guestTable.addCell(new Cell().add(new Paragraph("Check-In:").setBold()));
            guestTable.addCell(new Cell().add(new Paragraph(bill.getCreatedDate().format(dtf))));
            guestTable.addCell(new Cell().add(new Paragraph("Check-Out:").setBold()));
            guestTable.addCell(new Cell().add(new Paragraph(bill.getCreatedDate().plusDays(bill.getNumberOfDays()).format(dtf))));
            guestTable.addCell(new Cell().add(new Paragraph("Number of Nights:").setBold()));
            guestTable.addCell(new Cell().add(new Paragraph(String.valueOf(bill.getNumberOfDays()))));
            document.add(guestTable);
            document.add(new Paragraph("\n"));

            // ===========================
            // Charges Table
            // ===========================
            Table chargesTable = new Table(UnitValue.createPercentArray(new float[]{4,1,2,2})).useAllAvailableWidth();
            chargesTable.addHeaderCell(new Cell().add(new Paragraph("Description").setBold()));
            chargesTable.addHeaderCell(new Cell().add(new Paragraph("Days").setBold()));
            chargesTable.addHeaderCell(new Cell().add(new Paragraph("Unit Price").setBold()));
            chargesTable.addHeaderCell(new Cell().add(new Paragraph("Amount").setBold()));
            chargesTable.addCell("Room Charge");
            chargesTable.addCell(String.valueOf(bill.getNumberOfDays()));
            chargesTable.addCell(String.format("%.2f", bill.getRoomPrice()));
            chargesTable.addCell(String.format("%.2f", bill.getRoomPrice() * bill.getNumberOfDays()));
            document.add(chargesTable);
            document.add(new Paragraph("\n"));

            // ===========================
            // Summary Table
            // ===========================
            double subTotal = bill.getRoomPrice() * bill.getNumberOfDays();
            double serviceCharge = subTotal * 0.05;
            double tax = subTotal * 0.10;
            double grandTotal = subTotal + serviceCharge + tax;

            Table summaryTable = new Table(UnitValue.createPercentArray(new float[]{3,1})).useAllAvailableWidth();
            summaryTable.addCell(new Cell().add(new Paragraph("Sub Total").setBold()));
            summaryTable.addCell(new Cell().add(new Paragraph(String.format("%.2f", subTotal)).setBold()));
            summaryTable.addCell(new Cell().add(new Paragraph("Service Charge (5%)").setBold()));
            summaryTable.addCell(new Cell().add(new Paragraph(String.format("%.2f", serviceCharge)).setBold()));
            summaryTable.addCell(new Cell().add(new Paragraph("Tax (10%)").setBold()));
            summaryTable.addCell(new Cell().add(new Paragraph(String.format("%.2f", tax)).setBold()));
            summaryTable.addCell(new Cell().add(new Paragraph("GRAND TOTAL").setBold().setFontSize(14)));
            summaryTable.addCell(new Cell().add(new Paragraph(String.format("%.2f", grandTotal)).setBold().setFontSize(14)));
            document.add(summaryTable);
            document.add(new Paragraph("\n"));

            // ===========================
            // Footer
            // ===========================
            document.add(new Paragraph("############################################################").setTextAlignment(TextAlignment.CENTER));
            document.add(new Paragraph("*********** THANK YOU FOR STAYING WITH US! **************").setTextAlignment(TextAlignment.CENTER));
            document.add(new Paragraph("############################################################").setTextAlignment(TextAlignment.CENTER));

            document.close();

            // ===========================
            // Save PDF path & URL
            // ===========================
            bill.setPdfPath(filePath);
            bill.setPdfUrl("http://localhost:8080/bills/" + fileName);

            return billRepository.save(bill);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error generating PDF: " + e.getMessage());
        }
    }
}