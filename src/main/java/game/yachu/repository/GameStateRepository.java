package game.yachu.repository;

import game.yachu.domain.Player;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class GameStateRepository {

    public static Long id = 0L;
    private final Map<Long, Player> gameState = new ConcurrentHashMap<>();

    public Long newGame() {
        gameState.put(++id, new Player());
        return id;
    }

    public Player get(Long key) {
        return Optional.ofNullable(gameState.get(key))
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 key 입니다. key = " + key));
    }

    public void deleteGame(Long key) {
        gameState.remove(key);
    }
}
