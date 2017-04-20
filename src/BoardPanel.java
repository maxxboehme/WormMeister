import java.awt.*;

import javax.swing.JPanel;

/**
 * The {@code BoardPanel} class is responsible for managing and displaying the
 * contents of the game board.
 * @author Maxx Boehme
 *
 */
public class BoardPanel extends JPanel {

    /*
     * Serial Version UID.
     */
    private static final long serialVersionUID = 2513019940077893899L;

    /*
     * The number of columns on the board. (Should be odd so we can start in
     * the center).
     */
    public static final int COL_COUNT = 45;

    /*
     * The number of rows on the board. (Should be odd so we can start in
     * the center).
     */
    public static final int ROW_COUNT = 45;

    /*
     * The size of each tile in pixels.
     */
    public static final int TILE_SIZE = 11;

    /*
     * The number of pixels to offset the eyes from the sides.
     */
    public static final int EYE_LARGE_INSET = TILE_SIZE / 3;

    /*
     * The number of pixels to offset the eyes from the front.
     */
    public static final int EYE_SMALL_INSET = TILE_SIZE / 6;

    /*
     * The length of the eyes from the base (small inset).
     */
    public static final int EYE_LENGTH = TILE_SIZE / 5;

    /*
     * The font to draw the text with.
     */
    private static final Font FONT = new Font("Tahoma", Font.BOLD, 25);

    private static final Font NEW_LEVEL_FONT = new Font("Tahoma", Font.BOLD, 55);

    /*
     * The SnakeGame instance.
     */
    private SnakeGame game;

    /*
     * The array of tiles that make up this board.
     */
    private TileType[] tiles;

    private int numOfWall;

    /**
     * Creates a new BoardPanel instance.
     * @param game The SnakeGame instance.
     */
    public BoardPanel(SnakeGame game) {
        this.game = game;
        this.tiles = new TileType[ROW_COUNT * COL_COUNT];
        numOfWall = 0;

        setPreferredSize(new Dimension(COL_COUNT * TILE_SIZE, ROW_COUNT * TILE_SIZE));
        setBackground(Color.BLACK);
    }

    /**
     * Clears all of the tiles on the board and sets their values to null.
     */
    public void clearBoard() {
        for(int i = 0; i < tiles.length; i++) {
            tiles[i] = null;
        }
        numOfWall = 0;
    }

    public int getNumberOfWalls(){
        return numOfWall;
    }

    public boolean boardFromLevel(Level l){
        if(l.getColCount() == BoardPanel.COL_COUNT && l.getRowCount() == BoardPanel.ROW_COUNT){
            for(int i = 0; i < BoardPanel.ROW_COUNT; i++){
                for(int j = 0; j < BoardPanel.COL_COUNT; j++){
                    this.setTile(i, j, l.getTile(i, j));
                }
            }		
            return true;
        }
        return false;
    }

    /**
     * Sets the tile at the desired coordinate.
     * @param point The coordinate of the tile.
     * @param type The type to set the tile to.
     */
    public void setTile(Point point, TileType type) {
        setTile(point.x, point.y, type);
    }

    /**
     * Sets the tile at the desired coordinate.
     * @param x The x coordinate of the tile.
     * @param y The y coordinate of the tile.
     * @param type The type to set the tile to.
     */
    public void setTile(int x, int y, TileType type) {
        if(tiles[y * ROW_COUNT + x] == TileType.Wall){
            numOfWall--;
        }
        tiles[y * ROW_COUNT + x] = type;
        if(type == TileType.Wall){
            numOfWall++;
        }
    }

