package PegSolitaire;

import java.util.ArrayList;
import java.util.concurrent.RecursiveTask;

public class BoardEvaluate extends RecursiveTask<Integer> {

    private Board load; //Board object to examine
    private final int maxDepth; //Maximum depth to work
    private int curDepth;   //Current depth of this recurse
    private final int MAXREMAINING; //Max remaining pegs expected

    /**
     * Board evaluation constructor
     */
    public BoardEvaluate(Board workLoad, int MAXREMAINING, int maxDepth, int curDepth) {
        this.load = new Board();
        workLoad.copy(workLoad, load);
        this.maxDepth = maxDepth;
        this.curDepth = curDepth;
        this.MAXREMAINING = MAXREMAINING;
    }

    /**
     * Computes the current work.
     */
    protected Integer compute() {
        //System.out.println("depth: " + curDepth);
        int sum = 0;
        if (curDepth == maxDepth) {
            if (load.getRemainingPegs() == MAXREMAINING) {
                sum = 1;
            }
            curDepth++;
            return sum;
        } else {

            if (load.getRemainingPegs() == MAXREMAINING) {
                sum = 1;
            } else if (load.getRemainingMoves().size() > 0) {

                curDepth++;

                ArrayList<BoardEvaluate> subtasks = createSubtasks();
                for (BoardEvaluate subtask : subtasks) {
                    subtask.fork();
                }

                for (BoardEvaluate subtask : subtasks) {
                    sum += subtask.join();
                }
            }
            return sum;
        }
    }

    /**
     * Returns all the possible remaining moves of this current board
     */
    private ArrayList<BoardEvaluate> createSubtasks() {
        ArrayList<BoardEvaluate> subtasks = new ArrayList<>();

        for (Move m : load.getRemainingMoves()) {
            Board temp = new Board();
            this.load.copy(load, temp);
            temp.jump(m.x, m.y, m.dir);

            subtasks.add(new BoardEvaluate(temp, this.MAXREMAINING, maxDepth, curDepth));
        }
        return subtasks;
    }

}