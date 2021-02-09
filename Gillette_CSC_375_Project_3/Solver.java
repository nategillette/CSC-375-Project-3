package PegSolitaire;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;

/**
 * Nathan Gillette
 * CSC 375 Lab 3
 * Peg Solitaire
 */

public class Solver {

    static private ForkJoinPool forkJoinPool = new ForkJoinPool();

    public static void main(String args[]) throws ExecutionException, InterruptedException {
        //Set the parameters for the program.
        int MAXREMAINING = Integer.parseInt(args[0]);
        int DEPTH = Integer.parseInt(args[1]);

        System.out.print("Running...");
        //Solve this board with the argument values provided.
        Board game = new Board();
        ArrayList<Board> results = solveBoard(game, MAXREMAINING, DEPTH); // Solve the board
        if (results == null) { //This happens if the program cant find a board with the selected max pegs
            System.out.println("Failed. Retry with larger depth.");
        } else {
            System.out.println("done.");
            //Set up the board renderer, and a frame to display it in

            Renderer render = new Renderer();

            JFrame f = new JFrame();
            f.setResizable(false); //No resizing.

            f.setSize(500, 500);
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.setTitle("Peg Solitaire");//title.
            f.add(render);

            //Interpret args
            for (String s : args) {
                if (s.equals("-D")) {//Display path to result
                    for (Board b : results) {
                        System.out.println(b.toString());
                    }
                } else if (s.equals("-V")) { //Display result in graphical window
                    f.setVisible(true);//show the JFrame
                    for (Board b : results) {
                        if (b.getRemainingPegs() == MAXREMAINING) {
                            render.setGame(b);
                            render.refresh();
                        }
                    }
                }
            }
        }
    }

    private static ArrayList<Board> solveBoard(Board start, int pegs, int searchDepth) {

        ArrayList<Board> results = new ArrayList<>(); //list for the results
        results.add(start);//add the initial board.

        int last = -1; // the last score, to double check that the two boards arent the same
        Board next = new Board();
        next.copy(start, next);
        while (next.getRemainingPegs() != pegs) {
            int win = Integer.MIN_VALUE;
            last = next.getRemainingPegs();
            BoardEvaluate tasks[] = new BoardEvaluate[next.getRemainingPegs()];
            //Gather all the possible moves.
            for (Move m : next.getRemainingMoves()) {
                Board temp = new Board();
                next.copy(next, temp);
                temp.jump(m.x, m.y, m.dir);

                /**
                 * Fork/Join:
                 * removed the evaluate and individually forked and joined
                 * each board I want to evaluate
                 *
                 */
                for(int i = 0; i < tasks.length; ++i){
                    tasks[i] = new BoardEvaluate(start, pegs, searchDepth, 0);
                    tasks[i].fork();
                }
                for(int i = 0; i < tasks.length; ++i){
                    int result = tasks[i].join();
                    if (result > win) {
                        win = result;
                        next.copy(temp, next);
                    }
                }
            }
            //If both this board and the last seen board peg counts are the same we need to exit the program.
            //An infinite loop will happen if not.
            if (next.getRemainingPegs() == last) {
                return null;
            }
            Board test = new Board();
            test.copy(next, test);
            results.add(test);
        }
        return results;
    }
    }