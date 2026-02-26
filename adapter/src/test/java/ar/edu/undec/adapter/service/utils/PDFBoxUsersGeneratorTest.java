package ar.edu.undec.adapter.service.utils;

import ar.edu.undec.core.user.model.User;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PDFBoxUsersGeneratorTest {


    @Test
    void generateUsersPDF_shouldGenerateValidPdfWithUserData() throws Exception {

        LocalDateTime now = LocalDateTime.of(2026, 2, 25, 10, 0);

        User u1 = User.createNew("a@test.com", "pass", "code", now.plusDays(1), now);
        u1.assignId(1L);

        User u2 = User.createNew("b@test.com", "pass", "code2", now.plusDays(1), now);
        u2.assignId(2L);

        PDFBoxUsersGenerator generator = new PDFBoxUsersGenerator();

        ByteArrayOutputStream out = generator.generateUsersPDF(List.of(u1, u2));
        byte[] bytes = out.toByteArray();

        assertNotNull(bytes);
        assertTrue(bytes.length > 100);

        String header = new String(bytes, 0, Math.min(bytes.length, 4));
        assertEquals("%PDF", header);

        try (PDDocument doc = PDDocument.load(new ByteArrayInputStream(bytes))) {
            String text = new PDFTextStripper().getText(doc);

            assertTrue(text.contains("Lista de Usuarios"));
            assertTrue(text.contains("a@test.com"));
            assertTrue(text.contains("b@test.com"));
        }
    }
}
