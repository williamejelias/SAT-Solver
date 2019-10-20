# Sat-Solver



## Project Description

> In computer science, the [Boolean satisfiability problem](https://en.wikipedia.org/wiki/Boolean_satisfiability_problem) (sometimes called propositional satisfiability problem and abbreviated as SATISFIABILITY or SAT) is the problem of determining if there exists an interpretation that satisfies a given Boolean formula. In other words, it asks whether the variables of a given Boolean formula can be consistently replaced by the values TRUE or FALSE in such a way that the formula evaluates to TRUE. If this is the case, the formula is called satisfiable. On the other hand, if no such assignment exists, the function expressed by the formula is FALSE for all possible variable assignments and the formula is unsatisfiable. For example, the formula "a AND NOT b" is satisfiable because one can find the values a = TRUE and b = FALSE, which make (a AND NOT b) = TRUE. In contrast, "a AND NOT a" is unsatisfiable. 

> SAT is the first problem that was proven to be [NP-complete](https://en.wikipedia.org/wiki/NP-completeness).






## Project Features

  A recursive backtracking SAT Solver implemented in Java utilising Unit Propagation and Pure Literal Elimination heuristics to speed up solving.
  
Compile the files:
```bash
cd src
javac satsolver/*.java
```

Run the solver with:

```bash
java -classpath . satsolver.Main filename.cnf
```
where `filename.cnf` is a plaintext file in the `.cnf` format containing the SAT problem encoded in the DIMACS format.

