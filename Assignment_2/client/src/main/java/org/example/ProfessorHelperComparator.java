package org.example;

import entity.ProfessorHelper;

import java.util.Comparator;

public class ProfessorHelperComparator implements Comparator<ProfessorHelper> {

    @Override
    public int compare(ProfessorHelper o1, ProfessorHelper o2) {
        return Integer.compare(o1.getProfessorStudents().size(), o2.getProfessorStudents().size());
    }
}
