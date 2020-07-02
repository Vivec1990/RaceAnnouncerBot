package com.vivec.jimbot.announcer.blacklist;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.util.Strings;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public final class Blacklist {

    private static final Logger LOG = LogManager.getLogger(Blacklist.class);
    private static final String BLACKLIST_LOCATION = System.getProperty("user.home") + "/jimbot/blacklist.txt";
    private static final Blacklist INSTANCE = new Blacklist();
    private static Path blacklistPath;

    private List<String> blacklistedUsers = new ArrayList<>();

    private Blacklist() {
        LOG.info("Loading blacklist information from Disk");
        blacklistPath = Paths.get(BLACKLIST_LOCATION);
        ensureBlacklistFileExists();
    }

    private static void ensureBlacklistFileExists() {
        try {
            if (!blacklistPath.toFile().exists()) {
                Files.createFile(blacklistPath);
            }
        } catch (IOException e) {
            LOG.error("Error while reading or creating blacklist file", e);
        }
    }

    public static Blacklist getInstance() {
        return INSTANCE;
    }

    public void addBlacklistedUser(String user) {
        if (!getBlacklistedUsers().contains(user)) {
            getBlacklistedUsers().add(user);
            updateBlacklistFile();
        }
    }

    public List<String> getBlacklistedUsers() {
        return blacklistedUsers;
    }

    private void readBlacklist() {
        try {
            blacklistedUsers = Files.readAllLines(blacklistPath, StandardCharsets.UTF_8);
        } catch (IOException e) {
            LOG.error("Error reading blacklist information from disk", e);
        }
    }

    private void updateBlacklistFile() {
        try {
            Files.write(blacklistPath, Strings.join(getBlacklistedUsers(), '\n').getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            LOG.error("Error while writing blacklist file", e);
        }
    }
}
