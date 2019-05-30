package com.vivec.jimbot.announcer.gamesplits;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class GameSpecificConfiguration {
    @SerializedName("game_id")
    private long gameId;
    @SerializedName("game_name")
    private String gameName;
    @SerializedName("category")
    private String categoryName;
    private List<SplitConfiguration> splits;

    public long getGameId() {
        return gameId;
    }

    public void setGameId(long gameId) {
        this.gameId = gameId;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public List<SplitConfiguration> getSplits() {
        return new ArrayList<>(splits);
    }

    public void setSplits(List<SplitConfiguration> splits) {
        this.splits = new ArrayList<>(splits);
    }
}
