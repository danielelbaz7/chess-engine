public class ArtificialIntelligence extends Thread{


    public void run(Board b, int depth, boolean ourTurn) {
        findBestMove(b, depth, ourTurn);
    }

    //locates best move based on tree of moves
    public static MoveAndEval<Move, Double> findBestMove(Board b, int depth, boolean ourTurn) {
        double evaluation = BoardMethods.evaluateBoard(b.pieceBoards, b.kingLocations, ourTurn);
        if(depth == 0 || Math.abs(evaluation) == 1000) {
            return new MoveAndEval<>(null, evaluation);
        }

        MoveSet allPossibleMoves = ValidMoves.allAvailableMoves(b.pieceBoards, b.kingLocations, 1);
        Move bestMove = allPossibleMoves.toArray(new Move[0])[0];

        if(ourTurn) {
            double maxEval = Integer.MIN_VALUE;
            for(Move move : allPossibleMoves) {
                b.makeMove(move);
                double eval = findBestMove(b, depth-1, false).getEval();
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
                double eval = findBestMove(b, depth-1, true).getEval();
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
