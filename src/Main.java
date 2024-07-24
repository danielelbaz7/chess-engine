public class Main {
    public static void main(String[] args) {

        //generates a board and a gui based on it
        Board gameBoard = new Board();
        ChessGUI chessGUI = new ChessGUI(gameBoard);
        MoveAndEval<Move, Double> me = ArtificialIntelligence.findBestMove(new Board(), 3, true);
        System.out.println(me.getMove());
        System.out.println(me.getEval());

    }
}