import java.util.HashSet;

public class MoveSet extends HashSet<Move> {

    public boolean containsMove(int moveToFind) {
        for (Move m : this) {
            if (m.getMoveLocation() == moveToFind) {
                return true;
            }
        }
        return false;
    }
}
