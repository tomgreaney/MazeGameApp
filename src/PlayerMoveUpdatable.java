import java.awt.Point;

public interface PlayerMoveUpdatable {
    //Interface to be used on MazeObjects which get updated in a manner influenced by how the player moves, and
    // also influences the players movement, eg. PivotWall class.

    Point[] update(Point playerOrigin, Point oldPlayerOrigin);
    /*
    Method returns an array of points which correspond to grid cells on the maze.
    If the grid cell was previously accessible, then it becomes inaccessible and vice versa.
    */

    Point[] reverse(Point playerOrigin, Point oldPlayerOrigin);
    //Method reverts
}
