package game.yachu.domain;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Player {
    private List<Dice> dices;
    private Score score;
    private int chance;

    public Player() {
        final int initialValue = 0;
        dices = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            dices.add(new Dice(initialValue));
        }
        score = new Score();
        chance = 0;
    }

    public List<Dice> rollDices(List<Boolean> fixStates) {
        if (chance >= 3) {
            return dices;
        }

        for (int i = 0; i < fixStates.size(); i++) {
            if (fixStates.get(i)) {
                continue;
            }
            dices.get(i).roll();
        }
        chance++;
        return dices;
    }

    public void setScore(Genealogy select, int gained) {
        switch (select) {
            case ACES:
                score.setAces(gained);
                break;
            case DEUCES:
                score.setDeuces(gained);
                break;
            case THREES:
                score.setThrees(gained);
                break;
            case FOURS:
                score.setFours(gained);
                break;
            case FIVES:
                score.setFives(gained);
                break;
            case SIXES:
                score.setSixes(gained);
                break;
            case CHOICE:
                score.setChoice(gained);
                break;
            case FOUR_OF_KIND:
                score.setFourOfKind(gained);
                break;
            case FULL_HOUSE:
                score.setFullHouse(gained);
                break;
            case SMALL_STRAIGHT:
                score.setSmallStraight(gained);
                break;
            case LARGE_STRAIGHT:
                score.setLargeStraight(gained);
                break;
            case YACHU:
                score.setYachu(gained);
                break;
        }
    }

    public boolean isOver() {
        return score.isFull();
    }

    public void resetState() {
        chance = 0;
        dices.forEach(Dice::resetValue);
    }
}