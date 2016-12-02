package fr.wildcodeschool.apprenti.sportihome;

/**
 * Created by chantome on 23/11/2016.
 */

public class PriceModel {
    private int hightSeason;
    private int lowSeason;

    public PriceModel() {
    }

    public PriceModel(int hightSeason, int lowSeason) {
        setHightSeason(hightSeason);
        setLowSeason(lowSeason);
    }

    public int getHightSeason() {
        return hightSeason;
    }

    public void setHightSeason(int hightSeason) {
        this.hightSeason = hightSeason;
    }

    public int getLowSeason() {
        return lowSeason;
    }

    public void setLowSeason(int lowSeason) {
        this.lowSeason = lowSeason;
    }
}
