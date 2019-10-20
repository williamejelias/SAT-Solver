package satsolver;

import java.util.ArrayList;

public class SatSolver {

    public SatSolver(String fileName){
        Formula formula = new Formula();
        formula.readFile(fileName);
        System.out.println("Solving...");
        solve(formula);

    }

    public boolean dpSolver(Formula f) {
        // recursive backtracking search using Davis-Putnam Algorithm
        // empty formula or solved
        if (f.isFormulaEmpty()) return true;

        // empty clause can't be satisfied
        else if (f.hasEmptyClause()) return false;

        // perform unit propagation
        f.unitPropagation();

        // perform pure literal elimination
        ArrayList<Integer> pureVariables = f.pureLiteralElimination();
//        ArrayList<Integer> pureVariables = new ArrayList<>();

        if (f.isFormulaEmpty()) return true;

        // empty clause can't be satisfied
        else if (f.hasEmptyClause()) return false;

        // attempt to satisfy
        else {
            // get first available variable
            int varAvailable = f.firstAvailableVariable();

            // try to assign it true
            f.assign(varAvailable, 1);

            // check if satisfiable
            if (dpSolver(f)) return true;

            // recursively solve
            else{
                // unset the last assigned variables
                f.unsetVariable(varAvailable);
                for (int i : pureVariables)  f.unsetVariable(i);

                // try assigning it false
                f.assign(varAvailable, -1);

                // check if satisfiable
                if(dpSolver(f)) return true;

                // unset the variable
                else {
                    // if un setting a variable, need to re add all the clauses removed due to becoming pure-literals
                    f.unsetVariable(varAvailable);
                    for (int i : pureVariables) f.unsetVariable(i);
                }
                return false;
            }
        }
    }

    public void solve(Formula f) {
        if (dpSolver(f)) {System.out.println("The formula is satisfiable with the following variables:");
            f.printAssignment();
        }
        else {
            System.out.println("The formula is unsatisfiable!");
        }
    }
}
