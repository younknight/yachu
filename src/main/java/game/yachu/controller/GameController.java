package game.yachu.controller;

import game.yachu.controller.request.GainRequest;
import game.yachu.controller.request.RollRequest;
import game.yachu.controller.response.DiceResponse;
import game.yachu.controller.response.LoadResponse;
import game.yachu.domain.*;
import game.yachu.repository.GameStateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

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
    @PostMapping("/api/{id}/load")
    public LoadResponse load(@PathVariable Long id) {
        Player player = repository.get(id);
        List<Integer> diceValues = player.getDices().stream()
                .map(Dice::getValue)
                .collect(Collectors.toList());
        Score diceScore = getDiceScore(player, player.getDices());
        return new LoadResponse(player.getChance(), diceValues, player.getScore(), diceScore);
    }

    @ResponseBody
    @PostMapping("/api/{id}/roll")
    public DiceResponse roll(@PathVariable Long id, @RequestBody RollRequest rollRequest) {
        Player player = repository.get(id);
        List<Dice> dices = player.rollDices(rollRequest.getFixStates());
        Score calculated = getDiceScore(player, dices);
        return new DiceResponse(dices, calculated, player.getChance());
    }

    private static Score getDiceScore(Player player, List<Dice> dices) {
        Rank rank = new Rank(dices);
        Score calculated = rank.calculate();
        calculated.hasGained(player.getScore());
        return calculated;
    }

    @ResponseBody
    @PostMapping("/api/{id}/gain")
    public void gain(@PathVariable("id") Long id, @RequestBody GainRequest request) {
        Player player = repository.get(id);
        player.setScore(Genealogy.valueOf(request.getCategory()), request.getGained());
        player.resetState();
    }
}
