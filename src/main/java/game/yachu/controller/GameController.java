package game.yachu.controller;

import game.yachu.controller.request.GainRequest;
import game.yachu.controller.request.RecordRequest;
import game.yachu.controller.request.RollRequest;
import game.yachu.controller.response.GainResponse;
import game.yachu.controller.response.LoadResponse;
import game.yachu.controller.response.RollResponse;
import game.yachu.domain.*;
import game.yachu.repository.GameStateRepository;
import game.yachu.repository.RecordRepository;
import game.yachu.repository.dto.Record;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Controller
public class GameController {

    private final GameStateRepository gameStateRepository;
    private final RecordRepository recordRepository;

    @Autowired
    public GameController(GameStateRepository gameStateRepository, RecordRepository recordRepository) {
        this.gameStateRepository = gameStateRepository;
        this.recordRepository = recordRepository;
    }

    @GetMapping("/game/{id}")
    public String game(@PathVariable Long id) {
        System.out.println("id: " + id);
        return "gamePage";
    }

    @ResponseBody
    @PostMapping("/api/new")
    public Long newGame(HttpSession session) {
        Long id = gameStateRepository.newGame();
        session.setAttribute("id", id);
        log.info("session created id = {}, maxInactiveInterval = {}", id, session.getMaxInactiveInterval());
        return id;
    }

    @ResponseBody
    @PostMapping("/api/{id}/load")
    public LoadResponse load(@PathVariable Long id) {
        Player player = gameStateRepository.get(id);
        List<Integer> diceValues = player.getDices().stream()
                .map(Dice::getValue)
                .collect(Collectors.toList());
        Score diceScore = getDiceScore(player, player.getDices());
        return new LoadResponse(player.getChance(), diceValues, player.getScore(), diceScore);
    }

    @ResponseBody
    @PostMapping("/api/{id}/roll")
    public RollResponse roll(@PathVariable Long id, @RequestBody RollRequest rollRequest) {
        Player player = gameStateRepository.get(id);
        List<Dice> dices = player.rollDices(rollRequest.getFixStates());
        Score calculated = getDiceScore(player, dices);
        return new RollResponse(dices, calculated, player.getChance());
    }

    private Score getDiceScore(Player player, List<Dice> dices) {
        Rank rank = new Rank(dices);
        Score calculated = rank.calculate();
        calculated.hasGained(player.getScore());
        return calculated;
    }

    @ResponseBody
    @PostMapping("/api/{id}/gain")
    public GainResponse gain(@PathVariable("id") Long id, @RequestBody GainRequest request) {
        Player player = gameStateRepository.get(id);
        player.setScore(Genealogy.valueOf(request.getCategory()), request.getGained());
        if (player.isOver()) {
            log.info("Game is Over");
            gameStateRepository.deleteGame(id);
            return new GainResponse(player.getScore(), true);
        }
        player.resetState();
        return new GainResponse(player.getScore(), false);
    }

    @ResponseBody
    @GetMapping("/api/record")
    public List<Record> findTop10() {
        return recordRepository.findTop10();
    }

    @ResponseBody
    @PostMapping("/api/record/new")
    public Long save(@RequestBody RecordRequest request) {
        Record record = new Record(request.getNickname(), request.getScore());
        recordRepository.save(record); // record id 삽입
        log.info("Record Save - id={}", record.getId());
        return record.getId();
    }
}
