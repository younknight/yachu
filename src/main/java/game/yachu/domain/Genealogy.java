package game.yachu.domain;

import lombok.Getter;

@Getter
public enum Genealogy {
    ACES(0),
    DEUCES(1),
    THREES(2),
    FOURS(3),
    FIVES(4),
    SIXES(5),
    SUBTOTAL(6),
    BONUS(7),
    CHOICE(8),
    FOUR_OF_KIND(9),
    FULL_HOUSE(10),
    SMALL_STRAIGHT(11),
    LARGE_STRAIGHT(12),
    YACHU(13),
    TOTAL(14);

    private final int index;

    Genealogy(int index) {
        this.index = index;
    }
}
