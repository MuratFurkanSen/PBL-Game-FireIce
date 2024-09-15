import java.util.Random;

public class Fire {
    private static char[][] maze;
    private Coordinates start_cr;
    private Coordinates curr_cr;
    private CircularQueue all_pieces;
    private CircularQueue erasing_pieces;
    private CircularQueue erase_times;
    private int timer;
    private Coordinates[] dirs;
    private int dir_index;
    private int dir_rot;
    private boolean isPlaced;
    private int count;
    private int totalSpreadFire;
    Random random = new Random();

    Fire(char[][] maze) {
        this.maze = maze;
    }

    Fire(Coordinates start_cr) {
        this.all_pieces = new CircularQueue(50);
        this.erasing_pieces = new CircularQueue(50);
        this.erase_times = new CircularQueue(50);
        all_pieces.enqueue(start_cr);
        this.start_cr = start_cr;
        this.curr_cr = start_cr;
        this.timer = 1;
        this.dirs = new Coordinates[]{
                new Coordinates(0, -1),
                new Coordinates(1, 0),
                new Coordinates(0, 1),
                new Coordinates(-1, 0)};
        this.maze[start_cr.getY()][start_cr.getX()] = '-';
        erasing_pieces.enqueue(curr_cr);
        erase_times.enqueue(timer+100);
        totalSpreadFire = 1;
        dir_index = random.nextInt(0, 3);
        switch (random.nextInt(1, 2)) {
            case 1:
                dir_rot = 1;
                break;
            case 2:
                dir_rot = -1;
                break;
        }
        dir_index=0;
        dir_rot=1;
    }
    void increaseTimer() throws Exception {
        timer++;
        if (totalSpreadFire<50 && !all_pieces.isEmpty()){
            spread();
        }
        // Precision Timing for Erasing Operation
        if (!erasing_pieces.isEmpty() && !erase_times.isEmpty()&& ((int) erase_times.peek())==timer) {
            erase();
        }
    }

    void spread() throws Exception {
        isPlaced = false;
        Coordinates dir = dirs[dir_index % dirs.length];
        dir_index += dir_rot;
        if (maze[curr_cr.getY() + dir.getY()][curr_cr.getX() + dir.getX()] == ' ') {
            maze[curr_cr.getY() + dir.getY()][curr_cr.getX() + dir.getX()] = '-';
            all_pieces.enqueue(new Coordinates(curr_cr.getX() + dir.getX(), curr_cr.getY() + dir.getY()));
            erasing_pieces.enqueue(new Coordinates(curr_cr.getX() + dir.getX(), curr_cr.getY() + dir.getY()));
            isPlaced = true;
            totalSpreadFire++;
            erase_times.enqueue(timer+100);
        }
        count++;
        if (count >= 4) {
            count = 0;
            curr_cr = (Coordinates) all_pieces.peek();
            all_pieces.enqueue(all_pieces.dequeue());
            if (curr_cr.getX() == start_cr.getX()&&curr_cr.getY() == start_cr.getY()) {
                return;
            }
            dir_index += dir_rot * dirs.length * -1;
        }
        if (!isPlaced) {
            spread();
        }
    }
    void erase() {
        erase_times.dequeue();
        curr_cr = (Coordinates) erasing_pieces.dequeue();
        maze[curr_cr.getY()][curr_cr.getX()] = ' ';
    }
}
