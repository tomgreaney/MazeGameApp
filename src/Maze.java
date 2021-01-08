import java.awt.Point;
import java.util.ArrayList;

public class Maze {
    private final int width;
    private final int height;
    private final boolean[][] freeCells;//Cells which the player can move to
    private final ArrayList<MazeObject> mazeObjects;
    private final Player player;
    private final Exit exit;

    public Maze(int width, int height, ArrayList<MazeObject> mazeObjects, int playerX, int playerY, Exit exit){
        this.width = width;
        this.height = height;
        this.mazeObjects = mazeObjects;
        this.exit = exit;
        mazeObjects.add(exit);
        this.freeCells = new boolean[width][height];
        this.player = new Player(playerX, playerY);
    }

    //this overloaded method is temporary and will not be in finished product.
    public Maze(int width, int height, ArrayList<MazeObject> mazeObjects, int playerX, int playerY, boolean[][] freeCells, Exit exit){
        this.width = width;
        this.height = height;
        this.mazeObjects = mazeObjects;
        mazeObjects.add(exit);
        this.freeCells = freeCells;
        this.player = new Player(playerX, playerY);
        this.exit = exit;
    }

    private Point getTarget(byte direction, boolean opposite){
        if(opposite) {
            direction = (byte) (~direction & 0x03);
        }
        return switch (direction) {
            default -> new Point(player.origin.x, player.origin.y - 1);
            case (1) -> new Point(player.origin.x+1, player.origin.y);
            case (2) -> new Point(player.origin.x - 1, player.origin.y);
            case (3) -> new Point(player.origin.x, player.origin.y + 1);
        };
    }

    public Boolean movePlayer(byte direction){
        //direction can be one of four values: 0 is up, 1 is right, 2 is left, 3 is down
        //return true if and only if the player reached the exit.
        Point target = getTarget(direction, false);

        if(target.x < 0 || target.x >= width || target.y < 0 || target.y >= height){
            return false;
        }

        if(freeCells[target.y][target.x]){
            Point[] changedCells;
            Point oldPlayerOrigin = new Point(player.origin);
            if(player.move(direction)) {

                for (MazeObject foo : mazeObjects) {
                    if (foo instanceof PlayerMoveUpdatable) {
                        changedCells = ((PlayerMoveUpdatable) foo).update(player.origin, oldPlayerOrigin);
                        for (Point changedCell : changedCells) {
                            freeCells[changedCell.y][changedCell.x] = !freeCells[changedCell.y][changedCell.x];
                        }
                    }
                }
            }else{
                oldPlayerOrigin = getTarget(player.getLastMove(), true);//might run into bugs if player reverses after being pushed
                for (MazeObject foo : mazeObjects) {
                    if (foo instanceof PlayerMoveUpdatable) {
                        changedCells = ((PlayerMoveUpdatable) foo).reverse(player.origin, oldPlayerOrigin);
                        for (Point changedCell : changedCells) {
                            freeCells[changedCell.y][changedCell.x] = !freeCells[changedCell.y][changedCell.x];
                        }
                    }
                }
            }
        }
        return player.origin.equals(exit.origin);
    }

    public String toString(){
        //temporary toString method used for testing
        char[][] printedMaze = new char[height + 2][width + 2];
        for(int i = 0; i < width+2; i++){
            printedMaze[0][i] = 'X';
            printedMaze[height+1][i] = 'X';
        }
        for(int i = 1; i < height+1; i++){
            printedMaze[i][0] = 'X';
            printedMaze[i][width+1] = 'X';
        }
        for(int row = 1; row < freeCells.length + 1; row++){
            for(int col = 1; col < freeCells[0].length + 1; col++){
                if(!freeCells[row-1][col-1]){
                    printedMaze[row][col] = 'X';
                }else{
                    printedMaze[row][col] = ' ';
                }
            }
        }
        for(MazeObject foo : mazeObjects){
            if(foo instanceof Printable){
                Point[] coveredPoints = ((Printable) foo).getCoveredCells();
                char toPrint = ((Printable) foo).getPrintChar();
                for (Point coveredPoint : coveredPoints) {
                    printedMaze[coveredPoint.y + 1][coveredPoint.x + 1] = toPrint;
                }
            }
        }
        printedMaze[player.origin.y+1][player.origin.x+1] = 'O';

        StringBuilder toPrint = new StringBuilder();
        for(int row = 0; row < height+2; row++){
            for(int col = 0; col < width+2; col++){
                toPrint.append(printedMaze[row][col]);
                toPrint.append(' ');
            }
            toPrint.append('\n');
        }
        return toPrint.toString();
    }
}