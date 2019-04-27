package com.vivec.jimbot.database;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import com.vivec.jimbot.database.dao.Game;
import com.vivec.jimbot.database.dao.Participant;
import com.vivec.jimbot.database.dao.Player;
import com.vivec.jimbot.database.dao.Race;
import com.vivec.jimbot.database.dao.Segment;

public class DatabaseConnectionManager {

	private static final String DATABASE_LOCATION = System.getProperty("user.home") + "/JimBot162/";
	private static final String DATABASE_NAME = "JimBot.sqlite";
	private static final String DATABASE_FULLPATH = DATABASE_LOCATION + DATABASE_NAME;
	private static final String DATABASE_JDBC = "jdbc:sqlite:" + DATABASE_FULLPATH;

	private static DatabaseConnectionManager INSTANCE;

	private ConnectionSource dbConnection;

	public static DatabaseConnectionManager getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new DatabaseConnectionManager(true);
		}
		return INSTANCE;
	}

	private DatabaseConnectionManager(boolean initialSetup) {
		Path dbFolder = Paths.get(DATABASE_LOCATION);
		if (!Files.exists(dbFolder)) {
			try {
				Files.createDirectory(dbFolder);
			} catch (IOException e) {
				System.err.println("Error creating the database file, check permissions.");
			}
		}
		boolean setup = initialSetup || !Files.exists(Paths.get(DATABASE_FULLPATH));
		try {
			this.dbConnection = new JdbcConnectionSource(DATABASE_JDBC);
			if (setup)
				setup();
		} catch (SQLException e) {
			System.err.println("Connection to the database could not be established.");
		}
	}

	private void setup() throws SQLException {
		TableUtils.createTableIfNotExists(this.dbConnection, Player.class);
		TableUtils.createTableIfNotExists(this.dbConnection, Game.class);
		TableUtils.createTableIfNotExists(this.dbConnection, Race.class);
		TableUtils.createTableIfNotExists(this.dbConnection, Participant.class);
		TableUtils.createTableIfNotExists(this.dbConnection, Segment.class);
	}

	public static void main(String[] args) {
		DatabaseConnectionManager.getInstance();
	}

}
