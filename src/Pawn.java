import java.util.Random;

public class Pawn {
    private static int score = 0;
    private static char[][] maze;
    private Coordinates cor;
    private Coordinates dis;
    private int health;
    private Coordinates[] dirs = new Coordinates[]{new Coordinates(0,-1),
                                                   new Coordinates(1,0),
                                                   new Coordinates(0,1),
                                                   new Coordinates(-1,0)};

    Pawn(char[][] maze) {
        this.maze = maze;
    }

    Pawn(Coordinates cor) {
        this.cor = cor;
        this.health = 1000;
    }
    int move(char[] targets){
        maze[cor.getY()][cor.getX()] = ' ';
        Coordinates new_pos = findPath(targets);
        cor.setX(new_pos.getX());
        cor.setY(new_pos.getY());
        int val = maze[cor.getY()][cor.getX()];
        maze[cor.getY()][cor.getX()] = 'C';
        return val -48;

    }
    Coordinates findPath(char[] targets){
        LinkedList possible_paths = new LinkedList();
        LinkedList initial_path = new LinkedList();
        initial_path.add(cor);
        possible_paths.add(initial_path);
        boolean[][] bool_maze = new boolean[23][53];
        boolean treasureExists = false;
        for(int i = 0; i < maze.length; i++){
            for(int j = 0; j < maze[i].length; j++){
                if(treasureExists || checkTargets(j,i,targets)){
                    treasureExists = true;
                }
                if (maze[i][j] == ' '||checkTargets(j,i,targets)){
                    bool_maze[i][j] = true;
                }
            }
        }
        if(!treasureExists){
            return new Coordinates(cor.getX(),cor.getY());
        }
        Random rand = new Random();
        int dir_index = rand.nextInt(dirs.length);
        while (true){
            int start_size = possible_paths.size();
            if (start_size == 0){
                return new Coordinates(cor.getX(),cor.getY());
            }


            for(int i = 0; i < start_size; i++){
                for(int j = 0; j < dirs.length; j++){
                    LinkedList current_path = ((LinkedList) possible_paths.atIndex(0)).copy();
                    Coordinates last_pos = (Coordinates) current_path.atIndex(current_path.size()-1);
                    Coordinates new_pos = new Coordinates(last_pos.getX()+dirs[j].getX(),last_pos.getY()+dirs[j].getY());
                    if(bool_maze[new_pos.getY()][new_pos.getX()]){
                        current_path.add(new_pos);
                        bool_maze[new_pos.getY()][new_pos.getX()] = false;
                        if(maze[last_pos.getY()+dirs[j].getY()][last_pos.getX()+dirs[j].getX()] != ' '){
                            return (Coordinates) current_path.atIndex(1);
                        }
                        else {
                            possible_paths.add(current_path.copy());
                        }
                    }
                }
                possible_paths.pop(0);
            }
        }
    }
    boolean checkTargets(int x, int y, char[] targets) {
        for (char target : targets) {
            if (maze[y][x] == target) {
                return true;
            }
        }
        return false;
    }

    // Getters-Setters
    public static int getScore() {
        return score;
    }
    public static void setScore(int score) {
        Pawn.score = score;
    }

    public Coordinates getCor() {
        return cor;
    }

    public void setCor(Coordinates cor) {
        this.cor = cor;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }
}
