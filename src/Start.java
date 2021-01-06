import java.util.ArrayList;
import java.util.Scanner;

public class Start {
    public static void main(String[] args){
        //temporary, used for testing
        Scanner in = new Scanner(System.in);
        boolean[][] validSquares = {{true, true, true, true, true, true, true, true},
                                    {false, false, false, false, false, false, false, true},
                                    {true, true, false, true, true, true, true, true},
                                    {false, true, false, true, false, false, true, false},
                                    {false, true, true, true, true, false, true, true},
                                    {true, true, false, true, false, false, false, true},
                                    {false, true, false, true, false, false, false, true},
                                    {true, true, false, true, true, false, true, true}};
        PivotWall wall = new PivotWall(2, 3, 2, 1, true, 3, false, false);
        ArrayList<MazeObject> objects = new ArrayList<>();
        objects.add(wall);
        Maze maze = new Maze(8, 8, objects, 0, 0,  validSquares);
        boolean playing = true;
        String input;
        while(playing){
            System.out.println(maze);
            input = in.next();
            switch (input) {
                case "w" -> maze.movePlayer((byte) 0);
                case "a" -> maze.movePlayer((byte) 2);
                case "s" -> maze.movePlayer((byte) 3);
                case "d" -> maze.movePlayer((byte) 1);
                case "q" -> playing = false;
            }
        }
    }
}
