import java.awt.event.KeyEvent;
import java.util.Random;

public class Player {
    private int packed_ice;
    private int score;
    private int life;
    private Coordinates cor;
    private Coordinates dir;
    private static char[][] maze;
    private Random rand = new Random();

    // Dirs
    private Coordinates up = new Coordinates(0, -1);
    private Coordinates down = new Coordinates(0, 1);
    private Coordinates left = new Coordinates(-1, 0);
    private Coordinates right = new Coordinates(1, 0);

    Player(char[][] maze) {
        this.maze = maze;
    }

    Player() {

        this.life = 1000;
        this.score = 0;
        Coordinates[] dirs = new Coordinates[]{new Coordinates(0, -1),
                new Coordinates(0, 1),
                new Coordinates(-1, 0),
                new Coordinates(1, 0)};
        this.dir = dirs[rand.nextInt(dirs.length)];
        // Choosing Random Location for Player
        while (true) {
            int rand_x = rand.nextInt(maze[0].length);
            int rand_y = rand.nextInt(maze.length);

            if (maze[rand_y][rand_x] == ' ') {
                this.cor = new Coordinates(rand_x, rand_y);
                maze[rand_y][rand_x] = 'P';
                break;
            }
        }
    }

    Ice movePlayer(int rkey) {
        Ice newIce = null;
        maze[cor.getY()][cor.getX()] = ' ';
        if (rkey == KeyEvent.VK_LEFT && checkSpace(cor.getX() - 1, cor.getY())) {
            cor.setX(cor.getX() - 1);
            setDir(left);
        } else if (rkey == KeyEvent.VK_RIGHT && checkSpace(cor.getX() + 1, cor.getY())) {
            cor.setX(cor.getX() + 1);
            setDir(right);

        } else if (rkey == KeyEvent.VK_UP && checkSpace(cor.getX(), cor.getY() - 1)) {
            cor.setY(cor.getY() - 1);
            setDir(up);

        } else if (rkey == KeyEvent.VK_DOWN && checkSpace(cor.getX(), cor.getY() + 1)) {
            cor.setY(cor.getY() + 1);
            setDir(down);
        } else if (rkey == KeyEvent.VK_SPACE) {
            Coordinates newCor = new Coordinates(cor.getX() + dir.getX(),
                    cor.getY() + getDir().getY());
            if (maze[newCor.getY()][newCor.getX()] == ' ' && packed_ice>0) {
                newIce = new Ice(newCor, getDir());
                packed_ice--;
            }
        }
        int val = maze[cor.getY()][cor.getX()] - 48;
        if (val > 0) {
            switch (val) {
                case 1:
                    score += 3;
                    break;
                case 2:
                    score += 10;
                    break;
                case 3:
                    score += 30;
                    break;
                case 16:
                    packed_ice++;
            }
        }

        maze[cor.getY()][cor.getX()] = 'P';
        return newIce;
    }

    boolean checkSpace(int x, int y) {
        return (maze[y][x] == '@' || maze[y][x] == ' ' || maze[y][x] == '1') || (maze[y][x] == '2' || maze[y][x] == '3');
    }

    // Getters-Setters
    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Coordinates getCor() {
        return cor;
    }

    public void setCor(Coordinates cor) {
        this.cor = cor;
    }

    public Coordinates getDir() {
        return dir;
    }

    public void setDir(Coordinates dir) {
        this.dir = dir;
    }

    public int getLife() {
        return life;
    }

    public void setLife(int life) {
        this.life = life;
    }

    public int getPacked_ice() {
        return packed_ice;
    }

    public void setPacked_ice(int packed_ice) {
        this.packed_ice = packed_ice;
    }
}
