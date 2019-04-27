package com.vivec.jimbot.srl.api.adapter;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.vivec.jimbot.srl.api.Entrant;
import com.vivec.jimbot.srl.api.Game;
import com.vivec.jimbot.srl.api.Race;
import com.vivec.jimbot.srl.api.RaceState;

import java.lang.reflect.Type;
import java.util.List;
import java.util.stream.Collectors;

public class RaceDeserializer implements JsonDeserializer<Race> {

    private static final String RACE_ID = "id";
    private static final String RACE_GAME = "game";
    private static final String RACE_GOAL = "goal";
    private static final String RACE_TIME = "time";
    private static final String RACE_STATE = "state";
    private static final String RACE_ENTRANTS = "entrants";

    @Override
    public Race deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) {
        Gson gson = new Gson();
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        String id = jsonObject.get(RACE_ID).getAsString();
        Game game = gson.fromJson(jsonObject.get(RACE_GAME), Game.class);
        RaceState state = RaceState.byID(jsonObject.get(RACE_STATE).getAsInt());
        String goal = jsonObject.get(RACE_GOAL).getAsString();
        long time = jsonObject.get(RACE_TIME).getAsLong();

        JsonObject entrantsJson = jsonObject.get(RACE_ENTRANTS).getAsJsonObject();
        List<Entrant> entrants = entrantsJson.keySet()
                .stream()
                .map(jsonKey -> {
                    JsonElement jsonEntrant = entrantsJson.get(jsonKey);
                    Entrant entrant = gson.fromJson(jsonEntrant, Entrant.class);
                    entrant.setUserName(jsonKey);
                    return entrant;
                })
                .collect(Collectors.toList());
        return new Race(id, game, goal, time, state, entrants);
    }

}
