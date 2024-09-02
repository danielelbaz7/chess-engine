import javax.annotation.processing.SupportedSourceVersion;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class ChessGUI {

    //creates baseframe and gets the board template
    Board board;
    JFrame gameFrame = new JFrame();
    JPanel titlePanel = new JPanel();
    JLabel evaluationValueLabel;
    JPanel chessPanel = new JPanel();
    JPanel evaluationPanel = new JPanel();
    JPanel bestMovePanel = new JPanel();
    JLabel bestMove;
    String[][] mainBoard;
    MoveAndEval<Move, Double> bestMoveAndEval;
    Thread runningAIThread;
    private int AIDepth;

    public JLabel[] JLabelCollection;
    int[] pieceSelectedAndCoordinate = new int[2];
    public static int dimension = (int) Math.min(Toolkit.getDefaultToolkit().getScreenSize().getHeight(),
            Toolkit.getDefaultToolkit().getScreenSize().getWidth());

    //sets the board to the board passed and creates the chess gui
    public ChessGUI(Board b, int depth) {
        this.board = b;
        AIDepth = depth;
        mainBoard = Board.getBoardTemplate();
        Board fakeBoard = new Board(board);
        GameplayController.allowMovement = false;
        bestMoveAndEval = ArtificialIntelligence.findBestMove(fakeBoard, AIDepth, Integer.MIN_VALUE, Integer.MAX_VALUE, board.whiteTurn);
        GameplayController.allowMovement = true;
        evaluationValueLabel = new JLabel(String.valueOf(board.evaluationValue));
        bestMove = new JLabel("<html>" + (Conv.to64From120(bestMoveAndEval.getMove().getCurrentLocation()) + 1) % 8 + ", " + (((Conv.to64From120(bestMoveAndEval.getMove().getCurrentLocation())) / 8) + 1) +
                "<br>" + "\uD83E\uDC1F" + "<br>" + (Conv.to64From120(bestMoveAndEval.getMove().getMoveLocation()) + 1) % 8 + ", " + (((Conv.to64From120(bestMoveAndEval.getMove().getMoveLocation())) / 8) + 1) + "<br>" + "</html>");
        bestMove.setFont(new Font(Font.SANS_SERIF, Font.BOLD, dimension / 80));
        JLabelCollection = new JLabel[64];
        this.startChessGUI();
    }

    //creates chessboard gui
    private void initializeChessPanel() {
        //makes the frame an 8x8 grid layout
        chessPanel.setLayout(new GridLayout(8, 8));
        /*iterates through each cell and sets it to either black or white,
        also chooses the proper icon based on the board template and places it on the board
         */
        for (int i = 0; i < 64; i++) {
            JLabel b = new JLabel();
            b.setOpaque(true);
            String imgSrc = "./src/Assets/";

            String currentPiece = mainBoard[2 + (i / 8)][1 + (i % 8)];

            //sets block colors
            if (((i / 8) % 2) == 0) {
                if ((i % 2) == 0) {
                    b.setBackground(new Color(225, 215, 183));
                } else {
                    b.setBackground(new Color(57, 40, 20));
                }
            } else if ((i % 2) == 1) {
                b.setBackground(new Color(225, 215, 183));
            } else {
                b.setBackground(new Color(57, 40, 28));
            }

            //chooses correct icon for each phase of the iteration
            switch (currentPiece) {
                case " " -> chessPanel.add(b);
                case "r" -> imgSrc += "black-rook.png";
                case "n" -> imgSrc += "black-knight.png";
                case "b" -> imgSrc += "black-bishop.png";
                case "q" -> imgSrc += "black-queen.png";
                case "k" -> imgSrc += "black-king.png";
                case "p" -> imgSrc += "black-pawn.png";
                case "R" -> imgSrc += "white-rook.png";
                case "N" -> imgSrc += "white-knight.png";
                case "B" -> imgSrc += "white-bishop.png";
                case "Q" -> imgSrc += "white-queen.png";
                case "K" -> imgSrc += "white-king.png";
                case "P" -> imgSrc += "white-pawn.png";
            }

            //resizes image
            ImageIcon currentPieceImageIcon = new ImageIcon(imgSrc);
            Image currentPieceImage = currentPieceImageIcon.getImage();
            Image tempImg = currentPieceImage.getScaledInstance(dimension / 16, dimension / 16, Image.SCALE_SMOOTH);
            currentPieceImageIcon.setImage(tempImg);

            b.setIcon(currentPieceImageIcon);
            b.setSize(5, 5);
            JLabelCollection[i] = b;
        }

        placeBoardsAgain();

        //establishes board
        chessPanel.setVisible(true);

        GameplayController mouseListenerController = new GameplayController(this);
        chessPanel.addMouseListener(mouseListenerController);

    }

    private void initializeTitlePanel() {
        //creates a title for the game alongside a checkbox that determines if the evaluator will be shown
        titlePanel.setLayout(new GridLayout(1, 3));

        JLabel titleAndCheckBox = new JLabel("Chess Engine");
        titleAndCheckBox.setFont(new Font(Font.SANS_SERIF, Font.BOLD, dimension / 50));
        titleAndCheckBox.setForeground(Color.BLACK);

        JLabel authorText = new JLabel("Created By: Daniel Elbaz");
        authorText.setForeground(Color.BLUE);

        JCheckBox showEvaluationCheckBox = getEvaluationCheckBox();
        JCheckBox showAIBestMove = getShowAICheckBox();
        JButton artificialIntelligenceAssistantButton = getAIButton();


        titlePanel.add(titleAndCheckBox);
        titlePanel.add(authorText);
        titlePanel.add(showEvaluationCheckBox);
        titlePanel.add(artificialIntelligenceAssistantButton);
        titlePanel.add(showAIBestMove);
        titlePanel.setVisible(true);

    }

    private JCheckBox getShowAICheckBox() {
        JCheckBox showAIBestMove = new JCheckBox();
        showAIBestMove.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    bestMove.setVisible(true);
                } else if (e.getStateChange() == ItemEvent.DESELECTED) {
                    bestMove.setVisible(false);
                }
            }
        });
        showAIBestMove.setText("Show best move?");
        return showAIBestMove;
    }

    private JCheckBox getEvaluationCheckBox() {
        JCheckBox showEvaluationCheckBox = new JCheckBox();
        showEvaluationCheckBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    evaluationValueLabel.setVisible(true);
                } else if (e.getStateChange() == ItemEvent.DESELECTED) {
                    evaluationValueLabel.setVisible(false);
                }
            }
        });
        showEvaluationCheckBox.setText("Show evaluation?");
        return showEvaluationCheckBox;
    }

    private JButton getAIButton() {
        JButton artificialIntelligenceAssistantButton = new JButton();
        artificialIntelligenceAssistantButton.addActionListener(new ActionListener() {
            @Override
            //sets visibility on button press
            public void actionPerformed(ActionEvent e) {

                if (runningAIThread == null || !runningAIThread.isAlive()) {
                    runningAIThread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            GameplayController.allowMovement = false;
                            bestMove.setText("<html>" + (((Conv.to64From120(bestMoveAndEval.getMove().getCurrentLocation())) % 8) + 1) + ", " + (((Conv.to64From120(bestMoveAndEval.getMove().getCurrentLocation())) / 8) + 1) +
                                    "<br>" + "\uD83E\uDC1F" + "<br>" + (((Conv.to64From120(bestMoveAndEval.getMove().getMoveLocation())) % 8) + 1) + ", " + (((Conv.to64From120(bestMoveAndEval.getMove().getMoveLocation())) / 8) + 1) +
                                    "<br>" + "↻" + "</html>");
                            Board b = new Board(board);
                            bestMoveAndEval = ArtificialIntelligence.findBestMove(b, AIDepth, Integer.MIN_VALUE, Integer.MAX_VALUE, board.whiteTurn);
                            System.out.println(bestMoveAndEval.getEval());
                            bestMove.setText("<html>" + (((Conv.to64From120(bestMoveAndEval.getMove().getCurrentLocation())) % 8) + 1) + ", " + (((Conv.to64From120(bestMoveAndEval.getMove().getCurrentLocation())) / 8) + 1) +
                                    "<br>" + "\uD83E\uDC1F" + "<br>" + (((Conv.to64From120(bestMoveAndEval.getMove().getMoveLocation())) % 8) + 1) + ", " + (((Conv.to64From120(bestMoveAndEval.getMove().getMoveLocation())) / 8) + 1) + "<br>" + " " + "</html>");
                            GameplayController.allowMovement = true;
                        }
                    });
                    runningAIThread.start();
                }
            }
        });
        artificialIntelligenceAssistantButton.setText("Generate best move with AI?");
        return artificialIntelligenceAssistantButton;
    }

    private void initializeEvaluationPanel() {
        //creates eval panel with a box that grows in height depending on who is winning
        //set to black for visibility, temporary
        evaluationValueLabel.setForeground(Color.BLACK);
        evaluationValueLabel.setVisible(false);
        evaluationPanel.add(evaluationValueLabel);
        evaluationPanel.setVisible(true);
    }

    private void initializeBestMovePanel() {
        //creates the best move panel
        bestMovePanel.setForeground(Color.BLACK);
        bestMove.setVisible(false);
        bestMovePanel.add(bestMove);
        bestMovePanel.setVisible(true);

    }

    public void placeBoardsAgain() {
        for (JLabel j : JLabelCollection) {
            chessPanel.add(j);
        }
    }

    public void startChessGUI() {
        initializeChessPanel();
        initializeTitlePanel();
        initializeEvaluationPanel();
        initializeBestMovePanel();

        gameFrame.setResizable(false);
        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameFrame.setLayout(new BorderLayout());

        chessPanel.setSize(dimension / 2, dimension / 2);
        titlePanel.setPreferredSize(new Dimension((int)
                (chessPanel.getWidth() + evaluationPanel.getPreferredSize().getWidth() + bestMovePanel.getPreferredSize().getWidth()), dimension / 30));

        evaluationPanel.setPreferredSize
                (new Dimension(dimension / 30, (int) ((dimension / 2) + titlePanel.getPreferredSize().getHeight())));

        bestMovePanel.setPreferredSize
                (new Dimension(dimension / 30, (int) ((dimension / 2) + titlePanel.getPreferredSize().getHeight())));

        gameFrame.add(chessPanel, BorderLayout.CENTER);
        gameFrame.add(titlePanel, BorderLayout.NORTH);
        gameFrame.add(evaluationPanel, BorderLayout.WEST);
        gameFrame.add(bestMovePanel, BorderLayout.EAST);

        //sets the size to accommodate the other pieces of the window
        gameFrame.setSize((int) (chessPanel.getWidth() + evaluationPanel.getPreferredSize().getWidth() + bestMovePanel.getPreferredSize().getWidth()),
                (int) (chessPanel.getHeight() + titlePanel.getPreferredSize().getHeight()));
        gameFrame.setVisible(true);
    }

    public int isPieceSelected() {
        return pieceSelectedAndCoordinate[0];
    }

    public int getPointSelected() {
        if (pieceSelectedAndCoordinate[0] == 0) {
            return -1;
        }
        return pieceSelectedAndCoordinate[1];
    }

    public void setPieceSelectedTrue(int row, int col) {
        pieceSelectedAndCoordinate[0] = 1;
        pieceSelectedAndCoordinate[1] = ((row * 8) + col);
    }

    public void setPieceSelectedFalse() {
        pieceSelectedAndCoordinate[0] = 0;
        pieceSelectedAndCoordinate[1] = -1;
    }
}
