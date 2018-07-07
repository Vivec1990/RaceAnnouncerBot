package de.sebmey.jimbot.database.dao;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName="participant")
public class Participant {

	@DatabaseField(generatedId=true)
	private int id;
	@DatabaseField(foreign=true)
	private Race race;
	@DatabaseField(foreign=true)
	private Player player;
	@DatabaseField
	private boolean forfeit;
	@DatabaseField
	private Long time;
	
}
