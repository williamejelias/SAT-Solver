package satsolver;

import java.io.IOException;
import java.util.Comparator;

public class SatSolver {

    public SatSolver(String fileName) throws IOException {
        FormulaReader formulaReader = new FormulaReader();
        System.out.println("Solving...");
        Formula f = FormulaReader.readFromFile(fileName);
        solver(f);
    }

    public static Assignation solve(Formula f, Assignation assignation) {
        // recursive backtracking search using DPLL Algorithm
        // empty formula or solved
        if (f.isEmpty()) return assignation;

        // empty clause can't be satisfied
        if (f.containsEmptyClause()) return null;

        // perform unit propagation
        for (Integer unitLiteral : f.getUnitLiterals()) {
            f = unitPropagate(f, unitLiteral);
            assignation = assignation.addLiteralValue(unitLiteral);
        }

        // perform pure literal elimination
        for (Integer pureLiteral : f.getPureLiterals()) {
            f = eliminatePureLiteral(f, pureLiteral);
            assignation = assignation.addLiteralValue(pureLiteral);
        }

        // empty formula or solved
        if (f.isEmpty()) return assignation;

        // empty clause can't be satisfied
        if (f.containsEmptyClause()) return null;

        // pick an unchosen variable to branch on
        int literal = chooseLiteral(f);

        // attempt to satisfy by branching positive
        Formula cnfWithLiteralTrue = new Formula(f).addSingleLiteralClause(literal);
        Assignation result = solve(cnfWithLiteralTrue, assignation.addLiteralValue(literal));
        if (result != null) {
            return result;
        }

        // attempt to satisfy by branching negative
        Formula cnfWithLiteralFalse = new Formula(f).addSingleLiteralClause(-literal);
        return solve(cnfWithLiteralFalse, assignation.addLiteralValue(-literal));
    }

    // eliminate
    private static Formula unitPropagate(Formula f, int literal) {
        return eliminatePureLiteral(new Formula(f).removeLiteralInAllClauses(-literal), literal);
    }

    private static Formula eliminatePureLiteral(Formula f, int literal) {
        return new Formula(f).removeAllClausesWithLiteral(literal);
    }

    private static Integer chooseLiteral(Formula f) {
        return f.getClauses().stream()
                .flatMap(disjunction -> disjunction.values.stream())
                .map(Math::abs)
                .min(Comparator.naturalOrder())
                .get();
    }

    public void solver(Formula f) {
        Assignation result = solve(f, new Assignation());
        if (result != null) {System.out.println("The formula is satisfiable with the following variables:");
            System.out.println(result);
        }
        else {
            System.out.println("The formula is unsatisfiable!");
        }
    }
}
