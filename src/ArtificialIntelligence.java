import java.util.Arrays;
public class ArtificialIntelligence {

    Board board;
    ValidMoves vm;
    int[][] tempBitboards;
    int[] tempKingLocations;

    public ArtificialIntelligence(Board b) {
        this.board = b;
        this.vm = b.vm;

        tempBitboards = new int[12][120];
        tempKingLocations = Arrays.copyOf(b.kingLocations, 2);

        for (int i = 0; i < 12; i++) {
            System.arraycopy(b.pieceBoards[i], 0, tempBitboards[i], 0, 120);
        }
    }

    //locates best move based on tree of moves
    public double findBestMove(Board board) {
        int side = board.whiteTurn ? 1 : 0;

        /*if(depth == 0 || Board.isKingCheckmated(tempBitboards, side, tempKingLocations)) {
            return Board.evaluateBoard(tempBitboards, whiteTurn, tempKingLocations);
        }*/
        MoveSet allPossibleMoves = ValidMoves.allAvailableMoves()
        Move bestMove = ;

        if(whiteTurn) {
            double maxEval = Integer.MIN_VALUE;

        }

        return null;
    }

}
