package game.yachu.controller.listener;

import game.yachu.repository.GameStateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class SessionListener implements HttpSessionListener {

    private final GameStateRepository repository;

    @Override
    public void sessionDestroyed(HttpSessionEvent event) {
        HttpSession session = event.getSession();
        Long id = (Long) session.getAttribute("id");
        log.info("session destroyed - id = {}", id);
        repository.deleteGame(id);
    }
}
