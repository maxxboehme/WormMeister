import java.awt.*;

import javax.swing.JPanel;


public class TopPanel extends JPanel{

	/**
	 * Serial Version UID.
	 */
	private static final long serialVersionUID = 4921110690270257412L;

	/**
	 * Font used for the title.
	 */
	private static final Font TITLE_FONT = new Font("Tahoma", Font.BOLD, 22);
	
	private static final Font LARGE_FONT = new Font("Tahoma", Font.PLAIN, 20);
	
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
	TopPanel(SnakeGame game) {
		this.game = game;
		
		setPreferredSize(new Dimension(BoardPanel.ROW_COUNT * BoardPanel.TILE_SIZE, 80));
		setBackground(Color.BLACK);
	}
	
	private static final int STATISTICS_OFFSET = 20;
	
	private static final int SCORE_OFFSET = BoardPanel.ROW_COUNT * BoardPanel.TILE_SIZE - 10;
	
	private static final int LIVES_OFFSET = 25;
	
	private static final int MESSAGE_STRIDE = 30;
	
	private static final int SMALL_OFFSET = 10;
	
	private static final int LARGE_OFFSET = 50;
	
	private static final int TIMER_HEIGHT = 10;
	
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
		g.drawString("Worm Meister", getWidth() / 2 - g.getFontMetrics().stringWidth("Worm Mister") / 2, 20);
		g.drawString("Worm Meister", getWidth() / 2 - g.getFontMetrics().stringWidth("Worm Mister") / 2, 20);
		/*
		 * Draw the categories onto the window.
		 */
		g.setFont(MEDIUM_FONT);
		//g.drawString("Statistics", SMALL_OFFSET, STATISTICS_OFFSET);
		
		//g.drawString("Lives: ", SMALL_OFFSET, LIVES_OFFSET);
		
		int xlives = SMALL_OFFSET;
		g.setColor(Color.YELLOW);
		for(int i = 0; i < game.getLivesLeft(); i++){
			g.setColor(Color.YELLOW);
			g.fillRect(xlives, LIVES_OFFSET, BoardPanel.TILE_SIZE, BoardPanel.TILE_SIZE*3);
			g.setColor(Color.BLACK);
			int baseY = LIVES_OFFSET + BoardPanel.EYE_SMALL_INSET;
			g.drawLine(xlives + BoardPanel.EYE_LARGE_INSET, baseY, xlives + BoardPanel.EYE_LARGE_INSET, baseY + BoardPanel.EYE_LENGTH);
			g.drawLine(xlives + BoardPanel.TILE_SIZE - BoardPanel.EYE_LARGE_INSET, baseY, xlives + BoardPanel.TILE_SIZE - BoardPanel.EYE_LARGE_INSET, baseY + BoardPanel.EYE_LENGTH);
			xlives += BoardPanel.TILE_SIZE*2;
		}
				
		/*
		 * Draw the category content onto the window.
		 */
		g.setFont(LARGE_FONT);
		g.setColor(Color.WHITE);
		
		//Draw the content for the statistics category.
		int drawY = STATISTICS_OFFSET;
		g.drawString(game.getScore()+"", SCORE_OFFSET-g.getFontMetrics().stringWidth(game.getScore()+""), drawY += MESSAGE_STRIDE);
//		g.drawString("Fruit Eaten: " + game.getFruitsEaten(), LARGE_OFFSET, drawY += MESSAGE_STRIDE);
//		g.drawString("Fruit Score: " + game.getNextFruitScore(), LARGE_OFFSET, drawY += MESSAGE_STRIDE);
//		g.drawString("Current Level: " + game.getLevel(), LARGE_OFFSET, drawY += MESSAGE_STRIDE);
		
		double ratio = ((double)getWidth())/game.TIME_PER_LEVEL;
		double time = game.getTimeLeft()*ratio;
		//System.out.println("ratio: "+ratio+ " Time: "+ time);
		if((time/getWidth()) > .25){
			g.setColor(Color.GREEN);
		} else {
			g.setColor(Color.RED);
		}
		g.fillRect(0, getHeight()-(TIMER_HEIGHT+1), (int)time, TIMER_HEIGHT);
		g.setColor(Color.WHITE);
		g.drawRect(0, getHeight()-(TIMER_HEIGHT+1), getWidth(), TIMER_HEIGHT);
		//Draw the content for the controls category.
//		drawY = CONTROLS_OFFSET;
//		g.drawString("Move Up: W / Up Arrowkey", LARGE_OFFSET, drawY += MESSAGE_STRIDE);
//		g.drawString("Move Down: S / Down Arrowkey", LARGE_OFFSET, drawY += MESSAGE_STRIDE);
//		g.drawString("Move Left: A / Left Arrowkey", LARGE_OFFSET, drawY += MESSAGE_STRIDE);
//		g.drawString("Move Right: D / Right Arrowkey", LARGE_OFFSET, drawY += MESSAGE_STRIDE);
//		g.drawString("Pause Game: P", LARGE_OFFSET, drawY += MESSAGE_STRIDE);
	}
}

