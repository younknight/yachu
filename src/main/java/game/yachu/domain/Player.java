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
        score.gainPoint(select, gained);
    }

    public boolean isOver() {
        return score.isFull();
    }

    public void resetState() {
        chance = 0;
        dices.forEach(Dice::resetValue);
    }
}