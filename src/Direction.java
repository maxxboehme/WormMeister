import java.awt.Point;

/**
 * 
 * @author Maxx Boehme
 *
 */
public enum Direction {
    North, South, East, West;

    public static Direction getDirection(Point p1, Point p2){
        if(p1.x > p2.x && p1.y == p2.y){
            return Direction.West;
        } else if(p1.x < p2.x && p1.y == p2.y){
            return Direction.East;
        } else if(p1.y < p2.y && p1.x == p2.x){
            return Direction.South;
        } else if(p1.y > p2.y && p1.x == p2.x){
            return Direction.North;
        } else {
            return null;
        }
    }
}
