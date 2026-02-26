package ar.edu.undec.core.usecasetest;

import ar.edu.undec.core.user.input.ExportUsersForPDFInput;
import ar.edu.undec.core.user.model.User;
import ar.edu.undec.core.user.repository.FindUserRepository;
import ar.edu.undec.core.user.usecase.ExportUsersForPDFUseCase;
import ar.edu.undec.core.utils.input.PDFGeneratorInput;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)

public class ExportUsersForPDFUseCaseTest {


    @Mock
    private FindUserRepository repository;

    @Mock
    private PDFGeneratorInput pdfGeneratorInput;

    private ExportUsersForPDFInput input;

    @BeforeEach
    void setup() {
        input = new ExportUsersForPDFUseCase(repository, pdfGeneratorInput);
    }

    @Test
    void generateUsersPDF_FileGenerated_Successful() {

        LocalDateTime now = LocalDateTime.of(2026, 2, 26, 10, 0);

        User u1 = User.createNew("a@test.com", "pass", "code", now.plusDays(1), now);
        u1.assignId(1L);

        List<User> users = List.of(u1);

        ByteArrayOutputStream fakePdf = new ByteArrayOutputStream();
        fakePdf.write(1); // contenido mínimo

        when(repository.getAllSavedUsers()).thenReturn(users);
        when(pdfGeneratorInput.generateUsersPDF(users)).thenReturn(fakePdf);

        ByteArrayOutputStream reportPDF = input.generateUsersPDF();

        assertNotNull(reportPDF);
        assertEquals(fakePdf, reportPDF);

        verify(repository, times(1)).getAllSavedUsers();
        verify(pdfGeneratorInput, times(1)).generateUsersPDF(users);
    }

    @Test
    void generateUsersPDF_WhenGeneratorFails_ShouldThrowRuntimeException() {

        when(repository.getAllSavedUsers()).thenReturn(List.of());
        when(pdfGeneratorInput.generateUsersPDF(anyList()))
                .thenThrow(new RuntimeException("PDF error"));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> input.generateUsersPDF());

        assertTrue(ex.getMessage().contains("Error generating PDF"));

        verify(repository, times(1)).getAllSavedUsers();
        verify(pdfGeneratorInput, times(1)).generateUsersPDF(anyList());
    }
}
