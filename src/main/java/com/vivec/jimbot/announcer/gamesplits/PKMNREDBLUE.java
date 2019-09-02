package com.vivec.jimbot.announcer.gamesplits;

public enum PKMNREDBLUE {

	RIVAL(0, "Rival 1", true, "Rival", "Blue 1", "Gary 1", "Leave Lab", "Growls are bad mkay"),
	NIDORAN		(1, "Nidoran", true, "Nido", "NidoranM", "MankeyS"),
	BROCK		(2, "Brock", true, "Bro"),
	ROUTE3		(3,"Route 3", true, "Route 03", "Rt. 03", "Rt. 3"),
	MOON		(4,"Mt. Moon", true, "Mt Moon", "Moon", "Mt Doom"),
	BRIDGE		(5,"Nugget Bridge", true, "Bridge", "Nugget", "IT was a mistake"),
	MISTY		(6,"Misty", true),
	SURGE		(7,"Surge", true, "Lt Surge", "Lt. Surge"),
	FLY			(8,"Fly", true, "HM02", "HM2", "HM Fly"),
	FLUTE		(9,"Flute", true, "PokéFlute", "PokeFlute", "Poké Flute"),
	KOGA		(10,"Koga", true, "PSR TM"),
	ERIKA		(11,"Erika", true),
	BLAINE		(12,"Blaine", true),
	SABRINA		(13,"Sabrina", true),
	GIOVANNI	(14,"Giovanni", true),
	LORELEI		(15,"Lorelei", true),
	BRUNO		(16,"Bruno", true, "Come on and slam"),
	AGATHA		(17,"Agatha", true),
	LANCE		(18,"Lance", true, "LetLanceDecide"),
	CHAMPION	(19,"Champion", true, "Champ"),
	HALLOFFAME	(20,"Hall of Fame", true, "HoF", "RTA End", "End"),
	DONE		(1000,"Done", true);

	private Integer orderNr;
	private String name;
	private boolean announce;
	private String[] aliases;

	private PKMNREDBLUE(Integer orderNr, String name, boolean announce, String... aliases) {
		this.orderNr = orderNr;
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

	public Integer getOrderNr() { return orderNr; }

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
