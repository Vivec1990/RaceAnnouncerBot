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
	private String[] aliases;
	
	private PKMNREDBLUE(String name, boolean announce, String... aliases) {
		this.name = name;
		this.announce = announce;
		this.aliases = aliases;
	}
	
	public static String getSplitNameByNameOrAlias(String search) {
		for(PKMNREDBLUE split : PKMNREDBLUE.values()) {
			if(split.name != null && split.name.equalsIgnoreCase(search)) {
				return split.name;
			}
			for(String alias : split.getAliases()) {
				if(alias.equalsIgnoreCase(search)) {
					return split.name;
				}
			}
		}
		return null;
	}
	
	public static PKMNREDBLUE getSplitDataByNameOrAlias(String search) {
		for(PKMNREDBLUE split : PKMNREDBLUE.values()) {
			if(split.name != null && split.name.equalsIgnoreCase(search)) {
				return split;
			}
			for(String alias : split.getAliases()) {
				if(alias.equalsIgnoreCase(search)) {
					return split;
				}
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

	public String[] getAliases() {
		return aliases;
	}
	
}
