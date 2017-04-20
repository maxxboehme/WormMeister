/**
 *  @author Maxx Boehme
 */

import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

import javax.swing.JPanel;


public class SnakeGame extends JPanel {

    /*
     * The Serial Version UID.
     */
    private static final long serialVersionUID = 1890317935063860966L;


    public int FRAMES_PER_SECOND = 8;
    /**
     * The number of milliseconds that should pass between each frame.
     */
    private long FRAME_TIME = 1000L / FRAMES_PER_SECOND;

    /*
     * The minimum length of the snake. This allows the snake to grow
     * right when the game starts, so that we're not just a head moving
     * around on the board.
     */
    private static final int MIN_SNAKE_LENGTH = 5;

    /*
     * The BoardPanel instance.
     */
    private BoardPanel board;

    /*
     * The TopPanel instance.
     */
    private TopPanel top;

    /*
     * The BottomPanel instance.
     */
    private BottomPanel bottom;

    /*
     * The random number generator (used for spawning fruits).
     */
    private Random random;

    /*
     * Whether or not we're running a new game.
     */
    private GameState currentState;

    private ArrayList<EnemySnake> enemySnakes;

    private ArrayList<Point> fruitLocations;

    /*
     * The current score.
     */
    private int score;

    private int lifeScore;

    /*
     * The number of fruits that we've eaten.
     */
    private int fruitsEaten;

    private int currentLevel;

    private ArrayList<Level> levels;

    private int levelTimer;

    public int FRUITS_PER_LEVEL = 15;

    public int TIME_PER_LEVEL = 30 * (3*FRUITS_PER_LEVEL / 4);

    private static final int LIVES_TO_START = 3;

    private int livesLeft;

    private static final int SNAKE_GROWTH_RATE = 4;

    private static final int SETUP_TIME = 5;

    private int setUpTimer;

    private int THE_GOLDEN_RATIO = 10;

    private PathFinder pf;

    /*
     * The number of points that the next fruit will award us.
     */
    private int nextFruitScore;

    private Snake playerSnake;

    private int lawnchedSnakeEnemys = 0;

    private int count = 0;

    /*
     * Creates a new SnakeGame instance. Creates a new window,
     * and sets up the controller input.
     */
    SnakeGame() {
        setLayout(new BorderLayout());

        pf = new PathFinder();
        /*
         * Initialize the game's panels and add them to the window.
         */
        this.board = new BoardPanel(this);
        this.top = new TopPanel(this);
        this.bottom = new BottomPanel(this);

        enemySnakes = new ArrayList<EnemySnake>();
        fruitLocations = new ArrayList<Point>();

        fruitsEaten = 0;
        currentLevel = 0;
        levels = this.generateLevels();
        levelTimer = this.TIME_PER_LEVEL;

        add(board, BorderLayout.CENTER);
        add(top, BorderLayout.NORTH);
        //		add(bottom, BorderLayout.SOUTH);

        setVisible(true);
    }

    public void pause(){
        if(currentState == GameState.Running) {
            currentState = GameState.Paused;
        } else if(currentState == GameState.Paused){
            currentState = GameState.Running;
        }
    }

    public void enter() {
        if(currentState == GameState.NewGame || currentState == GameState.Won
                || currentState == GameState.GameOver) {
            resetGame();
                }
    }

    public void turn(Direction d){
        if(currentState == GameState.Running) {
            this.playerSnake.pushDirection(d);
        }
    }

