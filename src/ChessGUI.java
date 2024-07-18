import javax.swing.*;
import java.awt.*;

public class ChessGUI {

    //creates baseframe and gets the board template
    static JFrame gameFrame = new JFrame();
    static JPanel titlePanel = new JPanel();
    static JPanel chessPanel = new JPanel();
    static String[][] mainBoard = Board.getBoardTemplate();
    public static JLabel[] JLabelCollection = new JLabel[64];
    static int[] pieceSelectedAndCoordinate = new int[2];
    public static int dimension = (int) Math.min(Toolkit.getDefaultToolkit().getScreenSize().getHeight(),
            Toolkit.getDefaultToolkit().getScreenSize().getWidth());

    //creates chessboard gui
    private static void initializeChessFrame()
    {
        System.out.println("DIMENSION " + dimension);
        //makes the frame an 8x8 grid layout
        chessPanel.setLayout(new GridLayout(8, 8));
        /*iterates through each cell and sets it to either black or white,
        also chooses the proper icon based on the board template and places it on the board
         */
        for(int i = 0; i < 64; i++)
        {
            JLabel b = new JLabel();
            b.setOpaque(true);
            String imgSrc = "src/Assets/";

            String currentPiece = mainBoard[2+(i/8)][1+(i%8)];

            //sets block colors
            if(((i/8)%2) == 0) {
                if((i%2) == 0){
                    b.setBackground(new Color(225, 215, 183));
                }
                else {
                    b.setBackground(new Color(57, 40, 20));
                }
            }
            else if((i%2) == 1) {
                b.setBackground(new Color(225, 215, 183));
            }
            else {
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
            Image tempImg = currentPieceImage.getScaledInstance(dimension/16, dimension/16, Image.SCALE_SMOOTH);
            currentPieceImageIcon.setImage(tempImg);

            b.setIcon(currentPieceImageIcon);
            b.setSize(5, 5);
            JLabelCollection[i] = b;
        }

        placeBoardsAgain();

        //establishes board
        chessPanel.setSize(dimension/2, dimension/2);
        chessPanel.setVisible(true);

        GameplayController mouseListenerController = new GameplayController();
        chessPanel.addMouseListener(mouseListenerController);

    }

    private static void initializeTitleFrame() {
        //creates a title for the game alongside a checkbox that determines if the evaluator will be shown
        JLabel titleAndCheckBox = new JLabel("Chess Engine");
        titleAndCheckBox.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
        titleAndCheckBox.setForeground(Color.BLACK);
        JLabel authorText = new JLabel("Created By: Daniel Elbaz");
        authorText.setForeground(Color.BLUE);
        titlePanel.add(titleAndCheckBox);
        titlePanel.setPreferredSize(new Dimension(dimension/2, dimension/12));
        titlePanel.add(authorText);
        titlePanel.setVisible(true);


        /*GridBagLayout GBL = new GridBagLayout();
        GridBagConstraints GBC = new GridBagConstraints();
        GBL.setConstraints();
        titleAndCheckBox.setLayout(GBL);
        System.out.println(titleAndCheckBox.getLayout());*/
    }

    public static void placeBoardsAgain()
    {
        for(JLabel j : JLabelCollection)
        {
            chessPanel.add(j);
        }
    }

    public static void startChessGUI()
    {
        initializeTitleFrame();
        initializeChessFrame();
        gameFrame.setResizable(false);
        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameFrame.setLayout(new BorderLayout());
        gameFrame.add(chessPanel, BorderLayout.CENTER);
        gameFrame.add(titlePanel, BorderLayout.NORTH);
        gameFrame.setSize(dimension/2, (int) (chessPanel.getHeight() + titlePanel.getPreferredSize().getHeight()));
        gameFrame.setVisible(true);
    }

    public static int isPieceSelected() {
        return pieceSelectedAndCoordinate[0];
    }

    public static int getPointSelected()
    {
        if(pieceSelectedAndCoordinate[0] == 0) { return -1; }
        return pieceSelectedAndCoordinate[1];
    }

    public static void setPieceSelectedTrue(int row, int col) {
        pieceSelectedAndCoordinate[0] = 1;
        pieceSelectedAndCoordinate[1] = ((row*8) + col);
    }
    public static void setPieceSelectedFalse() {
        pieceSelectedAndCoordinate[0] = 0;
        pieceSelectedAndCoordinate[1] = -1;
    }
}
