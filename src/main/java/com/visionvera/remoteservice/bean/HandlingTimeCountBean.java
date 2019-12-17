package com.visionvera.remoteservice.bean;

/**
 * Created by jlm on 2019-07-11 11:11
 */
public class HandlingTimeCountBean {

    Integer zeroToFive;
    Integer fiveToTen;
    Integer tenToFifteen;
    Integer fifTeenToTwenty;
    Integer twentyToTwentyFive;
    Integer twentyFiveToThirty;
    Integer moreThanThirty;

    public HandlingTimeCountBean(Integer zeroToFive, Integer fiveToTen, Integer tenToFifteen, Integer fifTeenToTwenty, Integer twentyToTwentyFive, Integer twentyFiveToThirty, Integer moreThanThirty) {
        this.zeroToFive = zeroToFive;
        this.fiveToTen = fiveToTen;
        this.tenToFifteen = tenToFifteen;
        this.fifTeenToTwenty = fifTeenToTwenty;
        this.twentyToTwentyFive = twentyToTwentyFive;
        this.twentyFiveToThirty = twentyFiveToThirty;
        this.moreThanThirty = moreThanThirty;
    }

    public HandlingTimeCountBean() {
    }

    public Integer getZeroToFive() {
        return zeroToFive;
    }

    public void setZeroToFive(Integer zeroToFive) {
        this.zeroToFive = zeroToFive;
    }

    public Integer getFiveToTen() {
        return fiveToTen;
    }

    public void setFiveToTen(Integer fiveToTen) {
        this.fiveToTen = fiveToTen;
    }

    public Integer getTenToFifteen() {
        return tenToFifteen;
    }

    public void setTenToFifteen(Integer tenToFifteen) {
        this.tenToFifteen = tenToFifteen;
    }

    public Integer getFifTeenToTwenty() {
        return fifTeenToTwenty;
    }

    public void setFifTeenToTwenty(Integer fifTeenToTwenty) {
        this.fifTeenToTwenty = fifTeenToTwenty;
    }

    public Integer getTwentyToTwentyFive() {
        return twentyToTwentyFive;
    }

    public void setTwentyToTwentyFive(Integer twentyToTwentyFive) {
        this.twentyToTwentyFive = twentyToTwentyFive;
    }

    public Integer getTwentyFiveToThirty() {
        return twentyFiveToThirty;
    }

    public void setTwentyFiveToThirty(Integer twentyFiveToThirty) {
        this.twentyFiveToThirty = twentyFiveToThirty;
    }

    public Integer getMoreThanThirty() {
        return moreThanThirty;
    }

    public void setMoreThanThirty(Integer moreThanThirty) {
        this.moreThanThirty = moreThanThirty;
    }

    @Override
    public String toString() {
        return "HandlingTimeCountBean{" +
                "zeroToFive=" + zeroToFive +
                ", fiveToTen=" + fiveToTen +
                ", tenToFifteen=" + tenToFifteen +
                ", fifTeenToTwenty=" + fifTeenToTwenty +
                ", twentyToTwentyFive=" + twentyToTwentyFive +
                ", twentyFiveToThirty=" + twentyFiveToThirty +
                ", moreThanThirty=" + moreThanThirty +
                '}';
    }
}
