package uc.mei.is;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;

import java.io.FileOutputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;

public class App {
    private int numProfessors, numStudents;

    public App(int numProfessors, int numStudents) {
        this.numProfessors = numProfessors;
        this.numStudents = numStudents;
        myMain();
    }

    public static void main(String[] args ) {
        App myApp = new App(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
    }

    private void myMain() {
        Generator generator = new Generator();
        List<Professor> input = generator.generateInput(numProfessors, numStudents);
        ProfessorsContainer inputContainer = new ProfessorsContainer();
        inputContainer.setProfessors(input);

        SimpleDateFormat sdf1 = new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss");
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String output_file = sdf1.format(timestamp) + ".xml";

        try {
            JAXBContext contextObj = JAXBContext.newInstance(ProfessorsContainer.class, Professor.class, Student.class);

            Marshaller marshallerObj = contextObj.createMarshaller();
            marshallerObj.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            FileOutputStream xmlFile = new FileOutputStream(output_file);
            marshallerObj.marshal(inputContainer, xmlFile);
        } catch (Exception e) {
            System.out.println(e.toString());
        }

    }

}
