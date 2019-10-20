package satsolver;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.regex.Pattern;

public class FormulaReader {

    private int[][] formula;
    private int numVars, numClauses;

    public void read(String filename) {
        try {
            Scanner fileScanner = new Scanner(new File(filename));

            // look for "p cnf" ignore c lines as these are comments
            Pattern pattern = Pattern.compile("p cnf");

            // loop until found "p cnf"
            while (fileScanner.findInLine(pattern) == null) {
                fileScanner.nextLine();
            }

            numVars = fileScanner.nextInt();
            numClauses = fileScanner.nextInt();
            formula = new int[numClauses][];
            LinkedList<Integer> currentLine = new LinkedList<>();
            for (int currentRow = 0, currentNum; fileScanner.hasNextInt();) {
                currentNum = fileScanner.nextInt();

                // 0 marks end of clause
                if (currentNum == 0) {
                    formula[currentRow] = currentLine.stream().mapToInt(i->i).toArray();
                    currentRow ++;
                    currentLine.clear();
                    continue;
                }
                currentLine.add(currentNum);
            }
            fileScanner.close();

        } catch (FileNotFoundException e) {
            System.out.println("ERROR: Unable to read " + filename + ".");
            System.exit(0);
        }
    }

    public int[][] getFormula() {
        return formula;
    }

    public int getNumVars() {
        return numVars;
    }

    public int getNumClauses() {
        return numClauses;
    }
}
