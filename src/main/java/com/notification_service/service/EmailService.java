package com.notification_service.service;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment;
import com.notification_service.dto.BookingDTO;
import com.notification_service.dto.NotificationRequest;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;


import java.io.ByteArrayOutputStream;
@Service
@Slf4j
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendEmailWithPdf(NotificationRequest request) throws Exception {
        log.info("Email sent to this email: {}",request.getEmail());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        PdfWriter writer = new PdfWriter(outputStream);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc);

        document.add(new Paragraph("🎟️ Booking Confirmation")
                .setFontSize(20)
                .setBold()
                .setTextAlignment(TextAlignment.CENTER));

        document.add(new Paragraph("User Name: " + request.getUserName()));
        document.add(new Paragraph("User Email: " + request.getEmail()));
        document.add(new Paragraph("Event: " + request.getEventName()));
        document.add(new Paragraph("Booking ID: " + request.getBookingId()));
        document.add(new Paragraph("Number of Tickets: " + request.getNumberOfTickets()));
        document.add(new Paragraph("Payment Amount: $" + request.getPaymentAmount()));
        document.add(new Paragraph("Payment Status: " + request.getPaymentStatus()));
        document.add(new Paragraph("Booking Time: " + request.getBookingTime()));

        document.close();

        sendMailWithAttachment(request.getEmail(), "\uD83C\uDF9F\uFE0F \uD83C\uDF9F\uFE0F Booking Confirmation – Your Event Details Inside",
                "Dear "+request.getUserName()+",\n" +
                        "We’re thrilled to confirm your booking for "+ request.getEventName() +" !. Your reservation is now secured, and we can’t wait to see you there.\n" +
                        "\uD83D\uDCC5 Event Date: "+request.getEventDate()+"\n" +
                        "\uD83D\uDCCD Venue: "+request.getEventLocation()+"\n" +
                        "\uD83D\uDD22 Booking Reference: "+request.getBookingId()+"\n" +
                        "Attached, you’ll find your official booking receipt. Please keep it handy for event entry.\n" +
                        "If you have any questions or need assistance, feel free to reach out to us.\n" +
                        "Looking forward to an amazing experience together!\n" +
                        "Best regards,\n" +
                        "BeastHack Tracker\n", outputStream.toByteArray());
    }

    private void sendMailWithAttachment(String to, String subject, String body, byte[] pdfBytes) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body);


            ByteArrayResource pdfAttachment = new ByteArrayResource(pdfBytes);
            helper.addAttachment("Booking_Receipt.pdf", pdfAttachment);

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email with PDF", e);
        }
    }

    public void sendCancelNotification(BookingDTO bookingDTO,String email) {
        log.info("Email: {}",email);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        PdfWriter writer = new PdfWriter(outputStream);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc);
        document.add(new Paragraph("❌ Booking Cancellation Notice")
                .setFontSize(20)
                .setBold()
                .setTextAlignment(TextAlignment.CENTER));

        document.add(new Paragraph("User Email: " + email));
        document.add(new Paragraph("Event id: " + bookingDTO.getEventId()));
        document.add(new Paragraph("Booking ID: " + bookingDTO.getId()));
        document.add(new Paragraph("Number of Tickets Cancelled: " + bookingDTO.getNumberOfCancelTickets()));
        document.add(new Paragraph("Refund Amount: $" + 120));
        document.add(new Paragraph("Booking Status: " + bookingDTO.getBookingStatus()));
        document.add(new Paragraph("Cancellation Time: " + bookingDTO.getCancellationTime()));
        document.close();

        sendMailWithAttachment(email,
                "💔 Booking Cancelled – Details of Your Cancelled Reservation",
                "Dear " + email + ",\n" +
                        "Your booking for *" + bookingDTO.getEventId() + "* has been successfully cancelled. We're sorry to see you go, but we understand plans can change.\n\n" +
                        "🧾 Booking ID: " + bookingDTO.getId() + "\n" +
                        "🎟️ Number of Tickets Cancelled: " + bookingDTO.getNumberOfCancelTickets() + "\n" +
                        "💰 Refund Amount: $" + 120 + "\n" +
                        "Your refund (if applicable) will be processed shortly. Please allow a few business days for the transaction to reflect in your account.\n\n" +
                        "If you need help or wish to rebook, our support team is here for you.\n\n" +
                        "Warm regards,\n" +
                        "BeastHack Tracker",
                outputStream.toByteArray()
        );
    }
}
