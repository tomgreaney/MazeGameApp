import java.awt.Point;

public class PivotWall extends MazeObject implements PlayerMoveUpdatable, Printable {
    //A maze object which represents a rotating wall
    public static int count = 0;
    private Point reverseCell;
    private Point endPoint;
    private final Point nextEndPoint;
    private final int sequenceLength;
    private int reverseState;//0 means the sequence doesn't reverse, 1 means the sequence is not reversing, -1 means the sequence is reversing
    private int sequenceStep;
    private boolean clockWise;
    private final boolean changesPivot;

    public PivotWall(int xOrigin, int yOrigin, int endX, int endY, boolean clockWise, int sequenceLength, boolean changesPivot, boolean reversible){
        super(xOrigin, yOrigin);
        this.endPoint = new Point(endX, endY);
        this.clockWise = clockWise;
        this.sequenceLength = sequenceLength;
        this.changesPivot = changesPivot;
        if(reversible){
            this.reverseState = 1;
            sequenceStep = 0;
        }else{
            this.reverseState = 0;
        }
        this.nextEndPoint = new Point(endPoint);
        setNextEndPoint();
        count++;
    }

    public Point[][] reverse(Point oldPlayerOrigin, Point playerOrigin, byte lastMove, byte secondLastMove){
        Point[][] changedCells;
        setReverse();
        changedCells = update(oldPlayerOrigin, playerOrigin, lastMove, secondLastMove);
        setReverse();
        return changedCells;
    }

    private void setReverse(){
        if(reverseState == 0) {
            changePivot();
            clockWise = !clockWise;
        }else {
            if(((sequenceStep != 0 || reverseState != 1) && (sequenceStep != sequenceLength-1 || reverseState != -1))){
                //not at the end or start of the sequence
                changePivot();
                clockWise = !clockWise;
            }
            reverseState = reverseState * -1;
        }
        setNextEndPoint();
    }

    public Point[][] update(Point oldPlayerOrigin, Point playerOrigin, byte lastMove, byte secondLastMove){
        Point[] changedCells, conditionalChanges;
        Point wallCell = Move.midPoint(endPoint, origin);//Point which wall blocks
        Point nextWallCell = Move.midPoint(nextEndPoint, origin);//Point which wall will block

        if(oldPlayerOrigin.equals(reverseCell) && (wallCell.equals(Move.translate(oldPlayerOrigin, secondLastMove, false)) || playerOrigin.equals(wallCell))){
            changedCells = new Point[]{nextWallCell};
        }else{
            changedCells = new Point[]{wallCell, nextWallCell};
        }

        reverseCell = Move.midPoint(endPoint, nextEndPoint);

        if(playerOrigin.equals(reverseCell) || playerOrigin.equals(nextWallCell)){
            if(nextEndPoint.x == this.origin.x){
                playerOrigin.y = nextWallCell.y;
                playerOrigin.x = (nextEndPoint.x > endPoint.x)? nextEndPoint.x + 1 : nextEndPoint.x - 1;
            }else{
                playerOrigin.x = nextWallCell.x;
                playerOrigin.y = (nextEndPoint.y > endPoint.y)? nextEndPoint.y + 1 : nextEndPoint.y - 1;
            }
        }

        boolean sameDirection = lastMove == Move.getMove(Move.midPoint(endPoint, origin), origin);

        this.pivot();

        //wallCell = Move.midPoint(endPoint, origin);
        reverseCell = Move.midPoint(endPoint, nextEndPoint);
        if(sameDirection && playerOrigin.equals(reverseCell)){
            conditionalChanges = new Point[]{reverseCell};
        }else{
            conditionalChanges = new Point[]{};
        }

        if (reverseState != 0) {
            updateSequence();
        }

        return new Point[][]{changedCells, conditionalChanges};
    }

    private void pivot(){
        //rotates the pivot wall
        endPoint = new Point(nextEndPoint);
        changePivot();
        setNextEndPoint();
    }

    private void setNextEndPoint(){
        Point offSet = new Point((endPoint.x - origin.x),(endPoint.y-origin.y));

        if(offSet.x == 0){
            offSet.x = (clockWise)? offSet.y * -1 : offSet.y;
            offSet.y = 0;
        }else{
            offSet.y = (clockWise)? offSet.x : offSet.x * -1;
            offSet.x = 0;
        }

        nextEndPoint.x = origin.x + offSet.x;
        nextEndPoint.y = origin.y + offSet.y;
    }

    private void changePivot(){
        if(changesPivot) {
            Point foo = new Point(origin);
            origin = new Point(endPoint);
            endPoint = foo;
        }
    }

    private void updateSequence(){
        //updates the sequence state of a reversible PivotWall
        if((sequenceStep == 0 && reverseState == -1) || (sequenceStep == sequenceLength-1 && reverseState == 1)){
            reverseState = reverseState * -1;
            clockWise = !clockWise;
            if(changesPivot){
                changePivot();
            }
            setNextEndPoint();
        }else{
            if(reverseState == 1){
                sequenceStep++;
            }else{
                sequenceStep--;
            }
        }
    }

    public Point[] getCoveredCells(){
        Point[] coveredPoints = new Point[3];
        coveredPoints[0] = origin;
        coveredPoints[1] = new Point((origin.x + endPoint.x)/2, (origin.y + endPoint.y)/2);
        coveredPoints[2] = endPoint;
        return coveredPoints;
    }

    public char getPrintChar(){
        return '⯁';
    }
}