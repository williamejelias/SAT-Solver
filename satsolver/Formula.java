package satsolver;

import java.util.*;

public class Formula {

    private int numVars, numClauses;
    private int[][] formulaArray;
    private int[] assignedValues;
    private LinkedList<Integer> formula = new LinkedList<>();

    public void readFile(String filename) {
        FormulaReader reader = new FormulaReader();
        reader.read(filename);
        numVars = reader.getNumVars();
        numClauses = reader.getNumClauses();
        formulaArray = reader.getFormula();
        assignedValues = new int[numVars];
        for (int i = 0; i < numClauses; i++) {
            formula.add(i);
        }

        // delimiter
        formula.add(-1);
    }

    public boolean isFormulaEmpty() {
        return formula.getFirst() == -1;
    }

    public boolean isClauseEmpty(int clause) {
        for (int i : formulaArray[clause]) {
            if (assignedValues[Math.abs(i)-1] == 0) return false;
        }
        return true;
    }

    public boolean hasEmptyClause() {
        // for each clause in the formula
        for (int i : formula) {
            // end of unsatisfied clauses
            if (i == -1) break;
            if (isClauseEmpty(i)) return true;
        }
        return false;
    }

    public int firstAvailableVariable() {
        // return the first index of variable that hasn't yet been assigned
        for (int i = 0; i < numVars; i ++) {
            if (assignedValues[i] == 0) return i;
        }
        // all variables assigned
        return -1;
    }

    // returns unsatisfied clauses based on the -1 delimiter in formula
    public LinkedList<Integer> separateClauses() {
        LinkedList<Integer> returnList = new LinkedList<>();
        for (int i : formula) {
            // end of unsatisfied clauses
            if (i == -1) break;

            // unsatisfied clause so add to return
            returnList.add(i);
        }
        return returnList;
    }

    public void assign(int var, int value) {
//        System.out.println(var + " " +  value);
        // set the new value
        assignedValues[var] = value;

        // get which clauses are satisfied and unsatisfied after new assignment
        LinkedList<Integer> satisfiedList = new LinkedList<>();
        LinkedList<Integer> unsatisfiedList = new LinkedList<>();
        separateClauses().forEach((i) -> {
            if (isClauseSatisfied(i)) satisfiedList.add(i);
            else unsatisfiedList.add(i);
        });

        // construct the new formula with remaining clauses
        // unsatisfied clauses come first, then a -1 delimiter, then all the clauses that are currently satisfied
        ListIterator<Integer> newFormula;
        if (separateClauses().size() + 1 > formula.size()) newFormula = formula.listIterator(separateClauses().size());

        // may be 1 element longer due to extra delimiter
        else newFormula = formula.listIterator(separateClauses().size() + 1);

        // create the new formula of unsatisfied, -1, satisfied, -1
        if (satisfiedList.size() > 0) satisfiedList.add(-1);
        unsatisfiedList.add(-1);
        unsatisfiedList.addAll(satisfiedList);

        // reconstruct the formula
        while (newFormula.hasNext()) unsatisfiedList.add(newFormula.next());
        formula = unsatisfiedList;
    }

    public boolean isClauseSatisfied(int clause) {
        for (int i : formulaArray[clause]) {
            // clause is a negation
            if (i < 0) {
                // if the variable is false in the negated clause return true
                if (assignedValues[Math.abs(i)-1] == -1) return true;

            // clause is positive and variable is true in it
            } else if (assignedValues[i-1] == 1) return true;
        }
        return false;
    }

    public void unsetVariable(int var) {
        // because with each assignment, satisfied and unsatisfied clauses are split by a -1, can simply remove
        // the first -1, and the formula is then unsplit on the clauses that were satisfied/ unsatisfied by the
        // assignation of the most recent variable
        assignedValues[var] = 0;
        int index = 0;
        for (int i : formula) {
            if (i == -1) {
                formula.remove(index);
                break;
            }
            index ++;
        }
    }

    public void unitPropagation() {
        // remove any clauses which contain any 1 variable on its own, and set that variable
        for (int i : formula) {
            // read up to satisfied clauses or end of formula
            if (i == -1) {
                break;
            }

            if (formulaArray[i].length == 1) {
                // clause is a unit clause
                int unit = formulaArray[i][0];

                // assign this variable its value
                int unitVar = Math.abs(unit);
                int unitVarIndex = unitVar - 1;
                if (Math.abs(unit) == unit) {
                    // assign positive
                    assign(unitVarIndex, 1);
                } else {
                    // assign negative
                    assign(unitVarIndex, -1);
                }
            }
        }
    }

    public ArrayList<Integer> pureLiteralElimination() {
        // maintain a list of all the variables that were set during the setting of pure literals
        ArrayList<Integer> pureLiteralVariables = new ArrayList<>();

        // remove those clauses if some clauses of F contain x but no clauses contain -x
        Map<Integer, Integer> pureLiteralMap = new HashMap<>();

        for (int clause : formula) {
            // read up to satisfied clauses or end of formula
            if (clause == -1) {
                break;
            }

            // track each seen variable in the clause
            for (int variable : formulaArray[clause]) {
                int absVar = Math.abs(variable);
                int varVal = Math.abs(variable) == variable ? 1 : -1;

                // if not seen, add to map with its value
                if (!pureLiteralMap.containsKey(absVar))  pureLiteralMap.put(absVar, varVal);
                // if seen, check values match
                else if (pureLiteralMap.get(absVar) != varVal) {
                        // values dont match so set key value to 0
                        pureLiteralMap.remove(absVar);
                        pureLiteralMap.put(absVar, 0);
                }
            }
        }

        // all dictionary pairs in the hash map with non-zero values are pure literals
        for (Map.Entry<Integer, Integer> entry : pureLiteralMap.entrySet()) {
            Integer variable = entry.getKey();
            int varIndex = variable - 1;
            Integer value = entry.getValue();


            if (value != 0) {
                // variable is a pure literal

                // if not already assigned, assign variable
                if (assignedValues[varIndex] == 0) {
                    pureLiteralVariables.add(varIndex);
                    assign(varIndex, value);
                }
            }
        }
        return pureLiteralVariables;
    }

    public void printAssignment() {
        System.out.println("Assigned variables: " + Arrays.toString(assignedValues));
    }

    public void printFormula(){
        System.out.println("Formula LinkedList: " + formula.toString());
    }

    public void print(){
        //prints both assignment array and formula
        printAssignment();
        printFormula();
    }


}
