package de.sebmey.jimbot;

public enum PokemonRedSplit {

	EMPTY		("Empty"),
	OTHER		("Other"),
	NIDORAN		("Nidoran", "Nido", "NidoranM"),
	BROCK		("Brock", "Bro"),
	ROUTE3		("Route 3", "Route 03", "Mt. Moon Enter", "Mt Moon Enter"),
	MTMOON		("Mt. Moon", "Mt Moon", "Cave", "Zubat infested shithole"),
	BRIDGE		("Nugget Bridge", "Bridge", "Instant Regret"),
	MISTY		("Misty"),
	SURGE		("Surge", "Lt. Surge", "Lieutanant Surge"),
	FLY			("Fly", "HM02"),
	FLUTE		("Flute", "Pokeflute", "Pokèflute"),
	KOGA		("Koga", "Boom"),
	ERIKA		("Erika"),
	BLAINE		("Blaine"),
	SABRINA		("Sabrina"),
	GIOVANNI	("Giovanni", "Giovanni 2", "Giovanni II"),
	LORELEI		("Lorelei"),
	BRUNO		("Bruno"),
	AGATHA		("Agatha"),
	LANCE		("Lance"),
	CHAMPION	("Champion", "Champ"),
	HOF			("Hall of Fame", "Hall Of Fame", "HoF", "RTA End");
	
	private String name;
	private String alias1;
	private String alias2;
	private String alias3;
	
	private PokemonRedSplit(String name, String alias1, String alias2, String alias3) {
		this.name = name;
		this.alias1 = alias1;
		this.alias2 = alias2;
		this.alias3 = alias3;
	}
	
	private PokemonRedSplit(String name, String alias1, String alias2) {
		this(name, alias1, alias2, null);
	}
	
	private PokemonRedSplit(String name, String alias1) {
		this(name, alias1, null, null);
	}
	
	private PokemonRedSplit(String name) {
		this(name, null, null, null);
	}
	
	public PokemonRedSplit byNameOrAlias(String input) {
		if(input == null || input.isEmpty()) return PokemonRedSplit.EMPTY;
		for(PokemonRedSplit split : PokemonRedSplit.values()) {
			if(
				input.equals(split.getName()) ||
				input.equals(split.getAlias1()) ||
				input.equals(split.getAlias2()) ||
				input.equals(split.getAlias3())
			) {
				return split;
			}
		}
		return PokemonRedSplit.OTHER;
	}

	private String getName() {
		return name;
	}

	private String getAlias1() {
		return alias1;
	}

	private String getAlias2() {
		return alias2;
	}

	private String getAlias3() {
		return alias3;
	}
	
}
