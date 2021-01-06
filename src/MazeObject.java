import java.awt.Point;

public abstract class MazeObject {
    public Point origin;

    public MazeObject(int x, int y){
        this.origin = new Point(x, y);
    }
}