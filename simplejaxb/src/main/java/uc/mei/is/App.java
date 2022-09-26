package uc.mei.is;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;

import java.io.*;
import java.sql.PseudoColumnUsage;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class App {
    private int numProfessors, numStudents;
    private static final SimpleDateFormat sdf1 = new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss");

    public App(int numProfessors, int numStudents) {
        this.numProfessors = numProfessors;
        this.numStudents = numStudents;
        myMain();
    }

    public static void main(String[] args ) {
        App myApp = new App(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
    }

    private String xmlTracking(ProfessorsContainer input, String xml_output_file) {
        try {
            JAXBContext contextObj = JAXBContext.newInstance(ProfessorsContainer.class, Professor.class, Student.class);

            Marshaller marshallerObj = contextObj.createMarshaller();
            marshallerObj.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            FileOutputStream xmlFile = new FileOutputStream(xml_output_file);

            //Measure serialization speed
            long start = System.currentTimeMillis();
            marshallerObj.marshal(input, xmlFile);
            long finish = System.currentTimeMillis();
            long serializationTime = finish - start;

            File file = new File(xml_output_file);
            long serializationSize = file.length();

            long serializationSpeed = serializationSize / serializationTime;

            //Measure deserialization speed
            Unmarshaller jaxbUnmarshaller = contextObj.createUnmarshaller();
            start = System.currentTimeMillis();
            ProfessorsContainer recoveredInput = (ProfessorsContainer) jaxbUnmarshaller.unmarshal(file);
            finish = System.currentTimeMillis();
            long deserializationTime = finish - start;
            long deserializationSpeed = serializationSize / deserializationTime;

            String results = "XML results\n"
                    + "---------------------------" + "\n"
                    + "Num of professors: " + numProfessors + "\n"
                    + "Num of students: " + (numProfessors * numStudents) + "\n"
                    + "File Size: " + serializationSize + " bytes\n"
                    + "Serialization Speed: " + serializationSpeed + " bytes/ms\n"
                    + "Deserialization Speed: " + deserializationSpeed + " bytes/ms\n"
                    + "---------------------------" + "\n";

            saveResultsToFile(xml_output_file, results);
            System.out.println(results);

            return xml_output_file;
        } catch (Exception e) {
            System.out.println(e.toString());

            return null;
        }
    }

    private void gzipTracking(ProfessorsContainer input,  String outputSaveToFile){
        String titleOutput = "\n------------- XML + GZIP -------------\n";
        System.out.println(titleOutput);
        saveResultsToFile(outputSaveToFile,titleOutput);

        String fileName = xmlTracking(input, outputSaveToFile);
        String gzipFileName = fileName + ".gz";
        String newXMLFileName = "gzip_" + fileName;

        long start = System.currentTimeMillis();
        compressGzipFile(fileName, gzipFileName);
        long finish = System.currentTimeMillis();

        long compressTime = finish - start;
        File gzipFile = new File(gzipFileName);
        long compressSpeed = compressTime == 0 ? gzipFile.length() : gzipFile.length() / compressTime;

        start = System.currentTimeMillis();
        decompressGzipFile(gzipFileName, newXMLFileName);
        finish = System.currentTimeMillis();

        long decompressTime = finish - start;
        File newXMLFile = new File(newXMLFileName);
        long decompressSpeed = newXMLFile.length() / decompressTime;

        String results = "\tGZIP results\n"
                + "---------------------------" + "\n"
                + "Compress Time: " + compressTime + " ms\n"
                + "File Size (GZIP): " + gzipFile.length() + " bytes\n"
                + "Compress Speed: " + compressSpeed + " bytes/ms\n"
                + "Decompress Time: " + decompressTime + " ms\n"
                + "File Size (XML): " + newXMLFile.length() + " bytes\n"
                + "Decompress Speed: " + decompressSpeed + " bytes/ms\n"
                + "---------------------------" + "\n";

        saveResultsToFile(outputSaveToFile,results);
        System.out.println(results);
    }

    private void compressGzipFile(String file, String gzipFile) {
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            FileOutputStream fileOutputStream = new FileOutputStream(gzipFile);
            GZIPOutputStream gzipOS = new GZIPOutputStream(fileOutputStream);

            byte[] buffer = new byte[1024];
            int len;

            while((len=fileInputStream.read(buffer)) != -1){
                gzipOS.write(buffer, 0, len);
            }

            gzipOS.close();
            fileOutputStream.close();
            fileInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void decompressGzipFile(String gzipFile, String newXMLFile) {
        try {
            FileInputStream fileInputStream = new FileInputStream(gzipFile);
            GZIPInputStream gzipInputStream = new GZIPInputStream(fileInputStream);
            FileOutputStream fos = new FileOutputStream(newXMLFile);

            byte[] buffer = new byte[1024];
            int len;

            while((len = gzipInputStream.read(buffer)) != -1){
                fos.write(buffer, 0, len);
            }

            fos.close();
            gzipInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    
    /* Auxiliary Method */
    private void saveResultsToFile(String fileName, String output){

        fileName = "run_" + fileName.substring(0,  fileName.lastIndexOf('.')) + ".txt";

        try {
            File file = new File(fileName);
            file.createNewFile(); // Create if not exists
            FileWriter myWriter = new FileWriter(fileName, true);
            myWriter.write(output);
            myWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
    
    
    private void myMain() {
        Generator generator = new Generator();
        List<Professor> input = generator.generateInput(numProfessors, numStudents);
        ProfessorsContainer inputContainer = new ProfessorsContainer();
        inputContainer.setProfessors(input);

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String xml_output_file = sdf1.format(timestamp) + ".xml";

        xmlTracking(inputContainer, xml_output_file);
        gzipTracking(inputContainer, xml_output_file);
    }

}
