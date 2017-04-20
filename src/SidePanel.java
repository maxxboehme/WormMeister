import java.awt.*;

import javax.swing.JPanel;


public class SidePanel extends JPanel{

    /**
     * Serial Version UID.
     */
    private static final long serialVersionUID = 4921110690270257412L;

    /**
     * Font used for the title.
     */
    private static final Font TITLE_FONT = new Font("Tahoma", Font.BOLD, 20);

    /**
     * The medium font to draw with.
     */
    private static final Font MEDIUM_FONT = new Font("Tahoma", Font.BOLD, 16);

    /**
     * The small font to draw with.
     */
    private static final Font SMALL_FONT = new Font("Tahoma", Font.BOLD, 12);

    /**
     * The SnakeGame instance.
     */
    private SnakeGame game;

    /**
     * Creates a new SidePanel instance.
     * @param game The SnakeGame instance.
     */
    public SidePanel(SnakeGame game) {
        this.game = game;

        setPreferredSize(new Dimension(300, BoardPanel.ROW_COUNT * BoardPanel.TILE_SIZE));
        setBackground(Color.BLACK);
    }

    private static final int STATISTICS_OFFSET = 150;

    private static final int CONTROLS_OFFSET = 390;

    private static final int MESSAGE_STRIDE = 30;

    private static final int SMALL_OFFSET = 30;

    private static final int LARGE_OFFSET = 50;

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        /*
         * Set the color to draw the font in to white.
         */
        g.setColor(Color.WHITE);

        /*
         * Draw the game name onto the window.
         */
        g.setFont(TITLE_FONT);
        g.drawString("Snake Game", getWidth() / 2 - g.getFontMetrics().stringWidth("Snake Game") / 2, 50);

        /*
         * Draw the categories onto the window.
         */
        g.setFont(MEDIUM_FONT);
        g.drawString("Statistics", SMALL_OFFSET, STATISTICS_OFFSET);
        g.drawString("Controls", SMALL_OFFSET, CONTROLS_OFFSET);

        /*
         * Draw the category content onto the window.
         */
        g.setFont(SMALL_FONT);

        //Draw the content for the statistics category.
        int drawY = STATISTICS_OFFSET;
        g.drawString("Total Score: " + game.getScore(), LARGE_OFFSET, drawY += MESSAGE_STRIDE);
        g.drawString("Fruit Eaten: " + game.getFruitsEaten(), LARGE_OFFSET, drawY += MESSAGE_STRIDE);
        g.drawString("Fruit Score: " + game.getNextFruitScore(), LARGE_OFFSET, drawY += MESSAGE_STRIDE);
        g.drawString("Current Level: " + game.getLevel(), LARGE_OFFSET, drawY += MESSAGE_STRIDE);
        g.drawString("Time Left: " + game.getTimeLeft(), LARGE_OFFSET, drawY += MESSAGE_STRIDE);

        double ratio = 100.0/game.TIME_PER_LEVEL;
        double time = game.getTimeLeft()*ratio;
        if((time/100) > .25){
            g.setColor(Color.GREEN);
        } else {
            g.setColor(Color.RED);
        }
        g.fillRect(LARGE_OFFSET, drawY += MESSAGE_STRIDE, (int)time, 5);
        g.setColor(Color.WHITE);
        g.drawRect(LARGE_OFFSET, drawY, 100, 5);
        //Draw the content for the controls category.
        drawY = CONTROLS_OFFSET;
        g.drawString("Move Up: W / Up Arrowkey", LARGE_OFFSET, drawY += MESSAGE_STRIDE);
        g.drawString("Move Down: S / Down Arrowkey", LARGE_OFFSET, drawY += MESSAGE_STRIDE);
        g.drawString("Move Left: A / Left Arrowkey", LARGE_OFFSET, drawY += MESSAGE_STRIDE);
        g.drawString("Move Right: D / Right Arrowkey", LARGE_OFFSET, drawY += MESSAGE_STRIDE);
        g.drawString("Pause Game: P", LARGE_OFFSET, drawY += MESSAGE_STRIDE);
    }
}
