package game.yachu.domain;

public class Dice {
    private int value;

    public Dice(int value) {
        if (value <= 0 || value > 6) {
            throw new IllegalArgumentException("주사위 눈금은 1에서 6까지의 값만 가질 수 있습니다. value: " + value);
        }
        this.value = value;
    }

}
