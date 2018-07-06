package de.sebmey.jimbot.announcer.gamesplits;

public enum PKMNREDBLUE {

	NIDORAN		("Nidoran", true, "Nido", "NidoranM", "MankeyS"),
	BROCK		("Brock", true, "Bro"),
	ROUTE3		("Route 3", true, "Route 03", "Rt. 03", "Rt. 3"),
	MOON		("Mt. Moon", true, "Mt Moon", "Moon", "Mt Doom"),
	BRIDGE		("Nugget Bridge", true, "Bridge", "Nugget"),
	MISTY		("Misty", true),
	SURGE		("Surge", true),
	FLY			("Fly", true, "HM02", "HM2", "HM Fly"),
	FLUTE		("Flute", true, "PokéFlute", "PokeFlute"),
	KOGA		("Koga", true),
	ERIKA		("Erika", false),
	BLAINE		("Blaine", false),
	SABRINA		("Sabrina", true),
	GIOVANNI	("Giovanni", true),
	LORELEI		("Lorelei", true),
	BRUNO		("Bruno", true),
	AGATHA		("Agatha", true),
	LANCE		("Lance", true),
	CHAMPION	("Champion", true, "Champ"),
	HALLOFFAME	("Hall of Fame", true, "HoF", "RTA End", "End");
	
	private String name;
	private boolean announce;
	private String alias1;
	private String alias2;
	private String alias3;
	
	private PKMNREDBLUE(String name, boolean announce, String alias1, String alias2, String alias3) {
		this.name = name;
		this.announce = announce;
		this.alias1 = alias1;
		this.alias2 = alias2;
		this.alias3 = alias3;
	}
	
	private PKMNREDBLUE(String name, boolean announce, String alias1, String alias2) {
		this(name, announce, alias1, alias2, null);
	}
	
	private PKMNREDBLUE(String name, boolean announce, String alias1) {
		this(name, announce, alias1, null, null);
	}

	private PKMNREDBLUE(String name, boolean announce) {
		this(name, announce, null, null, null);
	}
	
	public static String getSplitNameByNameOrAlias(String search) {
		for(PKMNREDBLUE split : PKMNREDBLUE.values()) {
			if(split.name != null && split.name.equalsIgnoreCase(search)) {
				return split.name;
			}
			if(split.alias1 != null && split.alias1.equalsIgnoreCase(search)) {
				return split.name;
			}
			if(split.alias2 != null && split.alias2.equalsIgnoreCase(search)) {
				return split.name;
			}
			if(split.alias3 != null && split.alias3.equalsIgnoreCase(search)) {
				return split.name;
			}
		}
		return null;
	}
	
	public static PKMNREDBLUE getSplitDataByNameOrAlias(String search) {
		for(PKMNREDBLUE split : PKMNREDBLUE.values()) {
			if(split.name != null && split.name.equalsIgnoreCase(search)) {
				return split;
			}
			if(split.alias1 != null && split.alias1.equalsIgnoreCase(search)) {
				return split;
			}
			if(split.alias2 != null && split.alias2.equalsIgnoreCase(search)) {
				return split;
			}
			if(split.alias3 != null && split.alias3.equalsIgnoreCase(search)) {
				return split;
			}
		}
		return null;
	}

	public String getName() {
		return name;
	}

	public boolean isAnnounce() {
		return announce;
	}

	public String getAlias1() {
		return alias1;
	}

	public String getAlias2() {
		return alias2;
	}

	public String getAlias3() {
		return alias3;
	}
	
}
