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
        return Score.builder()
                .aces(calcEyes(1))
                .deuces(calcEyes(2))
                .threes(calcEyes(3))
                .fours(calcEyes(4))
                .fives(calcEyes(5))
                .sixes(calcEyes(6))
                .choice(calcChoice())
                .fourOfKind(calcFourOfKind())
                .fullHouse(calcFullHouse())
                .smallStraight(calcSmallStraight())
                .largeStraight(calcLargeStraight())
                .yachu(calcYachu())
                .build();
    }

    private int calcEyes(int index) {
        return index * count[index];
    }

    private int calcChoice() {
        int total = 0;
        for (int index = 1; index <= 6; index++){
            total += index * count[index];
        }
        return total;
    }

    private int calcFourOfKind() {
        for (int index = 1; index <= 6; index++){
            if (count[index] >= 4) {
                return calcChoice();
            }
        }
        return 0;
    }

    private int calcFullHouse() {
        if (checkTriple() && checkPair()) {
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

    private boolean checkStraight(int from, int to){
        for (int index = from; index <= to; index++) {
            if (count[index] < 1) return false;
        }
        return true;
    }

    private int calcYachu() {
        for (int index = 1; index <= 6; index++) {
            if (count[index] == 5) {
                return 50;
            }
        }
        return 0;
    }
}
