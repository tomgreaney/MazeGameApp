import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public class Start {
    public static void main(String[] args) {
        String mazeFile = "./Mazes/Level1.maz";
        Maze maze = null;
        if (Files.exists(Paths.get(mazeFile))) {
            maze = FileManager.parseMaze(mazeFile);
        }
        Scanner in = new Scanner(System.in);
        boolean playing = true;
        String input;
        if (maze != null) {
            System.out.println(maze);
            while (playing) {
                input = in.next();
                switch (input) {
                    case "w" -> playing = !maze.movePlayer((byte) 0);
                    case "a" -> playing = !maze.movePlayer((byte) 2);
                    case "s" -> playing = !maze.movePlayer((byte) 3);
                    case "d" -> playing = !maze.movePlayer((byte) 1);
                    case "q" -> playing = false;
                }
                System.out.println(maze);
                if (!playing) {
                    System.out.println("Game Over");
                }
            }
        }
    }
}
