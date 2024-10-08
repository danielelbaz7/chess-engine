import java.util.*;

public class ValidMoves {

    private static final int[] KING_OPERATIONS = {-11, -10, -9, -1, 1, 9, 10, 11};
    private static final int[][] ROOK_OPERATIONS = {
            {-1, -2, -3, -4, -5, -6, -7},
            {1, 2, 3, 4, 5, 6, 7},
            {-10, -20, -30, -40, -50, -60, -70},
            {10, 20, 30, 40, 50, 60, 70}
    };
    private static final int[] KNIGHT_OPERATIONS = {-19, -21, -8, 12, 21, 19, 8, -12};
    private static final int[][] BISHOP_OPERATIONS = {
            {-11, -22, -33, -44, -55, -66, -77},
            {-9, -18, -27, -36, -45, -54, -63},
            {11, 22, 33, 44, 55, 66, 77},
            {9, 18, 27, 36, 45, 54, 63}
    };

    private static final int[] BLACK_PAWN_OPERATIONS = {10, 20, 9, 11};
    private static final int[] WHITE_PAWN_OPERATIONS = {-10, -20, -9, -11};

    public static MoveSet possibleMoveFinderAllPieces(int location, int[][] bitboards, int pieceType) {
        MoveSet possibleMoves = new MoveSet();
        //checks what piecetype to determine what moves to find
        possibleMoves = switch (pieceType) {
            case 0 -> possibleRookMoves(location, 0, bitboards);
            case 1 -> possibleKnightMoves(location, 0, bitboards);
            case 2 -> possibleBishopMoves(location, 0, bitboards);
            case 3 -> possibleQueenMoves(location, 0, bitboards);
            case 4 -> possibleKingMoves(location, 0, bitboards);
            case 5 -> possibleBlackPawnMoves(location, bitboards);
            case 6 -> possibleRookMoves(location, 1, bitboards);
            case 7 -> possibleKnightMoves(location, 1, bitboards);
            case 8 -> possibleBishopMoves(location, 1, bitboards);
            case 9 -> possibleQueenMoves(location, 1, bitboards);
            case 10 -> possibleKingMoves(location, 1, bitboards);
            case 11 -> possibleWhitePawnMoves(location, bitboards);
            default -> possibleMoves;
        };

        return possibleMoves;
    }

    //returns list of possible moves for a king
    private static MoveSet possibleKingMoves(int location, int side, int[][] bitboards) {
        MoveSet totalPossibleMoves = new MoveSet();
        int currentBitboard = side == 0 ? 4 : 10;
        //runs through each possible operation and checks for validity
        for (int op : KING_OPERATIONS) {
            boolean cancelOperation = false;
            for (int i = 0; i < 12; i++) {
                if (bitboards[i][(location + op)] != 0) {
                    if (bitboards[i][(location + op)] == 2) {
                        //out of bounds
                        cancelOperation = true;
                        break;
                    }
                    //if the side is the same
                    if (side == (i / 6)) {
                        //same team
                        //makes it not a possible move
                        cancelOperation = true;
                        break;
                    } else {
                        //adds the possible move's value as the board of the piece it captures
                        totalPossibleMoves.add(new Move(location, currentBitboard, location + op, i));
                        cancelOperation = true;
                        break;
                    }
                }
            }
            if (Boolean.FALSE.equals(cancelOperation)) {
                totalPossibleMoves.add(new Move(location, currentBitboard, location + op, -1));
            }
        }
        return totalPossibleMoves;
    }

