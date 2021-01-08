import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public class Start {
    public static void main(String[] args) {
        Maze maze = null;
        if (Files.exists(Paths.get("./Mazes/Level1.maz"))) {
            maze = FileManager.parseMaze("./Mazes/Level1.maz");
        }
        Scanner in = new Scanner(System.in);
        boolean playing = true;
        String input;
        if (maze != null) {
            while (playing) {
                System.out.println(maze);
                input = in.next();
                switch (input) {
                    case "w" -> playing = !maze.movePlayer((byte) 0);
                    case "a" -> playing = !maze.movePlayer((byte) 2);
                    case "s" -> playing = !maze.movePlayer((byte) 3);
                    case "d" -> playing = !maze.movePlayer((byte) 1);
                    case "q" -> playing = false;
                }
                if (!playing) {
                    System.out.println("Game Over");
                }
            }
        }
    }
}
