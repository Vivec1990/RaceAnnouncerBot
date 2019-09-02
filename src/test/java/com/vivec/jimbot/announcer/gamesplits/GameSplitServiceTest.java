package com.vivec.jimbot.announcer.gamesplits;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class GameSplitServiceTest {

    @Nested
    @DisplayName("Tests for loading split configurations from disk")
    class LoadingFromDisk {
        @Test
        @DisplayName("Load a specific split configuration from disk")
        void load_pkmn_red_blue() throws URISyntaxException {
            final String filename = "6_PKMNREDBLUE.json";
            Path filePath = Paths.get(Objects.requireNonNull(GameSplitService.class.getClassLoader().getResource("test_gamesplits/" + filename)).toURI());
            GameSpecificConfiguration singleConfig = GameSplitService.loadSingleConfig(filePath);
            assertThat(singleConfig).isNotNull();
            assertThat(singleConfig.getGameId()).isEqualTo(6);
            assertThat(singleConfig.getCategoryName()).isEqualTo("Any% Glitchless");
            assertThat(singleConfig.getSplits()).hasSize(1);
        }

        @Test
        void load_all_config_files_from_directory() throws URISyntaxException {
            Path filePath = Paths.get(Objects.requireNonNull(GameSplitService.class.getClassLoader().getResource("test_gamesplits/")).toURI());
            List<GameSpecificConfiguration> allTestConfigs = GameSplitService.loadAllConfigsFromDirectory(filePath);
            assertThat(allTestConfigs).isNotNull();
            assertThat(allTestConfigs).hasSize(2);
        }
    }

    @Test
    void get_split_config_by_id() {
        GameSplitService.updateAvailableSplitConfiguration("test_gamesplits/");
        Optional<GameSpecificConfiguration> configurationBy = GameSplitService.getConfigurationBy(6);
        assertThat(configurationBy).isNotEmpty();
    }
}
