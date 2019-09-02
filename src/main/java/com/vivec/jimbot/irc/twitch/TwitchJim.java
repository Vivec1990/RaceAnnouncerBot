package com.vivec.jimbot.irc.twitch;

import com.cavariux.twitchirc.Chat.Channel;
import com.cavariux.twitchirc.Chat.User;
import com.cavariux.twitchirc.Core.TwitchBot;
import com.vivec.jimbot.announcer.RaceAnnouncer;
import com.vivec.jimbot.announcer.WatchedRace;
import com.vivec.jimbot.announcer.blacklist.Blacklist;
import com.vivec.jimbot.srl.SpeedrunsliveAPI;
import com.vivec.jimbot.srl.api.Race;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;

public class TwitchJim extends TwitchBot {

    private Channel botChannel;
    private SpeedrunsliveAPI api;
    private Blacklist blacklist = Blacklist.getInstance();

    TwitchJim(String username, String oauth) {
        this.setUsername(username);
        this.setOauth_Key(oauth);
        this.connect();
        this.botChannel = this.joinChannel(username);
        api = new SpeedrunsliveAPI();
    }

    @Override
    public void onMessage(User user, Channel channel, String message) {
        // do nothing
    }

    @Override
    @SuppressWarnings("deprecation")
    public void onCommand(User user, Channel channel, String command) {
        if (channel.equals(this.botChannel)) {

            RaceAnnouncer raceAnnouncer = RaceAnnouncer.getInstance();
            if ("race".equalsIgnoreCase(command)) {
                addRaceForUserExecutingCommand(user.toString(), channel, raceAnnouncer);
            } else if (command.startsWith("race ")) {
                addRaceWithId(user, channel, command, raceAnnouncer);
            } else if (command.startsWith("addtime ")) { // e.g. !addtime Headbob "Lance" 1:49:23.00
                manuallyAddTime(channel, command, raceAnnouncer);
            } else if (command.startsWith("addspectator ")) { // e.g. !addspectator raceId twitchname
                addSpectator(channel, command, raceAnnouncer);
            } else if (command.startsWith("remspectator ")) { // e.g. !remspectator raceId twitchname
                remSpectator(channel, command, raceAnnouncer);
            } else if (command.startsWith("addBL ")) { // e.g. !addBL JuanlyWays
                blacklistUser(command, channel);
            } else if (command.startsWith("remBL ")) { // e.g. !remBL JuanlyWays
                whitelistUser(command, channel);
            }
        }

        if (command.equalsIgnoreCase("raceinfo") || command.equalsIgnoreCase("racestats")) {
            sendRaceInformation(channel);
        }

        if (command.equalsIgnoreCase("standings")) {
            Race race = api.findRaceWithTwitchUser(channel.toString().substring(1));
            if(race != null) {
                WatchedRace wr = RaceAnnouncer.getInstance().getRaceByID(race.getId());
                this.sendMessage(wr.getStandings(), channel);
            }
            WatchedRace wr = RaceAnnouncer.getInstance().getRaceBySpectator(channel.toString().substring(1));
            if(wr != null) {
                this.sendMessage(wr.getStandings(), channel);
            }
        }
    }

    private void sendRaceInformation(Channel channel) {
        Race r = api.findRaceWithTwitchUser(channel.toString().substring(1));
        Optional.ofNullable(r)
                .map(Race::getRaceInfo)
                .ifPresent(raceInfo -> {
                    this.sendMessage(raceInfo[0], channel);
                    if (!StringUtils.isEmpty(raceInfo[1])) {
                        this.sendMessage(raceInfo[1], channel);
                    }
                });
    }

