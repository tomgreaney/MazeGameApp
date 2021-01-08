import java.awt.Point;

public class Exit extends MazeObject implements Printable{
    public Exit(int x, int y){
        super(x, y);
    }

    public Point[] getCoveredCells(){
        return new Point[]{this.origin};
    }

    public char getPrintChar(){
        return 'U';
    }
}
