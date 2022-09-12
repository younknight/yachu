package game.yachu.domain;

import java.util.List;

public class Rank {
    private int[] count;

    public Rank(List<Dice> dices) {
        count = new int[7]; // 1-index
        for (Dice dice : dices) {
            count[dice.getValue()]++;
        }
    }

    public Score calculate() {
        List<Integer> points = List.of(
                calcEyes(1),
                calcEyes(2),
                calcEyes(3),
                calcEyes(4),
                calcEyes(5),
                calcEyes(6),
                0, // subtotal
                0, // bonus
                calcChoice(),
                calcFourOfKind(),
                calcFullHouse(),
                calcSmallStraight(),
                calcLargeStraight(),
                calcYachu(),
                0 // total
        );
        return new Score(points);
    }

    private int calcEyes(int index) {
        return index * count[index];
    }

    private int calcChoice() {
        int total = 0;
        for (int index = 1; index <= 6; index++) {
            total += index * count[index];
        }
        return total;
    }

    private int calcFourOfKind() {
        for (int index = 1; index <= 6; index++) {
            if (count[index] >= 4) {
                return calcChoice();
            }
        }
        return 0;
    }

    private int calcFullHouse() {
        if (checkTriple() && checkPair() || isYachu()) {
            return calcChoice();
        }
        return 0;
    }

    private boolean checkTriple() {
        for (int index = 1; index <= 6; index++) {
            if (count[index] == 3) return true;
        }
        return false;
    }

    private boolean checkPair() {
        for (int index = 1; index <= 6; index++) {
            if (count[index] == 2) return true;
        }
        return false;
    }

    private int calcSmallStraight() {
        if (checkStraight(1, 4) || checkStraight(2, 5) || checkStraight(3, 6)) {
            return 15;
        }
        return 0;
    }

    private int calcLargeStraight() {
        if (checkStraight(1, 5) || checkStraight(2, 6)) {
            return 30;
        }
        return 0;
    }

    private boolean checkStraight(int from, int to) {
        for (int index = from; index <= to; index++) {
            if (count[index] < 1) return false;
        }
        return true;
    }

    private int calcYachu() {
        if (isYachu()) {
            return 50;
        }
        return 0;
    }

    private boolean isYachu() {
        for (int index = 1; index <= 6; index++) {
            if (count[index] == 5) {
                return true;
            }
        }
        return false;
    }
}
