package uc.mei.is.examples;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class GZIPExample {

    public static void main(String[] args) {
        String file = "26_09_2022_17_34_26.xml";
        String gzipFile = "26_09_2022_17_34_26.xml.gz";
        String newFile = "gzip_26_09_2022_17_34_26.xml";

        compressGzipFile(file, gzipFile);

        decompressGzipFile(gzipFile, newFile);
    }

    private static void decompressGzipFile(String gzipFile, String newFile) {
        try {
            FileInputStream fileInputStream = new FileInputStream(gzipFile);
            GZIPInputStream gzipInputStream = new GZIPInputStream(fileInputStream);
            FileOutputStream fos = new FileOutputStream(newFile);
            byte[] buffer = new byte[1024];
            int len;
            while((len = gzipInputStream.read(buffer)) != -1){
                fos.write(buffer, 0, len);
            }
            //close resources
            fos.close();
            gzipInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void compressGzipFile(String file, String gzipFile) {
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            FileOutputStream fileOutputStream = new FileOutputStream(gzipFile);
            GZIPOutputStream gzipOS = new GZIPOutputStream(fileOutputStream);

            byte[] buffer = new byte[1024];
            int len;
            while((len=fileInputStream.read(buffer)) != -1){
                gzipOS.write(buffer, 0, len);
            }
            //close resources
            gzipOS.close();
            fileOutputStream.close();
            fileInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}