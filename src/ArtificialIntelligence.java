public class ArtificialIntelligence {

    //locates best move based on tree of moves
    public static MoveAndEval<Move, Double> findBestMove(Board b, int depth, boolean ourTurn, int side) {
        if(depth == 0 || BoardMethods.isKingCheckmated(b.pieceBoards, b.kingLocations, 0) || BoardMethods.isKingCheckmated(b.pieceBoards, b.kingLocations, 1)) {
            return new MoveAndEval<>(null, BoardMethods.evaluateBoard(b.pieceBoards, b.kingLocations, true));
        }

        MoveSet allPossibleMoves = ValidMoves.allAvailableMoves(b.pieceBoards, b.kingLocations, 1);
        Move bestMove = allPossibleMoves.toArray(new Move[0])[0];
        for(Move m : allPossibleMoves) {
            bestMove = m;
            break;
        }

        if(ourTurn) {
            double maxEval = Integer.MIN_VALUE;
            for(Move move : allPossibleMoves) {
                b.makeMove(move);
                double eval = findBestMove(b, depth-1, false, 0).getEval();
                b.undoMove(move);
                if(eval > maxEval) {
                    maxEval = eval;
                    bestMove = move;
                }
            }
            return new MoveAndEval<>(bestMove, maxEval);

        } else {
            double minEval = Integer.MAX_VALUE;
            for(Move move : allPossibleMoves) {
                b.makeMove(move);
                double eval = findBestMove(b, depth-1, true, 1).getEval();
                b.undoMove(move);
                if(eval < minEval) {
                    minEval = eval;
                    bestMove = move;
                }
            }
            return new MoveAndEval<>(bestMove, minEval);
        }
    }

}
