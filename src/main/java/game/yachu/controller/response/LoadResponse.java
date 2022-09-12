package game.yachu.controller.response;

import game.yachu.domain.Score;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LoadResponse {
    private Integer chance;
    private List<Integer> dices;
    private Score playerScore;
    private Score diceScore;
}
