package satsolver;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

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
        List<Clause> clauses = new ArrayList<>();
        for (String line : lines) {
            if (line.startsWith("c") || line.startsWith("p")) {
                continue;
            }

            Clause clause = new Clause();
            StringTokenizer st = new StringTokenizer(line);
            while (st.hasMoreTokens()) {
                int literal = Integer.parseInt(st.nextToken());
                if (literal != 0) {
                    clause.addLiteral(literal);
                } else {
                    clauses.add(clause);
                    break;
                }
            }
        }
        return new Formula(clauses);
    }
}
