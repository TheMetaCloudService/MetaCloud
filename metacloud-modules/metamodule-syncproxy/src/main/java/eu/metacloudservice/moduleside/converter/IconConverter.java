package eu.metacloudservice.moduleside.converter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;

public class IconConverter {

    // Converts PNG image file to base64 string
    public String convertToBase64(String filePath) throws IOException {
        FileInputStream fis = new FileInputStream(filePath);
        byte[] imageData = new byte[(int) new File(filePath).length()];
        fis.read(imageData);
        fis.close();

        // Byte-Array als Base64 kodieren
        return Base64.getEncoder().encodeToString(imageData);
    }

    // Converts base64 encoded string to byte array
    public byte[] convertToByte(String base64EncodedImage) throws IOException {
        return Base64.getDecoder().decode(base64EncodedImage);
    }

}
