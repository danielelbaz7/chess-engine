public class Main {
    public static void main(String[] args) {

        //generates a board and a gui based on it
        Board gameBoard = new Board();
        ChessGUI chessGUI = new ChessGUI(gameBoard);
        long time = -System.currentTimeMillis();
        long ops = -ArtificialIntelligence.countOfOps;
        GameplayController.allowMovement = false;
        System.out.println(ArtificialIntelligence.findBestMove(gameBoard, 1, Integer.MIN_VALUE, Integer.MAX_VALUE, gameBoard.whiteTurn).getEval());
        GameplayController.allowMovement = true;
        time += System.currentTimeMillis();
        ops += ArtificialIntelligence.countOfOps;
        System.out.println((double) time / 1000 + " seconds, " + ops + " total operations. " + (1000 * ops) / (time) + " operations a second.");

    }
}