    private void manuallyAddTime(Channel channel, String command, RaceAnnouncer raceAnnouncer) {
        String arguments = command.substring("addtime ".length());
        String[] parts = arguments.split("\"");
        if (parts.length < 2) return;
        String splitRunner = parts[0].trim();
        String splitName = parts[1].trim();
        String splitTime = parts[2].trim();

        Race race =  api.findRaceWithSRLUser(splitRunner);
        if (race == null) {
            this.sendMessage(splitRunner + " is not part of a race on speedrunslive.com right now.", channel);
        } else {
            WatchedRace wr = raceAnnouncer.getRaceByID(race.getId());
            if (wr != null) {
                wr.recordSplitTime(splitName, splitRunner, splitTime);
                this.sendMessage("Recorded the SplitTime of " + splitRunner + " at " + splitName + " with a time of " + splitTime + ".", channel);
            } else {
                this.sendMessage("The race in which " + splitRunner + " is part of couldn't be found on speedrunslive.com.", channel);
            }
        }
    }

    private void addSpectator(Channel channel, String command, RaceAnnouncer raceAnnouncer) {
        String arguments = command.substring("addspectator ".length());
        String[] parts = arguments.split(" ");
        if (parts.length < 2) return;
        String raceId = parts[0].trim();
        String spectatorName = parts[1].trim();

        WatchedRace wr = raceAnnouncer.getRaceByID(raceId);
        if (wr != null) {
            if(wr.addSpectator(spectatorName) == 1)
                this.sendMessage("Added spectator " + spectatorName + " to race " + raceId + ".", channel);
            else
                this.sendMessage(spectatorName + " is already listed as a spectator for race " + raceId + ".", channel);
        } else {
            this.sendMessage("Couldn't find a race with that raceId.", channel);
        }
    }

    private void remSpectator(Channel channel, String command, RaceAnnouncer raceAnnouncer) {
        String arguments = command.substring("remspectator ".length());
        String[] parts = arguments.split(" ");
        if (parts.length < 2) return;
        String raceId = parts[0].trim();
        String spectatorName = parts[1].trim();

        WatchedRace wr = raceAnnouncer.getRaceByID(raceId);
        if (wr != null) {
            wr.remSpectator(spectatorName);
            this.sendMessage("Removed spectator " + spectatorName + " from race " + raceId + ".", channel);
        } else {
            this.sendMessage("Couldn't find a race with that raceId.", channel);
        }
    }

    private void addRaceWithId(User user, Channel channel, String command, RaceAnnouncer raceAnnouncer) {
        String raceId = command.substring("race ".length());
        Race race = api.getSingleRace(raceId);
        if (race == null) {
            this.sendMessage("@" + user.toString() + " No race with the offered ID has been found.", channel);
        } else {
            raceAnnouncer.addRace(new WatchedRace(race));
            this.sendMessage("@" + user.toString() + " The race was added and will be announced once splits are completed by all entrants. Good luck & have fun!", channel);
        }
    }

    private Race addRaceForUserExecutingCommand(String username, Channel channel, RaceAnnouncer raceAnnouncer) {
        Race race = api.findRaceWithTwitchUser(username);
        if (race == null) {
            this.sendMessage("@" + username + " You are not part of a race on speedrunslive.com right now.", channel);
        } else if (raceAnnouncer.getRaceByID(race.getId()) != null) {
            this.sendMessage("@" + username + " The race you are in is already managed, no need to initialize twice.", channel);
        } else {
            raceAnnouncer.addRace(new WatchedRace(race));
            this.sendMessage("@" + username + " The race was added and will be announced once splits are completed by all entrants. Good luck & have fun!", channel);
        }
        return race;
    }

    private void whitelistUser(String command, Channel channel) {
        String username = command.substring("remBL ".length());
        if (blacklist.getBlacklistedUsers().contains(username)) {
            blacklist.getBlacklistedUsers().remove(username);
            this.sendMessage("The user " + username + " was successfully removed from the blacklist.", channel);
        } else {
            this.sendMessage("The user you requested to be whitelisted was not blacklisted.", channel);
        }
    }

    private void blacklistUser(String command, Channel channel) {
        String username = command.substring("addBL ".length());
        blacklist.addBlacklistedUser(username);

        this.sendMessage("The user " + username + " was blacklisted.", channel);
    }

}
