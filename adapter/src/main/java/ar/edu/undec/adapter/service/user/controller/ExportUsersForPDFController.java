package ar.edu.undec.adapter.service.user.controller;


import ar.edu.undec.core.user.input.ExportUsersForPDFInput;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;

@RestController
@RequestMapping("users/export/pdf")


public class ExportUsersForPDFController {


    private final ExportUsersForPDFInput input;

    public ExportUsersForPDFController(ExportUsersForPDFInput input) {
        this.input = input;
    }

    @GetMapping
    public ResponseEntity<byte[]> getUsersPDF() {

        ByteArrayOutputStream pdf = input.generateUsersPDF();
        byte[] bytes = pdf.toByteArray();

        if (bytes == null || bytes.length == 0) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"Lista_de_Usuarios.pdf\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(bytes);
    }
}
