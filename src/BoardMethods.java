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

    //evaluates which side is winning and by how much
    public static double evaluateBoard(int[][] bitboards, int[] kingLocations, boolean isWhiteTurn) {
        //sets score to 1000 or -1000 if in check mate
        boolean[] kingsChecked = {false, false};
        double totalScore = 0;
        int sideToCheck = isWhiteTurn ? 1 : 0;
        int otherSide = isWhiteTurn ? 0 : 1;
        MoveSet availableMovesForSideToMove = ValidMoves.allAvailableMoves(bitboards, kingLocations, sideToCheck);
        if (BoardMethods.isKingChecked(bitboards, kingLocations, sideToCheck)) {
            if (availableMovesForSideToMove.isEmpty()) {
                if (sideToCheck == 1) {
                    totalScore = -1000;
                    return totalScore;
                } else {
                    totalScore = 1000;
                    return totalScore;
                }
            }
            else {
                if(sideToCheck == 1) {
                    totalScore -= 10;
                } else {
                    totalScore += 10;
                }
                kingsChecked[sideToCheck] = true;
            }
        }

        /*for(Move possibleCurrentSideMove : availableMovesForSideToMove) {
            int currentBitboard = possibleCurrentSideMove.getCurrentBitboard();
            totalScore += getBaseValue(currentBitboard);
            if(currentBitboard == 10) {
                totalScore -= 0.5 * ((Conv.to64From120(possibleCurrentSideMove.getCurrentBitboard())/8)-7);
            }
            totalScore += 0.05;
            int nextBitboard = possibleCurrentSideMove.getNextBitboard();
            if(nextBitboard != -1) {
                totalScore += (getBaseValue(nextBitboard) * -1);
            }
        }

        MoveSet otherSideCaptures = new MoveSet();
        for(Move possibleOtherSideMove : availableMovesForSideNotMoving) {
            int currentBitboard = possibleOtherSideMove.getCurrentBitboard();
            totalScore += getBaseValue(currentBitboard);
            if(currentBitboard == 4) {
                totalScore += 0.5 * ((Conv.to64From120(possibleOtherSideMove.getCurrentBitboard())/8));
            }
            totalScore += 0.05;
            if(possibleOtherSideMove.getNextBitboard() != -1) {
                otherSideCaptures.add(possibleOtherSideMove);
            }
        }

        int bestCapture = 0;
        for(Move capture : otherSideCaptures) {
            int baseValueOfCapturedPiece = getBaseValue(capture.getNextBitboard());
            totalScore += (baseValueOfCapturedPiece * -1);
            bestCapture = Math.max(Math.abs(bestCapture), Math.abs(baseValueOfCapturedPiece));
        }
        totalScore += bestCapture;


        //adds or removes based on distance and piece value
        /*for (int i = 0; i < 12; i++) {
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
            for (int j = 0; j < bitboards[i].length; j++) {
                if (bitboards[i][j] == 1) {
                    totalScore += baseValue;

                    if (i == 4) {
                        totalScore += ((j / 10) - 2) * 0.50;
                    } else if (i == 10) {
                        totalScore += ((j / 10) - 9) * 0.50;
                    } else {
                        for (Move possibleMove : ValidMoves.possibleMoveFinderAllPieces(j, bitboards, i)) {
                            //if it is not a capture add 0.05 for every possible moves
                            if (possibleMove.getNextBitboard() == -1) {
                                totalScore += (negativeOrPositive * 0.05);
                            } else {
                                //if we are attacking a piece, add its value to our score
                                if(sideToCheck == (i/6)) {
                                    totalScore += (getBaseValue(possibleMove) * -1);
                                }
                            }
                        }
                    }
                }
            }
        }*/

        return (double) Math.round(totalScore * 10) / 10;
    }

    //takes a location and set of boards to find the current bitboard of a piece
    private static int getBaseValue(int bitboard) {
        return switch (bitboard) {
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
