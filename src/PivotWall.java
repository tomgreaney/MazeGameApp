import java.awt.Point;

public class PivotWall extends MazeObject implements PlayerMoveUpdatable, Printable {
    //A maze object which represents a rotating wall
    private boolean reverseAllowed;
    private boolean isReversing;
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
            sequenceStep = 1;
        }else{
            this.reverseState = 0;
        }
        this.nextEndPoint = new Point(endPoint);
        setNextEndPoint();
        isReversing = false;
        reverseAllowed = false;
    }

    public Point[] reverse(Point playerOrigin, Point oldPlayerOrigin){
        isReversing = true;
        Point[] changedCells;
        setReverse();
        setNextEndPoint();
        changePivot();
        changedCells = update(playerOrigin, oldPlayerOrigin);
        setReverse();
        changePivot();
        setNextEndPoint();
        isReversing = false;
        return changedCells;
    }

    private void setReverse(){
        if(reverseState == 0){
            clockWise = !clockWise;
        }else{
            reverseState = reverseState * -1;
        }
    }

    public Point[] update(Point playerOrigin, Point oldPlayerOrigin) {
        Point center = new Point((this.endPoint.x + this.nextEndPoint.x) / 2, (this.endPoint.y + this.nextEndPoint.y) / 2);//Point between current endPoint and nextEndPoint
        Point wallCell = new Point(((endPoint.x + origin.x) / 2), (endPoint.y + origin.y) / 2);//Point which wall blocks
        Point nextWallCell = new Point(((nextEndPoint.x + origin.x) / 2), (nextEndPoint.y + origin.y) / 2);//Point which wall will block
        Point[] changedGridCells = {wallCell, nextWallCell};
        boolean reverseWasAllowed = reverseAllowed;

        if(reverseWasAllowed){
            if(isReversing && !playerOrigin.equals(wallCell)){
                changedGridCells = new Point[0];
            }else {
                changedGridCells = new Point[1];
                changedGridCells[0] = nextWallCell;
            }
        }
        reverseAllowed = false;//the player can reverse on their next move


        if(playerOrigin.equals(center) || playerOrigin.equals(nextWallCell) || oldPlayerOrigin.equals(nextWallCell)){
            int xOffset = wallCell.x - this.origin.x;
            int yOffset = wallCell.y - this.origin.y;
            int xPlayerOffset = oldPlayerOrigin.x - playerOrigin.x;
            int yPlayerOffset = oldPlayerOrigin.y - playerOrigin.y;
            if(xOffset == xPlayerOffset && yOffset == yPlayerOffset){
                reverseAllowed = true;
                if(reverseWasAllowed && !isReversing) {
                    changedGridCells = new Point[0];
                }else{
                    changedGridCells = new Point[1];
                    changedGridCells[0] = wallCell;
                }
            }
            if(nextEndPoint.x == this.origin.x){
                playerOrigin.y = nextWallCell.y;
                playerOrigin.x = (nextEndPoint.x > endPoint.x)? nextEndPoint.x + 1 : nextEndPoint.x - 1;
            }else{
                playerOrigin.x = nextWallCell.x;
                playerOrigin.y = (nextEndPoint.y > endPoint.y)? nextEndPoint.y + 1 : nextEndPoint.y - 1;
            }
        }

        this.pivot();

        if (reverseState != 0) {
            updateSequence();
        }

    return changedGridCells;
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
                setNextEndPoint();
            }
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
        return 'S';
    }
}