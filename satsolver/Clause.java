package satsolver;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Clause {
    public List<Integer> values;
    public boolean isEmpty = false;

    public Clause(Clause other) {
        this.values = new ArrayList<>(other.values);
        this.isEmpty = other.isEmpty;
    }

    public Clause(boolean isEmpty){
        this.values = new ArrayList<>();
        this.isEmpty = isEmpty;
    }

    public Clause(List<Integer> values){
        this.values = values;
    }

    public Clause() {
        values = new ArrayList<>();
    }

    public boolean hasUnitSize() {
        return values.size() == 1;
    }

    public Integer getFirst() {
        return values.get(0);
    }

    public boolean contains(int literal) {
        return values.contains(literal);
    }

    public void addLiteral(Integer literal) {
        values.add(literal);
    }

    public void remove(Integer literal) {
        values.remove(literal);
        if (values.size() == 0) {
            isEmpty = true;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Clause other = (Clause) o;
        return isEmpty == other.isEmpty && Objects.equals(values, other.values);
    }

    @Override
    public int hashCode() {
        return Objects.hash(values, isEmpty);
    }

    @Override
    public String toString() {
        return "[" +
                values.stream().map(String::valueOf).collect(Collectors.joining(", ")) +
                ']';
    }
}
