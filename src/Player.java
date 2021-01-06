public class Player extends MazeObject{
    private final MoveMemory moveMemory;

    public Player(int x, int y){
        super(x, y);
        moveMemory = new MoveMemory();
    }

    public boolean move(byte direction){
        if(direction % 3 == 0){
            this.origin.y += (direction == 3)? 1 : -1;
        }else{
            this.origin.x += (direction == 1)? 1 : -1;
        }
        return moveMemory.move(direction);
    }

    public byte getLastMove(){
        return moveMemory.getLastMove();
    }
}