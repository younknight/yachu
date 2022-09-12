package game.yachu.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Category {
    private Genealogy genealogy;
    private int point;
    private boolean acquired;

    public void acquire() {
        acquired = true;
    }

    public void gain(int point) {
        this.point += point;
        acquired = true;
    }
}
