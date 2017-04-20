import java.awt.EventQueue;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.FlowLayout;

import javax.swing.ButtonGroup;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.KeyStroke;

import java.awt.event.*;
import javax.swing.JSlider;


public class SnakeJFrame extends JFrame {

    private static final long serialVersionUID = 9083894152257381994L;
    private JPanel contentPane;
    private SnakeGame game;
    private JMenu mnNewMenu;
    private JMenuItem mntmNewGame;
    private JMenuItem mntmPause;
    private JMenu mnNewMenu_1;
    private JMenu speedMenu;
    private JRadioButtonMenuItem slowSpeedRadioButton;
    private JRadioButtonMenuItem mediumSpeedRadioButton;
    private JRadioButtonMenuItem fastSpeedRadioButton;
    private JMenu mnDifficulty;
    private JRadioButtonMenuItem easyDifficultyRadioButton;
    private JRadioButtonMenuItem mediumDifficultyRadioButton;
    private JRadioButtonMenuItem hardDifficultyRadioButton;
    private JMenuItem mntmExit;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    SnakeJFrame frame = new SnakeJFrame();
                    frame.startGame();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the frame.
     */
    public SnakeJFrame() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Worm Meister");

        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        mnNewMenu = new JMenu("File");
        menuBar.add(mnNewMenu);

