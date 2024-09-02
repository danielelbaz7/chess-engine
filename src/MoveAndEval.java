public class MoveAndEval<Move, Double> {

    public Move getMove() {
        return move;
    }

    public Double getEval() {
        return eval;
    }

    private Move move;
    private Double eval;

    public MoveAndEval(Move move, Double eval) {
        this.move = move;
        this.eval = eval;
    }

}
