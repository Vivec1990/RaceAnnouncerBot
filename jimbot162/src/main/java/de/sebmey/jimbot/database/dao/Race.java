package de.sebmey.jimbot.database.dao;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName="race")
public class Race {
	
	@DatabaseField(id=true)
	private String id;
	@DatabaseField(foreign=true)
	private Game game;
	@DatabaseField
	private String goal;

}
