import java.util.Arrays;
import java.util.Iterator;
import java.util.Scanner;

public class NIM {

    public static Scanner input = new Scanner(System.in);

    public static class Move {

        public int pile;
        public int count;

        public Move(int pile, int count) {
            this.pile = pile;
            this.count = count;
        }

        @Override
        public String toString() {
            return "(" + pile + "," + count + ")";
        }
    }

    public static class State implements Iterable<Move> {

        private int[] counts;

        public State(int[] counts) {
            this.counts = Arrays.copyOf(counts, counts.length);
        }

        public boolean won() {
            for (int count : this.counts) {
                if (count > 0) return false;
            }
            return true;
        }

        public State next(Move move) {
            assert move.count > 0;
            assert this.counts[move.pile] >= move.count;
            int[] counts = Arrays.copyOf(this.counts, this.counts.length);
            counts[move.pile] -= move.count;
            return new State(counts);
        }

        @Override
        public String toString() {
            return Arrays.toString(this.counts);
        }

        public Iterator<Move> iterator() {
            return new MoveIterator(this);
        }

        public class MoveIterator implements Iterator<Move> {

            private State state;
            private int pile;
            private int count;
            private int remain;

            public MoveIterator(State state) {
                this.state = state;
                this.pile = 0;
                this.count = 1;

                for (int count : this.state.counts) {
                    this.remain += count;
                }
            }


            public boolean hasNext() {
                return this.remain > 0;
            }

            public Move next() {
                while (this.pile < this.state.counts.length &&
                        this.count > this.state.counts[this.pile]) {
                    this.count = 1;
                    this.pile++;
                }
                this.remain--;
                return new Move(this.pile, this.count++);
            }
        }
    }



    public static Move bestMove(State state) {

        Move result = null;
        int max = Integer.MIN_VALUE;

        for (Move m : state){
            int value = minValue(state.next(m));
            if (max < value){
                max = value;
                result = m;
            }
        }
        return result;
    }

    public static int maxValue(State state){

        if (state.won()) return 1;
        int utilityValue = Integer.MIN_VALUE;

        for (Move m : state){
            utilityValue = Math.max(utilityValue, minValue(state.next(m)));
        }
        return utilityValue;
    }

    public static int minValue(State state){

        if (state.won()) return -1;
        int utilityValue = Integer.MAX_VALUE;
        for (Move m : state){
            utilityValue = Math.min(utilityValue, maxValue(state.next(m)));
        }
        return utilityValue;
    }



    public static Move getMove(State state) {
        System.out.print("Pile: ");
        int pile = input.nextInt();
        System.out.print("Count: ");
        int count = input.nextInt();
        return new Move(pile, count);
    }


    public static void main(String[] args) {
        int[] piles = new int[args.length];
        for (int i = 0; i < args.length; i++) {
            piles[i] = Integer.parseInt(args[i]);
        }

        boolean me = true;
        State state = new State(piles);
        while (!state.won()) {
            Move move;
            if (me) {
                move = bestMove(state);
            } else {
                move = getMove(state);
            }
            State next = state.next(move);
            System.out.print(me ? " ME: " : "YOU: ");
            System.out.print(move);
            System.out.print(" ==> " );
            System.out.println(next);
            state = next;
            me = !me;
        }
    }
}

