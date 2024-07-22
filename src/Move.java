import java.util.Objects;

public class Move {
    //L = current location
    //B = current board
    //M = location to move to
    //N = next board. -1 if not a capture

    private int currentLocation;
    private int currentBitboard;
    private int moveLocation;
    private int nextBitboard;

    public Move(int currentLoc, int currentBoard, int moveLoc, int nextBoard) {
        this.currentLocation = currentLoc;
        this.currentBitboard = currentBoard;
        this.moveLocation = moveLoc;
        this.nextBitboard = nextBoard;
    }


    public int getCurrentLocation() {
        return currentLocation;
    }

    public int getCurrentBitboard() {
        return currentBitboard;
    }

    public int getMoveLocation() {
        return moveLocation;
    }

    public int getNextBitboard() {
        return nextBitboard;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Move move = (Move) o;
        return currentLocation == move.currentLocation && currentBitboard == move.currentBitboard && moveLocation == move.moveLocation && nextBitboard == move.nextBitboard;
    }

}
