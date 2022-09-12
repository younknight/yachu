package game.yachu.repository.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Record {

    private Long id;
    private String nickname;
    private int score;
    private LocalDateTime dateTime;

    public Record(String nickname, int score) {
        this.nickname = nickname;
        this.score = score;
        this.dateTime = LocalDateTime.now();
    }

    public Record(Long id, String nickname, int score, LocalDateTime dateTime) {
        this.id = id;
        this.nickname = nickname;
        this.score = score;
        this.dateTime = dateTime;
    }
}
