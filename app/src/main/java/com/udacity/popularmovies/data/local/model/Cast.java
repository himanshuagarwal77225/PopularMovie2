package com.udacity.popularmovies.data.local.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(
        tableName = "cast",
        foreignKeys = {@ForeignKey(
                entity = Movie.class,
                childColumns = {"movie_id"},
                onUpdate = 5,
                onDelete = 5,
                parentColumns = {"id"}
        )},
        indices = {@Index({"movie_id"})}
)
public class Cast {

    @PrimaryKey
    @NonNull
    @SerializedName("credit_id")
    private String id;

    @ColumnInfo(name = "movie_id")
    private Long movieId = 0L;

    @SerializedName("character")
    private String characterName;

    @SerializedName("gender")
    private int gender = 0;

    @SerializedName("name")
    private String actorName;

    @SerializedName("order")
    private int order = 0;

    @SerializedName("profile_path")
    private String profileImagePath;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getMovieId() {
        return movieId;
    }

    public void setMovieId(Long movieId) {
        this.movieId = movieId;
    }

    public String getCharacterName() {
        return characterName;
    }

    public void setCharacterName(String characterName) {
        this.characterName = characterName;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getActorName() {
        return actorName;
    }

    public void setActorName(String actorName) {
        this.actorName = actorName;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getProfileImagePath() {
        return profileImagePath;
    }

    public void setProfileImagePath(String profileImagePath) {
        this.profileImagePath = profileImagePath;
    }
}
