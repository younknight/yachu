package game.yachu.controller.response;

import game.yachu.domain.Dice;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class DiceResponse {
    private List<Dice> dices;

    public DiceResponse(List<Dice> dices) {
        this.dices = dices;
    }
}
