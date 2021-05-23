package com.game.entity;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.Calendar;
import java.util.Date;

public class PlayerModel {
    private String name;
    private String title;
    @Enumerated(value = EnumType.STRING)
    private Race race;
    @Enumerated(value = EnumType.STRING)
    private Profession profession;
    private Long birthday;
    private boolean banned = false;
    private Integer experience;

    public PlayerModel(){}

    public PlayerModel(String name, String title, Race race, Profession profession, Long birthday, boolean banned, Integer experience) {
        this.name = name;
        this.title = title;
        this.race = race;
        this.profession = profession;
        this.birthday = birthday;
        this.banned = banned;
        this.experience = experience;
    }

    public boolean isNameValid(){
        return name.length()<=12;
    }

    public boolean isTitleValid(){
        return title.length()<=30;
    }

    public boolean isExperienceValid(){
        return experience >= 0 && experience <= 10000000L;
    }

    public boolean isBirthdayValid(){
        Calendar after = Calendar.getInstance();
        Calendar before = Calendar.getInstance();
        after.set(Calendar.YEAR, 2000);
        before.set(Calendar.YEAR, 3000);
        return birthday >= after.getTimeInMillis() && birthday <= before.getTimeInMillis() ;
    }

    public Date getSqlBirthday(){
        return new java.sql.Date(birthday);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setRace(Race race) {
        this.race = race;
    }

    public void setProfession(Profession profession) {
        this.profession = profession;
    }

    public void setBirthday(Long birthday) {
        this.birthday = birthday;
    }

    public void setBanned(boolean banned) {
        this.banned = banned;
    }

    public void setExperience(Integer experience) {
        this.experience = experience;
    }

    public String getName() {
        return name;
    }

    public String getTitle() {
        return title;
    }

    public Race getRace() {
        return race;
    }

    public Profession getProfession() {
        return profession;
    }

    public Long getBirthday() {
        return birthday;
    }

    public boolean isBanned() {
        return banned;
    }

    public Integer getExperience() {
        return experience;
    }
}
