import java.util.Stack;

public class MoveMemory {
    /*
    //Class used to keep track of the movement of MazeObjects which move by a fixed number of cells
    //latestMovement byte is a buffer for the last 32 moves:
    |    2-bits   |    2-bits    |    2-bits    |    2-bits    |...
    32nd last move,31st last move,30th last move,29th last move,...
    00: moved up
    01: moved right
    10: moved left
    11: moved down
     */
    private Stack<Long> movements;
    private long latestMovement;
    private int noOfMoves; // number of moves stored in latestMovement.

    public MoveMemory(){
        wipeMemory();
    }

    public boolean move(byte newMove){
        //Method returns false if and only if the new move cancels out the previous move (eg. new move = up, last move = down)
        if(noOfMoves == -1){
            noOfMoves = 0;
            latestMovement = newMove;
            return true;
        }

        byte lastMove = getLastMove();

        if((lastMove ^ newMove) == 0x03){
            //new Move cancels previous move.
            noOfMoves = (noOfMoves + 31) % 32;
            if(noOfMoves == 31){
                //latestMoves is empty and must be replaced
                if(movements.empty()){
                    //moved to the mazes starting square,
                    noOfMoves = -1;
                }else{
                    latestMovement = movements.pop();
                }
            }else{
                latestMovement = latestMovement >> 2;
            }
            return false;
        }else{
            noOfMoves = (noOfMoves + 1) % 32;
            if(noOfMoves == 0){
                //latestMovement is full
                movements.push(latestMovement);
                latestMovement = newMove;
            }else{
                latestMovement = (latestMovement << 2) | newMove;
            }
            return true;
        }
    }

    public void wipeMemory(){
        //To be called when MoveMemory is initialised, or when the MazeObject cannot reverse it's move
        noOfMoves = -1;
        movements = new Stack<>();
    }

    public byte getLastMove(){
        return (byte) (latestMovement & 0x03);
    }
}