package uc.mei.is;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;

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

    private void xmlTracking(ProfessorsContainer input) {
        try {
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            String xml_output_file = sdf1.format(timestamp) + ".xml";
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

            System.out.println(results);

        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    private void myMain() {
        Generator generator = new Generator();
        List<Professor> input = generator.generateInput(numProfessors, numStudents);
        ProfessorsContainer inputContainer = new ProfessorsContainer();
        inputContainer.setProfessors(input);

        xmlTracking(inputContainer);
    }

}
