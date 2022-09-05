package game.yachu.controller.response;

import game.yachu.domain.Score;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GainResponse {
    private Score score;
    private boolean isOver;
}
