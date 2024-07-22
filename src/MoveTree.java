import java.util.ArrayList;
import java.util.List;

public class MoveTree<K, V> {

    private Node<K, V> root;

    public MoveTree(K move, V enemyPieceType) {
        root = new Node<K, V>();
        root.move = move;
        root.enemyPieceType = enemyPieceType;
        root.children = new ArrayList<Node<K, V>>();
    }

    public static class Node<K, V> {
        private K move;
        private V enemyPieceType;
        private Node<K, V> parent;
        private List<Node<K, V>> children;

    }

}
