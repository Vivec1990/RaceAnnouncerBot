package com.vivec.jimbot.srl;

import com.vivec.jimbot.srl.api.Race;
import okhttp3.mock.MockInterceptor;
import org.intellij.lang.annotations.Language;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static okhttp3.mock.MediaTypes.MEDIATYPE_JSON;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class SpeedrunsliveAPITest {
    private SpeedrunsliveAPI api;
    private MockInterceptor interceptor;

    @BeforeEach
    void setUp() {
        api = new SpeedrunsliveAPI();
        interceptor = new MockInterceptor();
    }

    @Test
    void shouldMarshallToRace() {
        @Language("JSON") String json = "{\n" +
                "  \"id\": \"o4vqw\",\n" +
                "  \"game\": {\n" +
                "    \"id\": 3,\n" +
                "    \"name\": \"Super Metroid\",\n" +
                "    \"abbrev\": \"supermetroid\",\n" +
                "    \"popularity\": 272.000000,\n" +
                "    \"popularityrank\": 4\n" +
                "  },\n" +
                "  \"goal\": \"PRODIGY tournament Any% Zemkai vs stump\",\n" +
                "  \"time\": 0,\n" +
                "  \"state\": 1,\n" +
                "  \"statetext\": \"Entry Open\",\n" +
                "  \"filename\": \"\",\n" +
                "  \"numentrants\": 2,\n" +
                "  \"entrants\": {\n" +
                "    \"fc\": {\n" +
                "      \"displayname\": \"fc\",\n" +
                "      \"place\": 9995,\n" +
                "      \"time\": 0,\n" +
                "      \"message\": null,\n" +
                "      \"statetext\": \"Entered\",\n" +
                "      \"twitch\": \"\",\n" +
                "      \"trueskill\": \"0\"\n" +
                "    },\n" +
                "    \"marty7449\": {\n" +
                "      \"displayname\": \"marty7449\",\n" +
                "      \"place\": 9995,\n" +
                "      \"time\": 0,\n" +
                "      \"message\": null,\n" +
                "      \"statetext\": \"Entered\",\n" +
                "      \"twitch\": \"marty7449\",\n" +
                "      \"trueskill\": \"0\"\n" +
                "    }\n" +
                "  }\n" +
                "}";
        String raceId = "o4vqw";
        interceptor.addRule()
                .get("http://api.speedrunslive.com:81/races/" + raceId)
                .respond(json, MEDIATYPE_JSON);
        Race race = api.getSingleRace(raceId);

        assertEquals(raceId, race.getId());
    }

    @Test
    void getAllRaces() throws IOException {
        String allRacesJson = getAllRacesJson();

        interceptor.addRule()
                .get("http://api.speedrunslive.com:81/races/")
                .respond(allRacesJson, MEDIATYPE_JSON);

        List<Race> allRaces = api.getAllRaces();
        assertNotNull(allRaces);
    }

    private String getAllRacesJson() {
        return "\n" +
                "{\n" +
                "\"count\" : \"26\",\n" +
                "\"races\" :\n" +
                "[\n" +
                "{\n" +
                "\"id\" : \"obwnx\",\n" +
                "\"game\" : {\n" +
                "\"id\" : 3977,\n" +
                "\"name\" : \"The Legend of Zelda: A Link to the Past Hacks\",\n" +
                "\"abbrev\" : \"alttphacks\",\n" +
                "\"popularity\" : 53.000000,\n" +
                "\"popularityrank\" : 19},\n" +
                "\"goal\" : \"OWG tournament baliame vs Furaime\",\n" +
                "\"time\" : 0,\n" +
                "\"state\" : 1,\n" +
                "\"statetext\" : \"Entry Open\",\n" +
                "\"filename\" : \"\",\n" +
                "\"numentrants\" : 4,\n" +
                "\"entrants\" :\n" +
                "{\n" +
                "\"Furaime\" :\n" +
                "{\n" +
                "\"displayname\" : \"Furaime\",\n" +
                "\"place\" : 9995,\n" +
                "\"time\" : 0,\n" +
                "\"message\" : null,\n" +
                "\"statetext\" : \"Entered\",\n" +
                "\"twitch\" : \"furaime\",\n" +
                "\"trueskill\" : \"393\"\n" +
                "},\"baliame\" :\n" +
                "{\n" +
                "\"displayname\" : \"baliame\",\n" +
                "\"place\" : 9995,\n" +
                "\"time\" : 0,\n" +
                "\"message\" : null,\n" +
                "\"statetext\" : \"Entered\",\n" +
                "\"twitch\" : \"baliame\",\n" +
                "\"trueskill\" : \"16\"\n" +
                "},\"marty7449\" :\n" +
                "{\n" +
                "\"displayname\" : \"marty7449\",\n" +
                "\"place\" : 9995,\n" +
                "\"time\" : 0,\n" +
                "\"message\" : null,\n" +
                "\"statetext\" : \"Entered\",\n" +
                "\"twitch\" : \"marty7449\",\n" +
                "\"trueskill\" : \"0\"\n" +
                "},\"JOPEBUSTER\" :\n" +
                "{\n" +
                "\"displayname\" : \"JOPEBUSTER\",\n" +
                "\"place\" : 9995,\n" +
                "\"time\" : 0,\n" +
                "\"message\" : null,\n" +
                "\"statetext\" : \"Entered\",\n" +
                "\"twitch\" : \"\",\n" +
                "\"trueskill\" : \"0\"\n" +
                "}}\n" +
                "},{\n" +
                "\"id\" : \"qdetb\",\n" +
                "\"game\" : {\n" +
                "\"id\" : 3977,\n" +
                "\"name\" : \"The Legend of Zelda: A Link to the Past Hacks\",\n" +
                "\"abbrev\" : \"alttphacks\",\n" +
                "\"popularity\" : 53.000000,\n" +
                "\"popularityrank\" : 19},\n" +
                "\"goal\" : \"ALTTPR League: Ninban vs. Taale- Week # 6\",\n" +
                "\"time\" : 0,\n" +
                "\"state\" : 1,\n" +
                "\"statetext\" : \"Entry Open\",\n" +
                "\"filename\" : \"\",\n" +
                "\"numentrants\" : 4,\n" +
                "\"entrants\" :\n" +
                "{\n" +
                "\"Ninban\" :\n" +
                "{\n" +
                "\"displayname\" : \"Ninban\",\n" +
                "\"place\" : 9994,\n" +
                "\"time\" : -3,\n" +
                "\"message\" : \"\",\n" +
                "\"statetext\" : \"Ready\",\n" +
                "\"twitch\" : \"ninban\",\n" +
                "\"trueskill\" : \"333\"\n" +
                "},\"Taale\" :\n" +
                "{\n" +
                "\"displayname\" : \"Taale\",\n" +
                "\"place\" : 9995,\n" +
                "\"time\" : 0,\n" +
                "\"message\" : null,\n" +
                "\"statetext\" : \"Entered\",\n" +
                "\"twitch\" : \"TaaleNeko\",\n" +
                "\"trueskill\" : \"0\"\n" +
                "},\"marty7449\" :\n" +
                "{\n" +
                "\"displayname\" : \"marty7449\",\n" +
                "\"place\" : 9995,\n" +
                "\"time\" : 0,\n" +
                "\"message\" : null,\n" +
                "\"statetext\" : \"Entered\",\n" +
                "\"twitch\" : \"marty7449\",\n" +
                "\"trueskill\" : \"0\"\n" +
                "},\"JOPEBUSTER\" :\n" +
                "{\n" +
                "\"displayname\" : \"JOPEBUSTER\",\n" +
                "\"place\" : 9995,\n" +
                "\"time\" : 0,\n" +
                "\"message\" : null,\n" +
                "\"statetext\" : \"Entered\",\n" +
                "\"twitch\" : \"\",\n" +
                "\"trueskill\" : \"0\"\n" +
                "}}\n" +
                "},{\n" +
                "\"id\" : \"uyu5l\",\n" +
                "\"game\" : {\n" +
                "\"id\" : 3656,\n" +
                "\"name\" : \"The Legend of Zelda: Ocarina of Time Hacks\",\n" +
                "\"abbrev\" : \"oothacks\",\n" +
                "\"popularity\" : 82.000000,\n" +
                "\"popularityrank\" : 11},\n" +
                "\"goal\" : \"\",\n" +
                "\"time\" : 0,\n" +
                "\"state\" : 1,\n" +
                "\"statetext\" : \"Entry Open\",\n" +
                "\"filename\" : \"\",\n" +
                "\"numentrants\" : 3,\n" +
                "\"entrants\" :\n" +
                "{\n" +
                "\"anatomyz\" :\n" +
                "{\n" +
                "\"displayname\" : \"anatomyz\",\n" +
                "\"place\" : 9995,\n" +
                "\"time\" : 0,\n" +
                "\"message\" : null,\n" +
                "\"statetext\" : \"Entered\",\n" +
                "\"twitch\" : \"anatomyz_2\",\n" +
                "\"trueskill\" : \"1527\"\n" +
                "},\"Marco\" :\n" +
                "{\n" +
                "\"displayname\" : \"Marco\",\n" +
                "\"place\" : 9995,\n" +
                "\"time\" : 0,\n" +
                "\"message\" : null,\n" +
                "\"statetext\" : \"Entered\",\n" +
                "\"twitch\" : \"marco\",\n" +
                "\"trueskill\" : \"1310\"\n" +
                "},\"JOPEBUSTER\" :\n" +
                "{\n" +
                "\"displayname\" : \"JOPEBUSTER\",\n" +
                "\"place\" : 9995,\n" +
                "\"time\" : 0,\n" +
                "\"message\" : null,\n" +
                "\"statetext\" : \"Entered\",\n" +
                "\"twitch\" : \"\",\n" +
                "\"trueskill\" : \"0\"\n" +
                "}}\n" +
                "},{\n" +
                "\"id\" : \"h2nzc\",\n" +
                "\"game\" : {\n" +
                "\"id\" : 3977,\n" +
                "\"name\" : \"The Legend of Zelda: A Link to the Past Hacks\",\n" +
                "\"abbrev\" : \"alttphacks\",\n" +
                "\"popularity\" : 53.000000,\n" +
                "\"popularityrank\" : 19},\n" +
                "\"goal\" : \"Nordic Randomizer Tournament Group Stage Match\",\n" +
                "\"time\" : 0,\n" +
                "\"state\" : 1,\n" +
                "\"statetext\" : \"Entry Open\",\n" +
                "\"filename\" : \"\",\n" +
                "\"numentrants\" : 1,\n" +
                "\"entrants\" :\n" +
                "{\n" +
                "\"JOPEBUSTER\" :\n" +
                "{\n" +
                "\"displayname\" : \"JOPEBUSTER\",\n" +
                "\"place\" : 9995,\n" +
                "\"time\" : 0,\n" +
                "\"message\" : null,\n" +
                "\"statetext\" : \"Entered\",\n" +
                "\"twitch\" : \"\",\n" +
                "\"trueskill\" : \"0\"\n" +
                "}}\n" +
                "},{\n" +
                "\"id\" : \"unfvn\",\n" +
                "\"game\" : {\n" +
                "\"id\" : 3,\n" +
                "\"name\" : \"Super Metroid\",\n" +
                "\"abbrev\" : \"supermetroid\",\n" +
                "\"popularity\" : 272.000000,\n" +
                "\"popularityrank\" : 4},\n" +
                "\"goal\" : \"Weekend any% at 1pm EDT\",\n" +
                "\"time\" : 0,\n" +
                "\"state\" : 1,\n" +
                "\"statetext\" : \"Entry Open\",\n" +
                "\"filename\" : \"\",\n" +
                "\"numentrants\" : 1,\n" +
                "\"entrants\" :\n" +
                "{\n" +
                "\"InsaneFirebat\" :\n" +
                "{\n" +
                "\"displayname\" : \"InsaneFirebat\",\n" +
                "\"place\" : 9995,\n" +
                "\"time\" : 0,\n" +
                "\"message\" : null,\n" +
                "\"statetext\" : \"Entered\",\n" +
                "\"twitch\" : \"InsaneFirebot\",\n" +
                "\"trueskill\" : \"877\"\n" +
                "}}\n" +
                "},{\n" +
                "\"id\" : \"fst9y\",\n" +
                "\"game\" : {\n" +
                "\"id\" : 3977,\n" +
                "\"name\" : \"The Legend of Zelda: A Link to the Past Hacks\",\n" +
                "\"abbrev\" : \"alttphacks\",\n" +
                "\"popularity\" : 53.000000,\n" +
                "\"popularityrank\" : 19},\n" +
                "\"goal\" : \"german tourney Illus vs Ramond\",\n" +
                "\"time\" : 0,\n" +
                "\"state\" : 1,\n" +
                "\"statetext\" : \"Entry Open\",\n" +
                "\"filename\" : \"\",\n" +
                "\"numentrants\" : 0,\n" +
                "\"entrants\" :\n" +
                "{\n" +
                "}\n" +
                "},{\n" +
                "\"id\" : \"l3cbs\",\n" +
                "\"game\" : {\n" +
                "\"id\" : 1367,\n" +
                "\"name\" : \"Zelda 1 Hacks\",\n" +
                "\"abbrev\" : \"tlozhacks\",\n" +
                "\"popularity\" : 29.000000,\n" +
                "\"popularityrank\" : 29},\n" +
                "\"goal\" : \"OPEN RACE, Come discuss flags\",\n" +
                "\"time\" : 0,\n" +
                "\"state\" : 1,\n" +
                "\"statetext\" : \"Entry Open\",\n" +
                "\"filename\" : \"\",\n" +
                "\"numentrants\" : 0,\n" +
                "\"entrants\" :\n" +
                "{\n" +
                "}\n" +
                "},{\n" +
                "\"id\" : \"42xln\",\n" +
                "\"game\" : {\n" +
                "\"id\" : 3977,\n" +
                "\"name\" : \"The Legend of Zelda: A Link to the Past Hacks\",\n" +
                "\"abbrev\" : \"alttphacks\",\n" +
                "\"popularity\" : 53.000000,\n" +
                "\"popularityrank\" : 19},\n" +
                "\"goal\" : \"vt8 randomizer - casual open\",\n" +
                "\"time\" : 1556376517,\n" +
                "\"state\" : 3,\n" +
                "\"statetext\" : \"In Progress\",\n" +
                "\"filename\" : \"\",\n" +
                "\"numentrants\" : 14,\n" +
                "\"entrants\" :\n" +
                "{\n" +
                "\"KillerApp23\" :\n" +
                "{\n" +
                "\"displayname\" : \"KillerApp23\",\n" +
                "\"place\" : 1,\n" +
                "\"time\" : 6226,\n" +
                "\"message\" : \"great play early negated by a location missed until late  164\\/216\",\n" +
                "\"statetext\" : \"Finished\",\n" +
                "\"twitch\" : \"KillerApp23\",\n" +
                "\"trueskill\" : \"562\"\n" +
                "},\"BrewersFanJP\" :\n" +
                "{\n" +
                "\"displayname\" : \"BrewersFanJP\",\n" +
                "\"place\" : 2,\n" +
                "\"time\" : 6253,\n" +
                "\"message\" : \"Death in Eastern cost me first. Still, good run overall.\",\n" +
                "\"statetext\" : \"Finished\",\n" +
                "\"twitch\" : \"BrewersFanJP\",\n" +
                "\"trueskill\" : \"0\"\n" +
                "},\"Makinx\" :\n" +
                "{\n" +
                "\"displayname\" : \"Makinx\",\n" +
                "\"place\" : 3,\n" +
                "\"time\" : 6301,\n" +
                "\"message\" : \"death to ganon rightfully doesn't get the win\",\n" +
                "\"statetext\" : \"Finished\",\n" +
                "\"twitch\" : \"Makinx81\",\n" +
                "\"trueskill\" : \"330\"\n" +
                "},\"Julloninja\" :\n" +
                "{\n" +
                "\"displayname\" : \"Julloninja\",\n" +
                "\"place\" : 4,\n" +
                "\"time\" : 6466,\n" +
                "\"message\" : \"\",\n" +
                "\"statetext\" : \"Finished\",\n" +
                "\"twitch\" : \"julloninja\",\n" +
                "\"trueskill\" : \"263\"\n" +
                "},\"P-Train24\" :\n" +
                "{\n" +
                "\"displayname\" : \"P-Train24\",\n" +
                "\"place\" : 5,\n" +
                "\"time\" : 6476,\n" +
                "\"message\" : \"\",\n" +
                "\"statetext\" : \"Finished\",\n" +
                "\"twitch\" : \"ptrain24\",\n" +
                "\"trueskill\" : \"220\"\n" +
                "},\"SaltyPhry\" :\n" +
                "{\n" +
                "\"displayname\" : \"SaltyPhry\",\n" +
                "\"place\" : 6,\n" +
                "\"time\" : 6595,\n" +
                "\"message\" : \"\",\n" +
                "\"statetext\" : \"Finished\",\n" +
                "\"twitch\" : \"saltyphry\",\n" +
                "\"trueskill\" : \"512\"\n" +
                "},\"zelgadissan\" :\n" +
                "{\n" +
                "\"displayname\" : \"zelgadissan\",\n" +
                "\"place\" : 7,\n" +
                "\"time\" : 6615,\n" +
                "\"message\" : \"\",\n" +
                "\"statetext\" : \"Finished\",\n" +
                "\"twitch\" : \"zelgadissan\",\n" +
                "\"trueskill\" : \"572\"\n" +
                "},\"FEARAgent\" :\n" +
                "{\n" +
                "\"displayname\" : \"FEARAgent\",\n" +
                "\"place\" : 8,\n" +
                "\"time\" : 6637,\n" +
                "\"message\" : \"\",\n" +
                "\"statetext\" : \"Finished\",\n" +
                "\"twitch\" : \"thefearagent\",\n" +
                "\"trueskill\" : \"75\"\n" +
                "},\"fatboybowl300\" :\n" +
                "{\n" +
                "\"displayname\" : \"fatboybowl300\",\n" +
                "\"place\" : 9,\n" +
                "\"time\" : 6808,\n" +
                "\"message\" : \"163\\/216\",\n" +
                "\"statetext\" : \"Finished\",\n" +
                "\"twitch\" : \"fatboybowl300\",\n" +
                "\"trueskill\" : \"0\"\n" +
                "},\"Frostbite3030\" :\n" +
                "{\n" +
                "\"displayname\" : \"Frostbite3030\",\n" +
                "\"place\" : 10,\n" +
                "\"time\" : 6859,\n" +
                "\"message\" : \"\",\n" +
                "\"statetext\" : \"Finished\",\n" +
                "\"twitch\" : \"Frostbite3030\",\n" +
                "\"trueskill\" : \"159\"\n" +
                "},\"Korivain\" :\n" +
                "{\n" +
                "\"displayname\" : \"Korivain\",\n" +
                "\"place\" : 11,\n" +
                "\"time\" : 7029,\n" +
                "\"message\" : \"intersting seed. Thought I made good decision, but too many overworld checks. 159\\/216\",\n" +
                "\"statetext\" : \"Finished\",\n" +
                "\"twitch\" : \"korivain\",\n" +
                "\"trueskill\" : \"0\"\n" +
                "},\"jerryrun23\" :\n" +
                "{\n" +
                "\"displayname\" : \"jerryrun23\",\n" +
                "\"place\" : 12,\n" +
                "\"time\" : 7188,\n" +
                "\"message\" : \"\",\n" +
                "\"statetext\" : \"Finished\",\n" +
                "\"twitch\" : \"jerryrun23\",\n" +
                "\"trueskill\" : \"0\"\n" +
                "},\"adirondackrick\" :\n" +
                "{\n" +
                "\"displayname\" : \"adirondackrick\",\n" +
                "\"place\" : 9994,\n" +
                "\"time\" : -3,\n" +
                "\"message\" : \"\",\n" +
                "\"statetext\" : \"Ready\",\n" +
                "\"twitch\" : \"adirondackrick\",\n" +
                "\"trueskill\" : \"0\"\n" +
                "},\"humbugh\" :\n" +
                "{\n" +
                "\"displayname\" : \"humbugh\",\n" +
                "\"place\" : 9998,\n" +
                "\"time\" : -1,\n" +
                "\"message\" : \"irl stuff\",\n" +
                "\"statetext\" : \"Forfeit\",\n" +
                "\"twitch\" : \"humbughh\",\n" +
                "\"trueskill\" : \"57\"\n" +
                "}}\n" +
                "},{\n" +
                "\"id\" : \"4xndn\",\n" +
                "\"game\" : {\n" +
                "\"id\" : 154,\n" +
                "\"name\" : \"The Legend of Zelda\",\n" +
                "\"abbrev\" : \"tloz\",\n" +
                "\"popularity\" : 82.000000,\n" +
                "\"popularityrank\" : 12},\n" +
                "\"goal\" : \"100% no up+a\",\n" +
                "\"time\" : 1556380881,\n" +
                "\"state\" : 3,\n" +
                "\"statetext\" : \"In Progress\",\n" +
                "\"filename\" : \"\",\n" +
                "\"numentrants\" : 7,\n" +
                "\"entrants\" :\n" +
                "{\n" +
                "\"rcdrone\" :\n" +
                "{\n" +
                "\"displayname\" : \"rcdrone\",\n" +
                "\"place\" : 1,\n" +
                "\"time\" : 2191,\n" +
                "\"message\" : \"\",\n" +
                "\"statetext\" : \"Finished\",\n" +
                "\"twitch\" : \"rcdrone\",\n" +
                "\"trueskill\" : \"1127\"\n" +
                "},\"jsr_\" :\n" +
                "{\n" +
                "\"displayname\" : \"jsr_\",\n" +
                "\"place\" : 2,\n" +
                "\"time\" : 2228,\n" +
                "\"message\" : \"Patra 2ate my lunch and Patra 3 decided he didn't want to fight...\",\n" +
                "\"statetext\" : \"Finished\",\n" +
                "\"twitch\" : \"JSR_\",\n" +
                "\"trueskill\" : \"897\"\n" +
                "},\"RandomEffekt\" :\n" +
                "{\n" +
                "\"displayname\" : \"RandomEffekt\",\n" +
                "\"place\" : 3,\n" +
                "\"time\" : 2311,\n" +
                "\"message\" : \"Trash everything\",\n" +
                "\"statetext\" : \"Finished\",\n" +
                "\"twitch\" : \"randomeffekt\",\n" +
                "\"trueskill\" : \"966\"\n" +
                "},\"Kingdahl\" :\n" +
                "{\n" +
                "\"displayname\" : \"Kingdahl\",\n" +
                "\"place\" : 4,\n" +
                "\"time\" : 2571,\n" +
                "\"message\" : \"took a lot of damage :0\",\n" +
                "\"statetext\" : \"Finished\",\n" +
                "\"twitch\" : \"kingdahl\",\n" +
                "\"trueskill\" : \"672\"\n" +
                "},\"johntabin\" :\n" +
                "{\n" +
                "\"displayname\" : \"johntabin\",\n" +
                "\"place\" : 5,\n" +
                "\"time\" : 2692,\n" +
                "\"message\" : \"WR route w\\/ blue potion; drank potion and forced fairies in horrible level-8\",\n" +
                "\"statetext\" : \"Finished\",\n" +
                "\"twitch\" : \"johntabin\",\n" +
                "\"trueskill\" : \"516\"\n" +
                "},\"tetraly\" :\n" +
                "{\n" +
                "\"displayname\" : \"tetraly\",\n" +
                "\"place\" : 9994,\n" +
                "\"time\" : -3,\n" +
                "\"message\" : \"\",\n" +
                "\"statetext\" : \"Ready\",\n" +
                "\"twitch\" : \"tetraly\",\n" +
                "\"trueskill\" : \"618\"\n" +
                "},\"jazzthief81\" :\n" +
                "{\n" +
                "\"displayname\" : \"jazzthief81\",\n" +
                "\"place\" : 9994,\n" +
                "\"time\" : -3,\n" +
                "\"message\" : \"\",\n" +
                "\"statetext\" : \"Ready\",\n" +
                "\"twitch\" : \"jazzthief81\",\n" +
                "\"trueskill\" : \"482\"\n" +
                "}}\n" +
                "},{\n" +
                "\"id\" : \"2t1d3\",\n" +
                "\"game\" : {\n" +
                "\"id\" : 35,\n" +
                "\"name\" : \"Sonic Adventure 2 Battle\",\n" +
                "\"abbrev\" : \"sa2b\",\n" +
                "\"popularity\" : 122.000000,\n" +
                "\"popularityrank\" : 7},\n" +
                "\"goal\" : \"Randomizer: Hero Story\",\n" +
                "\"time\" : 1556312195,\n" +
                "\"state\" : 3,\n" +
                "\"statetext\" : \"In Progress\",\n" +
                "\"filename\" : \"\",\n" +
                "\"numentrants\" : 2,\n" +
                "\"entrants\" :\n" +
                "{\n" +
                "\"DemotedStaff\" :\n" +
                "{\n" +
                "\"displayname\" : \"DemotedStaff\",\n" +
                "\"place\" : 9994,\n" +
                "\"time\" : -3,\n" +
                "\"message\" : \"\",\n" +
                "\"statetext\" : \"Ready\",\n" +
                "\"twitch\" : \"Demotedstaff\",\n" +
                "\"trueskill\" : \"0\"\n" +
                "},\"stolenkryptic224\" :\n" +
                "{\n" +
                "\"displayname\" : \"stolenkryptic224\",\n" +
                "\"place\" : 9998,\n" +
                "\"time\" : -1,\n" +
                "\"message\" : \"\",\n" +
                "\"statetext\" : \"Forfeit\",\n" +
                "\"twitch\" : \"StolenKryptic224\",\n" +
                "\"trueskill\" : \"119\"\n" +
                "}}\n" +
                "},{\n" +
                "\"id\" : \"o4vqw\",\n" +
                "\"game\" : {\n" +
                "\"id\" : 3,\n" +
                "\"name\" : \"Super Metroid\",\n" +
                "\"abbrev\" : \"supermetroid\",\n" +
                "\"popularity\" : 272.000000,\n" +
                "\"popularityrank\" : 4},\n" +
                "\"goal\" : \"PRODIGY tournament Any% Zemkai vs stump\",\n" +
                "\"time\" : 1556381048,\n" +
                "\"state\" : 3,\n" +
                "\"statetext\" : \"In Progress\",\n" +
                "\"filename\" : \"\",\n" +
                "\"numentrants\" : 2,\n" +
                "\"entrants\" :\n" +
                "{\n" +
                "\"zenkai\" :\n" +
                "{\n" +
                "\"displayname\" : \"zenkai\",\n" +
                "\"place\" : 9994,\n" +
                "\"time\" : -3,\n" +
                "\"message\" : \"\",\n" +
                "\"statetext\" : \"Ready\",\n" +
                "\"twitch\" : \"zenkai\",\n" +
                "\"trueskill\" : \"459\"\n" +
                "},\"stump\" :\n" +
                "{\n" +
                "\"displayname\" : \"stump\",\n" +
                "\"place\" : 9994,\n" +
                "\"time\" : -3,\n" +
                "\"message\" : \"\",\n" +
                "\"statetext\" : \"Ready\",\n" +
                "\"twitch\" : \"stumpdotio\",\n" +
                "\"trueskill\" : \"45\"\n" +
                "}}\n" +
                "},{\n" +
                "\"id\" : \"pd1wx\",\n" +
                "\"game\" : {\n" +
                "\"id\" : 3977,\n" +
                "\"name\" : \"The Legend of Zelda: A Link to the Past Hacks\",\n" +
                "\"abbrev\" : \"alttphacks\",\n" +
                "\"popularity\" : 53.000000,\n" +
                "\"popularityrank\" : 19},\n" +
                "\"goal\" : \"ALTTPR League: Invictusthered vs. Ilus - Week # 6\",\n" +
                "\"time\" : 1556376614,\n" +
                "\"state\" : 3,\n" +
                "\"statetext\" : \"In Progress\",\n" +
                "\"filename\" : \"\",\n" +
                "\"numentrants\" : 2,\n" +
                "\"entrants\" :\n" +
                "{\n" +
                "\"Illus24\" :\n" +
                "{\n" +
                "\"displayname\" : \"Illus24\",\n" +
                "\"place\" : 1,\n" +
                "\"time\" : 5877,\n" +
                "\"message\" : \"\",\n" +
                "\"statetext\" : \"Finished\",\n" +
                "\"twitch\" : \"Illus24\",\n" +
                "\"trueskill\" : \"525\"\n" +
                "},\"Invictus2011\" :\n" +
                "{\n" +
                "\"displayname\" : \"Invictus2011\",\n" +
                "\"place\" : 9994,\n" +
                "\"time\" : -3,\n" +
                "\"message\" : \"\",\n" +
                "\"statetext\" : \"Ready\",\n" +
                "\"twitch\" : \"invictus2011\",\n" +
                "\"trueskill\" : \"0\"\n" +
                "}}\n" +
                "},{\n" +
                "\"id\" : \"pm1ou\",\n" +
                "\"game\" : {\n" +
                "\"id\" : 3977,\n" +
                "\"name\" : \"The Legend of Zelda: A Link to the Past Hacks\",\n" +
                "\"abbrev\" : \"alttphacks\",\n" +
                "\"popularity\" : 53.000000,\n" +
                "\"popularityrank\" : 19},\n" +
                "\"goal\" : \"Course ALttPR hebdomadaire francophone - Mode: Inverted. Seed: https:\\/\\/alttpr.com\\/en\\/h\\/29MQ05opGD Course d\\u00e9bute \\u00e0 12h EDT \\/ 18h CEST.\",\n" +
                "\"time\" : 1556381045,\n" +
                "\"state\" : 3,\n" +
                "\"statetext\" : \"In Progress\",\n" +
                "\"filename\" : \"\",\n" +
                "\"numentrants\" : 2,\n" +
                "\"entrants\" :\n" +
                "{\n" +
                "\"Linlinlin\" :\n" +
                "{\n" +
                "\"displayname\" : \"Linlinlin\",\n" +
                "\"place\" : 9994,\n" +
                "\"time\" : -3,\n" +
                "\"message\" : \"\",\n" +
                "\"statetext\" : \"Ready\",\n" +
                "\"twitch\" : \"LinlinlinNya\",\n" +
                "\"trueskill\" : \"220\"\n" +
                "},\"kryptixx\" :\n" +
                "{\n" +
                "\"displayname\" : \"kryptixx\",\n" +
                "\"place\" : 9994,\n" +
                "\"time\" : -3,\n" +
                "\"message\" : \"\",\n" +
                "\"statetext\" : \"Ready\",\n" +
                "\"twitch\" : \"xkryptixx\",\n" +
                "\"trueskill\" : \"96\"\n" +
                "}}\n" +
                "},{\n" +
                "\"id\" : \"t3be4\",\n" +
                "\"game\" : {\n" +
                "\"id\" : 3977,\n" +
                "\"name\" : \"The Legend of Zelda: A Link to the Past Hacks\",\n" +
                "\"abbrev\" : \"alttphacks\",\n" +
                "\"popularity\" : 53.000000,\n" +
                "\"popularityrank\" : 19},\n" +
                "\"goal\" : \"OWG tournament MichaelK__ vs Liam_Atlas\",\n" +
                "\"time\" : 1556377233,\n" +
                "\"state\" : 3,\n" +
                "\"statetext\" : \"In Progress\",\n" +
                "\"filename\" : \"\",\n" +
                "\"numentrants\" : 2,\n" +
                "\"entrants\" :\n" +
                "{\n" +
                "\"Liam_Atlas\" :\n" +
                "{\n" +
                "\"displayname\" : \"Liam_Atlas\",\n" +
                "\"place\" : 1,\n" +
                "\"time\" : 5532,\n" +
                "\"message\" : \"\",\n" +
                "\"statetext\" : \"Finished\",\n" +
                "\"twitch\" : \"liam_atlas\",\n" +
                "\"trueskill\" : \"0\"\n" +
                "},\"MichaelK__\" :\n" +
                "{\n" +
                "\"displayname\" : \"MichaelK__\",\n" +
                "\"place\" : 9994,\n" +
                "\"time\" : -3,\n" +
                "\"message\" : \"\",\n" +
                "\"statetext\" : \"Ready\",\n" +
                "\"twitch\" : \"MichaelK__\",\n" +
                "\"trueskill\" : \"0\"\n" +
                "}}\n" +
                "},{\n" +
                "\"id\" : \"cv5at\",\n" +
                "\"game\" : {\n" +
                "\"id\" : 71,\n" +
                "\"name\" : \"Mega Man 9\",\n" +
                "\"abbrev\" : \"mm9\",\n" +
                "\"popularity\" : 21.000000,\n" +
                "\"popularityrank\" : 41},\n" +
                "\"goal\" : \"any%\",\n" +
                "\"time\" : 1556363975,\n" +
                "\"state\" : 4,\n" +
                "\"statetext\" : \"Complete\",\n" +
                "\"filename\" : \"\",\n" +
                "\"numentrants\" : 3,\n" +
                "\"entrants\" :\n" +
                "{\n" +
                "\"slurpeeninja\" :\n" +
                "{\n" +
                "\"displayname\" : \"slurpeeninja\",\n" +
                "\"place\" : 1,\n" +
                "\"time\" : 1952,\n" +
                "\"message\" : \"What even was that race... 3 seconds.. o.o\",\n" +
                "\"statetext\" : \"Finished\",\n" +
                "\"twitch\" : \"slurpeeninja\",\n" +
                "\"trueskill\" : \"1067\"\n" +
                "},\"btfm17\" :\n" +
                "{\n" +
                "\"displayname\" : \"btfm17\",\n" +
                "\"place\" : 2,\n" +
                "\"time\" : 1964,\n" +
                "\"message\" : \"\",\n" +
                "\"statetext\" : \"Finished\",\n" +
                "\"twitch\" : \"\",\n" +
                "\"trueskill\" : \"547\"\n" +
                "},\"HandsomeJack_as\" :\n" +
                "{\n" +
                "\"displayname\" : \"HandsomeJack_as\",\n" +
                "\"place\" : 3,\n" +
                "\"time\" : 2044,\n" +
                "\"message\" : \"\",\n" +
                "\"statetext\" : \"Finished\",\n" +
                "\"twitch\" : \"handsomejack_ass\",\n" +
                "\"trueskill\" : \"516\"\n" +
                "}}\n" +
                "},{\n" +
                "\"id\" : \"gfnwt\",\n" +
                "\"game\" : {\n" +
                "\"id\" : 4,\n" +
                "\"name\" : \"Super Mario World\",\n" +
                "\"abbrev\" : \"smw\",\n" +
                "\"popularity\" : 242.000000,\n" +
                "\"popularityrank\" : 5},\n" +
                "\"goal\" : \"All Castles\",\n" +
                "\"time\" : 1556371514,\n" +
                "\"state\" : 4,\n" +
                "\"statetext\" : \"Complete\",\n" +
                "\"filename\" : \"\",\n" +
                "\"numentrants\" : 3,\n" +
                "\"entrants\" :\n" +
                "{\n" +
                "\"Ben\" :\n" +
                "{\n" +
                "\"displayname\" : \"Ben\",\n" +
                "\"place\" : 1,\n" +
                "\"time\" : 2120,\n" +
                "\"message\" : \"Spicy run!\",\n" +
                "\"statetext\" : \"Finished\",\n" +
                "\"twitch\" : \"BenSMW\",\n" +
                "\"trueskill\" : \"951\"\n" +
                "},\"shadyforce\" :\n" +
                "{\n" +
                "\"displayname\" : \"shadyforce\",\n" +
                "\"place\" : 2,\n" +
                "\"time\" : 2212,\n" +
                "\"message\" : \"GG Ben!\",\n" +
                "\"statetext\" : \"Finished\",\n" +
                "\"twitch\" : \"shadyforcegames\",\n" +
                "\"trueskill\" : \"800\"\n" +
                "},\"iDabz_\" :\n" +
                "{\n" +
                "\"displayname\" : \"iDabz_\",\n" +
                "\"place\" : 9998,\n" +
                "\"time\" : -1,\n" +
                "\"message\" : \"\",\n" +
                "\"statetext\" : \"Forfeit\",\n" +
                "\"twitch\" : \"idabz_\",\n" +
                "\"trueskill\" : \"593\"\n" +
                "}}\n" +
                "},{\n" +
                "\"id\" : \"hz4sn\",\n" +
                "\"game\" : {\n" +
                "\"id\" : 4,\n" +
                "\"name\" : \"Super Mario World\",\n" +
                "\"abbrev\" : \"smw\",\n" +
                "\"popularity\" : 242.000000,\n" +
                "\"popularityrank\" : 5},\n" +
                "\"goal\" : \"All Castles\",\n" +
                "\"time\" : 1556375389,\n" +
                "\"state\" : 4,\n" +
                "\"statetext\" : \"Complete\",\n" +
                "\"filename\" : \"\",\n" +
                "\"numentrants\" : 3,\n" +
                "\"entrants\" :\n" +
                "{\n" +
                "\"shadyforce\" :\n" +
                "{\n" +
                "\"displayname\" : \"shadyforce\",\n" +
                "\"place\" : 1,\n" +
                "\"time\" : 2220,\n" +
                "\"message\" : \"Close race\",\n" +
                "\"statetext\" : \"Finished\",\n" +
                "\"twitch\" : \"shadyforcegames\",\n" +
                "\"trueskill\" : \"800\"\n" +
                "},\"Ben\" :\n" +
                "{\n" +
                "\"displayname\" : \"Ben\",\n" +
                "\"place\" : 2,\n" +
                "\"time\" : 2227,\n" +
                "\"message\" : \"\",\n" +
                "\"statetext\" : \"Finished\",\n" +
                "\"twitch\" : \"BenSMW\",\n" +
                "\"trueskill\" : \"951\"\n" +
                "},\"iDabz_\" :\n" +
                "{\n" +
                "\"displayname\" : \"iDabz_\",\n" +
                "\"place\" : 3,\n" +
                "\"time\" : 2368,\n" +
                "\"message\" : \"\",\n" +
                "\"statetext\" : \"Finished\",\n" +
                "\"twitch\" : \"idabz_\",\n" +
                "\"trueskill\" : \"593\"\n" +
                "}}\n" +
                "},{\n" +
                "\"id\" : \"2cgyz\",\n" +
                "\"game\" : {\n" +
                "\"id\" : 213,\n" +
                "\"name\" : \"Kirby's Dream Land\",\n" +
                "\"abbrev\" : \"kdl1\",\n" +
                "\"popularity\" : 34.000000,\n" +
                "\"popularityrank\" : 25},\n" +
                "\"goal\" : \"beat the game\",\n" +
                "\"time\" : 1556376298,\n" +
                "\"state\" : 4,\n" +
                "\"statetext\" : \"Complete\",\n" +
                "\"filename\" : \"\",\n" +
                "\"numentrants\" : 2,\n" +
                "\"entrants\" :\n" +
                "{\n" +
                "\"jumpypenguin\" :\n" +
                "{\n" +
                "\"displayname\" : \"jumpypenguin\",\n" +
                "\"place\" : 1,\n" +
                "\"time\" : 700,\n" +
                "\"message\" : \"\",\n" +
                "\"statetext\" : \"Finished\",\n" +
                "\"twitch\" : \"speedman\",\n" +
                "\"trueskill\" : \"1461\"\n" +
                "},\"Thomasyim99\" :\n" +
                "{\n" +
                "\"displayname\" : \"Thomasyim99\",\n" +
                "\"place\" : 2,\n" +
                "\"time\" : 722,\n" +
                "\"message\" : \"\",\n" +
                "\"statetext\" : \"Finished\",\n" +
                "\"twitch\" : \"thomasyimloveche\",\n" +
                "\"trueskill\" : \"1172\"\n" +
                "}}\n" +
                "},{\n" +
                "\"id\" : \"6ivav\",\n" +
                "\"game\" : {\n" +
                "\"id\" : 3,\n" +
                "\"name\" : \"Super Metroid\",\n" +
                "\"abbrev\" : \"supermetroid\",\n" +
                "\"popularity\" : 272.000000,\n" +
                "\"popularityrank\" : 4},\n" +
                "\"goal\" : \"any% - Aessy vs apple_for_you\",\n" +
                "\"time\" : 1556359673,\n" +
                "\"state\" : 4,\n" +
                "\"statetext\" : \"Complete\",\n" +
                "\"filename\" : \"\",\n" +
                "\"numentrants\" : 2,\n" +
                "\"entrants\" :\n" +
                "{\n" +
                "\"aessy\" :\n" +
                "{\n" +
                "\"displayname\" : \"aessy\",\n" +
                "\"place\" : 1,\n" +
                "\"time\" : 3027,\n" +
                "\"message\" : \"\",\n" +
                "\"statetext\" : \"Finished\",\n" +
                "\"twitch\" : \"sonicattackk\",\n" +
                "\"trueskill\" : \"518\"\n" +
                "},\"tellmewhy\" :\n" +
                "{\n" +
                "\"displayname\" : \"tellmewhy\",\n" +
                "\"place\" : 2,\n" +
                "\"time\" : 3058,\n" +
                "\"message\" : \"\",\n" +
                "\"statetext\" : \"Finished\",\n" +
                "\"twitch\" : \"\",\n" +
                "\"trueskill\" : \"0\"\n" +
                "}}\n" +
                "},{\n" +
                "\"id\" : \"6nfq1\",\n" +
                "\"game\" : {\n" +
                "\"id\" : 1367,\n" +
                "\"name\" : \"Zelda 1 Hacks\",\n" +
                "\"abbrev\" : \"tlozhacks\",\n" +
                "\"popularity\" : 29.000000,\n" +
                "\"popularityrank\" : 29},\n" +
                "\"goal\" : \"Randomizer 3.3 Seed 4158200240 Flags cHCavPDmpJil8AIOjgKCPc\",\n" +
                "\"time\" : 1556378688,\n" +
                "\"state\" : 4,\n" +
                "\"statetext\" : \"Complete\",\n" +
                "\"filename\" : \"\",\n" +
                "\"numentrants\" : 2,\n" +
                "\"entrants\" :\n" +
                "{\n" +
                "\"FurySK\" :\n" +
                "{\n" +
                "\"displayname\" : \"FurySK\",\n" +
                "\"place\" : 1,\n" +
                "\"time\" : 4624,\n" +
                "\"message\" : \"bomb drop groups trolled me\",\n" +
                "\"statetext\" : \"Finished\",\n" +
                "\"twitch\" : \"FurySK\",\n" +
                "\"trueskill\" : \"398\"\n" +
                "},\"cytown\" :\n" +
                "{\n" +
                "\"displayname\" : \"cytown\",\n" +
                "\"place\" : 2,\n" +
                "\"time\" : 5051,\n" +
                "\"message\" : \"\",\n" +
                "\"statetext\" : \"Finished\",\n" +
                "\"twitch\" : \"cytown\",\n" +
                "\"trueskill\" : \"266\"\n" +
                "}}\n" +
                "},{\n" +
                "\"id\" : \"723z6\",\n" +
                "\"game\" : {\n" +
                "\"id\" : 3656,\n" +
                "\"name\" : \"The Legend of Zelda: Ocarina of Time Hacks\",\n" +
                "\"abbrev\" : \"oothacks\",\n" +
                "\"popularity\" : 82.000000,\n" +
                "\"popularityrank\" : 11},\n" +
                "\"goal\" : \"randomizer race bracket dev\",\n" +
                "\"time\" : 1556369615,\n" +
                "\"state\" : 4,\n" +
                "\"statetext\" : \"Complete\",\n" +
                "\"filename\" : \"\",\n" +
                "\"numentrants\" : 2,\n" +
                "\"entrants\" :\n" +
                "{\n" +
                "\"Nephistoss\" :\n" +
                "{\n" +
                "\"displayname\" : \"Nephistoss\",\n" +
                "\"place\" : 1,\n" +
                "\"time\" : 9726,\n" +
                "\"message\" : \"\",\n" +
                "\"statetext\" : \"Finished\",\n" +
                "\"twitch\" : \"nephisstos\",\n" +
                "\"trueskill\" : \"808\"\n" +
                "},\"Yanis\" :\n" +
                "{\n" +
                "\"displayname\" : \"Yanis\",\n" +
                "\"place\" : 9998,\n" +
                "\"time\" : -1,\n" +
                "\"message\" : \"\",\n" +
                "\"statetext\" : \"Forfeit\",\n" +
                "\"twitch\" : \"yanis2\",\n" +
                "\"trueskill\" : \"346\"\n" +
                "}}\n" +
                "},{\n" +
                "\"id\" : \"cu8zd\",\n" +
                "\"game\" : {\n" +
                "\"id\" : 5145,\n" +
                "\"name\" : \"Wind Waker Randomizer\",\n" +
                "\"abbrev\" : \"twwrandom\",\n" +
                "\"popularity\" : 14.000000,\n" +
                "\"popularityrank\" : 66},\n" +
                "\"goal\" : \"Finish Game. Seed: MS41LjEARGF1bnRsZXNzU2h5SHlsaWFuABUDAAAcBAEAAw==\",\n" +
                "\"time\" : 1556341212,\n" +
                "\"state\" : 4,\n" +
                "\"statetext\" : \"Complete\",\n" +
                "\"filename\" : \"\",\n" +
                "\"numentrants\" : 2,\n" +
                "\"entrants\" :\n" +
                "{\n" +
                "\"wooferzfg1\" :\n" +
                "{\n" +
                "\"displayname\" : \"wooferzfg1\",\n" +
                "\"place\" : 1,\n" +
                "\"time\" : 8100,\n" +
                "\"message\" : \"\",\n" +
                "\"statetext\" : \"Finished\",\n" +
                "\"twitch\" : \"wooferzfg_\",\n" +
                "\"trueskill\" : \"1191\"\n" +
                "},\"WindsRequiem\" :\n" +
                "{\n" +
                "\"displayname\" : \"WindsRequiem\",\n" +
                "\"place\" : 2,\n" +
                "\"time\" : 8632,\n" +
                "\"message\" : \"\",\n" +
                "\"statetext\" : \"Finished\",\n" +
                "\"twitch\" : \"heroof_winds\",\n" +
                "\"trueskill\" : \"555\"\n" +
                "}}\n" +
                "},{\n" +
                "\"id\" : \"gndlu\",\n" +
                "\"game\" : {\n" +
                "\"id\" : 3656,\n" +
                "\"name\" : \"The Legend of Zelda: Ocarina of Time Hacks\",\n" +
                "\"abbrev\" : \"oothacks\",\n" +
                "\"popularity\" : 82.000000,\n" +
                "\"popularityrank\" : 11},\n" +
                "\"goal\" : \"https:\\/\\/ootrandomizer.com\\/seed\\/get?id=116462\",\n" +
                "\"time\" : 1556373059,\n" +
                "\"state\" : 4,\n" +
                "\"statetext\" : \"Complete\",\n" +
                "\"filename\" : \"\",\n" +
                "\"numentrants\" : 2,\n" +
                "\"entrants\" :\n" +
                "{\n" +
                "\"Cloudike\" :\n" +
                "{\n" +
                "\"displayname\" : \"Cloudike\",\n" +
                "\"place\" : 1,\n" +
                "\"time\" : 6633,\n" +
                "\"message\" : \"fun first spoiler log race, multitask is key Kappa\",\n" +
                "\"statetext\" : \"Finished\",\n" +
                "\"twitch\" : \"Cloudike\",\n" +
                "\"trueskill\" : \"769\"\n" +
                "},\"HN-Charlie\" :\n" +
                "{\n" +
                "\"displayname\" : \"HN-Charlie\",\n" +
                "\"place\" : 2,\n" +
                "\"time\" : 9580,\n" +
                "\"message\" : \"If you tilt, you lose\",\n" +
                "\"statetext\" : \"Finished\",\n" +
                "\"twitch\" : \"hncharlie\",\n" +
                "\"trueskill\" : \"147\"\n" +
                "}}\n" +
                "},{\n" +
                "\"id\" : \"h822f\",\n" +
                "\"game\" : {\n" +
                "\"id\" : 3977,\n" +
                "\"name\" : \"The Legend of Zelda: A Link to the Past Hacks\",\n" +
                "\"abbrev\" : \"alttphacks\",\n" +
                "\"popularity\" : 53.000000,\n" +
                "\"popularityrank\" : 19},\n" +
                "\"goal\" : \"vt8 randomizer - casual open\",\n" +
                "\"time\" : 1556364385,\n" +
                "\"state\" : 4,\n" +
                "\"statetext\" : \"Complete\",\n" +
                "\"filename\" : \"\",\n" +
                "\"numentrants\" : 2,\n" +
                "\"entrants\" :\n" +
                "{\n" +
                "\"Zmegolaz\" :\n" +
                "{\n" +
                "\"displayname\" : \"Zmegolaz\",\n" +
                "\"place\" : 1,\n" +
                "\"time\" : 6738,\n" +
                "\"message\" : \"\",\n" +
                "\"statetext\" : \"Finished\",\n" +
                "\"twitch\" : \"zmegolaz\",\n" +
                "\"trueskill\" : \"0\"\n" +
                "},\"nobly\" :\n" +
                "{\n" +
                "\"displayname\" : \"nobly\",\n" +
                "\"place\" : 9998,\n" +
                "\"time\" : -1,\n" +
                "\"message\" : \"\",\n" +
                "\"statetext\" : \"Forfeit\",\n" +
                "\"twitch\" : \"nobly\",\n" +
                "\"trueskill\" : \"0\"\n" +
                "}}\n" +
                "},{\n" +
                "\"id\" : \"if2iy\",\n" +
                "\"game\" : {\n" +
                "\"id\" : 3977,\n" +
                "\"name\" : \"The Legend of Zelda: A Link to the Past Hacks\",\n" +
                "\"abbrev\" : \"alttphacks\",\n" +
                "\"popularity\" : 53.000000,\n" +
                "\"popularityrank\" : 19},\n" +
                "\"goal\" : \"german tourney Yoshi vs Jem\",\n" +
                "\"time\" : 1556367039,\n" +
                "\"state\" : 4,\n" +
                "\"statetext\" : \"Complete\",\n" +
                "\"filename\" : \"\",\n" +
                "\"numentrants\" : 2,\n" +
                "\"entrants\" :\n" +
                "{\n" +
                "\"Jem\" :\n" +
                "{\n" +
                "\"displayname\" : \"Jem\",\n" +
                "\"place\" : 1,\n" +
                "\"time\" : 5395,\n" +
                "\"message\" : \"\",\n" +
                "\"statetext\" : \"Finished\",\n" +
                "\"twitch\" : \"jem041\",\n" +
                "\"trueskill\" : \"85\"\n" +
                "},\"iiYoshii\" :\n" +
                "{\n" +
                "\"displayname\" : \"iiYoshii\",\n" +
                "\"place\" : 9998,\n" +
                "\"time\" : -1,\n" +
                "\"message\" : \"\",\n" +
                "\"statetext\" : \"Forfeit\",\n" +
                "\"twitch\" : \"iiyoshii\",\n" +
                "\"trueskill\" : \"352\"\n" +
                "}}\n" +
                "},{\n" +
                "\"id\" : \"pl8bn\",\n" +
                "\"game\" : {\n" +
                "\"id\" : 7,\n" +
                "\"name\" : \"Super Mario Sunshine\",\n" +
                "\"abbrev\" : \"sms\",\n" +
                "\"popularity\" : 283.000000,\n" +
                "\"popularityrank\" : 3},\n" +
                "\"goal\" : \"Hoverless Max%\",\n" +
                "\"time\" : 1556342104,\n" +
                "\"state\" : 4,\n" +
                "\"statetext\" : \"Complete\",\n" +
                "\"filename\" : \"\",\n" +
                "\"numentrants\" : 2,\n" +
                "\"entrants\" :\n" +
                "{\n" +
                "\"Guy2308\" :\n" +
                "{\n" +
                "\"displayname\" : \"Guy2308\",\n" +
                "\"place\" : 1,\n" +
                "\"time\" : 12434,\n" +
                "\"message\" : \"lost 6 minutes in noki and delfino stuff. The rest was p okay\",\n" +
                "\"statetext\" : \"Finished\",\n" +
                "\"twitch\" : \"guy2308\",\n" +
                "\"trueskill\" : \"1004\"\n" +
                "},\"ShadowMario27\" :\n" +
                "{\n" +
                "\"displayname\" : \"ShadowMario27\",\n" +
                "\"place\" : 2,\n" +
                "\"time\" : 12667,\n" +
                "\"message\" : \"\",\n" +
                "\"statetext\" : \"Finished\",\n" +
                "\"twitch\" : \"ShadowMario27\",\n" +
                "\"trueskill\" : \"496\"\n" +
                "}}\n" +
                "}]\n" +
                "}";
    }
}
