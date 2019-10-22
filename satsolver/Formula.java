package satsolver;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class Formula {
    private List<Clause> clauses;

    // formula from list of clauses
    public Formula(List<Clause> clauses) {
        this.clauses = clauses;
    }

    // formula from existing formula
    public Formula(Formula toCopy) {
        this.clauses = toCopy.clauses.stream()
                .map(Clause::new)
                .collect(Collectors.toList());
    }

    public Formula addSingleLiteralClause(Integer literal) {
        clauses.add(new Clause(literal));
        return this;
    }

    public List<Clause> getClauses() {
        return clauses;
    }

    public Formula removeAllClausesWithLiteral(int literal) {
        clauses.removeIf(clause -> clause.contains(literal));
        return this;
    }

    public Formula removeLiteralInAllClauses(int literal) {
        clauses.forEach(clause -> clause.remove(literal));
        return this;
    }

    public boolean isEmpty() {
        return clauses.isEmpty();
    }

    public boolean containsEmptyClause() {
        return clauses.stream().anyMatch(clause -> clause.isEmpty);
    }

    public List<Integer> getUnitLiterals() {
        return clauses.stream()
                .filter(Clause::hasUnitSize)
                .map(Clause::getFirst)
                .collect(Collectors.toList());
    }

    public List<Integer> getPureLiterals() {
        Set<Integer> literalsList = clauses.stream()
                .flatMap(clause -> clause.values.stream())
                .collect(Collectors.toSet());

        List<Integer> result = new ArrayList<>();
        for (Integer literal : literalsList) {
            if (!literalsList.contains(-literal)) {
                result.add(literal);
            }
        }
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Formula f = (Formula) o;
        return Objects.equals(clauses, f.clauses);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clauses);
    }

    @Override
    public String toString() {
        return "[" +
                clauses.stream().map(String::valueOf).collect(Collectors.joining(" ")) +
                ']';
    }
}
