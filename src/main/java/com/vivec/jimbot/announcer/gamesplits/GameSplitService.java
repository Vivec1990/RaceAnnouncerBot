package com.vivec.jimbot.announcer.gamesplits;

import com.google.gson.Gson;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

final class GameSplitService {

    private static Gson gson = new Gson();

    private GameSplitService() {}

    static GameSpecificConfiguration loadSingleConfig(String filename) throws URISyntaxException, IOException {
        byte[] fileContent = getFileContentFromResource(filename);
        return gson.fromJson(new String(fileContent), GameSpecificConfiguration.class);
    }

    private static byte[] getFileContentFromResource(String filename) throws URISyntaxException, IOException {
        Path filePath = Paths.get(Objects.requireNonNull(GameSplitService.class.getClassLoader().getResource("gamesplits/" + filename)).toURI());
        return Files.readAllBytes(filePath);
    }
}
