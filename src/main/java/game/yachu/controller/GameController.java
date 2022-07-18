package game.yachu.controller;

import game.yachu.controller.response.DiceResponse;
import game.yachu.domain.Dice;
import game.yachu.domain.Player;
import game.yachu.repository.GameStateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class GameController {

    private final GameStateRepository repository;

    @Autowired
    public GameController(GameStateRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/game/{id}")
    public String game(@PathVariable Long id) {
        System.out.println("id: " + id);
        return "gamePage";
    }

    @ResponseBody
    @PostMapping("/api/new")
    public Long newGame() {
        return repository.newGame();
    }

    @ResponseBody
    @PostMapping("/api/{id}/roll")
    public DiceResponse roll(@PathVariable Long id) {
        Player player = repository.get(id);
        List<Dice> dices = player.rollDices();
        return new DiceResponse(dices);
    }

    @ResponseBody
    @PostMapping("/api/{id}/toggle/{index}")
    public void toggle(@PathVariable("id") Long id, @PathVariable("index") int index) {
        Player player = repository.get(id);
//        System.out.println("[toggle] selected dice: " + (index + 1));
        Dice dice = player.getDice(index);
//        System.out.println("[toggle] current fixed state: " + dice.isFixed());
        dice.changeFixedState();
//        System.out.println("[toggle] change fixed state: " + dice.isFixed());
    }
}
