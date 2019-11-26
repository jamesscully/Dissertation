package game;

import cards.Card;
import enums.Face;
import enums.Rank;

import java.util.Objects;

public class TResult {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TResult result = (TResult) o;
        return highest == result.highest &&
                rank == result.rank;
    }

    @Override
    public int hashCode() {
        return Objects.hash(highest, rank);
    }

    public Face highest;
    public Rank rank;

    public TResult(Face highest, Rank rank) {
        this.highest = highest;
        this.rank = rank;
    }

    @Override
    public String toString() {
        return "TResult{" +
                "highest=" + highest +
                ", rank=" + rank +
                '}';
    }
}
