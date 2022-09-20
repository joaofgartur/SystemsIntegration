package uc.mei.is;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class App {
    private int numProfessors, maxStudents, totalStudents;

    public App() {
        myMain();
    }

    public static void main( String[] args ) {
        App myApp = new App();
    }

    private void myMain() {
        numProfessors = 3;
        maxStudents = 10;
        totalStudents = 0;

        List<Professor> input = generateInput(numProfessors, maxStudents);


        for(Professor p: input) {
            System.out.println(p.toString());
            for(Student s: p.studentList) {
                System.out.println("\t" + s.toString());
            }
        }
        System.out.println("Total professors: " + numProfessors + "\n"
                + "Total students: " + totalStudents);
    }

    private String generateRandomAlphaString(int length) {
        String AlphaStr = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvxyz";

        // creating a StringBuffer size of AlphaNumericStr

        StringBuilder s = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int ch = (int)(AlphaStr.length() * Math.random());
            s.append(AlphaStr.charAt(ch));
        }

        return s.toString();
    }

    private String generateRandomIntString(int length) {
        String Digits = "0123456789";

        StringBuilder s = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int ch = (int)(Digits.length() * Math.random());
            s.append(Digits.charAt(ch));
        }

        return s.toString();

    }

    public static int randBetween(int start, int end) {
        return start + (int)Math.round(Math.random() * (end - start));
    }

    private String generateRandomDate() {
        GregorianCalendar gc = new GregorianCalendar();

        int year = randBetween(1970, 2022);
        gc.set(Calendar.YEAR, year);
        int dayOfYear = randBetween(1, gc.getActualMaximum(Calendar.DAY_OF_YEAR));
        gc.set(Calendar.DAY_OF_YEAR, dayOfYear);

        return  gc.get(Calendar.DAY_OF_MONTH) + "/" + (gc.get(Calendar.MONTH) + 1) + "/" + gc.get(Calendar.YEAR);
    }

    private List<Professor> generateInput(int numProfessors, int maxStudents) {
        List<Professor> professors = new ArrayList<>();
        int nameSize = 10, addressSize = 30, phoneSize = 9, idSize = 4;

        for(int i = 0; i < numProfessors; i++) {

            String profId = generateRandomIntString(idSize);
            String profName = generateRandomAlphaString(nameSize);
            String profAddress = generateRandomAlphaString(addressSize);
            String profPhone = generateRandomIntString(phoneSize);
            String profBirthDate = generateRandomDate();
            Professor p = new Professor(profId, profName, profBirthDate, profPhone, profAddress);

            int numStudents = randBetween(1, maxStudents);
            totalStudents += numStudents;
            String genders[] = {"Male", "Female", "Other"};
            for(int j = 0; j < numStudents; j++) {

                int genderChoice = randBetween(0, 2);
                String studentGender = genders[genderChoice];
                String studentId = generateRandomIntString(idSize);
                String studentName = generateRandomAlphaString(nameSize);
                String studentAddress = generateRandomAlphaString(addressSize);
                String studentPhone = generateRandomIntString(phoneSize);
                String studentBirthDate = generateRandomDate();
                String studentRegistrationDate = generateRandomDate();

                Student s = new Student(studentId, studentName, studentPhone, studentGender, studentBirthDate,
                        studentRegistrationDate, studentAddress);
                p.studentList.add(s);
                s.setProfessor(p);

            }

            professors.add(p);
        }

        return professors;
    }

}
