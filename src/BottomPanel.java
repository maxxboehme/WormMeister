import java.awt.*;

import javax.swing.JPanel;


public class BottomPanel extends JPanel{

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
    public BottomPanel(SnakeGame game) {
        this.game = game;

        setPreferredSize(new Dimension(BoardPanel.ROW_COUNT * BoardPanel.TILE_SIZE, 60));
        setBackground(Color.BLACK);
    }

    private static final int STATISTICS_OFFSET = 150;

    private static final int CONTROLS_OFFSET = 15;

    private static final int MESSAGE_STRIDE = 12;

    private static final int SMALL_OFFSET = 10;

    private static final int LARGE_OFFSET = BoardPanel.ROW_COUNT * BoardPanel.TILE_SIZE - 250;

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        /*
         * Set the color to draw the font in to white.
         */
        g.setColor(Color.WHITE);

        /*
         * Draw the categories onto the window.
         */
        g.setFont(MEDIUM_FONT);
        g.drawString("Controls", SMALL_OFFSET, CONTROLS_OFFSET);

        /*
         * Draw the category content onto the window.
         */
        g.setFont(SMALL_FONT);

        //Draw the content for the controls category.
        int drawY = CONTROLS_OFFSET;
        g.drawString("Move Up: W / Up Arrowkey", SMALL_OFFSET, drawY += MESSAGE_STRIDE);
        g.drawString("Move Down: S / Down Arrowkey", LARGE_OFFSET, drawY);
        g.drawString("Move Left: A / Left Arrowkey", SMALL_OFFSET, drawY += MESSAGE_STRIDE);
        g.drawString("Move Right: D / Right Arrowkey", LARGE_OFFSET, drawY);
        g.drawString("Pause Game: P", SMALL_OFFSET, drawY += MESSAGE_STRIDE);
    }
}
