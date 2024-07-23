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
        evaluationValue = BoardMethods.evaluateBoard(pieceBoards, kingLocations, whiteTurn);
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



    //performs a move
    public int[][] makeMove(Move move) {
        pieceBoards[move.getCurrentBitboard()][move.getCurrentLocation()] = 0;
        if(move.getNextBitboard() != -1) {
            pieceBoards[move.getNextBitboard()][move.getMoveLocation()] = 0;
        }
        pieceBoards[move.getCurrentBitboard()][move.getMoveLocation()] = 1;

        //simulates king move
        if(move.getCurrentBitboard() == 4) {
            kingLocations[0] = move.getMoveLocation();
        }
        else if(move.getCurrentBitboard() == 10) {
            kingLocations[1] = move.getMoveLocation();
        }

        return pieceBoards;
    }

    public int[][] undoMove(Move move) {
        pieceBoards[move.getCurrentBitboard()][move.getCurrentLocation()] = 1;
        if(move.getNextBitboard() != -1) {
            pieceBoards[move.getNextBitboard()][move.getMoveLocation()] = 1;
        }
        pieceBoards[move.getCurrentBitboard()][move.getMoveLocation()] = 0;

        //simulates king move
        if(move.getCurrentBitboard() == 4) {
            kingLocations[0] = move.getCurrentLocation();
        }
        else if(move.getCurrentBitboard() == 10) {
            kingLocations[1] = move.getCurrentLocation();
        }

        return pieceBoards;
    }

    public String[][] getBoardTemplate() {
        return boardTemplate;
    }

}