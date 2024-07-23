import java.util.HashSet;
import java.util.Map;

public class ArtificialIntelligence {

    //locates best move based on tree of moves
    public double findBestMove(Board b, int depth, boolean ourTurn) {

        if(depth == 0 || BoardMethods.isKingCheckmated(b.pieceBoards, b.kingLocations, 0) || BoardMethods.isKingCheckmated(b.pieceBoards, b.kingLocations, 1)) {
            return BoardMethods.evaluateBoard(b.pieceBoards, b.kingLocations, true);
        }

        if(ourTurn) {
            double maxEval = Integer.MIN_VALUE;
            for(Move move : ValidMoves.allAvailableMoves(b.pieceBoards, b.kingLocations, 1)) {
                b.makeMove(move);
                double eval = findBestMove(b, depth-1, false);
                b.undoMove(move);
                maxEval = Math.max(maxEval, eval);
            }
            return maxEval;
        } else {
            double minEval = Integer.MAX_VALUE;
            for(Move move : ValidMoves.allAvailableMoves(b.pieceBoards, b.kingLocations, 0)) {
                b.makeMove(move);
                double eval = findBestMove(b, depth-1, true);
                b.undoMove(move);
                minEval = Math.min(minEval, eval);
            }
            return minEval;
        }
    }

}
