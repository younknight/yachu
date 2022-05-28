package game.yachu.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
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

}
