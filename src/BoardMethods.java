//used to look at and check a Board object
public class BoardMethods {

    //gui usage
    public static boolean isThereAPieceThere(Board b, int rowpass, int colpass) {
        int startingIndex = 0;
        if (Boolean.TRUE.equals(b.whiteTurn)) {
            startingIndex = 6;
        }
        int pieceIndex = (((rowpass * 8) + 20) + (colpass + 1)) + (rowpass * 2);
        for (int i = startingIndex; i < startingIndex + 6; i++) {
            if (b.pieceBoards[i][pieceIndex] == 1) {
                return true;
            }
        }
        return false;
    }

    public static boolean isKingChecked(int[][] bitboards, int[] kingLocations, int side) {
        int otherSide = side == 1 ? 0 : 1;
        MoveSet totalPossibleMoves = new MoveSet();
        //looks through enemy pieces and finds their possible moves
        for (int i = otherSide * 6; i < otherSide * 6 + 6; i++) {
            for (int j = 0; j < bitboards[0].length; j++) {
                if (bitboards[i][j] == 1) {
                    totalPossibleMoves.addAll(ValidMoves.possibleMoveFinderAllPieces(j, bitboards, i));
                }
            }
        }
        return totalPossibleMoves.containsMove(kingLocations[side]);
    }

}