    //returns a list of possible knight moves
    private static MoveSet possibleKnightMoves(int location, int side, int[][] bitboards) {
        MoveSet totalPossibleMoves = new MoveSet();
        int currentBitboard = side == 0 ? 1 : 7;
        //runs through each possible operation and checks for validity
        for (int op : KNIGHT_OPERATIONS) {
            boolean cancelOperation = false;
            for (int i = 0; i < 12; i++) {
                if (bitboards[i][(location + op)] != 0) {
                    if (bitboards[i][(location + op)] == 2) {
                        //out of bounds
                        cancelOperation = true;
                        break;
                    }
                    //if the side is the same
                    if (side == (i / 6)) {
                        //same team
                        //makes it not a possible move
                        cancelOperation = true;
                        break;
                    } else {
                        //adds the possible move's value as the board of the piece it captures
                        totalPossibleMoves.add(new Move(location, currentBitboard, location + op, i));
                        cancelOperation = true;
                        break;
                    }
                }
            }
            if (Boolean.FALSE.equals(cancelOperation)) {
                totalPossibleMoves.add(new Move(location, currentBitboard, location + op, -1));
            }
        }

        int otherSide = switch (side) {
            case 0 -> 1;
            case 1 -> 0;
            default -> -1;
        };

        return totalPossibleMoves;
    }

    //returns a list of possible moves for a rook
    private static MoveSet possibleRookMoves(int location, int side, int[][] bitboards) {
        MoveSet totalPossibleMoves = new MoveSet();
        int currentBitboard = side == 0 ? 0 : 6;
        //iterates through the set of sets of rook ops
        for (int j = 0; j < 4; j++) {
            //iterates through each set of rook ops
            for (int i = 0; i < ROOK_OPERATIONS[1].length; i++) {
                //if out of bounds quit
                boolean cancelOperation = false;
                if (bitboards[0][location + ROOK_OPERATIONS[j][i]] == 2) {
                    break;
                } else {
                    for (int k = 0; k < 12; k++) {
                        //if there is a piece there then check if it is the enemy, if enemy allow it if friendly do not
                        //break the rest of the loop both times
                        if (bitboards[k][location + ROOK_OPERATIONS[j][i]] == 1) {
                            cancelOperation = true;
                            if (k / 6 == side) {
                                break;
                            } else {
                                totalPossibleMoves.add(new Move(location, currentBitboard, location + ROOK_OPERATIONS[j][i], k));
                                break;
                            }
                        }
                    }
                }
                if (!cancelOperation) {
                    totalPossibleMoves.add(new Move(location, currentBitboard, location + ROOK_OPERATIONS[j][i], -1));
                } else {
                    break;
                }
            }
        }

        int otherSide = switch (side) {
            case 0 -> 1;
            case 1 -> 0;
            default -> -1;
        };

        return totalPossibleMoves;
    }

    //returns a list of possible moves for a bishop
    private static MoveSet possibleBishopMoves(int location, int side, int[][] bitboards) {
        MoveSet totalPossibleMoves = new MoveSet();
        int currentBitboard = side == 0 ? 2 : 8;
        //iterates through the set of sets of bishop ops
        for (int j = 0; j < 4; j++) {
            //iterates through each set of bishop ops
            for (int i = 0; i < BISHOP_OPERATIONS[0].length; i++) {
                //if out of bounds quit
                boolean cancelOperation = false;
                if (bitboards[0][location + BISHOP_OPERATIONS[j][i]] == 2) {
                    break;
                } else {
                    for (int k = 0; k < 12; k++) {
                        //if there is a piece there then check if it is the enemy, if enemy allow it if friendly do not
                        //break the rest of the loop both times
                        if (bitboards[k][location + BISHOP_OPERATIONS[j][i]] == 1) {
                            cancelOperation = true;
                            if (k / 6 == side) {
                                break;
                            } else {
                                totalPossibleMoves.add(new Move(location, currentBitboard, location + BISHOP_OPERATIONS[j][i], k));
                                break;
                            }
                        }
                    }
                }
                if (!cancelOperation) {
                    totalPossibleMoves.add(new Move(location, currentBitboard, location + BISHOP_OPERATIONS[j][i], -1));
                } else {
                    break;
                }
            }
        }

        int otherSide = switch (side) {
            case 0 -> 1;
            case 1 -> 0;
            default -> -1;
        };

        return totalPossibleMoves;
    }

