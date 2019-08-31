package com.vivec.jimbot.announcer.gamesplits;

public enum PKMNREDBLUE {

	NIDORAN		(0, "Nidoran", true, "Nido", "NidoranM", "MankeyS"),
	BROCK		(1, "Brock", true, "Bro"),
	ROUTE3		(2,"Route 3", true, "Route 03", "Rt. 03", "Rt. 3"),
	MOON		(3,"Mt. Moon", true, "Mt Moon", "Moon", "Mt Doom"),
	BRIDGE		(4,"Nugget Bridge", true, "Bridge", "Nugget", "IT was a mistake"),
	MISTY		(5,"Misty", true),
	SURGE		(6,"Surge", true, "Lt Surge", "Lt. Surge"),
	FLY		(7,"Fly", true, "HM02", "HM2", "HM Fly"),
	FLUTE		(8,"Flute", true, "PokéFlute", "PokeFlute", "Poké Flute"),
	KOGA		(9,"Koga", true, "PSR TM"),
	ERIKA		(10,"Erika", true),
	BLAINE		(11,"Blaine", true),
	SABRINA		(12,"Sabrina", true),
	GIOVANNI	(13,"Giovanni", true),
	LORELEI		(14,"Lorelei", true),
	BRUNO		(15,"Bruno", true, "Come on and slam"),
	AGATHA		(16,"Agatha", true),
	LANCE		(17,"Lance", true, "LetLanceDecide"),
	CHAMPION	(18,"Champion", true, "Champ"),
	HALLOFFAME	(19,"Hall of Fame", true, "HoF", "RTA End", "End"),
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
