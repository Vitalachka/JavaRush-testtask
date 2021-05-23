package com.game.entity;

import javax.persistence.*;
import java.util.Date;
import java.util.Optional;

@Entity

public class Player {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private long id;
    private String name; //up to 12 symbols inclusive
    private String title; // up to 30 symbols inclusive
    @Enumerated(value = EnumType.STRING)
    private Race race;
    @Enumerated(value = EnumType.STRING)
    private Profession profession;
    private Integer experience; //from 0 to 10 000 000
    private Integer level;
    private Integer untilNextLevel;
    private java.sql.Date birthday; // from 2000 to 3000 inclusive
    private boolean banned;

    public Player(){}

    public Player(long id,
                  String name,
                  String title,
                  Race race,
                  Profession profession,
                  Integer experience,
                  Integer level,
                  Integer untilNextLevel,
                  java.sql.Date birthday,
                  boolean banned) {
        this.id = id;
        this.name = name;
        this.title = title;
        this.race = race;
        this.profession = profession;
        this.birthday = birthday;
        this.banned = banned;
        this.experience = experience;
        this.level = level;
        this.untilNextLevel = untilNextLevel;
    }


    public long getId() {
        return id;
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

    public Integer getExperience() {
        return experience;
    }

    public Integer getLevel() {
        return level;
    }

    public Integer getUntilNextLevel() {
        return untilNextLevel;
    }

    public java.sql.Date getBirthday() {
        return birthday;
    }

    public boolean isBanned() {
        return banned;
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

    public void setExperience(Integer experience) {
        this.experience = experience;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public void setUntilNextLevel(Integer untilNextLevel) {
        this.untilNextLevel = untilNextLevel;
    }

    public void setBirthday(java.sql.Date birthday) {
        this.birthday = birthday;
    }

    public void setBanned(boolean banned) {
        this.banned = banned;
    }
}
