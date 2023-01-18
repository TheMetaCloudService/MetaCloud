package eu.themetacloudservice.networking.fileformar;

import lombok.SneakyThrows;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.unzip.Unzip;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Base64;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class FileCoder {


    public FileCoder() {
    }

    @SneakyThrows
    public void zip(String file, String zipFile){
        ZipFile zip = new ZipFile(zipFile);
        ZipParameters zipParameters = new ZipParameters();
        zip.addFolder(new File(file), zipParameters);
    }

    public void unZip(String zipFile){
        try {
            FileInputStream fis = new FileInputStream(zipFile);
            ZipInputStream zis = new ZipInputStream(fis);
            ZipEntry ze = zis.getNextEntry();
            byte[] buffer = new byte[1024];
            while (ze != null) {
                FileOutputStream fos = new FileOutputStream(ze.getName());
                int len;
                while ((len = zis.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }
                fos.close();
                ze = zis.getNextEntry();
            }
            zis.closeEntry();
            zis.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public byte[] zipToString(String zip){
        try {
            File file = new File(zip);
            byte[] bytes = Files.readAllBytes(file.toPath());
            return bytes;
        } catch (IOException ignored) {}

        return null;
    }

    public void stringToZip(byte[]  content, String outputFile){
        try {
            File file = new File(outputFile);
            Files.write(file.toPath(), content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
