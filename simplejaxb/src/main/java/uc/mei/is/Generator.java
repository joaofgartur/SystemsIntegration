package uc.mei.is;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class Generator {
    private static final int NAMESIZE = 8, ADDRESSSIZE = 16, PHONESIZE = 8, IDSIZE = 4;

    public Generator() {};

    private String generateRandomAlphaString(int length) {
        String AlphaStr = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvxyz";

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

    public School generateInput(int numProfessors, int numStudents) {

        List<Professor> professors = new ArrayList<>();
        for(int i = 0; i < numProfessors; i++) {
            String id = generateRandomIntString(IDSIZE);
            String name = generateRandomAlphaString(NAMESIZE);
            String address = generateRandomAlphaString(ADDRESSSIZE);
            String phone = generateRandomIntString(PHONESIZE);
            String birth = generateRandomDate();
            professors.add(new Professor(id, name, birth, phone, address));
        }

        String[] genders = {"Male", "Female", "Other"};
        List<Student> students = new ArrayList<>();
        for(int j = 0; j < numStudents; j++) {
            int genderChoice = randBetween(0, 2);
            String gender = genders[genderChoice];
            String id = generateRandomIntString(IDSIZE);
            String name = generateRandomAlphaString(NAMESIZE);
            String address = generateRandomAlphaString(ADDRESSSIZE);
            String phone = generateRandomIntString(PHONESIZE);
            String birth = generateRandomDate();
            String registration = generateRandomDate();

            Student student = new Student(id, name, phone, gender, birth,
                    registration, address);

            int index = randBetween(-1, professors.size() - 1);
            if (index >= 0) {
                student.setProfessor(professors.get(index).getId());
                professors.get(index).getStudents().add(id);
            }

            students.add(student);
        }

        School school = new School();
        school.setProfessors(professors);
        school.setStudents(students);

        return school;
    }

}