    //returns a list of possible queen moves
    private static MoveSet possibleQueenMoves(int location, int side, int[][] bitboards) {
        int currentBitboard = side == 1 ? 9 : 3;
        MoveSet totalPossibleMoves = new MoveSet();
        for(Move possibleMove : possibleRookMoves(location, side, bitboards)) {
            totalPossibleMoves.add(new Move(possibleMove.getCurrentLocation(), currentBitboard, possibleMove.getMoveLocation(), possibleMove.getNextBitboard()));
        }
        for(Move possibleMove : possibleBishopMoves(location, side, bitboards)) {
            totalPossibleMoves.add(new Move(possibleMove.getCurrentLocation(), currentBitboard, possibleMove.getMoveLocation(), possibleMove.getNextBitboard()));
        }
        return totalPossibleMoves;
    }

    //returns all possible moves for a black pawn
    private static MoveSet possibleBlackPawnMoves(int location, int[][] bitboards) {
        MoveSet totalPossibleMoves = new MoveSet();
        //adds all not out of bounds moves to possible moves with the rest of the method taking out invalid ones
        for (int op : BLACK_PAWN_OPERATIONS) {
            if (!(bitboards[0][location + op] == 2)) {
                totalPossibleMoves.add(new Move(location, 5, op + location, -1));
            }
        }

        //removes moving forward once and twice if the forward move is unavailable
        for (int i = 0; i < 12; i++) {
            if (bitboards[i][location + BLACK_PAWN_OPERATIONS[0]] == 1) {
                totalPossibleMoves.remove(location + BLACK_PAWN_OPERATIONS[0]);
                totalPossibleMoves.remove(location + BLACK_PAWN_OPERATIONS[1]);
                break;
            }
        }

        if (location < 39 && location > 30) {
            for (int j = 0; j < 12; j++) {
                if (bitboards[j][location + BLACK_PAWN_OPERATIONS[1]] == 1) {
                    totalPossibleMoves.remove(location + BLACK_PAWN_OPERATIONS[1]);
                    break;
                }
            }
        } else {
            totalPossibleMoves.remove(location + BLACK_PAWN_OPERATIONS[1]);
        }

        for (int k = 0; k < 12; k++) {
            //checks for an enemy piece
            if (bitboards[k][location + BLACK_PAWN_OPERATIONS[2]] == 1) {
                if ((k / 6) == 0) {
                    totalPossibleMoves.remove(location + BLACK_PAWN_OPERATIONS[2]);
                } else {
                    totalPossibleMoves.remove(location + BLACK_PAWN_OPERATIONS[2]);
                    totalPossibleMoves.add(new Move(location, 5, location + BLACK_PAWN_OPERATIONS[2], k));
                }
                break;
            }
            if (k == 11) {
                totalPossibleMoves.remove(location + BLACK_PAWN_OPERATIONS[2]);
            }
        }

        for (int l = 0; l < 12; l++) {
            if (bitboards[l][location + BLACK_PAWN_OPERATIONS[3]] == 1) {
                if ((l / 6) == 0) {
                    totalPossibleMoves.remove(location + BLACK_PAWN_OPERATIONS[3]);
                } else {
                    totalPossibleMoves.remove(location + BLACK_PAWN_OPERATIONS[3]);
                    totalPossibleMoves.add(new Move(location, 5, location + BLACK_PAWN_OPERATIONS[3], l));
                }
                break;
            }
            if (l == 11) {
                totalPossibleMoves.remove(location + BLACK_PAWN_OPERATIONS[3]);
            }
        }
        return new MoveSet(totalPossibleMoves);
    }

