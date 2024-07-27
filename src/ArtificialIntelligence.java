public class ArtificialIntelligence{

    static int countOfOps = 0;

    //locates best move based on tree of moves
    public static MoveAndEval<Move, Double> findBestMove(Board b, int depth, double alpha, double beta, boolean whiteTurn) {
        double evaluation = BoardMethods.evaluateBoard(b.pieceBoards, b.kingLocations, whiteTurn);
        if(depth == 0 || Math.abs(evaluation) == 1000) {
            return new MoveAndEval<>(null, evaluation);
        }

        MoveSet allPossibleMoves = ValidMoves.allAvailableMoves(b.pieceBoards, b.kingLocations, whiteTurn ? 1 : 0);
        Move bestMove = null;

        if(whiteTurn) {
            double maxEval = Integer.MIN_VALUE;
            for(Move move : allPossibleMoves) {
                countOfOps++;
                b.makeMove(move);
                double eval = findBestMove(b, depth-1, alpha, beta, false).getEval();
                b.undoMove(move);
                if(eval > maxEval) {
                    maxEval = eval;
                    bestMove = move;
                }
                alpha = Math.max(alpha, eval);
                if(beta <= alpha) {
                    break;
                }

            }
            return new MoveAndEval<>(bestMove, maxEval);

        } else {
            double minEval = Integer.MAX_VALUE;
            for(Move move : allPossibleMoves) {
                countOfOps++;
                b.makeMove(move);
                double eval = findBestMove(b, depth-1, alpha, beta,true).getEval();
                b.undoMove(move);
                if(eval < minEval) {
                    minEval = eval;
                    bestMove = move;
                }
                beta = Math.min(beta, eval);
                if(beta <= alpha) {
                    break;
                }
            }
            return new MoveAndEval<>(bestMove, minEval);
        }
    }

}
