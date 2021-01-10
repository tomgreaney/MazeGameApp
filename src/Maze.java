import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;

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

    public Boolean movePlayer(byte direction){
        //direction can be one of four values: 0 is up, 1 is right, 2 is left, 3 is down
        //return true if and only if the player reached the exit.
        Point target = Move.translate(player.origin, direction, true);

        if(target.x < 0 || target.x >= width || target.y < 0 || target.y >= height){
            return false;
        }

        if(freeCells[target.y][target.x]){
            Point oldPlayerOrigin = new Point(player.origin);
            byte secondLastMove = player.getLastMove();
            Point[] conditionalChangeCells = new Point[PivotWall.count];
            int count = 0;

            if(player.move(direction)) {
                for (MazeObject foo : mazeObjects) {
                    if (foo instanceof PivotWall) {
                        Point[][] updateInfo = ((PlayerMoveUpdatable) foo).update(oldPlayerOrigin, player.origin, player.getLastMove(), secondLastMove);
                        count = handleUpdate(conditionalChangeCells, count, updateInfo);
                    }
                }
            }else{
                //oldPlayerOrigin = getTarget(player.getLastMove(), true);//might run into bugs if player reverses after being pushed
                for (MazeObject foo : mazeObjects) {
                    if (foo instanceof PivotWall) {
                        Point[][] updateInfo = ((PlayerMoveUpdatable) foo).reverse(oldPlayerOrigin, player.origin, player.getLastMove(), player.getLastMove());
                        count = handleUpdate(conditionalChangeCells, count, updateInfo);
                    }
                }
            }
            conditionalChangeCells = Arrays.copyOf(conditionalChangeCells, count);
            for(Point conditionalChangeCell : conditionalChangeCells){
                if(conditionalChangeCell.equals(player.origin)){
                    conditionalChangeCell = Move.translate(conditionalChangeCell, player.getLastMove(), false);
                    freeCells[conditionalChangeCell.y][conditionalChangeCell.x] = !freeCells[conditionalChangeCell.y][conditionalChangeCell.x];
                    break;
                }
            }

        }
        return player.origin.equals(exit.origin);
    }

    private int handleUpdate(Point[] conditionalChangeCells, int count, Point[][] updateInfo) {
        for (Point changedCell : updateInfo[0]) {
            freeCells[changedCell.y][changedCell.x] = !freeCells[changedCell.y][changedCell.x];
        }
        if(updateInfo[1].length > 0){
            conditionalChangeCells[count] = updateInfo[1][0];
            count++;
        }
        return count;
    }

    public String toString(){
        //temporary toString method used for testing
        char wallCell = '⬛';
        char[][] printedMaze = new char[height + 2][width + 2];
        for(int i = 0; i < width+2; i++){
            printedMaze[0][i] = wallCell;
            printedMaze[height+1][i] = wallCell;
        }
        for(int i = 1; i < height+1; i++){
            printedMaze[i][0] = wallCell;
            printedMaze[i][width+1] = wallCell;
        }
        for(int row = 1; row < freeCells.length + 1; row++){
            for(int col = 1; col < freeCells[0].length + 1; col++){
                if(!freeCells[row-1][col-1]){
                    printedMaze[row][col] = wallCell;
                }else{
                    printedMaze[row][col] = '⬜';
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
        if(player.origin.equals(exit.origin)){
            printedMaze[player.origin.y+1][player.origin.x+1] = '⯍';
        }else{
            printedMaze[player.origin.y+1][player.origin.x+1] = '⯌';
        }

        StringBuilder toPrint = new StringBuilder();
        for(int row = 0; row < height+2; row++){
            for(int col = 0; col < width+2; col++){
                toPrint.append(printedMaze[row][col]);
                toPrint.append(' ');
            }
            toPrint.append('\n');
        }
        toPrint.append("Last Move: ");
        switch (player.getLastMove()){
            case (0) -> toPrint.append("\uD83E\uDC1D");
            case (1) -> toPrint.append("\uD83E\uDC1E");
            case (2) -> toPrint.append("\uD83E\uDC1C");
            case (3) -> toPrint.append("\uD83E\uDC1F");
        }
        toPrint.append('\n');
        return toPrint.toString();
    }
}