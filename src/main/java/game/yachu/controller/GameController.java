package game.yachu.controller;

import game.yachu.repository.GameStateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class GameController {

    private final GameStateRepository repository;

    @Autowired
    public GameController(GameStateRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/game/{id}")
    public String game(Model model, @PathVariable Long id) {
        model.addAttribute("id", id);
        return "gamePage";
    }

    @ResponseBody
    @PostMapping("/api/new")
    public Long newGame() {
        return repository.newGame();
    }
}
