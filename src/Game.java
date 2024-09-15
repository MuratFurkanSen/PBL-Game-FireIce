import enigma.console.Console;
import enigma.console.TextAttributes;
import enigma.core.Enigma;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

public class Game {
    // ------ Standard Variables for Keyboard ------
    public KeyListener klis;
    public int keypr;   // Key Pressed ?
    public int rkey;    // Key   (For Press/Release)

    // Colors
    TextAttributes orange = new TextAttributes(new Color(255, 75, 0));
    TextAttributes blue = new TextAttributes(Color.blue);
    TextAttributes cyan = new TextAttributes(Color.CYAN);
    TextAttributes green = new TextAttributes(Color.GREEN);
    TextAttributes red = new TextAttributes(Color.RED);
    TextAttributes purple = new TextAttributes(new Color(128, 0, 128));
    TextAttributes white = new TextAttributes(Color.WHITE);
    TextAttributes yellow_on_yellow = new TextAttributes(Color.ORANGE, Color.ORANGE);

    // General Game Attributes
    Random random = new Random();
    static Console cn;
    private Player player;
    private char[][] maze;
    private int gameTime;

    // Ice-Fire-Treasure-Pawn Lists and Input Queue
    CircularQueue inputQueue = new CircularQueue(10);
    LinkedList ices = new LinkedList();
    LinkedList fires = new LinkedList();
    LinkedList pawns = new LinkedList();
    char[] treasures = new char[]{'1', '2', '3'};

    // High Score Table
    DoubleLinkedList table = new DoubleLinkedList(true);


    Game() throws Exception {
        // Initializing Enigma Console
        cn = Enigma.getConsole("FireIce", 75, 23, 20, 6);
        // Reading Maze
        Scanner sc = new Scanner(new File("maze.txt"));
        maze = new char[23][53];
        int index = 0;
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            if (line.length() == 0) {
                continue;
            }
            maze[index] = line.toCharArray();
            index++;
        }
        // Keyboard Stream Listener
        klis = new KeyListener() {
            public void keyTyped(KeyEvent e) {
            }

            public void keyPressed(KeyEvent e) {
                if (keypr == 0) {
                    keypr = 1;
                    rkey = e.getKeyCode();
                }
            }

            public void keyReleased(KeyEvent e) {
            }
        };
        cn.getTextWindow().addKeyListener(klis);


        // General Game Setup
        new Player(maze);
        new Fire(maze);
        new Ice(maze);
        new Pawn(maze);

        player = new Player();

        // Filling Maze At Beginning
        fillUpInputQueue();
        while (!inputQueue.isEmpty()) {
            placeInput();
        }
        fillUpInputQueue();

