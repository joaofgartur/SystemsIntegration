package uc.mei.is;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import uc.mei.is.ProtocolBufferClasses.ProtoSchool;

import java.io.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class App {
    private static final SimpleDateFormat sdf1 = new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss");

    public App() {
        myMain();
    }

    public static void main(String[] args ) {
        App myApp = new App();
    }

    private String xmlTracking(School input, String xml_output_file, int numProfessors, int numStudents) {
        try {

            long serializationTime = createXMLFile(input, xml_output_file);

            File file = new File(xml_output_file);
            long fileSize = file.length();

            double serializationSpeed = ((double) fileSize) / serializationTime;

            //Measure deserialization speed
            JAXBContext contextObj = JAXBContext.newInstance(School.class, Professor.class, Student.class);
            Unmarshaller jaxbUnmarshaller = contextObj.createUnmarshaller();

            long start = System.currentTimeMillis();
            School recoveredInput = (School) jaxbUnmarshaller.unmarshal(file);
            long finish = System.currentTimeMillis();

            long deserializationTime = finish - start;
            double deserializationSpeed = ((double) fileSize) / deserializationTime;

            /*String results = "\tXML results\n"
                    + "---------------------------" + "\n"
                    + "Num of professors: " + numProfessors + "\n"
                    + "Num of students: " + numStudents + "\n"
                    + "File Size: " + fileSize + " bytes\n"
                    + "Serialization Time: " + serializationTime + " ms\n"
                    + "Serialization Speed: " + serializationSpeed + " bytes/ms\n"
                    + "Deserialization Time: " + deserializationTime + " ms\n"
                    + "Deserialization Speed: " + deserializationSpeed + " bytes/ms\n"
                    + "---------------------------" + "\n";*/

            String results = numProfessors+";"+numStudents+";"+fileSize+";"+serializationTime+";"
                + serializationSpeed+";"+deserializationTime+";"+deserializationSpeed+"\n";

            return results;
        } catch (Exception e) {
            System.out.println(e.toString());
            return "XML error!";
        }


    }

    private String gzipTracking(School input, String outputSaveToFile, int numProfessors, int numStudents){

        long serializationTime = createXMLFile(input, outputSaveToFile);
        String gzipFileName = outputSaveToFile + ".gz";
        String newXMLFileName = "gzip_" + outputSaveToFile;

        long start = System.currentTimeMillis();
        compressGzipFile(outputSaveToFile, gzipFileName);
        long finish = System.currentTimeMillis();

        long compressTime = finish - start;
        File gzipFile = new File(gzipFileName);
        double compressSpeed = compressTime == 0 ? gzipFile.length() : ((double) gzipFile.length()) / compressTime;

        start = System.currentTimeMillis();
        decompressGzipFile(gzipFileName, newXMLFileName);
        finish = System.currentTimeMillis();

        long decompressTime = finish - start;
        File newXMLFile = new File(newXMLFileName);
        double decompressSpeed = ((double) newXMLFile.length()) / decompressTime;

        /*String results = "\tGZIP results\n"
                + "---------------------------" + "\n"
                + "Serialization Time: " + serializationTime + " ms\n"
                + "File Size (GZIP): " + gzipFile.length() + " bytes\n"
                + "Compress Time: " + compressTime + " ms\n"
                + "Compress Speed: " + compressSpeed + " bytes/ms\n"
                + "File Size (XML): " + newXMLFile.length() + " bytes\n"
                + "Decompress Time: " + decompressTime + " ms\n"
                + "Decompress Speed: " + decompressSpeed + " bytes/ms\n"
                + "---------------------------" + "\n";*/

        String results = numProfessors+";"+numStudents+";"+serializationTime+";"+compressTime+";"+compressSpeed+";"+(serializationTime+compressTime)
                +";"+gzipFile.length()+";"+decompressTime+";"+decompressSpeed+";"+newXMLFile.length()+"\n";

        return results;
    }

    private long createXMLFile(School input, String xml_output_file){
        try{
            JAXBContext contextObj = JAXBContext.newInstance(School.class, Professor.class, Student.class);
            FileOutputStream xmlFile = new FileOutputStream(xml_output_file);

            Marshaller marshallerObj = contextObj.createMarshaller();
            marshallerObj.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            long start = System.currentTimeMillis();
            marshallerObj.marshal(input, xmlFile);
            long finish = System.currentTimeMillis();

            return finish - start;
        } catch (Exception e) {
            System.out.println(e.toString());
            return -1;
        }
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

    private String protocolTracking(School school, String fileName, int numProfessors, int numStudents) {
        ProtoSchool protoSchool = school.convertToProto();

        try {
            FileOutputStream outputFile = new FileOutputStream(fileName);
            long start = System.currentTimeMillis();
            protoSchool.writeTo(outputFile);
            long finish = System.currentTimeMillis();
            outputFile.close();
            long serializationTime = finish - start;

            File file = new File(fileName);
            long fileSize = file.length();
            double serializationSpeed = ((double) fileSize) / serializationTime;

            start = System.currentTimeMillis();
            protoSchool = ProtoSchool.parseFrom(new FileInputStream(fileName));
            finish = System.currentTimeMillis();
            long deserializationTime = finish - start;
            double deserializationSpeed = ((double) (fileSize) / deserializationTime);

            /*String results = "\tProtocol Buffer results\n"
                    + "---------------------------" + "\n"
                    + "Num of professors: " + numProfessors + "\n"
                    + "Num of students: " + numStudents + "\n"
                    + "File Size: " + fileSize + " bytes\n"
                    + "Serialization Time: " + serializationTime + " ms\n"
                    + "Serialization Speed: " + serializationSpeed + " bytes/ms\n"
                    + "Deserialization Time: " + deserializationTime + " ms\n"
                    + "Deserialization Speed: " + deserializationSpeed + " bytes/ms\n"
                    + "---------------------------" + "\n";*/

            String results = numProfessors+";"+numStudents+";"+fileSize+";"+serializationTime+";"+serializationSpeed+";"
                    +deserializationTime+";"+deserializationSpeed+"\n";

            return results;
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return "Protocol buffer error!";
        }
    }

    /* Auxiliary Method */
    private void saveResultsToFile(String content,String resultsFile){
        try {
            String fullpath = "outputs/" + resultsFile;
            File file = new File(fullpath);
            file.getParentFile().mkdirs();
            file.createNewFile(); // Create if not exists

            FileWriter myWriter = new FileWriter(fullpath, true);
            myWriter.write(content);
            myWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    private void myMain() {
        Generator generator = new Generator();

        // Create Files
        saveResultsToFile("numProfessors;numStudents;fileSize;serializationTime;serializationSpeed;deserializationTime;deserializationSpeed\n", "xml.csv");
        saveResultsToFile("numProfessors;numStudents;serializationTime;compressTime;compressSpeed;totalTime;gzipSize;decompressTime;decompressSpeed;xmlSize\n", "gzip.csv");
        saveResultsToFile("numProfessors;numStudents;fileSize;serializationTime;serializationSpeed;deserializationTime;deserializationSpeed\n", "protoBuff.csv");

        int[] professorTests = {10, 100, 1000, 10000};
        int[] studentTests = {10, 100, 1000, 10000};
        for (int numProfessors: professorTests) {
            for(int numStudents: studentTests) {
                System.out.println("Prof: "+numProfessors+" Students: "+numStudents);
                School input = generator.generateInput(numProfessors, numStudents);

                Timestamp timestamp = new Timestamp(System.currentTimeMillis());

                String outputFile = sdf1.format(timestamp);
                String xmlOutputFile = outputFile + ".xml";
                String protocolOutputFile = outputFile + ".bin";
                //String resultsFile = outputFile + ".txt";

                String xmlResults = xmlTracking(input, xmlOutputFile, numProfessors, numStudents);
                String gzipResults = gzipTracking(input, xmlOutputFile, numProfessors, numStudents);
                String protocolResults = protocolTracking(input, protocolOutputFile, numProfessors, numStudents);

                saveResultsToFile(xmlResults, "xml.csv");
                saveResultsToFile(gzipResults, "gzip.csv");
                saveResultsToFile(protocolResults, "protoBuff.csv");


            }
        }
    }
}
