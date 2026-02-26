package ar.edu.undec.core.utils.input;

import ar.edu.undec.core.user.model.User;

import java.io.ByteArrayOutputStream;
import java.util.List;

public interface PDFGeneratorInput {

    ByteArrayOutputStream generateUsersPDF(List<User> users);
}
