package game.yachu.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Score {
    private int aces; // 1기 방패
    private int deuces; // 2기 방패
    private int threes;
    private int fours;
    private int fives;
    private int sixes;
    private int choice; // 초기방패
    private int fourOfKind;
    private int fullHouse;
    private int smallStraight;//+15
    private int largeStraight;//+30
    private int yachu;//+50

    public Score() {
        aces = -2;
        deuces = -2;
        threes = -2;
        fours = -2;
        fives = -2;
        sixes = -2;
        choice = -2;
        fourOfKind = -2;
        fullHouse = -2;
        smallStraight=-2;
        largeStraight = -2;
        yachu = -2;

    }
    @Builder
    public Score(int aces, int deuces, int threes, int fours,
                 int fives, int sixes, int choice, int fourOfKind,
                 int fullHouse, int smallStraight, int largeStraight, int yachu) {
        this.aces = aces;
        this.deuces = deuces;
        this.threes = threes;
        this.fours = fours;
        this.fives = fives;
        this.sixes = sixes;
        this.choice = choice;
        this.fourOfKind = fourOfKind;
        this.fullHouse = fullHouse;
        this.smallStraight = smallStraight;
        this.largeStraight = largeStraight;
        this.yachu = yachu;
    }

    // 플레이어가 이미 획득한 점수임을 표시
    public void hasGained(Score score) {
        if (score.aces >= 0) {
            this.aces = -1;
        }
        if (score.deuces >= 0) {
            this.deuces = -1;
        }
        if (score.threes >= 0) {
            this.threes = -1;
        }
        if (score.fours >= 0) {
            this.fours = -1;
        }
        if (score.fives >= 0) {
            this.fives = -1;
        }
        if (score.sixes >= 0) {
            this.sixes = -1;
        }
        if (score.choice >= 0) {
            this.choice = -1;
        }
        if (score.fourOfKind >= 0) {
            this.fourOfKind = -1;
        }
        if (score.fullHouse >= 0) {
            this.fullHouse = -1;
        }
        if (score.smallStraight >= 0) {
            this.smallStraight = -1;
        }
        if (score.largeStraight >= 0) {
            this.largeStraight = -1;
        }
        if (score.yachu >= 0) {
            this.yachu = -1;
        }
    }
}
