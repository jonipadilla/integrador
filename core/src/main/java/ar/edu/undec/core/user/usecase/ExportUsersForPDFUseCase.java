package ar.edu.undec.core.user.usecase;


import ar.edu.undec.core.user.input.ExportUsersForPDFInput;
import ar.edu.undec.core.user.repository.FindUserRepository;
import ar.edu.undec.core.utils.input.PDFGeneratorInput;

import java.io.ByteArrayOutputStream;

public class ExportUsersForPDFUseCase implements ExportUsersForPDFInput{

    private final FindUserRepository repository;
    private final PDFGeneratorInput pdfGeneratorInput;

    public ExportUsersForPDFUseCase(FindUserRepository repository,
                                    PDFGeneratorInput pdfGeneratorInput) {
        this.repository = repository;
        this.pdfGeneratorInput = pdfGeneratorInput;
    }

    @Override
    public ByteArrayOutputStream generateUsersPDF() {
        try {
            return pdfGeneratorInput.generateUsersPDF(repository.getAllSavedUsers());
        } catch (Exception e) {
            throw new RuntimeException("Error generating PDF: " + e.getMessage(), e);
        }
    }
}
