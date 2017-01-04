package fr.wildcodeschool.apprenti.sportihome;

/**
 * Created by chantome on 23/11/2016.
 */

public class SpotRatingModel {

    private int difficulty=0;
    private int beauty=0;
    private int quality=0;
    private int overallRating=0;
    private int numberOfRatings=0;

    public SpotRatingModel() {
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public int getBeauty() {
        return beauty;
    }

    public void setBeauty(int beauty) {
        this.beauty = beauty;
    }

    public int getQuality() {
        return quality;
    }

    public void setQuality(int quality) {
        this.quality = quality;
    }

    public int getOverallRating() {
        return overallRating;
    }

    public void setOverallRating(int overallRating) {
        this.overallRating = overallRating;
    }

    public int getNumberOfRatings() {
        return numberOfRatings;
    }

    public void setNumberOfRatings(int numberOfRatings) {
        this.numberOfRatings = numberOfRatings;
    }
}
