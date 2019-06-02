package com.vivec.jimbot.announcer.gamesplits;

import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

final class GameSplitService {
    private static final Logger LOG = LogManager.getLogger(GameSplitService.class);
    private static Gson gson = new Gson();

    private GameSplitService() {}

    static GameSpecificConfiguration loadSingleConfig(Path filePath) {
        try {
            byte[] fileContent = Files.readAllBytes(filePath);
            return gson.fromJson(new String(fileContent), GameSpecificConfiguration.class);
        } catch (IOException e) {
            LOG.error("Error while reading from file at path {}", filePath.getFileName(), e);
            throw new IllegalArgumentException("Error reading from file");
        }

    }

    static List<GameSpecificConfiguration> loadAllConfigsFromDirectory(Path dirPath) {
        try (Stream<Path> files = Files.walk(dirPath)) {
            return files
                    .filter(Files::isRegularFile)
                    .filter(Files::isReadable)
                    .filter(p -> p.toString().endsWith(".json"))
                    .map(GameSplitService::loadSingleConfig)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            LOG.error("Error walking the directory {}", dirPath.getFileName(), e);
            throw new IllegalArgumentException("Error walking requested directory", e);
        }
    }
}
