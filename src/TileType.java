/**
 * 
 * @author Maxx Boehme
 *
 */
public enum TileType {
	Fruit, SnakeHead, SnakeBody, Wall, Exiting, Exit, GoldFruit, EnemySnakeBody, EnemySnakeHeadNorth, EnemySnakeHeadSouth, EnemySnakeHeadEast, EnemySnakeHeadWest, Test;
	
	public boolean isEnemySnake(){
		return this == EnemySnakeBody || this == EnemySnakeHeadNorth || this == EnemySnakeHeadSouth || this == EnemySnakeHeadEast || this == EnemySnakeHeadWest;
	}
	
	public boolean isEnemySnakeHead(){
		return this == EnemySnakeHeadNorth || this == EnemySnakeHeadSouth || this == EnemySnakeHeadEast || this == EnemySnakeHeadWest;
	}
	
	public boolean isSnake(){
		return this == SnakeBody || this == SnakeHead;
	}
	
	public boolean isOK(){
		return this == Fruit || this == GoldFruit;
	}
	
	public boolean isNotOk(){
		return this == Wall || this.isSnake() || this.isEnemySnake();
	}
	
	
}
