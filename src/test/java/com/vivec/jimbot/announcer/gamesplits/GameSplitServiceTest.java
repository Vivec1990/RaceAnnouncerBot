package com.vivec.jimbot.announcer.gamesplits;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

class GameSplitServiceTest {

    @Nested
    @DisplayName("Tests for loading split configurations from disk")
    class LoadingFromDisk {
        @Test
        @DisplayName("Load a specific split configuration from disk")
        void load_pkmn_red_blue() throws IOException, URISyntaxException {
            final String filename = "6_PKMNREDBLUE.json";
            GameSpecificConfiguration singleConfig = GameSplitService.loadSingleConfig(Paths.get(Objects.requireNonNull(GameSplitService.class.getClassLoader().getResource("gamesplits/" + filename)).toURI()));
            assertThat(singleConfig).isNotNull();
            assertThat(singleConfig.getGameId()).isEqualTo(6);
            assertThat(singleConfig.getCategoryName()).isEqualTo("Any% Glitchless");
            assertThat(singleConfig.getSplits()).hasSize(1);
        }
    }

}
