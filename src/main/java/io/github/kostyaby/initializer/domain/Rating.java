package io.github.kostyaby.initializer.domain;

/**
 * Created by kostya_by on 4/7/16.
 */
public class Rating {
    private final int rank;
    private final int votes;

    public Rating(int rank, int votes) {
        this.rank = rank;
        this.votes = votes;
    }

    public int getRank() {
        return rank;
    }

    public int getVotes() {
        return votes;
    }
}
