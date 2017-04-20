import java.awt.Point;
import java.util.LinkedList;


public class Snake {

	protected LinkedList<Point> snake;

	protected int snakeLength;

	protected Direction lastDirection;

	protected LinkedList<Direction> directions;

	/*
	 * The maximum number of directions that we can have polled in the
	 * direction list.
	 */
	private static final int MAX_DIRECTIONS = 3;

	Snake(Point head, Direction d, int length){
		snake = new LinkedList<Point>();
		snake.push(head);
		snakeLength = length;
		this.directions = new LinkedList<Direction>();
		lastDirection = d;
	}

	public void pushDirection(Direction d){
		if(directions.size() < MAX_DIRECTIONS) {
			Direction last = this.lastDirection;
			if(!this.directions.isEmpty()){
				last = this.directions.peekLast();
			}
			if(d == Direction.West || d == Direction.East){
				if(last != Direction.West && last != Direction.East) {
					directions.addLast(d);
				}
			} else {
				if(last != Direction.North && last != Direction.South) {
					directions.addLast(d);
				}
			}
		}
	}

	public int size(){
		return this.snake.size();
	}

	public Direction getCurrentDirection(){
		return this.lastDirection;
	}

	public void increaseLength(int n){
		snakeLength += n;
	}

	public Point getHeadLocation(){
		return (Point)this.snake.peek().clone();
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
			board.setTile(p.x,  p.y, TileType.SnakeBody);
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
			return TileType.SnakeBody;
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
		board.setTile(snake.peekFirst(), TileType.SnakeBody);
		snake.push(head);
		board.setTile(head, TileType.SnakeHead);

		return old;
	}


	//	/**
	//	 * Updates the snake's position and size.
	//	 * @return Tile tile that the head moved into.
	//	 */
	//	private TileType updateSnake() {
	//
	//		/*
	//		 * Here we peek at the next direction rather than polling it. While
	//		 * not game breaking, polling the direction here causes a small bug
	//		 * where the snake's direction will change after a game over (though
	//		 * it will not move).
	//		 */
	//		Direction direction = directions.peekFirst();
	//
	//		/*
	//		 * Here we calculate the new point that the snake's head will be at
	//		 * after the update.
	//		 */		
	//		Point head = new Point(snake.peekFirst());
	//		switch(direction) {
	//		case North:
	//			head.y--;
	//			break;
	//
	//		case South:
	//			head.y++;
	//			break;
	//
	//		case West:
	//			head.x--;
	//			break;
	//
	//		case East:
	//			head.x++;
	//			break;
	//		}
	//
	//		/*
	//		 * If the snake has moved out of bounds ('hit' a wall), we can just
	//		 * return that it's collided with itself, as both cases are handled
	//		 * identically.
	//		 */
	//		if(head.x < 0 || head.x >= BoardPanel.COL_COUNT || head.y < 0 || head.y >= BoardPanel.ROW_COUNT) {
	//			Point p = snake.peekFirst();
	//			board.setTile(p.x,  p.y, TileType.SnakeBody);
	//			Point tail = snake.removeLast();
	//			board.setTile(tail, null);
	//			if(snake.isEmpty()){
	//				return TileType.Exit;
	//			} else {
	//				return TileType.Exiting;
	//			}
	//		}
	//
	//		TileType temp = board.getTile(head.x, head.y);
	//		if(temp == TileType.Wall || (temp != null && temp.isEnemySnake())){
	//			return TileType.SnakeBody;
	//		}
	//
	//		//		if(board.getTile(head.x, head.y) == TileType.Exit){
	//		//			return TileType.Exit;
	//		//		}
	//
	//		/*
	//		 * Here we get the tile that was located at the new head position and
	//		 * remove the tail from of the snake and the board if the snake is
	//		 * long enough, and the tile it moved onto is not a fruit.
	//		 * 
	//		 * If the tail was removed, we need to retrieve the old tile again
	//		 * incase the tile we hit was the tail piece that was just removed
	//		 * to prevent a false game over.
	//		 */
	//		TileType old = board.getTile(head.x, head.y);
	//		//		if(old != TileType.Fruit && snake.size() > MIN_SNAKE_LENGTH) {
	//		//			Point tail = snake.removeLast();
	//		//			board.setTile(tail, null);
	//		//			old = board.getTile(head.x, head.y);
	//		//		}
	//
	//		if(snake.size() > this.snakeLength) {
	//			Point tail = snake.removeLast();
	//			board.setTile(tail, null);
	//			old = board.getTile(head.x, head.y);
	//		}
	//
	//		/*
	//		 * Update the snake's position on the board if we didn't collide with
	//		 * our tail:
	//		 * 
	//		 * 1. Set the old head position to a body tile.
	//		 * 2. Add the new head to the snake.
	//		 * 3. Set the new head position to a head tile.
	//		 * 
	//		 * If more than one direction is in the queue, poll it to read new
	//		 * input.
	//		 */
	//		if(old != TileType.SnakeBody) {
	//			board.setTile(snake.peekFirst(), TileType.SnakeBody);
	//			snake.push(head);
	//			board.setTile(head, TileType.SnakeHead);
	//			if(directions.size() > 1) {
	//				directions.poll();
	//			}
	//		}
	//
	//		return old;
	//	}
}
