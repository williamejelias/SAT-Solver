package satsolver;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class FormulaReader {

    public static Formula readFromFile(String filename) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(filename));

        List<String> lines = new ArrayList<>();
        String line;
        while ((line = br.readLine()) != null) {
            lines.add(line);
        }

        return deserialize(lines);
    }

    private static Formula deserialize(List<String> lines) {
        int numVariables;
        int numClauses = 0;

        Map<Integer, Clause> clauses = new HashMap<>();
        int clauseID = 0;
        for (String line : lines) {
            if (line.startsWith("c")) {
                continue;
            }

            if (line.startsWith("p")) {
                StringTokenizer pst = new StringTokenizer(line);
                while (pst.hasMoreTokens()) {
                    String p = pst.nextToken();
                    String cnf = pst.nextToken();
                    numVariables = Integer.parseInt(pst.nextToken());
                    numClauses = Integer.parseInt(pst.nextToken());
                }
                continue;
            }

            Set<Integer> clauseIntegers = new HashSet<>();
            StringTokenizer st = new StringTokenizer(line);
            while (st.hasMoreTokens()) {
                int literal = Integer.parseInt(st.nextToken());
                if (literal != 0) {
                    clauseIntegers.add(literal);
                } else {
                    clauses.put(clauseID, new Clause(new ArrayList<>(clauseIntegers)));
                    clauseID ++;
                    break;
                }
            }
        }

        clauseID = 0;
        Map<Integer, Set<Integer>> variableMap = new HashMap<>();
        for (Clause clause : clauses.values()) {
            for (int i : clause.values) {
                // if i in variableMap, add the clause to its set
                try {
                    Set<Integer> clauseIDSet = variableMap.get(i);
                    clauseIDSet.add(clauseID);
                    variableMap.put(i, clauseIDSet);
                } catch (Exception e) {
                    // otherwise add i to variableMap with new set
                    Set<Integer> clauseIDSet = new HashSet<>();
                    clauseIDSet.add(clauseID);
                    variableMap.put(i, clauseIDSet);
                }
            }
            clauseID ++;
        }

        numClauses = clauseID;
//        return new Formula(clauses);
        return new Formula(numClauses, clauses, variableMap);
    }
}
