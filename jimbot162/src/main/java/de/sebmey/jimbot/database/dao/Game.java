package de.sebmey.jimbot.database.dao;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName="game")
public class Game {

	@DatabaseField(id=true)
	private int id;
	@DatabaseField
	private String name;
	@DatabaseField
	private String abbreviation;
	
	public Game() {}
	
}
