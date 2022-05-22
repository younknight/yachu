package game.yachu.domain;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private List<Dice> dices;

    public Player() {
        final int initialValue = 1;
        dices = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            dices.add(new Dice(initialValue));
        }
    }

    public void rollDices() {
        for (Dice dice : dices) {
            if (dice.isFixed()) {
                continue;
            }
            dice.roll();
        }
    }

}