        mntmNewGame = new JMenuItem("New Game");
        mnNewMenu.add(mntmNewGame);
        mntmNewGame.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_MASK));
        mntmNewGame.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent arg0) {
                game.newGame();
            }

        });

        mntmPause = new JMenuItem("Pause");
        mntmPause.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent arg0) {
                game.pause();
            }
        });
        mnNewMenu.add(mntmPause);
        mntmPause.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, InputEvent.CTRL_MASK));

        mntmExit = new JMenuItem("Exit");
        mntmExit.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent arg0) {
                System.exit(0);
            }
        });
        mnNewMenu.addSeparator();
        mnNewMenu.add(mntmExit);

        mnNewMenu_1 = new JMenu("Options");
        menuBar.add(mnNewMenu_1);

        speedMenu = new JMenu("Speed");
        mnNewMenu_1.add(speedMenu);

        ButtonGroup speedButtonGroup = new ButtonGroup();
        slowSpeedRadioButton = new JRadioButtonMenuItem("Slow");
        slowSpeedRadioButton.addItemListener(new ItemListener(){

            @Override
            public void itemStateChanged(ItemEvent a) {
                if(a.getStateChange() == ItemEvent.SELECTED){
                    System.out.println("Slow");
                    game.changeFramerate(6);
                }
            }

        });
        speedButtonGroup.add(slowSpeedRadioButton);
        speedMenu.add(slowSpeedRadioButton);

        mediumSpeedRadioButton = new JRadioButtonMenuItem("Medium");
        mediumSpeedRadioButton.setSelected(true);
        mediumSpeedRadioButton.addItemListener(new ItemListener(){

            @Override
            public void itemStateChanged(ItemEvent a) {
                if(a.getStateChange() == ItemEvent.SELECTED){
                    System.out.println("Medium");
                    game.changeFramerate(8);
                }
            }

        });
        speedButtonGroup.add(mediumSpeedRadioButton);
        speedMenu.add(mediumSpeedRadioButton);

        fastSpeedRadioButton = new JRadioButtonMenuItem("Fast");
        fastSpeedRadioButton.addItemListener(new ItemListener(){

            @Override
            public void itemStateChanged(ItemEvent a) {
                if(a.getStateChange() == ItemEvent.SELECTED){
                    System.out.println("Fast");
                    game.changeFramerate(12);
                }
            }

        });
        speedMenu.add(fastSpeedRadioButton);
        speedButtonGroup.add(fastSpeedRadioButton);

        mnDifficulty = new JMenu("Difficulty");
        mnNewMenu_1.add(mnDifficulty);

        ButtonGroup difficultyButtonGroup = new ButtonGroup();
        easyDifficultyRadioButton = new JRadioButtonMenuItem("Easy");
        mnDifficulty.add(easyDifficultyRadioButton);
        difficultyButtonGroup.add(easyDifficultyRadioButton);
        easyDifficultyRadioButton.addItemListener(new ItemListener(){

            @Override
            public void itemStateChanged(ItemEvent a) {
                if(a.getStateChange() == ItemEvent.SELECTED){
                    game.pause();
                    game.changeNumberOfFruit(10);
                    game.newGame();
                }
            }

        });

        mediumDifficultyRadioButton = new JRadioButtonMenuItem("Medium");
        mediumDifficultyRadioButton.setSelected(true);
        mnDifficulty.add(mediumDifficultyRadioButton);
        difficultyButtonGroup.add(mediumDifficultyRadioButton);
        mediumDifficultyRadioButton.addItemListener(new ItemListener(){

            @Override
            public void itemStateChanged(ItemEvent a) {
                if(a.getStateChange() == ItemEvent.SELECTED){
                    game.pause();
                    game.changeNumberOfFruit(15);
                    game.newGame();
                }
            }

        });

        hardDifficultyRadioButton = new JRadioButtonMenuItem("Hard");
        mnDifficulty.add(hardDifficultyRadioButton);
        difficultyButtonGroup.add(hardDifficultyRadioButton);
        hardDifficultyRadioButton.addItemListener(new ItemListener(){

            @Override
            public void itemStateChanged(ItemEvent a) {
                if(a.getStateChange() == ItemEvent.SELECTED){
                    game.pause();
                    game.changeNumberOfFruit(20);
                    game.newGame();
                }
            }

        });

        contentPane = new JPanel();
        FlowLayout flowLayout = (FlowLayout) contentPane.getLayout();
        flowLayout.setVgap(0);
        flowLayout.setHgap(0);
        //		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        game = new SnakeGame();
        setContentPane(contentPane);
        contentPane.add(game);

        /*
         * Adds a new key listener to the frame to process input. 
         */
        addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {
                switch(e.getKeyCode()) {

                    /*
                     * If the game is not paused, and the game is not over...
                     * 
                     * Ensure that the direction list is not full, and that the most
                     * recent direction is adjacent to North before adding the
                     * direction to the list.
                     */
                    case KeyEvent.VK_W:
                    case KeyEvent.VK_UP:
                        game.turn(Direction.North);
                        break;

                        /*
                         * If the game is not paused, and the game is not over...
                         * 
                         * Ensure that the direction list is not full, and that the most
                         * recent direction is adjacent to South before adding the
                         * direction to the list.
                         */	
                    case KeyEvent.VK_S:
                    case KeyEvent.VK_DOWN:
                        game.turn(Direction.South);
                        break;

                        /*
                         * If the game is not paused, and the game is not over...
                         * 
                         * Ensure that the direction list is not full, and that the most
                         * recent direction is adjacent to West before adding the
                         * direction to the list.
                         */						
                    case KeyEvent.VK_A:
                    case KeyEvent.VK_LEFT:
                        game.turn(Direction.West);
                        break;

                        /*
                         * If the game is not paused, and the game is not over...
                         * 
                         * Ensure that the direction list is not full, and that the most
                         * recent direction is adjacent to East before adding the
                         * direction to the list.
                         */		
                    case KeyEvent.VK_D:
                    case KeyEvent.VK_RIGHT:
                        game.turn(Direction.East);
                        break;

                        /*
                         * Reset the game if one is not currently in progress.
                         */
                    case KeyEvent.VK_ENTER:
                        game.enter();
                        break;
                }
            }

        });

        pack();
        setLocationRelativeTo(null);
    }

    public void startGame(){
        Thread t = new Thread() {
            public void run(){
                game.startGame();
            }
        };
        t.start();
    }

}
