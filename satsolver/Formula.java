package satsolver;

import java.util.*;
import java.util.stream.Collectors;

public class Formula {
    public Map<Integer, Clause> clauses;

    public static Map<Integer, Set<Integer>> variableMap;
    public int numClauses;

    // formula from list of clauses
    public Formula(Integer numClauses, Map<Integer, Clause> clauses, Map<Integer, Set<Integer>> variableMap) {
        this.numClauses = numClauses;
        this.clauses = clauses;
        Formula.variableMap = variableMap;
//        System.out.println("Clauses: " + clauses);
//        System.out.println(Formula.variableMap);
    }

    // formula from existing formula
    public Formula(Formula toCopy) {
        this.numClauses = toCopy.numClauses;
        this.clauses = toCopy.clauses.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
//        System.out.println(Formula.variableMap);
//        System.out.println("\nClauses: " + clauses);



//        this.variableMap = toCopy.variableMap.entrySet().stream()
//                .collect(Collectors.toMap(Map.Entry::getKey, e -> new HashSet(e.getValue())));

//        this.variableMap = toCopy.variableMap.entrySet().stream()
//                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public Formula addSingleLiteralClause(Integer literal) {
//        numClauses ++;
//        clauses.put(numClauses, new Clause(literal));
//        variableMap.get(literal).add(numClauses);
//        System.out.println("Removing all clauses containing " + literal);
        removeAllClausesWithLiteral(literal);
//        System.out.println("Clauses: " + clauses);

//        System.out.println("Removing" + -literal + " in all clauses");
        removeLiteralInAllClauses(-literal);
//        System.out.println("Clauses: " + clauses);


//        try {
//            variableMap.get(literal).add(numClauses);
//        } catch (NullPointerException e) {
//            variableMap.put(literal, new HashSet<>(numClauses));
//        }
        return this;
    }

    public List<Clause> getClauses() {
        return new ArrayList<>(clauses.values());
    }

    public Formula removeAllClausesWithLiteral(int literal) {
        try {
            variableMap.get(literal).forEach(clauseID -> clauses.remove(clauseID));
//            variableMap.remove(literal);
        } catch (NullPointerException e) {
            //variable already removed
        }
        return this;
    }

    public Formula removeLiteralInAllClauses(int literal) {
        // need to add all versions without this literal of clauses containing this to all variables concerned
        // in variable map
        try {
            Set<Integer> clausesContainingLiteral = new HashSet<>(variableMap.get(literal));
            for (Integer cID : clausesContainingLiteral) {
                try {
                    Clause d = new Clause(clauses.get(cID));
                    d.remove(literal);
//                    System.out.println("New clause: " + d);

//                    numClauses++;
                    clauses.put(cID, d);
//                    for (int i : d.values) {
//                        variableMap.get(i).add(numClauses);
//                    }
                } catch (NullPointerException e) {
                    // pass as the clause has been removed
                }
            }

            try {
//                variableMap.get(literal).forEach(clauseID -> clauses.remove(clauseID));
//                variableMap.remove(literal);
            } catch (NullPointerException e) {
                //variable already removed
            }
        } catch (NullPointerException e){
            // do nothing
        }
        return this;
    }

    public boolean isEmpty() {
        return clauses.values().isEmpty();
    }

    public boolean containsEmptyClause() {
        return clauses.values().stream()
                .anyMatch(clause -> clause.isEmpty);
    }

    public List<Integer> getUnitLiterals() {
        return clauses.values().stream()
                .filter(Clause::hasUnitSize)
                .map(Clause::getFirst)
                .collect(Collectors.toList());
    }

    public List<Integer> getPureLiterals() {
        Set<Integer> literalsList = clauses.values().stream()
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
                clauses.values().stream()
                        .map(String::valueOf)
                        .collect(Collectors.joining(" ")) +
                ']';
    }
}
