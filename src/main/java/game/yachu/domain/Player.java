package game.yachu.domain;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private List<Dice> dices;
    private Score score;

    public Player() {
        final int initialValue = 1;
        dices = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            dices.add(new Dice(initialValue));
        }
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

    public void setScore(Rank rank, Genealogy select) {
        Score genealogy = rank.calculate();

        switch (select) {
            case ACES:
                score.setAces(genealogy.getAces());
                break;
            case DEUCES:
                score.setDeuces(genealogy.getDeuces());
                break;
            case THREES:
                score.setThrees(genealogy.getThrees());
                break;
            case FOURS:
                score.setFours(genealogy.getFours());
                break;
            case FIVES:
                score.setFives(genealogy.getFives());
                break;
            case SIXES:
                score.setSixes(genealogy.getSixes());
                break;
            case CHOICE:
                score.setChoice(genealogy.getChoice());
                break;
            case FOUR_OF_KIND:
                score.setFourOfKind(genealogy.getFourOfKind());
                break;
            case FULL_HOUSE:
                score.setFullHouse(genealogy.getFullHouse());
                break;
            case SMALL_STRAIGHT:
                score.setSmallStraight(genealogy.getSmallStraight());
                break;
            case LARGE_STRAIGHT:
                score.setLargeStraight(genealogy.getLargeStraight());
                break;
            case YACHU:
                score.setYachu(genealogy.getYachu());
                break;
        }
    }
}