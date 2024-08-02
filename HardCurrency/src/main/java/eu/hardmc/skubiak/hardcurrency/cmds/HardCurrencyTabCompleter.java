package eu.hardmc.skubiak.hardcurrency.cmds;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import eu.hardmc.skubiak.hardcurrency.HardCurrency;

public class HardCurrencyTabCompleter implements TabCompleter {
	
	private HardCurrency plugin;
	
	public HardCurrencyTabCompleter(HardCurrency plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		if (args.length == 1) {
			/*
			 * hc show
			 * hc currency
			 * hc player
			 */
			return handle0Args(sender);
		}
		if (args.length == 2) {
			/*
			 * hc show 	<Currency>
			 * hc currency 	<add / remove / list>
			 * hc player 	<add / remove / set>
			 */
			switch (args[0].toLowerCase()) {
			case "show":
				return getCurrenciesWithShowPermission(sender);
			case "currency":
				return handleCurrency1Arg(sender);
			case "player":
				return handlePlayer1Arg(sender);
			}
		}
		
		if (args.length == 3) {
			/*
			 * hc show 	<Currency> 		<Player>
			 * hc currency 	<add / remove> 		<Currency>
			 * hc player 	<add / remove / set>	<Currency>
			 */
			switch (args[0].toLowerCase()) {
			case "show":
				return getPlayersArg(sender, Permissions.SHOW_PLAYER_CURRENCY);
		
			case "currency":
				switch (args[1].toLowerCase()) {
				case "add":
					return Arrays.asList("<name>");
				case "remove":
					return getCurrenciesArg(sender, Permissions.CURRENCY_REMOVE);
				}
				
			case "player":
				switch (args[1].toLowerCase()) {
				case "add":
					return getCurrenciesArg(sender, Permissions.PLAYER_ADD);
				case "remove":
					return getCurrenciesArg(sender, Permissions.PLAYER_REMOVE);
				case "set":
					return getCurrenciesArg(sender, Permissions.PLAYER_SET);
				}
			}
		}
		if (args.length == 4) {
			/*
			 * hc show 	<Currency> 		<Player>	--null--		
             * hc currency 	<add / remove>		<Currency>      --null--		
             * hc currency 	list			--null--	--null--			
			 * hc player 	<add / remove / set>	<Currency>	<Player>
			 */
			switch (args[0].toLowerCase()) {		
			case "player":
				switch (args[1].toLowerCase()) {
				case "add":
					return getPlayersArg(sender, Permissions.PLAYER_ADD);
				case "remove":
					return getPlayersArg(sender, Permissions.PLAYER_REMOVE);
				case "set":
					return getPlayersArg(sender, Permissions.PLAYER_SET);
				}
			}
		}
		
		if (args.length == 5) {
			/*
			 * hc show 	<Currency> 		<Player>	--null--	--null--	 
			 * hc currency 	<add / remove>		<Currency>      --null--	--null--  	
			 * hc currency 	list			--null--	--null--	--null-- 	
			 * hc player 	<add / remove / set>	<Currency>	<Player>	<amount>
			 */
			switch (args[0].toLowerCase()) {		
			case "player":
				switch (args[1].toLowerCase()) {
				case "add":
					return Arrays.asList("<amount>");
				case "remove":
					return Arrays.asList("<amount>");
				case "set":
					return Arrays.asList("<amount>");
				}
			}
		}
		return Arrays.asList();
	}
	
	// ---Arg 1-----
	
	private List<String> handle0Args(CommandSender sender) {
		List<String> args = new ArrayList<>();
		if (hasPermissionOrChild(sender, Permissions.HELP)) 			args.add("help");
		if (hasPermissionOrChild(sender, Permissions.SHOW_CURRENCY)) 	args.add("show");
		if (hasPermissionOrChild(sender, Permissions.CURRENCY)) 		args.add("currency");
		if (hasPermissionOrChild(sender, Permissions.PLAYER)) 			args.add("player");
		return args;
	}
	
	// ---Arg 2-----
	
	private List<String> handleCurrency1Arg(CommandSender sender) {
		List<String> args = new ArrayList<>();
		if (sender.hasPermission(Permissions.CURRENCY_ADD.getPermission())) 	args.add("add");
		if (sender.hasPermission(Permissions.CURRENCY_REMOVE.getPermission()))	args.add("remove");
		if (sender.hasPermission(Permissions.CURRENCY_LIST.getPermission())) 	args.add("list");
		return args;
	}
	
	private List<String> handlePlayer1Arg(CommandSender sender) {
		List<String> args = new ArrayList<>();
		if (sender.hasPermission(Permissions.PLAYER_ADD.getPermission())) 		args.add("add");
		if (sender.hasPermission(Permissions.PLAYER_REMOVE.getPermission()))	args.add("remove");
		if (sender.hasPermission(Permissions.PLAYER_SET.getPermission())) 		args.add("set");
		return args;
	}
	
	
	
	// ---Arg 3-----
	
	
	//-------
	
	private List<String> getPlayersArg(CommandSender sender, Permissions permission) {
		List<String> args = new ArrayList<>();
		if (sender.hasPermission(permission.getPermission())) args = getAllPlayers();
		return args;
	}
	
	private List<String> getCurrenciesArg(CommandSender sender, Permissions permission) {
		List<String> args = new ArrayList<>();
		if (sender.hasPermission(permission.getPermission())) args = plugin.getCurrencyManager().getAllCurrencies();
		return args;
	}
	
	// --- Other -----
	
	private List<String> getAllPlayers() {
	    List<String> players = new ArrayList<>();
	    for (OfflinePlayer player : Bukkit.getOfflinePlayers()) {
	        if (!players.contains(player.getName())) players.add(player.getName());
	    }
        // Using HashSet to remove duplicates
        Set<String> set = new HashSet<>(players);
        List<String> uniquePlayers = new ArrayList<>(set);
        
	    return uniquePlayers;
	}
	
	private List<String> getCurrenciesWithShowPermission(CommandSender sender) {
		List<String> args = new ArrayList<>();
		List<String> currencies = plugin.getCurrencyManager().getAllCurrencies();
		for (String currency : currencies) {
			if (sender.hasPermission(Permissions.SHOW_CURRENCY.getPermission() + "." + currency)) args.add(currency);
		}	
		return args;
	}
	
    public boolean hasPermissionOrChild(CommandSender sender, Permissions permission) {
        // Sprawdza, czy gracz ma dokładnie podaną permisję
        if (sender.hasPermission(permission.getPermission())) {
            return true;
        }

        // Sprawdza wszystkie permisje gracza
        for (String perm : sender.getEffectivePermissions().stream().map(p -> p.getPermission()).toArray(String[]::new)) {
            // Sprawdza, czy permisja zaczyna się od podanego prefiksu i jest "child"
            if (perm.startsWith(permission.getPermission() + ".") && sender.hasPermission(perm)) {
                return true;
            }
        }

        // Jeśli żadna z powyższych nie zwróciła true, zwraca false
        return false;
    }

}
