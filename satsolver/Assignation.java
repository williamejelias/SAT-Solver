package satsolver;

import java.util.*;

public class Assignation {
    private Set<Integer> assignation;

    public Assignation() {
        assignation = new TreeSet<>();
    }

    public Assignation(Set<Integer> assignation) {
        this.assignation = new TreeSet<>(assignation);
    }

    public Assignation addLiteralValue(int literal) {
        Assignation newAssignation = new Assignation(assignation);
        newAssignation.assignation.add(literal);
        return newAssignation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Assignation other = (Assignation) o;
        return Objects.equals(assignation, other.assignation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(assignation);
    }

    @Override
    public String toString() {
        // order the result
        ArrayList<Integer> values = new ArrayList<>(assignation);
        Collections.sort(values, Comparator.comparingInt(Math::abs));
        return values.toString();
    }
}
