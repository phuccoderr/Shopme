package com.shopme.admin.order;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.shopme.admin.export.AbstractExporter;
import com.shopme.common.entity.User;
import com.shopme.common.entity.order.Order;
import jakarta.servlet.http.HttpServletResponse;

import java.awt.*;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;

public class OrderPdfExporter extends AbstractExporter {
    public void export(List<Order> listOrders, HttpServletResponse response) throws IOException {
        super.setResponseHeader(response,"application/pdf",".pdf","orders_");

        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document,response.getOutputStream());

        document.open();

        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        font.setSize(18);
        font.setColor(Color.cyan);

        Paragraph paragraph = new Paragraph("Danh sách các Orders",font );
        paragraph.setAlignment(Element.ALIGN_CENTER);

        document.add(paragraph);

        PdfPTable table = new PdfPTable(7);
        table.setWidthPercentage(100f);
        table.setSpacingBefore(10);
        table.setWidths(new float[] {1.5f, 2.0f, 3.5f, 3.0f, 3.0f, 1.5f,1.5f});

        writeTableHeader(table);
        writeTableData(table,listOrders);
        document.add(table);


        document.close();

    }

    private void writeTableHeader(PdfPTable table) {
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(Color.CYAN);
        cell.setPadding(5);

        Font font = FontFactory.getFont(FontFactory.HELVETICA);
        font.setColor(Color.BLACK);

        cell.setPhrase(new Phrase("Order ID",font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Customer",font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Address",font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Phone Number",font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Payment Method",font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Status",font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Total",font));
        table.addCell(cell);
    }
    private void writeTableData(PdfPTable table, List<Order> listUsers) {
        for (Order order : listUsers) {
            table.addCell(String.valueOf(order.getId()));
            table.addCell(order.getCustomer().getFullName());
            table.addCell(order.getDestination());
            table.addCell(order.getPhoneNumber());
            table.addCell(String.valueOf(order.getPaymentMethod()));
            table.addCell(String.valueOf(order.getStatus()));
            DecimalFormat decimalFormat = new DecimalFormat("#,###");
            table.addCell(String.valueOf(decimalFormat.format(order.getTotal())));
        }

    }


}
