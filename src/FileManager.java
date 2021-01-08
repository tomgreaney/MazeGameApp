import java.io.*;
import java.util.ArrayList;

public class FileManager {
    private FileManager(){

    }

    public static Maze parseMaze(String fileName){
        Maze maze = null;
        FileReader fr;
        try {
            fr = new FileReader(fileName);
        } catch (FileNotFoundException ex) {
            System.out.println("FileReader FileNotFoundException: " + ex.getMessage());
            return null;
        }

        try (BufferedReader br = new BufferedReader(fr)) {
            String line;
            String[] mazeValues = getLineValues(br.readLine(), 7);
            int width = Integer.parseInt(mazeValues[0]);
            int height = Integer.parseInt(mazeValues[1]);
            int playerX = Integer.parseInt(mazeValues[2]);
            int playerY = Integer.parseInt(mazeValues[3]);
            int exitX = Integer.parseInt(mazeValues[4]);
            int exitY = Integer.parseInt(mazeValues[5]);
            boolean[][] freeCells = getFreeCells(mazeValues[6], width, height);
            ArrayList<MazeObject> mazeObjects = new ArrayList<>();
            while((line = br.readLine()) != null && !line.isEmpty()) {
                switch (line) {
                    case "0001" -> mazeObjects.add(parsePivotWall(br.readLine()));
                }
            }
            maze = new Maze(width, height, mazeObjects, playerX, playerY, freeCells, new Exit(exitX, exitY));
        }catch(IOException ex) {
            System.out.println("IOException: " + ex.getMessage());
        } catch(NullPointerException ex) {
            System.out.println("NullPointerException: " + ex.getMessage());
        }
        return maze;
    }

    private static PivotWall parsePivotWall(String line){
        String[] pivotWallValues = getLineValues(line, 8);
        return new PivotWall(Integer.parseInt(pivotWallValues[0]), Integer.parseInt(pivotWallValues[1]), Integer.parseInt(pivotWallValues[2]), Integer.parseInt(pivotWallValues[3]), pivotWallValues[5].equals("0"), Integer.parseInt(pivotWallValues[4]), pivotWallValues[6].equals("0"), pivotWallValues[7].equals("0"));
    }

    private static String[] getLineValues(String line, int numberOfValues){
        String[] lineValues = new String[numberOfValues];
        int lastCommaIndex = 0;
        int nextCommaIndex = line.indexOf(',',lastCommaIndex);
        for(int i = 0; i < numberOfValues; i++){
           lineValues[i] = line.substring(lastCommaIndex, nextCommaIndex);
           lastCommaIndex = nextCommaIndex+1;
           nextCommaIndex = line.indexOf(',',lastCommaIndex);
        }
        return lineValues;
    }

    private static boolean[][] getFreeCells(String info, int width, int height){
        boolean[][] freeCells = new boolean[height][width];
        int counter = 0;
        for(int row = 0; row < height; row++){
            for(int col = 0; col < width; col++){
                freeCells[row][col] = info.charAt(counter) == '0';
                counter++;
            }
        }
        return freeCells;
    }
}