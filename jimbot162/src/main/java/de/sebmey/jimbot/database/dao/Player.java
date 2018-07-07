package de.sebmey.jimbot.database.dao;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "player")
public class Player {
	
	@DatabaseField(id=true)
	private String username;
	
	public Player() {}

}
