package game.yachu.controller.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class RollRequest {
    private List<Boolean> fixStates;

    public RollRequest(List<Boolean> fixStates) {
        this.fixStates = fixStates;
    }
}