        // Main Game Loop
        gameLayout();
        printMaze();
        gameTime = 0;
        while (player.getLife() > 0) {
            // Player Movement and Actions
            if (keypr == 1) {
                Ice newIce = player.movePlayer(rkey);
                if (newIce != null) {
                    ices.add(newIce);
                }
                keypr = 0;
            }
            //Sync Time of Fires
            for (int i = 0; i < fires.size(); i++) {
                ((Fire) fires.atIndex(i)).increaseTimer();
            }
            //Sync Time of Ices
            for (int i = 0; i < ices.size(); i++) {
                ((Ice) ices.atIndex(i)).increaseTimer();
            }
            //Move Computer Pawns
            if (gameTime % 4 == 0) {
                movePawns();
            }

            // Input Queue Placing
            if (gameTime % 20 == 0) {
                placeInput();
                fillUpInputQueue();
            }

            // Interactions between Game Elements
            interactions();

            // Game Time and Output
            gameInfo();
            printMaze();
            gameTime++;
            Thread.sleep(100);
        }
        clearScreen();
        readTableFile();
        if (player.getScore()>Pawn.getScore()){
            addPlayerToTable();
            writeTableFile();
        }
        displayTable();
    }


    // Console Outputs
    void clearScreen() {
        String space = "                                                                    ";
        for (int i = 0; i < 23; i++) {
            cn.getTextWindow().setCursorPosition(0, i);
            cn.getTextWindow().output(space);
        }
    }

    void gameLayout() {
        cn.getTextWindow().setCursorPosition(55, 0);
        cn.getTextWindow().output("Input");
        cn.getTextWindow().setCursorPosition(55, 1);
        cn.getTextWindow().output("<<<<<<<<<<");
        cn.getTextWindow().setCursorPosition(55, 3);
        cn.getTextWindow().output("<<<<<<<<<<");
        cn.getTextWindow().setCursorPosition(55, 6);
        cn.getTextWindow().output("Time    : ");
        cn.getTextWindow().setCursorPosition(55, 9);
        cn.getTextWindow().output("P.Score : ");
        cn.getTextWindow().setCursorPosition(55, 10);
        cn.getTextWindow().output("P.Life  : ");
        cn.getTextWindow().setCursorPosition(55, 11);
        cn.getTextWindow().output("P.Ice   : ");
        cn.getTextWindow().setCursorPosition(55, 14);
        cn.getTextWindow().output("C.Score : ");
        cn.getTextWindow().setCursorPosition(55, 15);
        cn.getTextWindow().output("C.Robots: ");
    }

    void gameInfo() {
        displayInputQueue(55, 2);
        // Cleaning
        cn.getTextWindow().setCursorPosition(65, 6);
        cn.getTextWindow().output("                 ");
        cn.getTextWindow().setCursorPosition(65, 9);
        cn.getTextWindow().output("                 ");
        cn.getTextWindow().setCursorPosition(65, 10);
        cn.getTextWindow().output("                 ");
        cn.getTextWindow().setCursorPosition(65, 11);
        cn.getTextWindow().output("                 ");
        cn.getTextWindow().setCursorPosition(65, 14);
        cn.getTextWindow().output("                 ");
        cn.getTextWindow().setCursorPosition(65, 15);
        cn.getTextWindow().output("                 ");
        // Info
        cn.getTextWindow().setCursorPosition(65, 6);
        cn.getTextWindow().output(gameTime/10 + " ");
        cn.getTextWindow().setCursorPosition(65, 9);
        cn.getTextWindow().output(player.getScore() + " ");
        cn.getTextWindow().setCursorPosition(65, 10);
        cn.getTextWindow().output(player.getLife() + " ");
        cn.getTextWindow().setCursorPosition(65, 11);
        cn.getTextWindow().output(player.getPacked_ice() + " ");
        cn.getTextWindow().setCursorPosition(65, 14);
        cn.getTextWindow().output(Pawn.getScore() + " ");
        cn.getTextWindow().setCursorPosition(65, 15);
        cn.getTextWindow().output(pawns.size() + " ");


    }

    void printMaze() {
        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[i].length; j++) {
                cn.getTextWindow().setCursorPosition(j, i);
                cn.getTextWindow().output(maze[i][j], ColorSelector(maze[i][j]));

            }
        }
    }

    void displayInputQueue(int x, int y) {
        cn.getTextWindow().setCursorPosition(x, y);
        cn.getTextWindow().output("          ");
        cn.getTextWindow().setCursorPosition(x, y);
        for (int i = 0; i < inputQueue.size(); i++) {
            char ch = (char) inputQueue.peek();
            cn.getTextWindow().output(ch, ColorSelector(ch));
            inputQueue.enqueue(inputQueue.dequeue());
        }
    }

    TextAttributes ColorSelector(char ch) {
        if (ch == '1' || ch == '2' || ch == '3') {
            return green;
        } else if (ch == 'C') {
            return red;
        } else if (ch == '-') {
            return orange;
        } else if (ch == '@') {
            return blue;
        } else if (ch == '+') {
            return cyan;
        } else if (ch == 'P') {
            return purple;
        } else if (ch == '#'){
            return yellow_on_yellow;
        }
        else {
            return white;
        }
    }

    // Game Actions
    void fillUpInputQueue() {
        Random random = new Random();
        while (!inputQueue.isFull()) {
            int randomNum = random.nextInt(0, 30);
            if (randomNum < 5) {
                inputQueue.enqueue('1');
            } else if (randomNum < 10) {
                inputQueue.enqueue('2');
            } else if (randomNum < 15) {
                inputQueue.enqueue('3');
            } else if (randomNum < 21) {
                inputQueue.enqueue('-');
            } else if (randomNum < 27) {
                inputQueue.enqueue('@');
            } else if (randomNum < 30) {
                inputQueue.enqueue('C');
            }
        }
    }

    void placeInput() {
        int rand_x;
        int rand_y;
        boolean isFilled = false;
        while (!isFilled) {
            rand_x = random.nextInt(0, maze[0].length);
            rand_y = random.nextInt(0, maze.length);
            if (maze[rand_y][rand_x] == ' ') {
                char new_item = (char) inputQueue.dequeue();
                maze[rand_y][rand_x] = new_item;
                if (new_item == '-') {
                    fires.add(new Fire(new Coordinates(rand_x, rand_y)));
                } else if (new_item == 'C') {
                    pawns.add(new Pawn(new Coordinates(rand_x, rand_y)));
                }
                isFilled = true;
            }
        }

    }

    void movePawns() {
        for (int i = 0; i < pawns.size(); i++) {
            Pawn curr_pawn = ((Pawn) pawns.atIndex(i));
            int val = curr_pawn.move(treasures);
            if (val > 0) {
                // Adding Score to Computer
                switch (val) {
                    case 1:
                        Pawn.setScore(Pawn.getScore() + 9);
                        break;
                    case 2:
                        Pawn.setScore(Pawn.getScore() + 30);
                        break;
                    case 3:
                        Pawn.setScore(Pawn.getScore() + 90);
                        break;
                }
            }
        }

    }
    void checkPawnsLife(){
        for (int i = 0; i < pawns.size(); i++) {
            Pawn curr_pawn = (Pawn) pawns.atIndex(i);
            if (curr_pawn.getHealth() <=0){
                maze[curr_pawn.getCor().getY()][curr_pawn.getCor().getX()] = ' ';
                pawns.pop(i);
            }
        }
    }

    void interactions() {
        int life = player.getLife();
        life -= checkAround(player.getCor(), 'C') * 50;
        life -= checkAround(player.getCor(), '-') * 1;
        player.setLife(life);
        for (int i = 0; i < pawns.size(); i++) {
            Pawn curr_pawn = ((Pawn) pawns.atIndex(i));
            int pawn_life = curr_pawn.getHealth();
            pawn_life -= checkAround(curr_pawn.getCor(), '+') * 50;
            curr_pawn.setHealth(pawn_life);
            checkPawnsLife();
        }

    }

    int checkAround(Coordinates cor, char ch) {
        Coordinates[] dirs = new Coordinates[]{
                new Coordinates(0, -1),
                new Coordinates(1, 0),
                new Coordinates(0, 1),
                new Coordinates(-1, 0)};
        int count = 0;
        for (int i = 0; i < dirs.length; i++) {
            if (maze[cor.getY() + dirs[i].getY()][cor.getX() + dirs[i].getX()] == ch) {
                count++;
            }
        }
        return count;
    }

    // High Score Table Actions
    void readTableFile() throws IOException {
        // Reading Top Players from File
        Scanner scanner_scores;
        try {
            scanner_scores = new Scanner(new File("highscoretable.txt"));
        } catch (FileNotFoundException e) {
            FileWriter create = new FileWriter("highscoretable.txt");
            create.close();
            scanner_scores = new Scanner(new File("highscoretable.txt"));
        }
        while (scanner_scores.hasNextLine()) {
            String input = scanner_scores.nextLine();
            if (!input.equals("")) {
                int priority = Integer.parseInt(input.split(" ")[input.split(" ").length - 1]);
                table.add(input, priority);
            }
        }
        scanner_scores.close();
        //----------------

    }

    void writeTableFile() throws IOException {
        FileWriter writer = new FileWriter("highscoretable.txt");
        for (int i = 0; i < table.size(); i++) {
            writer.write(table.atIndex(i) + "\n");
        }
        writer.close();
    }

    void addPlayerToTable() {
        Scanner scanner = new Scanner(System.in);
        cn.getTextWindow().setCursorPosition(0, 0);
        System.out.print("Please enter your name: ");
        String name = scanner.nextLine().trim();
        table.add(name+" " + player.getScore(), player.getScore());
    }

    void displayTable() {

        cn.getTextWindow().setCursorPosition(0, 1);
        cn.getTextWindow().output("----------------------------------");
        cn.getTextWindow().setCursorPosition(0, 2);
        cn.getTextWindow().output("High Score Table");
        cn.getTextWindow().setCursorPosition(0, 4);

        table.display("\n");
    }
}