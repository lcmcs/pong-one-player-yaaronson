package edu.touro.cs.mcon364;

public class Score implements Comparable<Score> {

    private int score;
    private String initials;

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getInitials() {
        return initials;
    }

    public void setInitials(String initials) {
        this.initials = initials;
    }

    Score(int score, String initials){
        this.score = score;
        this.initials = initials;
}

    @Override
    public int compareTo(Score o) {
       return o.getScore() - this.getScore();
    }
}
