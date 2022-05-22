package game.yachu.domain;

import lombok.Getter;

@Getter
public class Dice {
    private int value;
    private boolean isFixed;

    public Dice(int value) {
        if (isOutOfRange(value)) {
            throw new IllegalArgumentException("주사위 눈금은 1에서 6까지의 값만 가질 수 있습니다. value: " + value);
        }
        this.value = value;
    }

    public void roll() {
        this.value = RandomUtil.randomBetweenOneAndSix();
    }

    public void changeFixedState() {
        this.isFixed = !this.isFixed;
    }

    private boolean isOutOfRange(int value) {
        return value <= 0 || value > 6;
    }
}
