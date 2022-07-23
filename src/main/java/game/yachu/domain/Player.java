package game.yachu.domain;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Player {
    private List<Dice> dices;
    private Score score;

    public Player() {
        final int initialValue = 1;
        dices = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            dices.add(new Dice(initialValue));
        }
        score = new Score();
    }

    public List<Dice> rollDices() {
        for (Dice dice : dices) {
            if (dice.isFixed()) {
                continue;
            }
            dice.roll();
        }
        return dices;
    }

    public Dice getDice(int index) {
        return dices.get(index);
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
}