    /**
     * Starts the game running.
     */
    public void startGame() {
        /*
         * Initialize everything we're going to be using.
         */
        this.random = new Random();
        //this.updateClock = new UpdateClock(8.0f);
        board.boardFromLevel(this.levels.get(currentLevel));
        currentState = GameState.NewGame;

        /*
         * This is the game loop. It will update and render the game and will
         * continue to run until the game window is closed.
         */
        while(true) {
            //Get the current frame's start time.
            long start = System.currentTimeMillis();

            /*
             * If a cycle has elapsed on the logic timer, then update the game.
             */
            if(currentState == GameState.SetUp){
                this.setUpTimer--;
                if(this.setUpTimer < 0){
                    currentState = GameState.Running;
                }
            } else if(currentState == GameState.Running){
                updateGame();
            }

            //Repaint the board and side panel with the new content.
            board.repaint();
            top.repaint();

            /*
             * Calculate the delta time between since the start of the frame
             * and sleep for the excess time to cap the frame rate. While not
             * incredibly accurate, it is sufficient for our purposes.
             */
            long delta = (System.currentTimeMillis() - start);
            if(delta < FRAME_TIME) {
                try {
                    Thread.sleep(FRAME_TIME - delta);
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Updates the game's logic.
     */
    private void updateGame() {


        /*
         * Gets the type of tile that the head of the snake collided with. If 
         * the snake hit a wall, SnakeBody will be returned, as both conditions
         * are handled identically.
         */
        //		TileType collision = updateSnake();
        TileType collision = this.playerSnake.updateSnake(board);

        /*
         * Here we handle the different possible collisions.
         * 
         * Fruit: If we collided with a fruit, we increment the number of
         * fruits that we've eaten, update the score, and spawn a new fruit.
         * 
         * SnakeBody: If we collided with our tail (or a wall), we flag that
         * the game is over and pause the game.
         * 
         * If no collision occurred, we simply decrement the number of points
         * that the next fruit will give us if it's high enough. This adds a
         * bit of skill to the game as collecting fruits more quickly will
         * yield a higher score.
         */
        count++;
        boolean dontSkip = true;
        if(collision == TileType.Fruit) {
            //			this.snakeLength += SNAKE_GROWTH_RATE;
            this.playerSnake.increaseLength(SNAKE_GROWTH_RATE);
            fruitsEaten++;
            this.fruitLocations.remove(this.playerSnake.getHeadLocation());
            if(this.fruitLocations.size() == 0){
                board.setTile(new Point(BoardPanel.COL_COUNT / 2, 0), null);
            }
            score += 1;
            lifeScore += 1;
            this.testScore(lifeScore);
        } else if(collision == TileType.GoldFruit){
            //			this.snakeLength += SNAKE_GROWTH_RATE;
            this.playerSnake.increaseLength(SNAKE_GROWTH_RATE);
            //this.livesLeft++;
            this.fruitLocations.remove(this.playerSnake.getHeadLocation());
            fruitsEaten++;
            if(this.fruitLocations.size() == 0){
                board.setTile(new Point(BoardPanel.COL_COUNT / 2, 0), null);
            }
            score += 3;
            lifeScore += 3;
            this.testScore(lifeScore);
        } else if(collision == TileType.SnakeBody) {
            this.livesLeft--;
            if(this.livesLeft <= 0){
                this.currentState = GameState.GameOver;
            } else {
                this.resetLevel();
                dontSkip = false;
            }
        } else if(collision == TileType.Exit){
            FRAME_TIME = 1000L / (FRAMES_PER_SECOND);
            this.nextLevel();
            dontSkip = false;
        }

        if(board.getTile(BoardPanel.COL_COUNT / 2, BoardPanel.ROW_COUNT-1) == null){
            board.setTile(new Point(BoardPanel.COL_COUNT / 2, BoardPanel.ROW_COUNT-1), TileType.Wall);
        }

        if(collision != TileType.Exiting && dontSkip){
            int index = 0;
            boolean increment = true;
            if(this.lawnchedSnakeEnemys < this.levels.get(this.currentLevel).getNumberOfSnakes() && board.getTile(BoardPanel.COL_COUNT / 2, 0) == null || board.getTile(BoardPanel.COL_COUNT / 2, 0) == TileType.Wall){
                board.setTile(BoardPanel.COL_COUNT / 2, 0, TileType.Wall);
                this.lawnchedSnakeEnemys++;
            }
            for(int i = 0; i < this.enemySnakes.size() && i < this.lawnchedSnakeEnemys; i++){
                EnemySnake enemy = this.enemySnakes.get(i);
                pf.parseBoard(board);

                /*
                 * If there is fruits left
                 */
                if(this.fruitLocations.size() > 0){
                    LinkedList<Node> path = pf.findPath(enemy.getHeadLocation(), this.fruitLocations.get(index % this.fruitLocations.size()));
                    if(path.size() >= 2){

                        enemy.pushDirection(Direction.getDirection(enemy.getHeadLocation(), new Point(path.get(1).getX(), path.get(1).getY())));
                        TileType t = enemy.updateSnake(board);
                        if(t == TileType.Fruit || t == TileType.GoldFruit || t == TileType.Test){
                            this.fruitLocations.remove(enemy.getHeadLocation());
                            enemy.increaseLength(SnakeGame.SNAKE_GROWTH_RATE);
                            if(this.fruitLocations.size() == 0){
                                board.setTile(new Point(BoardPanel.COL_COUNT / 2, 0), null);
                            }
                        } else if(t == TileType.Wall || (t != null && t.isEnemySnake())){
                            enemy.removeFromBoard(board);
                            this.enemySnakes.remove(i);
                        }
                    } else {
                        enemy.pushDirection(enemy.getChosenDirection(board));
                        TileType t = enemy.updateSnake(board);
                        if(t == TileType.Fruit || t == TileType.GoldFruit || t == TileType.Test){
                            this.fruitLocations.remove(enemy.getHeadLocation());
                            enemy.increaseLength(SnakeGame.SNAKE_GROWTH_RATE);
                            if(this.fruitLocations.size() == 0){
                                board.setTile(new Point(BoardPanel.COL_COUNT / 2, 0), null);
                            }
                        } else if(t == TileType.Wall || (t != null && t.isEnemySnake())){
                            enemy.removeFromBoard(board);
                            this.enemySnakes.remove(i);
                        }
                    }
                    index++;

                } else {
                    LinkedList<Node> path = pf.findPath(enemy.getHeadLocation(), new Point(BoardPanel.COL_COUNT / 2, 0));
                    if(path.size() >= 2){
                        enemy.pushDirection(Direction.getDirection(enemy.getHeadLocation(), new Point(path.get(1).getX(), path.get(1).getY())));
                        TileType t = enemy.updateSnake(board);
                        if(t == TileType.Fruit || t == TileType.GoldFruit || t == TileType.Test){
                            this.fruitLocations.remove(enemy.getHeadLocation());
                            enemy.increaseLength(SnakeGame.SNAKE_GROWTH_RATE);
                            if(this.fruitLocations.size() == 0){
                                board.setTile(new Point(BoardPanel.COL_COUNT / 2, 0), null);
                            }
                        } else if(t == TileType.Wall || (t != null && t.isEnemySnake())){
                            enemy.removeFromBoard(board);
                            this.enemySnakes.remove(i);
                        }
                    } else {
                        enemy.pushDirection(enemy.getChosenDirection(board));
                        TileType t = enemy.updateSnake(board);
                        if(t == TileType.Exit){
                            board.setTile(new Point(BoardPanel.COL_COUNT / 2, 0), TileType.Wall);
                            this.generateFruits();
                            this.enemySnakes.remove(i);
                            this.levelTimer = this.TIME_PER_LEVEL;
                        } else if(t == TileType.Exiting){
                            increment = false;
                        } else if(t == TileType.Wall || (t != null && t.isEnemySnake())){
                            enemy.removeFromBoard(board);
                            this.enemySnakes.remove(i);
                        }
                    }
                }
            }

            if(this.lawnchedSnakeEnemys == this.enemySnakes.size() && board.getTile(BoardPanel.COL_COUNT / 2, 0) == null && this.levelTimer != 0){
                board.setTile(BoardPanel.COL_COUNT / 2, 0, TileType.Wall);
            }
            if(increment){
                this.levelTimer--;
            }
            if(this.levelTimer < 0){
                this.generateFruits();
                this.levelTimer = this.TIME_PER_LEVEL;
                board.setTile(new Point(BoardPanel.COL_COUNT / 2, 0), TileType.Wall);
            }
        }
        if(collision == TileType.Exiting){
            FRAME_TIME = 1000L / (4*FRAMES_PER_SECOND);
        }
    }

    private void testScore(int score){
        if(this.lifeScore >= 100){
            this.livesLeft++;
            this.lifeScore -= 100;
            testScore(this.lifeScore); // for
        }
    }


    /**
     * Resets the game's variables to their default states and starts a new game.
     */
    private void nextLevel() {

        this.score += 10;
        this.lifeScore += 10;
        this.testScore(this.lifeScore);

        /*
         * Create the head at the center of the board.
         */
        Point head = new Point(BoardPanel.COL_COUNT / 2, BoardPanel.ROW_COUNT-1);

        currentState = GameState.SetUp;

        /*
         * Clear the snake list and add the head.
         */

        playerSnake = new Snake(head, Direction.North, SnakeGame.MIN_SNAKE_LENGTH);
        playerSnake.pushDirection(Direction.North);


        /*
         * Logic to decide if we have won or need to move to next level
         */
        levelTimer = this.TIME_PER_LEVEL;
        if(currentLevel < levels.size()-1){
            currentLevel++;
        } else {
            currentState = GameState.Won;
        }

        /*
         * Create appropriate amount of enemy stakes for the level
         */
        this.lawnchedSnakeEnemys = 0;
        this.enemySnakes.clear();
        for(int i = 0; i < this.levels.get(this.currentLevel).getNumberOfSnakes(); i++){
            this.enemySnakes.add(new EnemySnake(new Point(BoardPanel.COL_COUNT / 2, 0), Direction.South, 4));
        }

        /*
         * Clear the board and add the head.
         */
        board.boardFromLevel(levels.get(currentLevel));
        board.setTile(head, TileType.SnakeHead);

        /*
         * Spawn a new fruit.
         */
        this.fruitLocations.clear();
        generateFruits();

        TIME_PER_LEVEL = SnakeGame.calculateTimePerLevel(FRUITS_PER_LEVEL, this.enemySnakes.size());
        levelTimer = this.TIME_PER_LEVEL;
    }

    /**
     * Resets the game's variables to their default states and starts a new game.
     */
    private void resetLevel() {
        this.score -= 5;
        this.lifeScore -= 5;

        /*
         * Create the head at the center of the board.
         */
        Point head = new Point(BoardPanel.COL_COUNT / 2, BoardPanel.ROW_COUNT-1);

        currentState = GameState.SetUp;

        this.setUpTimer = SnakeGame.SETUP_TIME;

        /*
         * Clear the snake list and add the head.
         */
        playerSnake = new Snake(head, Direction.North, SnakeGame.MIN_SNAKE_LENGTH);
        playerSnake.pushDirection(Direction.North);


        /*
         * Create appropriate amount of enemy snakes taking into accounts some may of already exited
         */
        this.lawnchedSnakeEnemys = 0;
        int snaksLeft = this.enemySnakes.size();
        this.enemySnakes.clear();
        for(int i = 0; i < snaksLeft; i++){
            this.enemySnakes.add(new EnemySnake(new Point(BoardPanel.COL_COUNT / 2, 0), Direction.South, 4));
        }

        /*
         * Clear the board and add the head.
         */
        board.boardFromLevel(levels.get(currentLevel));
        board.setTile(head, TileType.SnakeHead);

        /*
         * Spawn a new fruit.
         */
        this.fruitLocations.clear();
        generateFruits();

        TIME_PER_LEVEL = SnakeGame.calculateTimePerLevel(FRUITS_PER_LEVEL, this.enemySnakes.size());
        levelTimer = this.TIME_PER_LEVEL;
    }

    /**
     * Resets the game's variables to their default states and starts a new game.
     */
    private void resetGame() {
        /*
         * Reset the score statistics. (Note that nextFruitPoints is reset in
         * the spawnFruit function later on).
         */
        this.score = 0;
        this.lifeScore = 50;
        this.fruitsEaten = 0;

        /*
         * Reset both the new game and game over flags.
         */
        currentState = GameState.SetUp;

        this.setUpTimer = SnakeGame.SETUP_TIME;

        this.currentLevel = 0;

        this.livesLeft = SnakeGame.LIVES_TO_START;


        /*
         * Create the head at the lower center of the board.
         */
        Point head = new Point(BoardPanel.COL_COUNT / 2, BoardPanel.ROW_COUNT-1);

        /*
         * Create new Player Snake
         */
        playerSnake = new Snake(head, Direction.North, SnakeGame.MIN_SNAKE_LENGTH);
        playerSnake.pushDirection(Direction.North);

        /*
         * Create appropiate amout of enemy stakes for this level
         */
        this.lawnchedSnakeEnemys = 0;
        this.enemySnakes.clear();
        for(int i = 0; i < this.levels.get(this.currentLevel).getNumberOfSnakes(); i++){
            this.enemySnakes.add(new EnemySnake(new Point(BoardPanel.COL_COUNT / 2, 0), Direction.South, 4));
        }

        /*
         * Clear the board and add the head.
         */
        board.clearBoard();
        board.boardFromLevel(levels.get(currentLevel));
        board.setTile(head, TileType.SnakeHead);

        /*
         * Spawn a new fruit.
         */
        fruitLocations.clear();
        generateFruits();

        TIME_PER_LEVEL = SnakeGame.calculateTimePerLevel(FRUITS_PER_LEVEL, this.enemySnakes.size());
        levelTimer = this.TIME_PER_LEVEL;
    }

    private static int calculateTimePerLevel(int fruits, int enemySnakes){
        return (30 - (2* enemySnakes)) * ((3 * fruits) / 4);
    }

    /**
     * Gets the flag that indicates whether or not we're playing a new game.
     * @return The new game flag.
     */
    public boolean isNewGame() {
        return currentState == GameState.NewGame;
    }

    /**
     * Gets the flag that indicates whether or not the game is over.
     * @return The game over flag.
     */
    public boolean hasWon() {
        return currentState == GameState.Won;
    }

    /**
     * Gets the flag that indicates whether or not the game is over.
     * @return The game over flag.
     */
    public boolean isGameOver() {
        return currentState == GameState.GameOver;
    }

    /**
     * Gets the flag that indicates whether or not the game is over.
     * @return The game over flag.
     */
    public boolean isSettingUp() {
        return currentState == GameState.SetUp;
    }

    /**
     * Gets the flag that indicates whether or not the game is paused.
     * @return The paused flag.
     */
    public boolean isPaused() {
        return currentState == GameState.Paused;
    }

    private void generateFruits(){
        generateFruitHelper(0, this.FRUITS_PER_LEVEL, 0);
    }

    private void generateFruitHelper(int start, int end, int fruitsLeft){
        if(start < end){
            int maxEmpty = BoardPanel.COL_COUNT * BoardPanel.ROW_COUNT - this.playerSnake.size() - board.getNumberOfWalls()-start-fruitsLeft;
            int index = random.nextInt(maxEmpty);

            int isGold = random.nextInt(THE_GOLDEN_RATIO);

            int freeFound = -1;
            for(int x = 0; x < BoardPanel.COL_COUNT; x++) {
                for(int y = 0; y < BoardPanel.ROW_COUNT; y++) {
                    TileType type = board.getTile(x, y);
                    if(type == null) {
                        if(++freeFound == index) {
                            if(isGold == 0){
                                board.setTile(x, y, TileType.GoldFruit);
                            } else {
                                board.setTile(x, y, TileType.Fruit);
                            }
                            fruitLocations.add(new Point(x, y));
                            fruitsLeft++;
                            break;
                        }
                    }
                }
            }
            generateFruitHelper(start+1, end, fruitsLeft);
        }
    }

    /**
     * Spawns a new fruit onto the board.
     */
    private void spawnFruit() {
        //Reset the score for this fruit to 100.
        this.nextFruitScore = 100;

        /*
         * Get a random index based on the number of free spaces left on the board.
         */
        int index = random.nextInt(BoardPanel.COL_COUNT * BoardPanel.ROW_COUNT - this.playerSnake.size() - board.getNumberOfWalls());


        /*
         * While we could just as easily choose a random index on the board
         * and check it if it's free until we find an empty one, that method
         * tends to hang if the snake becomes very large.
         * 
         * This method simply loops through until it finds the nth free index
         * and selects uses that. This means that the game will be able to
         * locate an index at a relatively constant rate regardless of the
         * size of the snake.
         */
        int freeFound = -1;
        for(int x = 0; x < BoardPanel.COL_COUNT; x++) {
            for(int y = 0; y < BoardPanel.ROW_COUNT; y++) {
                TileType type = board.getTile(x, y);
                if(type == null) {
                    if(++freeFound == index) {
                        board.setTile(x, y, TileType.Fruit);
                        break;
                    }
                }
            }
        }
    }

    /**
     * Gets the current score.
     * @return The score.
     */
    public int getScore() {
        return score;
    }

    /**
     * Gets the number of fruits eaten.
     * @return The fruits eaten.
     */
    public int getFruitsEaten() {
        return fruitsEaten;
    }

    /**
     * Gets the next fruit score.
     * @return The next fruit score.
     */
    public int getNextFruitScore() {
        return nextFruitScore;
    }

    /**
     * Gets the current direction of the snake.
     * @return The current direction.
     */
    public Direction getDirection() {
        return this.playerSnake.getCurrentDirection();
    }


    private ArrayList<Level> generateLevels(){
        ArrayList<Level> result = new ArrayList<Level>();
        Level l1 = new Level(BoardPanel.COL_COUNT, BoardPanel.ROW_COUNT, 1);
        Level l2 = new Level(BoardPanel.COL_COUNT, BoardPanel.ROW_COUNT, 2);
        Level l3 = new Level(BoardPanel.COL_COUNT, BoardPanel.ROW_COUNT, 3);
        Level l4 = new Level(BoardPanel.COL_COUNT, BoardPanel.ROW_COUNT, 4);
        Level l5 = new Level(BoardPanel.COL_COUNT, BoardPanel.ROW_COUNT, 2);
        Level l6 = new Level(BoardPanel.COL_COUNT, BoardPanel.ROW_COUNT, 0);
        Level l7 = new Level(BoardPanel.COL_COUNT, BoardPanel.ROW_COUNT, 0);
        Level l8 = new Level(BoardPanel.COL_COUNT, BoardPanel.ROW_COUNT, 0);
        Level l9= new Level(BoardPanel.COL_COUNT, BoardPanel.ROW_COUNT, 0);
        Level l10 = new Level(BoardPanel.COL_COUNT, BoardPanel.ROW_COUNT, 0);
        Level lreal5 = new Level(BoardPanel.COL_COUNT, BoardPanel.ROW_COUNT, 0);

        //		for(int i = 0; i < BoardPanel.COL_COUNT; i++){
        //			l2.setTile(new Point(i, 0), TileType.Wall);
        //			l2.setTile(new Point(i, BoardPanel.ROW_COUNT-1), TileType.Wall);
        //		}

        int ygap = BoardPanel.ROW_COUNT/5;
        int xgap = BoardPanel.COL_COUNT/5;

        // Level 2
        for(int i = ygap; i < BoardPanel.ROW_COUNT-ygap; i++){
            l2.setTile(new Point(xgap, i), TileType.Wall);
            l2.setTile(new Point(BoardPanel.COL_COUNT-1-xgap, i), TileType.Wall);
        }

        // Level 3
        xgap = BoardPanel.COL_COUNT/2;
        for(int i = ygap; i < BoardPanel.ROW_COUNT-ygap; i++){
            l3.setTile(new Point(xgap, i), TileType.Wall);
        }

        xgap = BoardPanel.COL_COUNT/5;
        for(int i = xgap; i < BoardPanel.ROW_COUNT-ygap; i++){
            l3.setTile(new Point(i, ygap), TileType.Wall);
        }

        // Level 4
        for(int i = xgap; i < BoardPanel.ROW_COUNT-ygap; i++){
            l4.setTile(new Point(i, ygap), TileType.Wall);
        }

        for(int i = ygap*2; i < BoardPanel.ROW_COUNT; i++){
            l4.setTile(new Point(xgap, i), TileType.Wall);
            l4.setTile(new Point(BoardPanel.COL_COUNT-1-xgap, i), TileType.Wall);
        }

        // Level 5
        for(int i = ygap; i < BoardPanel.ROW_COUNT-ygap; i++){
            l5.setTile(new Point(xgap, i), TileType.Wall);
            l5.setTile(new Point(BoardPanel.COL_COUNT-1-xgap, i), TileType.Wall);
        }

        for(int i = xgap; i < xgap*2; i++){
            l5.setTile(new Point(i, ygap), TileType.Wall);
            l5.setTile(new Point(i, BoardPanel.COL_COUNT-ygap-1), TileType.Wall);
        }

        for(int i = xgap*3; i < xgap*4; i++){
            l5.setTile(new Point(i, ygap), TileType.Wall);
            l5.setTile(new Point(i, BoardPanel.COL_COUNT-ygap-1), TileType.Wall);
        }

        // Level 6
        for(int i = ygap; i < BoardPanel.ROW_COUNT-ygap; i++){
            l6.setTile(new Point(xgap, i), TileType.Wall);
            l6.setTile(new Point(BoardPanel.COL_COUNT-1-xgap, i), TileType.Wall);

            l6.setTile(new Point(xgap+(xgap/2), i), TileType.Wall);
            l6.setTile(new Point(BoardPanel.COL_COUNT-1-xgap-(xgap/2), i), TileType.Wall);
        }

        // Level 7
        xgap = BoardPanel.COL_COUNT/5;
        for(int i = xgap; i < BoardPanel.ROW_COUNT-xgap; i++){
            l7.setTile(new Point(i, ygap-(ygap/4)), TileType.Wall);
        }

        int end = xgap *2;
        for(int i = xgap; i < end ; i++){
            l7.setTile(new Point(i, ygap+(ygap/2)), TileType.Wall);
        }
        for(int i = BoardPanel.COL_COUNT-1-end; i < BoardPanel.ROW_COUNT-ygap; i++){
            l7.setTile(new Point(i, ygap+(ygap/2)), TileType.Wall);
        }

        xgap = xgap*2;
        for(int i = ygap+(ygap/2); i < BoardPanel.ROW_COUNT-ygap; i++){
            l7.setTile(new Point(xgap, i), TileType.Wall);
            l7.setTile(new Point(BoardPanel.COL_COUNT-1-xgap, i), TileType.Wall);
        }

        // Level 8
        xgap = BoardPanel.COL_COUNT/5;
        for(int i = ygap*2; i < BoardPanel.ROW_COUNT; i++){
            l8.setTile(new Point(xgap, i), TileType.Wall);
            l8.setTile(new Point(BoardPanel.COL_COUNT-1-xgap, i), TileType.Wall);

            l8.setTile(new Point(xgap+(xgap/2), i), TileType.Wall);
            l8.setTile(new Point(BoardPanel.COL_COUNT-1-xgap-(xgap/2), i), TileType.Wall);
        }

        for(int i = xgap; i < BoardPanel.ROW_COUNT-xgap; i++){
            l8.setTile(new Point(i, ygap), TileType.Wall);
            l8.setTile(new Point(i, ygap+(ygap/2)), TileType.Wall);
        }

        // Level 9
        xgap = BoardPanel.COL_COUNT/5;
        // outside vertical
        for(int i = ygap; i < BoardPanel.ROW_COUNT-ygap; i++){
            l9.setTile(new Point(xgap, i), TileType.Wall);
            l9.setTile(new Point(BoardPanel.COL_COUNT-1-xgap, i), TileType.Wall);
        }

        // inside vertical
        for(int i = ygap+(ygap/2); i < BoardPanel.ROW_COUNT-ygap-(ygap/2)+1; i++){
            l9.setTile(new Point(xgap+(xgap/2), i), TileType.Wall);
            l9.setTile(new Point(BoardPanel.COL_COUNT-1-xgap-(xgap/2), i), TileType.Wall);
        }

        // outside horizontal left
        for(int i = xgap; i < xgap*2; i++){
            l9.setTile(new Point(i, ygap), TileType.Wall);
            l9.setTile(new Point(i, ygap*4), TileType.Wall);
        }

        // inside horizontal left
        for(int i = xgap+(xgap/2); i < xgap*2; i++){
            l9.setTile(new Point(i, ygap*4-(ygap/2)), TileType.Wall);
            l9.setTile(new Point(i, ygap+(ygap/2)), TileType.Wall);
        }

        // outside horizontal right
        for(int i = xgap*3; i < BoardPanel.COL_COUNT-xgap; i++){
            l9.setTile(new Point(i, ygap), TileType.Wall);
            l9.setTile(new Point(i, ygap*4), TileType.Wall);
        }

        for(int i = (xgap*3); i < (xgap*3)+(xgap/2); i++){
            l9.setTile(new Point(i, ygap*4-(ygap/2)), TileType.Wall);
            l9.setTile(new Point(i, ygap+(ygap/2)), TileType.Wall);
        }

        // Level 10
        xgap = BoardPanel.COL_COUNT/5;
        // outside vertical
        for(int i = ygap; i < BoardPanel.ROW_COUNT-ygap; i++){
            l10.setTile(new Point(xgap, i), TileType.Wall);
            l10.setTile(new Point(BoardPanel.COL_COUNT-1-xgap, i), TileType.Wall);
        }

        // inside vertical
        for(int i = ygap; i < BoardPanel.ROW_COUNT-ygap-(ygap/2)+1; i++){
            l10.setTile(new Point(xgap+(xgap/2), i), TileType.Wall);
        }
        for(int i = ygap+(ygap/2); i < BoardPanel.ROW_COUNT-ygap; i++){
            l10.setTile(new Point(BoardPanel.COL_COUNT-1-xgap-(xgap/2), i), TileType.Wall);
        }

        // outside horizontal left
        for(int i = xgap; i < xgap*3; i++){
            l10.setTile(new Point(i, ygap*4), TileType.Wall);
        }

        // inside horizontal left
        for(int i = xgap+(xgap/2); i < xgap*3; i++){
            l10.setTile(new Point(i, ygap*4-(ygap/2)), TileType.Wall);
            //l9.setTile(new Point(i, ygap+(ygap/2)), TileType.Wall);
        }

        // outside horizontal right
        for(int i = xgap*2; i < BoardPanel.COL_COUNT-xgap; i++){
            l10.setTile(new Point(i, ygap), TileType.Wall);
        }

        for(int i = (xgap*2); i < (xgap*3)+(xgap/2); i++){
            l10.setTile(new Point(i, ygap+(ygap/2)), TileType.Wall);
        }

        int centerX = BoardPanel.COL_COUNT/2;
        int centerY = BoardPanel.ROW_COUNT/2;
        l10.setTile(new Point(centerX-1, centerY-1), TileType.Wall);
        l10.setTile(new Point(centerX-1, centerY+1), TileType.Wall);
        l10.setTile(new Point(centerX+1, centerY-1), TileType.Wall);
        l10.setTile(new Point(centerX+1, centerY+1), TileType.Wall);

        // Level 5
        for(int i = ygap; i < BoardPanel.ROW_COUNT-ygap; i++){
            lreal5.setTile(new Point(xgap, i), TileType.Wall);
            lreal5.setTile(new Point(BoardPanel.COL_COUNT-1-xgap, i), TileType.Wall);
        }

        int middle = BoardPanel.COL_COUNT / 2;
        for(int i = xgap; i < middle; i++){
            lreal5.setTile(new Point(i, BoardPanel.ROW_COUNT-ygap-1), TileType.Wall);
        }

        for(int i = middle; i < xgap*4; i++){
            lreal5.setTile(new Point(i, ygap), TileType.Wall);
        }

        int middleY = BoardPanel.ROW_COUNT/2;
        lreal5.setTile(new Point(middle, middleY), TileType.Wall);

        result.add(l1);
        result.add(l2);
        result.add(l3);
        result.add(l4);
        result.add(lreal5);
        result.add(l5);
        result.add(l6);
        result.add(l7);
        result.add(l8);
        result.add(l9);
        result.add(l10);
        return result;
    }

    public void changeFramerate(int rate){
        FRAMES_PER_SECOND = rate;
        FRAME_TIME = 1000L / FRAMES_PER_SECOND;
    }

    public void changeNumberOfFruit(int n){
        FRUITS_PER_LEVEL = n;
        TIME_PER_LEVEL = 30 * (3*FRUITS_PER_LEVEL / 4);
    }

    public int getLevel(){
        return this.currentLevel+1;
    }

    public int getTimeLeft(){
        return this.levelTimer;
    }

    public int getLivesLeft(){
        return this.livesLeft;
    }

    public void newGame(){
        this.resetGame();
        currentState = GameState.NewGame;
    }
}

