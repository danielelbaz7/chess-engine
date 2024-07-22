public class Board {

    //declares white pieces
    int[] whiteRooks, whiteKnights, whiteBishops, whiteQueen, whiteKing, whitePawns = new int[120];

    //black pieces
    int[] blackRooks, blackKnights, blackBishops, blackQueen, blackKing, blackPawns = new int[120];

    int[][] pieceBoards = {blackRooks, blackKnights, blackBishops, blackQueen, blackKing, blackPawns,
            whiteRooks, whiteKnights, whiteBishops, whiteQueen, whiteKing, whitePawns};

    //determines side to move
    double evaluationValue = 0;
    boolean whiteTurn = true;
    boolean[] kingsChecked = {false, false};
    int[] kingLocations = {-1, -1};
    final ValidMoves vm = new ValidMoves(this);

    //creates a default board template used to generate the bitboards
    private static final String[][] boardTemplate = {
            {"2", "2", "2", "2", "2", "2", "2", "2", "2", "2"},
            {"2", "2", "2", "2", "2", "2", "2", "2", "2", "2"},
            {"2", "r", "n", "b", "q", "k", "b", "n", "r", "2"},
            {"2", "p", "p", "p", "p", "p", "p", "p", "p", "2"},
            {"2", " ", " ", " ", " ", " ", " ", " ", " ", "2"},
            {"2", " ", " ", " ", " ", " ", " ", " ", " ", "2"},
            {"2", " ", " ", " ", " ", " ", " ", " ", " ", "2"},
            {"2", " ", " ", " ", " ", " ", " ", " ", " ", "2"},
            {"2", "P", "P", "P", "P", "P", "P", "P", "P", "2"},
            {"2", "R", "N", "B", "Q", "K", "B", "N", "R", "2"},
            {"2", "2", "2", "2", "2", "2", "2", "2", "2", "2"},
            {"2", "2", "2", "2", "2", "2", "2", "2", "2", "2"}
    };

    public Board() {
        this.generateBoards();
        evaluationValue = evaluateBoard(pieceBoards, whiteTurn, kingLocations);
    }

    //initializes the bitboards
    private void createBitboards(int[][] bitboards) {
        //runs the creator for each individual board
        for (int i = 0; i < 12; i++) {
            //logs the current board
            int[] currentBoard = new int[120];
            //base board without any logged pieces
            StringBuilder baseLong = new StringBuilder
                    ("222222222222222222222000000002200000000220000000022000000002200000000220000000022000000002200000000222222222222222222222");
            for (int j = 0; j < 120; j++) {
                String currentPiece = boardTemplate[j / 10][j % 10];

                if (i == 0) {
                    if (currentPiece.equals("r")) {
                        baseLong.replace(j, j + 1, "1");
                    }
                }
                if (i == 1) {
                    if (currentPiece.equals("n")) {
                        baseLong.replace(j, j + 1, "1");
                    }
                }
                if (i == 2) {
                    if (currentPiece.equals("b")) {
                        baseLong.replace(j, j + 1, "1");
                    }
                }
                if (i == 3) {
                    if (currentPiece.equals("q")) {
                        baseLong.replace(j, j + 1, "1");
                    }
                }
                if (i == 4) {
                    if (currentPiece.equals("k")) {
                        baseLong.replace(j, j + 1, "1");
                        kingLocations[0] = j;
                    }
                }
                if (i == 5) {
                    if (currentPiece.equals("p")) {
                        baseLong.replace(j, j + 1, "1");
                    }
                }
                if (i == 6) {
                    if (currentPiece.equals("R")) {
                        baseLong.replace(j, j + 1, "1");
                    }
                }
                if (i == 7) {
                    if (currentPiece.equals("N")) {
                        baseLong.replace(j, j + 1, "1");
                    }
                }
                if (i == 8) {
                    if (currentPiece.equals("B")) {
                        baseLong.replace(j, j + 1, "1");
                    }
                }
                if (i == 9) {
                    if (currentPiece.equals("Q")) {
                        baseLong.replace(j, j + 1, "1");
                    }
                }
                if (i == 10) {
                    if (currentPiece.equals("K")) {
                        baseLong.replace(j, j + 1, "1");
                        kingLocations[1] = j;
                    }
                }
                if (i == 11) {
                    if (currentPiece.equals("P")) {
                        baseLong.replace(j, j + 1, "1");
                    }
                }

            }
            for (int k = 0; k < 120; k++) {
                currentBoard[k] = Integer.parseInt(baseLong.substring(k, k + 1));
            }
            bitboards[i] = currentBoard;
        }
    }

    //sets the boardlist to the newly generated boards and prints the board to the console
    public void generateBoards() {
        createBitboards(pieceBoards);
        for (int i = 0; i < 120; i++) {
            if (i % 10 == 0) {
                System.out.println();
            }
            System.out.print(pieceBoards[5][i]);
        }
        System.out.println();
    }

    //gui usage
    public boolean isThereAPieceThere(int rowpass, int colpass) {
        int startingIndex = 0;
        if (Boolean.TRUE.equals(whiteTurn)) {
            startingIndex = 6;
        }
        int pieceIndex = (((rowpass * 8) + 20) + (colpass + 1)) + (rowpass * 2);
        for (int i = startingIndex; i < startingIndex + 6; i++) {
            if (pieceBoards[i][pieceIndex] == 1) {
                return true;
            }
        }
        return false;
    }

    //moved to board as it made more sense, checks the current board value
    //no longer static to remove static global state

    public static boolean isKingChecked(int[][] bitboards, int side, int[] kingLocations) {
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

    public static boolean isKingCheckmated(int[][] bitboards, int side, int[] kingLocations) {
        return isKingChecked(bitboards, side, kingLocations) && ValidMoves.allAvailableMoves(bitboards, kingLocations, side).isEmpty();
    }

    //evaluates which side is winning and by how much
    public static double evaluateBoard(int[][] bitboards, boolean isWhiteTurn, int[] kingLocations) {
        //sets score to 1000 or -1000 if in check mate
        double totalScore = 0;
        int sideToCheck = isWhiteTurn ? 1 : 0;
        if (isKingChecked(bitboards, sideToCheck, kingLocations)) {
            if (ValidMoves.allAvailableMoves(bitboards, kingLocations, sideToCheck).isEmpty()) {
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
            for (int j = 0; j < bitboards[i].length; j++) {
                if (bitboards[i][j] == 1) {
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
                        for (Move possibleMove : ValidMoves.possibleMoveFinderAllPieces(j, bitboards, i)) {
                            //if it is not a capture add 0.05 for every possible moves
                            if (possibleMove.getNextBitboard() == -1) {
                                totalScore += negativeOrPositive * 0.05;
                            } else {
                                //if the other piece can attack our tested piece, add to the total score the basevalue
                                // of our piece minus the basevalue of the other piece to determine the value of the attack
                                if (ValidMoves.possibleMoveFinderAllPieces(possibleMove.getMoveLocation(), bitboards, i).containsMove(j)) {
                                    System.out.println(possibleMove.getMoveLocation() + "IS THE PIECE WE ARE ATTACKING");
                                    totalScore += (baseValue - getBaseValue(possibleMove.getNextBitboard(), bitboards)) * -1;
                                } else {
                                    totalScore += (getBaseValue(possibleMove.getMoveLocation(), bitboards) * -1);
                                }
                            }
                        }
                    }
                }
            }
        }

        return (double) Math.round(totalScore * 10) / 10;
    }

    //performs a move
    public int[][] doMove(int[][] bitboards, Move move) {
        bitboards[move.getCurrentBitboard()][move.getCurrentLocation()] = 0;
        if(move.getNextBitboard() != -1) {
            bitboards[move.getNextBitboard()][move.getMoveLocation()] = 0;
        }
        bitboards[move.getCurrentBitboard()][move.getMoveLocation()] = 1;

        //simulates king move
        if(move.getCurrentBitboard() == 4) {
            kingLocations[0] = move.getMoveLocation();
        }
        else if(move.getCurrentBitboard() == 10) {
            kingLocations[1] = move.getMoveLocation();
        }

        return bitboards;
    }

    //takes a location and set of boards to find the current bitboard of a piece
    private static int getBaseValue(int location, int[][] bitboards) {
        int boardWithPiece = -1;

        for (int i = 0; i < 12; i++) {
            if (bitboards[i][location] == 1) {
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

    public static String[][] getBoardTemplate() {
        return boardTemplate;
    }

}