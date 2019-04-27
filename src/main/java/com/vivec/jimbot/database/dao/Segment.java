package com.vivec.jimbot.database.dao;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName="segment")
public class Segment {
	@DatabaseField(generatedId=true)
	private int id;
	@DatabaseField(foreign=true)
	private Participant participant;
	@DatabaseField(canBeNull=false,unique=true,index=true)
	private String name;
	@DatabaseField
	private Long time;
}
