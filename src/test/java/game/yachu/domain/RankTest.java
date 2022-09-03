package game.yachu.domain;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class RankTest {

    @Test
    void FULL_HOUSE를_계산할_수_있다() {
        List<Dice> dices1 = List.of(
                new Dice(1), new Dice(1), new Dice(1),
                new Dice(2), new Dice(2)
        );

        Category fullHouse1 = calcAndGetCategory(dices1, Genealogy.FULL_HOUSE);
        assertThat(fullHouse1.getPoint()).isEqualTo(7);

        List<Dice> dices2 = List.of(
                new Dice(2), new Dice(2), new Dice(2),
                new Dice(6), new Dice(6)
        );

        Category fullHouse2 = calcAndGetCategory(dices2, Genealogy.FULL_HOUSE);
        assertThat(fullHouse2.getPoint()).isEqualTo(18);
    }

    @Test
    void FULL_HOUSE는_YACHU의_조합에서도_점수를_획득할_수_있다() {
        List<Dice> dices1 = List.of(
                new Dice(1), new Dice(1), new Dice(1),
                new Dice(1), new Dice(1)
        );

        Category fullHouse1 = calcAndGetCategory(dices1, Genealogy.FULL_HOUSE);
        assertThat(fullHouse1.getPoint()).isEqualTo(5);

        List<Dice> dices2 = List.of(
                new Dice(6), new Dice(6), new Dice(6),
                new Dice(6), new Dice(6)
        );

        Category fullHouse2 = calcAndGetCategory(dices2, Genealogy.FULL_HOUSE);
        assertThat(fullHouse2.getPoint()).isEqualTo(30);
    }

    private static Category calcAndGetCategory(List<Dice> dices, Genealogy genealogy) {
        Rank rank = new Rank(dices);
        Score calculated = rank.calculate();

        return calculated.getCategories().get(genealogy.getIndex());
    }

}