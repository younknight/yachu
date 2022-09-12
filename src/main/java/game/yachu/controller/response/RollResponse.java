package game.yachu.controller.response;

import game.yachu.domain.Dice;
import game.yachu.domain.Score;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class RollResponse {
    private List<Dice> dices;
    private Score score;
    private int chance;

    public RollResponse(List<Dice> dices, Score score, int chance) {
        this.dices = dices;
        this.score = score;
        this.chance = chance;
    }
}
