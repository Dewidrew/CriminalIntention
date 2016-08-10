package com.augmentis.ayp.crimin.model;

import java.util.Date;
import java.util.UUID;

/**
 * Created by Nutdanai on 7/18/2016.
 */
public class Crime {
    private UUID id;
    private  String title;
    private Date crimeDate;
    private boolean solved;
    private String suspect;

    public String getSuspect() {
        return suspect;
    }

    public void setSuspect(String suspect) {
        this.suspect = suspect;
    }

    public  Crime(){
       this(UUID.randomUUID());


    }

    public Crime(UUID uuid){
        this.id = uuid;
        crimeDate = new Date();
    }

    public UUID getId() {return id;}
    public String getTitle() {return title;}
    public Date getCrimeDate() {return crimeDate;}
    public boolean isSolved() {return solved;}

    public void setId(UUID id) {this.id = id;}
    public void setTitle(String t) {this.title = t;}
    public void setCrimeDate(Date crimeDate) {this.crimeDate = crimeDate;}
    public void setSolved(boolean solved) {this.solved = solved;}

    public String getPhotoFilename(){
        return "IMG_" + getId().toString() + ".jpg";
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("UUID=").append(id);
        builder.append(",Title=").append(title);
        builder.append(",Crime Date=").append(crimeDate);
        builder.append(",Solved=").append(solved);
        builder.append(",Suspect=").append(suspect);
        return builder.toString();
    }
}
