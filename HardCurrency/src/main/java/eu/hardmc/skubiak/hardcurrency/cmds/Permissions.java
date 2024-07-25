package eu.hardmc.skubiak.hardcurrency.cmds;

public enum Permissions {
    
	// main args
    SHOW_CURRENCY("hardcurrency.show"),
    CURRENCY("hardcurrency.currency"),
    PLAYER("hardcurrency.player"),
    
    //show args
    SHOW_PLAYER_CURRENCY("hardcurrency.show.player"),
    
    // currency args
    CURRENCY_ADD("hardcurrency.currency.add"),
    CURRENCY_REMOVE("hardcurrency.currency.remove"),
    CURRENCY_LIST("hardcurrency.currency.list"),
	
	// player args
    PLAYER_ADD("hardcurrency.player.add"),
    PLAYER_REMOVE("hardcurrency.player.remove"),
    PLAYER_SET("hardcurrency.player.set");
	

    private final String permission;

    Permissions(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}