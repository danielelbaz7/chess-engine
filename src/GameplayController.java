import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;
import java.util.Map;

public class GameplayController implements MouseListener {

    int selectedPiece = -1;
    HashMap<Integer, Integer> possibleMovesForSelectedPiece = null;
    int selectedPieceType = -1;
    Board board;
    ValidMoves vm;
    ChessGUI chessGUI;

    public GameplayController(ChessGUI c) {
        this.chessGUI = c;
        board = chessGUI.board;
        vm = new ValidMoves(board);
    }

    //makes the selected square gray and the possible moves red
    private void setPieceSelectedGUI(int rowpass, int colpass) {
        selectedPiece = (Conv.to120RC(rowpass, colpass));
        possibleMovesForSelectedPiece = vm.possibleMoveFinderAllPieces(selectedPiece, board.pieceBoards);
        //sets the current piece type
        for (int i = 0; i < 12; i++) {
            if (board.pieceBoards[i][Conv.to120RC(rowpass, colpass)] == 1) {
                selectedPieceType = i;
            }
        }
        int otherSide = selectedPieceType > 5 ? 0 : 1;

        //iterates through all possible moves and put moves that check into a hashset to avoid concurrent modification exception
        HashMap<Integer, Integer> movesToRemove = new HashMap<>();
        for (Map.Entry<Integer, Integer> possibleMove : possibleMovesForSelectedPiece.entrySet()) {
            System.out.println(selectedPiece + ": " + selectedPieceType + ", " + possibleMove.getKey() + ": " + possibleMove.getValue());
            if (vm.willThisMovePutOurKingInCheck(selectedPiece, selectedPieceType, possibleMove.getKey(), possibleMove.getValue())) {
                movesToRemove.put(possibleMove.getKey(), possibleMove.getValue());
            }
        }

        for (Map.Entry<Integer, Integer> moveToRemove : movesToRemove.entrySet()) {
            possibleMovesForSelectedPiece.remove(moveToRemove.getKey());
        }

        //removes king from possible moves if in check
        if (possibleMovesForSelectedPiece.containsKey(board.kingLocations[otherSide])) {
            possibleMovesForSelectedPiece.remove(board.kingLocations[otherSide]);
            board.kingsChecked[0] = true;
        }

        if ((chessGUI.isPieceSelected() == 0)) {
            chessGUI.JLabelCollection[(rowpass * 8) + colpass].setBackground(Color.DARK_GRAY);
            chessGUI.setPieceSelectedTrue(rowpass, colpass);
            //handles showing the possible moves
            for (Map.Entry<Integer, Integer> possibleMove : possibleMovesForSelectedPiece.entrySet()) {
                //checks for capture
                if (possibleMove.getValue() != -1) {
                    chessGUI.JLabelCollection[Conv.to64From120(possibleMove.getKey())].setBackground(Color.GREEN);
                    continue;
                }
                chessGUI.JLabelCollection[Conv.to64From120(possibleMove.getKey())].setBackground(Color.RED);
            }
        }

        //deselects and sets colors back
        else if ((chessGUI.getPointSelected() - colpass) - (rowpass * 8) == 0) {
            selectedPiece = -1;
            selectedPieceType = -1;
            setSquareToOriginalColor(rowpass, colpass);
            for (Map.Entry<Integer, Integer> possibleMoveToRemove : possibleMovesForSelectedPiece.entrySet()) {
                //checks for capture
                setSquareToOriginalColor(Conv.toRC120(possibleMoveToRemove.getKey())[0],
                        Conv.toRC120(possibleMoveToRemove.getKey())[1]);
            }
            chessGUI.setPieceSelectedFalse();
            possibleMovesForSelectedPiece = null;
        }

        chessGUI.placeBoardsAgain();
    }

    //sets square back to its original color
    private void setSquareToOriginalColor(int rowpass, int colpass) {
        if (rowpass % 2 == 0) {
            if (colpass % 2 == 0) {
                chessGUI.JLabelCollection[(rowpass * 8) + colpass].setBackground(new Color(225, 215, 183));
            } else {
                chessGUI.JLabelCollection[(rowpass * 8) + colpass].setBackground(new Color(57, 40, 20));
            }
        } else if (colpass % 2 == 0) {
            chessGUI.JLabelCollection[(rowpass * 8) + colpass].setBackground(new Color(57, 40, 20));
        } else {
            chessGUI.JLabelCollection[(rowpass * 8) + colpass].setBackground(new Color(225, 215, 183));
        }
    }

