package game.yachu.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class DiceTest {

    @Test
    void 주사위는_1부터_6까지의_값만_가진다() {
        assertThatThrownBy(() -> new Dice(0))
                .isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() -> new Dice(7))
                .isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() -> new Dice(-1))
                .isInstanceOf(IllegalArgumentException.class);

    }

    @Test
    void 주사위를_굴린다() {
        Dice dice = new Dice(1);
        for (int i = 0; i < 10; i++) {
            dice.roll();
            System.out.println(dice.getValue());
        }
    }
}