package com.example.buchladen.Utilities;

import com.example.buchladen.Model.Order;
import com.example.buchladen.Service.CartServiceImpl;
import com.example.buchladen.web.dto.CartDto;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class PdfInvoiceGenerator {

    public static byte[] generate(Order order) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, out);
            document.open();

            try {
                Image logo = Image.getInstance(PdfInvoiceGenerator.class.getResource("/static/images/logo.png"));
                logo.scaleToFit(100, 100);
                document.add(logo);

            } catch (Exception ex) {
                document.add(new Paragraph("[Bookshop Logo]"));
            }

            Paragraph shopInfo = new Paragraph("Buchhandlung GmbH\n 123 Gross Strasse\nMuenchen, Deutschland",
                    FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12));
            shopInfo.setAlignment(Element.ALIGN_RIGHT);
            document.add(shopInfo);

            document.add(new Paragraph(" "));

            Paragraph header = new Paragraph("Invoice #" + order.getId(),
            FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18));
            header.setAlignment(Element.ALIGN_CENTER);
            document.add(header);

            document.add(new Paragraph(" "));

            Paragraph customerInfo = new Paragraph(
                    "Billed To:\n" + order.getUser().getEmail() + "\n",
                    FontFactory.getFont(FontFactory.HELVETICA, 12)
            );

            customerInfo.setSpacingAfter(10);
            document.add(customerInfo);

            PdfPTable table = new PdfPTable(3);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{4, 1, 2});

            table.addCell("Buch");
            table.addCell("Qty");
            table.addCell("Price");

            order.getItems().forEach(item -> {
                table.addCell(item.getBook().getTitle());
                table.addCell(String.valueOf(item.getQuantity()));
                table.addCell(item.getBook().getPrice() + "Euro");
            });

            document.add(table);


            Paragraph total = new Paragraph("Total: "  + order.getTotalAmount() + " Euro",
                    FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14));
            total.setAlignment(Element.ALIGN_RIGHT);
            total.setSpacingBefore(10);
            document.add(total);

            document.add(new Paragraph("\nDanke Sie fuer Einkaufen bei uns!"));

            document.close();

            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error", e);
        }
    }
}