    /**
     * Gets the tile at the desired coordinate.
     * @param x The x coordinate of the tile.
     * @param y The y coordinate of the tile.
     * @return
     */
    public TileType getTile(int x, int y) {
        return tiles[y * ROW_COUNT + x];
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        /*
         * Loop through each tile on the board and draw it if it
         * is not null.
         */
        for(int x = 0; x < COL_COUNT; x++) {
            for(int y = 0; y < ROW_COUNT; y++) {
                TileType type = getTile(x, y);
                if(type != null) {
                    drawTile(x * TILE_SIZE, y * TILE_SIZE, type, g);
                }
            }
        }

        /*
         * Draw the grid on the board. This makes it easier to see exactly
         * where we in relation to the fruit.
         * 
         * The panel is one pixel too small to draw the bottom and right
         * outlines, so we outline the board with a rectangle separately.
         */
        //		g.setColor(Color.BLACK);
        //		g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
        //		for(int x = 0; x < COL_COUNT; x++) {
        //			for(int y = 0; y < ROW_COUNT; y++) {
        //				g.drawLine(x * TILE_SIZE, 0, x * TILE_SIZE, getHeight());
        //				g.drawLine(0, y * TILE_SIZE, getWidth(), y * TILE_SIZE);
        //			}
        //		}


        //		for(int x = 0; x < COL_COUNT; x++) {
        //			for(int y = 0; y < ROW_COUNT; y++) {
        //				TileType type = getTile(x, y);
        //				if(type != null && (type == TileType.SnakeBody || type == TileType.SnakeHead)) {
        //					drawTile(x * TILE_SIZE, y * TILE_SIZE, type, g);
        //				}
        //			}
        //		}
        //		
        /*
         * Get the center coordinates of the board.
         */
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;

        String largeMessage = null;
        String smallMessage = null;

        /*
         * Show a message on the screen based on the current game state.
         */
        if(game.isGameOver() || game.isNewGame() || game.isPaused() || game.hasWon()) {
            g.setColor(Color.WHITE);

            /*
             * Allocate the messages for and set their values based on the game
             * state.
             */
            if(game.isNewGame()) {
                largeMessage = "Snake Game!";
                smallMessage = "Press Enter to Start";
            } else if(game.isGameOver()) {
                largeMessage = "Game Over!";
                smallMessage = "Press Enter to Restart";
            } else if(game.isPaused()) {
                largeMessage = "Paused";
                smallMessage = "Press P to Resume";
            } else if(game.hasWon()){
                largeMessage = "You Have Won!";
                smallMessage = "Press Enter to Play Again";
            }

            /*
             * Set the message font and draw the messages in the center of the board.
             */
            g.setFont(FONT);
            g.drawString(largeMessage, centerX - g.getFontMetrics().stringWidth(largeMessage) / 2, centerY - 50);
            g.drawString(smallMessage, centerX - g.getFontMetrics().stringWidth(smallMessage) / 2, centerY + 50);
        } else if(game.isSettingUp()){
            g.setColor(Color.WHITE);
            largeMessage = "Level "+game.getLevel();
            g.setFont(NEW_LEVEL_FONT);
            g.drawString(largeMessage, centerX - g.getFontMetrics().stringWidth(largeMessage) / 2, centerY - 50);
        }
    }

