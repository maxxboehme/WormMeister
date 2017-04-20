import java.awt.Point;
import java.util.LinkedList;
import java.util.Random;

public class EnemySnake extends Snake {

    EnemySnake(Point head, Direction d, int length){
        super(head, d, length);
    }

    public Direction getChosenDirection(BoardPanel board){
        Point newHead = this.getHeadLocation();
        Random r = new Random();
        TileType ahead = EnemySnake.getTilTypeAhead(board, newHead, this.lastDirection);
        if(ahead == TileType.Exiting){
            return this.lastDirection;
        } else if(ahead == null){
            if(r.nextInt(10) > 0){
                return this.lastDirection;
            }
        }

        boolean choice = r.nextBoolean();
        if(this.lastDirection == Direction.North || this.lastDirection == Direction.South){
            if(choice){
                if(EnemySnake.getTilTypeAhead(board, newHead, Direction.West) == null){
                    return Direction.West;
                } else if(EnemySnake.getTilTypeAhead(board, newHead, Direction.East) == null){
                    return Direction.East;
                }
            } else {
                if(EnemySnake.getTilTypeAhead(board, newHead, Direction.East) == null){
                    return Direction.East;
                } else if(EnemySnake.getTilTypeAhead(board, newHead, Direction.West) == null){
                    return Direction.West;
                }
            }
        } else {
            if(choice){
                if(EnemySnake.getTilTypeAhead(board, newHead, Direction.North) == null){
                    return Direction.North;
                } else if(EnemySnake.getTilTypeAhead(board, newHead, Direction.South) == null){
                    return Direction.South;
                }
            } else {
                if(EnemySnake.getTilTypeAhead(board, newHead, Direction.South) == null){
                    return Direction.South;
                } else if(EnemySnake.getTilTypeAhead(board, newHead, Direction.North) == null){
                    return Direction.North;
                }
            }
        }

        return this.lastDirection;
    }

    public void reTileSnake(BoardPanel board){
        for(Point p: this.snake){
            board.setTile(p, TileType.EnemySnakeBody);
        }
        Point head = this.snake.peek();
        switch(this.lastDirection){
            case East:
                board.setTile(head, TileType.EnemySnakeHeadEast);
                break;
            case North:
                board.setTile(head, TileType.EnemySnakeHeadNorth);
                break;
            case South:
                board.setTile(head, TileType.EnemySnakeHeadSouth);
                break;
            case West:
                board.setTile(head, TileType.EnemySnakeHeadWest);
                break;
            default:
                break;
        }
    }

    public void removeFromBoard(BoardPanel board){
        for(Point p: this.snake){
            board.setTile(p, null);
        }
    }

    public static TileType getTilTypeAhead(BoardPanel board, Point h, Direction direction){
        Point head = (Point)h.clone();
        switch(direction) {
            case North:
                head.y--;
                break;

            case South:
                head.y++;
                break;

            case West:
                head.x--;
                break;

            case East:
                head.x++;
                break;
        }

        if(head.x < 0 || head.x >= BoardPanel.COL_COUNT || head.y < 0 || head.y >= BoardPanel.ROW_COUNT) {
            return TileType.Exiting;
        }

        return board.getTile(head.x, head.y);
    }

    /**
     * Updates the snake's position and size.
     * @return Tile tile that the head moved into.
     */
    public TileType updateSnake(BoardPanel board) {

        /*
         * Here we peek at the next direction rather than polling it. While
         * not game breaking, polling the direction here causes a small bug
         * where the snake's direction will change after a game over (though
         * it will not move).
         */
        Direction direction = this.lastDirection;
        if(!this.directions.isEmpty()){
            direction = this.directions.poll();
            this.lastDirection = direction;
        }

        /*
         * Here we calculate the new point that the snake's head will be at
         * after the update.
         */		
        Point head = new Point(snake.peekFirst());
        switch(direction) {
            case North:
                head.y--;
                break;

            case South:
                head.y++;
                break;

            case West:
                head.x--;
                break;

            case East:
                head.x++;
                break;
        }

        /*
         * If the snake has moved out of bounds ('hit' a wall), we can just
         * return that it's collided with itself, as both cases are handled
         * identically.
         */
        if(head.x < 0 || head.x >= BoardPanel.COL_COUNT || head.y < 0 || head.y >= BoardPanel.ROW_COUNT) {
            Point p = snake.peekFirst();
            board.setTile(p.x,  p.y, TileType.EnemySnakeBody);
            Point tail = snake.removeLast();
            board.setTile(tail, null);
            if(snake.isEmpty()){
                return TileType.Exit;
            } else {
                return TileType.Exiting;
            }
        }

        TileType old = board.getTile(head.x, head.y);
        if(old != null && old.isNotOk()){
            return TileType.Wall;
        }

        //		if(board.getTile(head.x, head.y) == TileType.Exit){
        //			return TileType.Exit;
        //		}

        /*
         * Here we get the tile that was located at the new head position and
         * remove the tail from of the snake and the board if the snake is
         * long enough, and the tile it moved onto is not a fruit.
         * 
         * If the tail was removed, we need to retrieve the old tile again
         * incase the tile we hit was the tail piece that was just removed
         * to prevent a false game over.
         */
        //		TileType old = board.getTile(head.x, head.y);
        //		if(old != TileType.Fruit && snake.size() > MIN_SNAKE_LENGTH) {
        //			Point tail = snake.removeLast();
        //			board.setTile(tail, null);
        //			old = board.getTile(head.x, head.y);
        //		}

        if(snake.size() > this.snakeLength) {
            Point tail = snake.removeLast();
            board.setTile(tail, null);
            old = board.getTile(head.x, head.y);
        }

        /*
         * Update the snake's position on the board if we didn't collide with
         * our tail:
         * 
         * 1. Set the old head position to a body tile.
         * 2. Add the new head to the snake.
         * 3. Set the new head position to a head tile.
         * 
         * If more than one direction is in the queue, poll it to read new
         * input.
         */
        board.setTile(snake.peekFirst(), TileType.EnemySnakeBody);
        snake.push(head);
        switch(direction){
            case East:
                board.setTile(head, TileType.EnemySnakeHeadEast);
                break;
            case North:
                board.setTile(head, TileType.EnemySnakeHeadNorth);
                break;
            case South:
                board.setTile(head, TileType.EnemySnakeHeadSouth);
                break;
            case West:
                board.setTile(head, TileType.EnemySnakeHeadWest);
                break;
            default:
                break;
        }

        return old;
    }
}
