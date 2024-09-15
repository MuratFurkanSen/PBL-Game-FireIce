import enigma.console.TextAttributes;

import java.awt.*;
import java.util.Random;

public class Ice {
    private static char[][] maze;
    TextAttributes cyan = new TextAttributes(Color.CYAN);
    private Coordinates start_cr;
    private Coordinates curr_cr;
    private Stack all_pieces;
    private Stack temp_stack;
    private Stack erase_times;

    private Coordinates dirs;
    private int timer;
    boolean isPlaced = false;
    Random random = new Random();
    private int rnd_x;
    private int rnd_y;

    Ice(char[][] maze) {
        this.maze = maze;
    }


    Ice(Coordinates start_cr, Coordinates dirs) {
        this.curr_cr = start_cr;
        this.dirs = dirs;
        this.all_pieces = new Stack(25);
        this.temp_stack = new Stack(25);
        this.erase_times = new Stack(25);
        this.timer = 1;

        maze[curr_cr.getY()][curr_cr.getX()] = '+';
        all_pieces.push(curr_cr);
        erase_times.push(timer + 100);
        int[] randDirs = new int[]{1, -1};
        rnd_x = 0;
        rnd_y = 0;
        if (dirs.getX() == 0) {
            rnd_x = randDirs[random.nextInt(1, 2)];
        } else {
            rnd_y = randDirs[random.nextInt(1, 2)];
        }
    }

    void increaseTimer() {
        timer++;
        if (timer <= 25) {
            spread();

        }
        while (!erase_times.isEmpty()) {
            temp_stack.push(erase_times.pop());
        }
        if (!temp_stack.isEmpty() && ((int) temp_stack.peek() == timer)) {
            temp_stack.pop();
            while (!temp_stack.isEmpty()) {
                erase_times.push(temp_stack.pop());
            }
            while (!all_pieces.isEmpty()) {
                temp_stack.push(all_pieces.pop());
            }
            erase();
            while (!temp_stack.isEmpty()) {
                all_pieces.push(temp_stack.pop());
            }
        } else {
            while (!temp_stack.isEmpty()) {
                erase_times.push(temp_stack.pop());
            }
        }

    }

    void spread() {
        isPlaced = false;
        if (maze[curr_cr.getY() + dirs.getY()][curr_cr.getX() + dirs.getX()] == ' ') {
            maze[curr_cr.getY() + dirs.getY()][curr_cr.getX() + dirs.getX()] = '+';
            isPlaced = true;
            erase_times.push(timer + 100);
            curr_cr = new Coordinates(curr_cr.getX() + dirs.getX(), curr_cr.getY() + dirs.getY());

        } else {
            if (maze[curr_cr.getY() + rnd_y][curr_cr.getX() + rnd_x] == ' ') {
                maze[curr_cr.getY() + rnd_y][curr_cr.getX() + rnd_x] = '+';
                isPlaced = true;
                erase_times.push(timer + 100);
                curr_cr = new Coordinates(curr_cr.getX() + rnd_x, curr_cr.getY() + rnd_y);
            } else if (maze[curr_cr.getY() + (rnd_y * -1)][curr_cr.getX() + (rnd_x * -1)] == ' ') {
                maze[curr_cr.getY() + (rnd_y * -1)][curr_cr.getX() + rnd_x * -1] = '+';
                isPlaced = true;
                erase_times.push(timer + 100);
                curr_cr = new Coordinates(curr_cr.getX() + (rnd_x * -1), curr_cr.getY() + (rnd_y * -1));
            } else if (maze[curr_cr.getY() + (dirs.getY() * -1)][curr_cr.getX() + (dirs.getX() * -1)] == ' ') {
                maze[curr_cr.getY() + (dirs.getY() * -1)][curr_cr.getX() + (dirs.getX() * -1)] = '+';
                isPlaced = true;
                erase_times.push(timer + 100);
                curr_cr = new Coordinates(curr_cr.getX() + (dirs.getX() * -1), curr_cr.getY() + (dirs.getY() * -1));
            }

        }
        if (!isPlaced) {
            if (all_pieces.peek() != null) {
                curr_cr = (Coordinates) all_pieces.peek();
                temp_stack.push(all_pieces.pop());
                spread();
            }

        } else {
            while (!temp_stack.isEmpty()) {
                all_pieces.push(temp_stack.pop());
            }
            all_pieces.push(curr_cr);
        }
    }

    void erase() {
        curr_cr = (Coordinates) temp_stack.pop();
        maze[curr_cr.getY()][curr_cr.getX()] = ' ';
    }

    // Getters and Setters
    public Coordinates getStart_cr() {
        return start_cr;
    }

    public void setStart_cr(Coordinates start_cr) {
        this.start_cr = start_cr;
    }

    public Coordinates getDirs() {
        return dirs;
    }

    public void setDirs(Coordinates dirs) {
        this.dirs = dirs;
    }

    public Coordinates getCurr_cr() {
        return curr_cr;
    }

    public void setCurr_cr(Coordinates curr_cr) {
        this.curr_cr = curr_cr;
    }

    public Stack getAll_pieces() {
        return all_pieces;
    }

    public void setAll_pieces(Stack all_pieces) {
        this.all_pieces = all_pieces;
    }
}