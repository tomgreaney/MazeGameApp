import java.awt.Point;

public class Move {
    //Class used for performing calculations on points and moves.
    private Move(){

    }

    public static Point translate(Point origin, byte lastMove, boolean fromOrigin){
        if(fromOrigin){
            lastMove = (byte)(lastMove ^ 0x03);
        }
        return switch (lastMove){
            case (0) -> new Point(origin.x, origin.y+1);
            case (1) -> new Point(origin.x-1, origin.y);
            case (2) -> new Point(origin.x+1, origin.y);
            default -> new Point(origin.x, origin.y-1);
        };
    }

    public static Point midPoint(Point p1, Point p2){
        return new Point((p1.x + p2.x)/2, (p1.y + p2.y)/2);
    }

    public static byte getMove(Point from, Point to){
        return (byte) ((from.x == to.x)? ((from.y < to.y)? 3 : 0) : ((from.x < to.x)? 1 : 2));
    }
}