    /**
     * Draws a tile onto the board.
     * @param x The x coordinate of the tile (in pixels).
     * @param y The y coordinate of the tile (in pixels).
     * @param type The type of tile to draw.
     * @param g The graphics object to draw to.
     */
    private void drawTile(int x, int y, TileType type, Graphics g) {
        /*
         * Because each type of tile is drawn differently, it's easiest
         * to just run through a switch statement rather than come up with some
         * overly complex code to handle everything.
         */
        switch(type) {

            /*
             * A fruit is depicted as a small red circle that with a bit of padding
             * on each side.
             */
            case Fruit:
                g.setColor(Color.RED);
                g.fillOval(x + 1, y + 1, TILE_SIZE - 2, TILE_SIZE - 2);
                g.setColor(Color.GREEN);
                int middle = TILE_SIZE/2;
                g.fillOval(x + 2 + middle, y, middle-2, middle-2);
                int quarter = middle/2;
                g.setColor(Color.WHITE);
                g.fillOval(x + quarter, y + quarter, quarter, quarter);
                break;

            case GoldFruit:
                g.setColor(Color.YELLOW);
                g.fillOval(x + 1, y + 1, TILE_SIZE - 2, TILE_SIZE - 2);
                g.setColor(Color.GREEN);
                int middleg = TILE_SIZE/2;
                g.fillOval(x + 2+middleg, y, middleg-2, middleg-2);
                int quarterg = middleg/2;
                g.setColor(Color.WHITE);
                g.fillOval(x + quarterg, y + quarterg, quarterg, quarterg);
                break;

            case Wall:
                g.setColor(Color.BLUE);
                g.fillRect(x, y, TILE_SIZE, TILE_SIZE);
                break;

            case Test:
                g.setColor(Color.PINK);
                g.fillRect(x, y, TILE_SIZE, TILE_SIZE);
                break;

                /*
                 * The snake body is depicted as a green square that takes up the
                 * entire tile.
                 */
            case SnakeBody:
                g.setColor(Color.YELLOW);
                g.fillRect(x, y, TILE_SIZE, TILE_SIZE);
                break;

            case EnemySnakeBody:
                g.setColor(Color.GREEN);
                g.fillRect(x, y, TILE_SIZE, TILE_SIZE);
                break;

            case EnemySnakeHeadNorth:
                g.setColor(Color.GREEN);
                g.fillRect(x, y, TILE_SIZE, TILE_SIZE);

                //Set the color to black so that we can start drawing the eyes.
                g.setColor(Color.BLACK);
                int eBaseY = y + EYE_SMALL_INSET;
                //			g.fillOval(x + EYE_LARGE_INSET, eBaseY, x + EYE_LARGE_INSET, eBaseY + EYE_LENGTH);
                //			g.fillOval(x + TILE_SIZE - EYE_LARGE_INSET, eBaseY, x + TILE_SIZE - EYE_LARGE_INSET, eBaseY + EYE_LENGTH);
                g.drawLine(x + EYE_LARGE_INSET, eBaseY, x + EYE_LARGE_INSET, eBaseY + EYE_LENGTH);
                g.drawLine(x + TILE_SIZE - EYE_LARGE_INSET, eBaseY, x + TILE_SIZE - EYE_LARGE_INSET, eBaseY + EYE_LENGTH);
                break;
            case EnemySnakeHeadSouth:
                g.setColor(Color.GREEN);
                g.fillRect(x, y, TILE_SIZE, TILE_SIZE);

                //Set the color to black so that we can start drawing the eyes.
                g.setColor(Color.BLACK);
                int eSBaseY = y + TILE_SIZE - EYE_SMALL_INSET;
                g.drawLine(x + EYE_LARGE_INSET, eSBaseY, x + EYE_LARGE_INSET, eSBaseY - EYE_LENGTH);
                g.drawLine(x + TILE_SIZE - EYE_LARGE_INSET, eSBaseY, x + TILE_SIZE - EYE_LARGE_INSET, eSBaseY - EYE_LENGTH);
                break;
            case EnemySnakeHeadEast:
                g.setColor(Color.GREEN);
                g.fillRect(x, y, TILE_SIZE, TILE_SIZE);

                //Set the color to black so that we can start drawing the eyes.
                g.setColor(Color.BLACK);
                int eBaseX = x + TILE_SIZE - EYE_SMALL_INSET;
                g.drawLine(eBaseX, y + EYE_LARGE_INSET, eBaseX - EYE_LENGTH, y + EYE_LARGE_INSET);
                g.drawLine(eBaseX, y + TILE_SIZE - EYE_LARGE_INSET, eBaseX - EYE_LENGTH, y + TILE_SIZE - EYE_LARGE_INSET);
                break;
            case EnemySnakeHeadWest:
                g.setColor(Color.GREEN);
                g.fillRect(x, y, TILE_SIZE, TILE_SIZE);

                //Set the color to black so that we can start drawing the eyes.
                g.setColor(Color.BLACK);
                int eWBaseX = x + EYE_SMALL_INSET;
                g.drawLine(eWBaseX, y + EYE_LARGE_INSET, eWBaseX + EYE_LENGTH, y + EYE_LARGE_INSET);
                g.drawLine(eWBaseX, y + TILE_SIZE - EYE_LARGE_INSET, eWBaseX + EYE_LENGTH, y + TILE_SIZE - EYE_LARGE_INSET);
                break;

                /*
                 * The snake head is depicted similarly to the body, but with two
                 * lines (representing eyes) that indicate it's direction.
                 */
            case SnakeHead:
                //Fill the tile in with green.
                g.setColor(Color.YELLOW);
                g.fillRect(x, y, TILE_SIZE, TILE_SIZE);

                //Set the color to black so that we can start drawing the eyes.
                g.setColor(Color.BLACK);

                /*
                 * The eyes will always 'face' the direction that the snake is
                 * moving.
                 * 
                 * Vertical lines indicate that it's facing North or South, and
                 * Horizontal lines indicate that it's facing East or West.
                 * 
                 * Additionally, the eyes will be closer to whichever edge it's
                 * facing.
                 * 
                 * Drawing the eyes is fairly simple, but is a bit difficult to
                 * explain. The basic process is this:
                 * 
                 * First, we add (or subtract) EYE_SMALL_INSET to or from the
                 * side of the tile representing the direction we're facing. This
                 * will be constant for both eyes, and is represented by the
                 * variable 'baseX' or 'baseY' (depending on orientation).
                 * 
                 * Next, we add (or subtract) EYE_LARGE_INSET to and from the two
                 * neighboring directions (Example; East and West if we're facing
                 * north).
                 * 
                 * Finally, we draw a line from the base offset that is EYE_LENGTH
                 * pixels in length at whatever the offset is from the neighboring
                 * directions.
                 * 
                 */
                switch(game.getDirection()) {
                    case North: {
                                    int baseY = y + EYE_SMALL_INSET;
                                    //				g.fillOval(x + EYE_LARGE_INSET, baseY, x + EYE_LARGE_INSET, baseY + EYE_LENGTH);
                                    //				g.fillOval(x + TILE_SIZE - EYE_LARGE_INSET, baseY, x + TILE_SIZE - EYE_LARGE_INSET, baseY + EYE_LENGTH);
                                    g.drawLine(x + EYE_LARGE_INSET, baseY, x + EYE_LARGE_INSET, baseY + EYE_LENGTH);
                                    g.drawLine(x + TILE_SIZE - EYE_LARGE_INSET, baseY, x + TILE_SIZE - EYE_LARGE_INSET, baseY + EYE_LENGTH);
                                    break;
                    }

                    case South: {
                                    int baseY = y + TILE_SIZE - EYE_SMALL_INSET;
                                    g.drawLine(x + EYE_LARGE_INSET, baseY, x + EYE_LARGE_INSET, baseY - EYE_LENGTH);
                                    g.drawLine(x + TILE_SIZE - EYE_LARGE_INSET, baseY, x + TILE_SIZE - EYE_LARGE_INSET, baseY - EYE_LENGTH);
                                    break;
                    }

                    case West: {
                                   int baseX = x + EYE_SMALL_INSET;
                                   g.drawLine(baseX, y + EYE_LARGE_INSET, baseX + EYE_LENGTH, y + EYE_LARGE_INSET);
                                   g.drawLine(baseX, y + TILE_SIZE - EYE_LARGE_INSET, baseX + EYE_LENGTH, y + TILE_SIZE - EYE_LARGE_INSET);
                                   break;
                    }

                    case East: {
                                   int baseX = x + TILE_SIZE - EYE_SMALL_INSET;
                                   g.drawLine(baseX, y + EYE_LARGE_INSET, baseX - EYE_LENGTH, y + EYE_LARGE_INSET);
                                   g.drawLine(baseX, y + TILE_SIZE - EYE_LARGE_INSET, baseX - EYE_LENGTH, y + TILE_SIZE - EYE_LARGE_INSET);
                                   break;
                    }

                }
                break;
        }
    }

}