    //returns all possible moves for a white pawn
    private static MoveSet possibleWhitePawnMoves(int location, int[][] bitboards) {
        MoveSet totalPossibleMoves = new MoveSet();
        //adds all not out of bounds moves to possible moves with the rest of the method taking out invalid ones
        for (int op : WHITE_PAWN_OPERATIONS) {
            if (!(bitboards[0][location + op] == 2)) {
                totalPossibleMoves.add(new Move(location, 11, op + location, -1));
            }
        }

        //removes moving forward once and twice if the forward move is unavailable
        for (int i = 0; i < 12; i++) {
            if (bitboards[i][location + WHITE_PAWN_OPERATIONS[0]] == 1) {
                totalPossibleMoves.remove(location + WHITE_PAWN_OPERATIONS[0]);
                totalPossibleMoves.remove(location + WHITE_PAWN_OPERATIONS[1]);
                break;
            }
        }

        if (location < 89 && location > 80) {
            for (int j = 0; j < 12; j++) {
                if (bitboards[j][location + WHITE_PAWN_OPERATIONS[1]] == 1) {
                    totalPossibleMoves.remove(location + WHITE_PAWN_OPERATIONS[1]);
                    break;
                }
            }
        } else {
            totalPossibleMoves.remove(location + WHITE_PAWN_OPERATIONS[1]);
        }

        for (int k = 0; k < 12; k++) {
            //checks for an enemy piece
            if (bitboards[k][location + WHITE_PAWN_OPERATIONS[2]] == 1) {
                if ((k / 6) == 1) {
                    totalPossibleMoves.remove(location + WHITE_PAWN_OPERATIONS[2]);
                } else {
                    totalPossibleMoves.remove(location + WHITE_PAWN_OPERATIONS[2]);
                    totalPossibleMoves.add(new Move(location, 11, location + WHITE_PAWN_OPERATIONS[2], k));
                }
                break;
            }
            if (k == 11) {
                totalPossibleMoves.remove(location + WHITE_PAWN_OPERATIONS[2]);
            }
        }

        for (int l = 0; l < 12; l++) {
            if (bitboards[l][location + WHITE_PAWN_OPERATIONS[3]] == 1) {
                if ((l / 6) == 1) {
                    totalPossibleMoves.remove(location + WHITE_PAWN_OPERATIONS[3]);
                } else {
                    totalPossibleMoves.remove(location + WHITE_PAWN_OPERATIONS[3]);
                    totalPossibleMoves.add(new Move(location, 11, location + WHITE_PAWN_OPERATIONS[3], l));
                }
                break;
            }
            if (l == 11) {
                totalPossibleMoves.remove(location + WHITE_PAWN_OPERATIONS[3]);
            }
        }

        return new MoveSet(totalPossibleMoves);
    }

    //returns if the move will put the king in check
    //CLBNLB = currentLocation&Bitboard, nextLocation&Bitboard
    public static boolean willThisMovePutOurKingInCheck(int[][] bitboards, int[] kingLocations, Move move) {

        int currentLocation = move.getCurrentLocation();
        int currentBitboard = move.getCurrentBitboard();
        int nextLocation = move.getMoveLocation();
        int nextBitboard = move.getNextBitboard();


        //creates temporary bitboards to run isKingChecked on;
        int[][] tempBitboards = new int[12][120];
        int[] tempKingLocations = Arrays.copyOf(kingLocations, 2);


        for (int i = 0; i < 12; i++) {
            System.arraycopy(bitboards[i], 0, tempBitboards[i], 0, 120);
        }

        //simulates move change
        tempBitboards[currentBitboard][currentLocation] = 0;
        if (nextBitboard != -1) {
            tempBitboards[nextBitboard][nextLocation] = 0;
        }
        tempBitboards[currentBitboard][nextLocation] = 1;

        //simulates king location change
        if (currentBitboard == 4 || currentBitboard == 10) {
            tempKingLocations[currentBitboard / 6] = nextLocation;
        }

        return BoardMethods.isKingChecked(tempBitboards, tempKingLocations, currentBitboard / 6);
    }

    //finds all possible moves for a side
    public static MoveSet allAvailableMoves(int[][] bitboards, int[] kingLocations, int side) {
        MoveSet possibleMovesForSide = new MoveSet();
        for (int i = side * 6; i < (side * 6) + 6; i++) {
            for (int j = 0; j < bitboards[0].length; j++) {
                if (bitboards[i][j] != 1) {
                    continue;
                }
                //checks each piece to see if it removes the check
                MoveSet possibleMovesForPiece = possibleMoveFinderAllPieces(j, bitboards, i);
                possibleMovesForSide.addAll(possibleMovesForPiece);
                for (Move possibleMove : possibleMovesForPiece) {
                    //if check is maintained then remove the move. it is impossible to make
                    if (willThisMovePutOurKingInCheck(bitboards, kingLocations, possibleMove)) {
                        possibleMovesForSide.remove(possibleMove.getMoveLocation());
                    }
                }

            }
        }
        return possibleMovesForSide;
    }
}