public class Main {
    public static void main(String[] args) {

        //creates a default board template used to generate the bitboards
        final String[][] boardTemplate = {
                {"2", "2", "2", "2", "2", "2", "2", "2", "2", "2"},
                {"2", "2", "2", "2", "2", "2", "2", "2", "2", "2"},
                {"2", "r", "n", "b", "q", "k", "b", "n", "r", "2"},
                {"2", "p", "p", "p", "p", " ", "p", "p", "p", "2"},
                {"2", " ", " ", " ", " ", " ", " ", " ", " ", "2"},
                {"2", " ", " ", " ", " ", "p", " ", " ", " ", "2"},
                {"2", " ", " ", " ", " ", "P", " ", " ", " ", "2"},
                {"2", " ", " ", " ", " ", " ", " ", " ", " ", "2"},
                {"2", "P", "P", "P", "P", " ", "P", "P", "P", "2"},
                {"2", "R", "N", "B", "Q", "K", "B", "N", "R", "2"},
                {"2", "2", "2", "2", "2", "2", "2", "2", "2", "2"},
                {"2", "2", "2", "2", "2", "2", "2", "2", "2", "2"}
        };

        //generates a board and a gui based on it
        Board gameBoard = new Board(boardTemplate);
        ChessGUI chessGUI = new ChessGUI(gameBoard);
        System.out.println(ArtificialIntelligence.findBestMove(gameBoard, 3, true));

    }
}