package ar.edu.undec.adapter.service.utils;


import ar.edu.undec.core.user.model.User;
import ar.edu.undec.core.utils.input.PDFGeneratorInput;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.util.List;

@Component

public class PDFBoxUsersGenerator implements PDFGeneratorInput{


    @Override
    public ByteArrayOutputStream generateUsersPDF(List<User> users) {

        try (PDDocument document = new PDDocument()) {

            PDPage page = new PDPage(PDRectangle.LETTER);
            document.addPage(page);

            try (PDPageContentStream content = new PDPageContentStream(document, page)) {

                float y = 750;

                content.beginText();
                content.setFont(PDType1Font.HELVETICA_BOLD, 16);
                content.newLineAtOffset(50, y);
                content.showText("Lista de Usuarios");
                content.endText();

                y -= 30;

                content.beginText();
                content.setFont(PDType1Font.HELVETICA, 11);
                content.newLineAtOffset(50, y);

                content.showText("id | email | status | createdAt");
                content.newLineAtOffset(0, -15);

                for (User u : users) {
                    String line = String.format(
                            "%s | %s | %s | %s",
                            u.getId(),
                            u.getEmail(),
                            u.getStatus(),
                            u.getCreatedAt()
                    );
                    content.showText(line.length() > 120 ? line.substring(0, 120) : line);
                    content.newLineAtOffset(0, -15);

                    y -= 15;
                    if (y < 80) break; // suficiente para práctico
                }

                content.endText();
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            document.save(out);
            return out;

        } catch (Exception e) {
            throw new RuntimeException("Error generating PDF: " + e.getMessage(), e);
        }
    }
}
