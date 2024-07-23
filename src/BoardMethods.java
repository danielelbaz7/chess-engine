//this class holds all methods to evaluate or check features of a Board object
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

    public static boolean isKingChecked(Board b, int side) {
        int otherSide = b.whiteTurn ? 0 : 1;
        MoveSet totalPossibleMoves = new MoveSet();
        //looks through enemy pieces and finds their possible moves
        for (int i = otherSide * 6; i < otherSide * 6 + 6; i++) {
            for (int j = 0; j < b.pieceBoards[0].length; j++) {
                if (b.pieceBoards[i][j] == 1) {
                    totalPossibleMoves.addAll(ValidMoves.possibleMoveFinderAllPieces(j, b, side));
                }
            }
        }
        return totalPossibleMoves.containsMove(b.kingLocations[side]);
    }

    public static boolean isKingCheckmated(Board b, int side) {
        return BoardMethods.isKingChecked(b, side) && ValidMoves.allAvailableMoves(b, side).isEmpty();
    }

    //evaluates which side is winning and by how much
    public static double evaluateBoard(Board b) {
        //sets score to 1000 or -1000 if in check mate
        double totalScore = 0;
        int sideToCheck = b.whiteTurn ? 1 : 0;
        if (isKingChecked(b, sideToCheck)) {
            if (ValidMoves.allAvailableMoves(b, sideToCheck).isEmpty()) {
                if (sideToCheck == 1) {
                    totalScore = -1000;
                    return totalScore;
                } else {
                    totalScore = 1000;
                    return totalScore;
                }
            }
        }
        //adds or removes based on distance and piece value
        for (int i = 0; i < 12; i++) {
            int baseValue = switch (i) {
                case 0 -> -5;
                case 1, 2 -> -3;
                case 3 -> -9;
                case 5 -> -1;
                case 6 -> 5;
                case 7, 8 -> 3;
                case 9 -> 9;
                case 11 -> 1;
                default -> 0;
            };
            double negativeOrPositive = baseValue > 0 ? 1 : -1;
            //runs through each bitboard, checks if there is a piece there
            for (int j = 0; j < b.pieceBoards[i].length; j++) {
                if (b.pieceBoards[i][j] == 1) {
                    //if there is a piece there, find the value and find its distance from the center and multiply that by the base value

                    /*double centerDistance = Math.abs(Conv.to64From120(j) % 8 - 3.5);
                    double heightDistance = Math.abs(Conv.to64From120(j) / 8 - 3.5);
                    totalScore += (baseValue / (centerDistance * heightDistance)) / 4;*/

                    totalScore += baseValue;

                    if (i == 4) {
                        totalScore += ((j / 10) - 2) * 0.50;
                    } else if (i == 10) {
                        totalScore += ((j / 10) - 9) * 0.50;
                    } else {
                        for (Move possibleMove : ValidMoves.possibleMoveFinderAllPieces(j, b, i)) {
                            //if it is not a capture add 0.05 for every possible moves
                            if (possibleMove.getNextBitboard() == -1) {
                                totalScore += negativeOrPositive * 0.05;
                            } else {
                                //if the other piece can attack our tested piece, add to the total score the basevalue
                                // of our piece minus the basevalue of the other piece to determine the value of the attack
                                if (ValidMoves.possibleMoveFinderAllPieces(possibleMove.getMoveLocation(), b, i).containsMove(j)) {
                                    System.out.println(possibleMove.getMoveLocation() + "IS THE PIECE WE ARE ATTACKING");
                                    totalScore += (baseValue - getBaseValue(possibleMove.getNextBitboard(), b)) * -1;
                                } else {
                                    totalScore += (getBaseValue(possibleMove.getMoveLocation(), b) * -1);
                                }
                            }
                        }
                    }
                }
            }
        }

        return (double) Math.round(totalScore * 10) / 10;
    }

    //takes a location and set of boards to find the current bitboard of a piece
    private static int getBaseValue(int location, Board b) {
        int boardWithPiece = -1;

        for (int i = 0; i < 12; i++) {
            if (b.pieceBoards[i][location] == 1) {
                boardWithPiece = i;
            }
        }

        return switch (boardWithPiece) {
            case 0 -> -5;
            case 1, 2 -> -3;
            case 3 -> -9;
            case 5 -> -1;
            case 6 -> 5;
            case 7, 8 -> 3;
            case 9 -> 9;
            case 11 -> 1;
            default -> 0;
        };
    }


}
