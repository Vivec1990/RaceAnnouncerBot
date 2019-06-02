package com.vivec.jimbot.announcer.gamesplits;

import com.google.gson.Gson;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

final class GameSplitService {

    private static Gson gson = new Gson();

    private GameSplitService() {}

    static GameSpecificConfiguration loadSingleConfig(Path filePath) throws IOException {
        byte[] fileContent = Files.readAllBytes(filePath);
        return gson.fromJson(new String(fileContent), GameSpecificConfiguration.class);
    }

}
