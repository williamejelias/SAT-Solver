package satsolver;

public class Main {
    public static void main(String[] args) {
        // argument is a filename.cnf
        if (args.length != 1) {
            System.out.println("ERROR: A single input file was not specified correctly!");
            System.exit(0);
        }

        Timer timer = new Timer();
        timer.start(); //start timer
        SatSolver solver = new SatSolver(args[0]);
        timer.stop(); //stop timer
        System.out.println("Total SAT Execution Time = " + timer.getDuration() + " milliseconds!"); //print calculated total time
    }
}