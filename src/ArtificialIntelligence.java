import java.util.Map;

public class ArtificialIntelligence {

    //locates best move based on tree of moves
    public Map.Entry<Integer, Integer> findBestMove(Board board, int depth, boolean ourTurn) {
        int side = board.whiteTurn ? 1 : 0;

        for (int i = side * 6; i < (side * 6) + 6; i++) {
            for (int j = 0; j < board.pieceBoards[0].length; j++) {
                if (board.pieceBoards[i][j] == 1) {

                }
            }
        }
        return null;
    }

}
