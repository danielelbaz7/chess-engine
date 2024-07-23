import java.util.HashSet;

public class MoveSet extends HashSet<Move> {

    public MoveSet() {

    }

    public MoveSet(MoveSet moves) {
        this.addAll(moves);
    }

    public boolean containsMove(int moveToFind) {
        for (Move m : this) {
            if (m.getMoveLocation() == moveToFind) {
                return true;
            }
        }
        return false;
    }

    public boolean remove(int moveLocation) {
        for (Move m : this) {
            if (m.getMoveLocation() == moveLocation) {
                return remove(m);
            }
        }
        return false;
    }

}