    public int[][] movePiece(int row, int col, int[][] bitboards) {
        int currentSquare = Conv.to120RC(row, col);
        boolean onPossibleSquare = false;
        boolean capturingMove = false;
        if (possibleMovesForSelectedPiece == null) {
            return bitboards;
        }
        //searches through all possible moves for selected piece
        for (Map.Entry<Integer, Integer> currentPossibleMove : possibleMovesForSelectedPiece.entrySet()) {
            //checks for capture
            if (currentPossibleMove.getValue() != -1) {
                capturingMove = true;
            }
            //if the player clicks on a possible move, indicate that with the boolean
            if (currentPossibleMove.getKey() == currentSquare) {
                onPossibleSquare = true;
                break;
            }
        }

        //if the player clicks on a not possible move return, no piece to move
        if (!onPossibleSquare) {
            return bitboards;
        }

        //sets the bitboard spot of the captured piece to 0 if the move is a capture
        if (capturingMove) {
            for (int i = 0; i < selectedPieceType; i++) {
                if (bitboards[i][currentSquare] == 1) {
                    bitboards[i][currentSquare] = 0;
                }
            }
            if (selectedPieceType != 11) {
                for (int j = selectedPieceType + 1; j < 12; j++) {
                    if (bitboards[j][currentSquare] == 1) {
                        bitboards[j][currentSquare] = 0;
                    }
                }
            }
        }

        //removes king from being a possible capture, 4 for black king 10 for white.
        if (selectedPieceType == 4) {
            board.kingLocations[0] = currentSquare;
        } else if (selectedPieceType == 10) {
            board.kingLocations[1] = currentSquare;
        }

        bitboards[selectedPieceType][currentSquare] = 1;
        bitboards[selectedPieceType][selectedPiece] = 0;

        setSquareToOriginalColor(Conv.toRC120(selectedPiece)[0], Conv.toRC120(selectedPiece)[1]);
        for (Map.Entry<Integer, Integer> possibleMoveToRemove : possibleMovesForSelectedPiece.entrySet()) {
            //checks for capture
            //resets square of possible highlighted move
            setSquareToOriginalColor(Conv.toRC120(possibleMoveToRemove.getKey())[0],
                    Conv.toRC120(possibleMoveToRemove.getKey())[1]);
        }

        chessGUI.JLabelCollection[Conv.to64From120(currentSquare)].setIcon(chessGUI.JLabelCollection[Conv.to64From120(selectedPiece)].getIcon());
        chessGUI.JLabelCollection[Conv.to64From120(selectedPiece)].setIcon(null);

        selectedPiece = -1;
        selectedPieceType = -1;
        possibleMovesForSelectedPiece = null;
        board.whiteTurn = !board.whiteTurn;

        chessGUI.setPieceSelectedFalse();

        return bitboards;

    }

    //sets the selected square dark gray if there is a playable piece there
    @Override
    public void mousePressed(MouseEvent e) {
        int row = (int) (((double) e.getY()) / ((double) ChessGUI.dimension / 16) + 0.5);
        int col = e.getX() / (ChessGUI.dimension / 16);
        if (board.isThereAPieceThere(row, col)) {
            setPieceSelectedGUI(row, col);
        } else {
            //controls checks
            board.pieceBoards = movePiece(row, col, board.pieceBoards);
            //resets to find new evaluation value
            board.evaluationValue = board.evaluateBoard(board.pieceBoards, board.whiteTurn);
            if (Math.abs(board.evaluationValue) != 1000) {
                chessGUI.evaluationValuePanel.setText(String.valueOf(board.evaluationValue));
            } else {
                switch ((int) board.evaluationValue) {
                    case -1000 -> chessGUI.evaluationValuePanel.setText("-M");
                    case 1000 -> chessGUI.evaluationValuePanel.setText("M");
                }
            }
            int sideToCheck = board.whiteTurn ? 1 : 0;
            int otherSide = sideToCheck == 1 ? 0 : 1;
            if (board.kingsChecked[otherSide]) {
                board.kingsChecked[otherSide] = false;
            } else if (board.isKingChecked(board.pieceBoards, sideToCheck, board.kingLocations)) {
                board.kingsChecked[sideToCheck] = true;
                //game over is printed outside the method as isCheckMated is used in simulation as well
                if (vm.allAvailableMoves(board.pieceBoards, sideToCheck).isEmpty()) {
                    System.out.println("GAME OVER");
                }
            }
        }

    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
