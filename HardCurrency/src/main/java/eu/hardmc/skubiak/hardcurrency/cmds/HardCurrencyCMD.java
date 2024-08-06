package eu.hardmc.skubiak.hardcurrency.cmds;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import eu.hardmc.skubiak.hardcurrency.HardCurrency;
import eu.hardmc.skubiak.hardcurrency.account.Currency;
import eu.hardmc.skubiak.hardcurrency.account.CurrencyPlayerAccount;
import eu.hardmc.skubiak.hardcurrency.utils.ConfigMessages;

public class HardCurrencyCMD implements CommandExecutor {
	private HardCurrency plugin;
	
	public HardCurrencyCMD(HardCurrency plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args.length == 0) {
			return sendHelp(sender, label);
		}
		if (args.length == 1) {
			/*
			 * hc show
			 * hc currency
			 * hc player
			 */
			
			switch (args[0].toLowerCase()) {
			case "help":
				return sendHelp(sender, label);
			case "show":
				return showCurrencies(sender);
			case "currency":
				return wrongCurrencyCmd(sender);
			case "player":
				return wrongPlayersCmd(sender);
			default:
				return wrongArgument(sender);
			}
		}
		if (args.length == 2) {
			/*
			 * hc show 	<Currency>
			 * hc currency 	<add / remove / list>
			 * hc player 	<add / remove / set>
			 */
			switch (args[0].toLowerCase()) {
			case "show":
				return showCurrencies(sender, args[1]);
				
			case "currency":
				switch (args[1].toLowerCase()) {
				case "add":
					return wrongCurrencyCmd(sender);
				case "remove":
					return wrongCurrencyCmd(sender);
				case "list":
					return currencyList(sender);
				}
				
			case "player":
				return wrongPlayersCmd(sender);
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
				return showCurrencies(sender, args[1], args[2]);
		
			case "currency":
				switch (args[1].toLowerCase()) {
				case "add":
					return currencyAdd(sender, args[2]);
				case "remove":
					return currencyRemove(sender, args[2]);
				}
				
			case "player":
				return wrongPlayersCmd(sender);
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
				return wrongPlayersCmd(sender);
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
					return playerCurrencyAdd(sender, args[2], args[3], args[4]);
				case "remove":
					return playerCurrencyRemove(sender, args[2], args[3], args[4]);
				case "set":
					return playerCurrencySet(sender, args[2], args[3], args[4]);
				}
			}
		}
		//return Arrays.asList();
		return false;
	}
	
	// Help
	
	private boolean sendHelp(CommandSender sender, String cmd) {
		if (!hasPermissionOrChild(sender, Permissions.HELP)) return noPermission(sender);
		
		//Help
		/*if (sender.hasPermission(Permissions.HELP.getPermission())) sender.sendMessage(ChatColor.GRAY + "/" + cmd + " help" + " - Shows This Page");
		
		// Show
		if (hasPermissionOrChild(sender, Permissions.SHOW_CURRENCY)) {
			sender.sendMessage(ChatColor.GRAY + "/" + cmd + " show" + " - show all avaiable to you currencies");
			sender.sendMessage(ChatColor.GRAY + "/" + cmd + " show <Currency>" + " - show currency to you currencies");
		}
		if (sender.hasPermission(Permissions.SHOW_PLAYER_CURRENCY.getPermission())) sender.sendMessage(ChatColor.GRAY + "/" + cmd + " show <Currency> <Player>" + " - show how much player has specific currency");
		
		// Currency
		if (sender.hasPermission(Permissions.CURRENCY_ADD.getPermission())) sender.sendMessage(ChatColor.GRAY + "/" + cmd + " currency add <name>" + " - Add new Currency");
		if (sender.hasPermission(Permissions.CURRENCY_REMOVE.getPermission())) sender.sendMessage(ChatColor.GRAY + "/" + cmd + " currency remove <name>" + " - Remove Currency");
		if (sender.hasPermission(Permissions.CURRENCY_LIST.getPermission())) sender.sendMessage(ChatColor.GRAY + "/" + cmd + " currency list" + " - List all Currencies");
		
		// Player
		if (sender.hasPermission(Permissions.PLAYER_ADD.getPermission())) sender.sendMessage(ChatColor.GRAY + "/" + cmd + " player add <Currency> <Player> <Amount>" + " - Add Currency to player");
		if (sender.hasPermission(Permissions.PLAYER_REMOVE.getPermission())) sender.sendMessage(ChatColor.GRAY + "/" + cmd + " player remove <Currency> <Player> <Amount>" + " - Remove Currency from player");
		if (sender.hasPermission(Permissions.PLAYER_SET.getPermission())) sender.sendMessage(ChatColor.GRAY + "/" + cmd + " player set <Currency> <Player> <Amount>" + " - Set player Currency");
		*/
		// new
		
		/*for (String message : plugin.getConfig().getStringList(ConfigMessages.HELP.getPath())){
			message = ConfigMessages.formatString(plugin, message);
			message = message.replaceAll("%cmd%", cmd);
			//Help
			if (message.startsWith("[HELP]") 			&& sender.hasPermission(Permissions.HELP.getPermission())) { 
				sender.sendMessage(message.replaceFirst("\\[HELP\\]", "")); continue; }
			//Show
			if (message.startsWith("[SHOW]") 			&& hasPermissionOrChild(sender, Permissions.SHOW_CURRENCY)) { 
				sender.sendMessage(message.replaceFirst("\\[SHOW\\]", "")); continue; }
			if (message.startsWith("[SHOW_PLAYER]") 	&& sender.hasPermission(Permissions.SHOW_PLAYER_CURRENCY.getPermission())) { 
				sender.sendMessage(message.replaceFirst("\\[SHOW_PLAYER\\]", "")); continue; }
			//Currency
			if (message.startsWith("[CURRENCY_ADD]") 	&& sender.hasPermission(Permissions.CURRENCY_ADD.getPermission())) { 
				sender.sendMessage(message.replaceFirst("\\[CURRENCY_ADD\\]", "")); continue; }
			if (message.startsWith("[CURRENCY_REMOVE]") && sender.hasPermission(Permissions.CURRENCY_REMOVE.getPermission())) { 
				sender.sendMessage(message.replaceFirst("\\[CURRENCY_REMOVE\\]", "")); continue; }
			if (message.startsWith("[CURRENCY_LIST]") 	&& sender.hasPermission(Permissions.CURRENCY_LIST.getPermission())) { 
				sender.sendMessage(message.replaceFirst("\\[CURRENCY_LIST\\]", "")); continue; }
			//Player
			if (message.startsWith("[PLAYER_ADD]") 		&& sender.hasPermission(Permissions.PLAYER_ADD.getPermission())) { 
				sender.sendMessage(message.replaceFirst("\\[PLAYER_ADD\\]", "")); continue; }
			if (message.startsWith("[PLAYER_REMOVE]") 	&& sender.hasPermission(Permissions.PLAYER_REMOVE.getPermission())) { 
				sender.sendMessage(message.replaceFirst("\\[PLAYER_REMOVE\\]", "")); continue; }
			if (message.startsWith("[PLAYER_SET]")	 	&& sender.hasPermission(Permissions.PLAYER_SET.getPermission())) { 
				sender.sendMessage(message.replaceFirst("\\[PLAYER_SET\\]", "")); continue; }
			
			if (!message.matches("^\\\\[[^\\\\]]+\\\\].*"))	sender.sendMessage(message);
		}	*/
		Map<String, String> permissionMap = new HashMap<>();
	    permissionMap.put("[HELP]", Permissions.HELP.getPermission());
	    permissionMap.put("[SHOW]", Permissions.SHOW_CURRENCY.getPermission());
	    permissionMap.put("[SHOW_PLAYER]", Permissions.SHOW_PLAYER_CURRENCY.getPermission());
	    permissionMap.put("[CURRENCY_ADD]", Permissions.CURRENCY_ADD.getPermission());
	    permissionMap.put("[CURRENCY_REMOVE]", Permissions.CURRENCY_REMOVE.getPermission());
	    permissionMap.put("[CURRENCY_LIST]", Permissions.CURRENCY_LIST.getPermission());
	    permissionMap.put("[PLAYER_ADD]", Permissions.PLAYER_ADD.getPermission());
	    permissionMap.put("[PLAYER_REMOVE]", Permissions.PLAYER_REMOVE.getPermission());
	    permissionMap.put("[PLAYER_SET]", Permissions.PLAYER_SET.getPermission());

	    List<String> messages = plugin.getConfig().getStringList(ConfigMessages.HELP.getPath());
	    
	    for (String message : messages) {
	        message = ConfigMessages.formatString(plugin, message);
	        message = message.replaceAll("%cmd%", cmd);
	        
	        boolean handled = false;
	        
	        for (Map.Entry<String, String> entry : permissionMap.entrySet()) {
	            String tag = entry.getKey();
	            String permission = entry.getValue();
	            
	            if (message.startsWith(tag) && sender.hasPermission(permission)) {
	                sender.sendMessage(message.replaceFirst("\\Q" + tag + "\\E", ""));
	                handled = true;
	                break;
	            }
	        }
	        
	        if (!handled) {
	            if (!message.matches("^\\[[^\\]]+\\].*")) {
	                sender.sendMessage(message);
	            }
	        }
	    }
		return false;
	}
	
	
	//Show Currency
	
	private boolean showCurrencies(CommandSender sender) {
		if (!sender.hasPermission(Permissions.SHOW_CURRENCY.getPermission())) return noPermission(sender);
		if (!(sender instanceof Player)) {
			onlyPlayer(sender);
			wrongShowCmd(sender);
			return false;
		}
		Player player = (Player) sender;
		CurrencyPlayerAccount account = new CurrencyPlayerAccount(plugin, player);
		List<Currency> currencies = this.getCurrenciesWithShowPermission(sender);
		if (currencies.size() == 0) {
			player.sendMessage(ConfigMessages.NO_CURRENCIES_FOUND.getMessage(plugin));
			return false;
		}
		//player.sendMessage("Twoje waluty:");
		List<String> messages = ConfigMessages.SHOW.getList(plugin);
		int i = 0;
		for (String message : messages) {
			message = ConfigMessages.formatString(plugin, message);
			if (message.startsWith("[CURRENCY_REPEAT]")) break;
			player.sendMessage(message);
			i++;
		}
		for (Currency currency : currencies) {
			//player.sendMessage(currency.getName() + " - " + account.getPlayerCurrency(currency));
			String message = messages.get(i);
			message = message.replaceFirst("\\[CURRENCY_REPEAT\\]", ""); 
			message = ConfigMessages.formatString(plugin, message);
			message = formatShowMessage(message, currency.getName(), account.getFormatedCurrencyValue(currency), player.getName());
			player.sendMessage(message);
		}
		if (i+1 >= messages.size()) return true;
		for (int i2 = i+1; i2 < messages.size(); i2++) {
			String message = messages.get(i2);
			message = ConfigMessages.formatString(plugin, message);
			player.sendMessage(message);
		}
		return true;
	}
	
	private boolean showCurrencies(CommandSender sender, String currencyName) {
		if (!hasShowPermission(sender, currencyName)) return noPermission(sender);
		if (!(sender instanceof Player)) {
			onlyPlayer(sender);
			wrongShowCmd(sender);
			return false;
		}
		Player player = (Player) sender;
		CurrencyPlayerAccount account = new CurrencyPlayerAccount(plugin, player);
		Currency currency = Currency.getCurrencyFromName(currencyName);
		if (currency == null) {
			return currencyNotExists(sender, currencyName);
		}
		//player.sendMessage(currencyName + " - " + account.getPlayerCurrency(currency));
		List<String> messages = ConfigMessages.SHOW_CURRENCY.getList(plugin);
		for (String message : messages) {
			message = ConfigMessages.formatString(plugin, message);
			message = formatShowMessage(message, currencyName, account.getFormatedCurrencyValue(currency), player.getName());
			player.sendMessage(message);
		}
		return true;
	}
	
	@SuppressWarnings("deprecation")
	private boolean showCurrencies(CommandSender sender, String currencyName, String playerName) {
		if (!hasShowPermission(sender, currencyName) || !hasShowPermission(sender, "player")) return noPermission(sender);
		Currency currency = Currency.getCurrencyFromName(currencyName);
		if (currency == null) {
			return currencyNotExists(sender, currencyName);
		}
		OfflinePlayer player = Bukkit.getOfflinePlayer(playerName);
		/*if (player.hasPlayedBefore()) {
			return notPlayedBefore(sender);
		}*/
		CurrencyPlayerAccount account = new CurrencyPlayerAccount(plugin, player);
		//sender.sendMessage("Waluta: " + currencyName + " Gracza " + playerName + " wynosi: " + account.getPlayerCurrency(currency));
		List<String> messages = ConfigMessages.SHOW_PLAYER.getList(plugin);
		for (String message : messages) {
			message = ConfigMessages.formatString(plugin, message);
			message = formatShowMessage(message, currencyName, account.getFormatedCurrencyValue(currency), player.getName());
			sender.sendMessage(message);
		}
		return true;
	}
	
	public String formatShowMessage(String message, String currencyName, String amount, String playerName) {
		message = message.replaceAll("%CURRENCY%", currencyName);
		message = message.replaceAll("%AMOUNT%", amount);
		message = message.replaceAll("%PLAYER%", playerName);
		return message;
	}
	
	
	private List<Currency> getCurrenciesWithShowPermission(CommandSender sender) {
		List<Currency> permCurrencies = new ArrayList<>();
		List<String> currencies = plugin.getCurrencyManager().getAllCurrencies();
		for (String currency : currencies) {
			if (hasShowPermission(sender, currency)) permCurrencies.add(Currency.getCurrencyFromName(currency));
		}	
		return permCurrencies;
	}
	
	private boolean hasShowPermission(CommandSender sender, String currencyName) {
		return sender.hasPermission(Permissions.SHOW_CURRENCY.getPermission() + "." + currencyName.toLowerCase());
	}
	
	
	//Manage Currency
	
	private boolean currencyList(CommandSender sender) {
		if (!sender.hasPermission(Permissions.CURRENCY_LIST.getPermission())) return noPermission(sender);
		List<String> currencies = plugin.getCurrencyManager().getAllCurrencies();
		if (currencies.size() == 0) {
			sender.sendMessage(ConfigMessages.NO_CURRENCIES_FOUND.getMessage(plugin));
			return false;
		}
		/*sender.sendMessage("Wszystkie waluty:");
		for (String currency : currencies) {
			sender.sendMessage(currency);
		}*/
		List<String> messages = ConfigMessages.CURRENCY_LIST.getList(plugin);
		int i = 0;
		for (String message : messages) {
			message = ConfigMessages.formatString(plugin, message);
			if (message.startsWith("[CURRENCY_REPEAT]")) break;
			sender.sendMessage(message);
			i++;
		}
		for (String currencyName : currencies) {
			String message = messages.get(i);
			message = message.replaceFirst("\\[CURRENCY_REPEAT\\]", ""); 
			message = ConfigMessages.formatString(plugin, message);
			message = message.replaceAll("%CURRENCY%", currencyName);
			sender.sendMessage(message);
		}
		if (i+1 >= messages.size()) return true;
		for (int i2 = i+1; i2 < messages.size(); i2++) {
			String message = messages.get(i2);
			message = ConfigMessages.formatString(plugin, message);
			sender.sendMessage(message);
		}
		return true;
	}
	
	private boolean currencyAdd(CommandSender sender, String currencyName) {
		if (!sender.hasPermission(Permissions.CURRENCY_ADD.getPermission())) return noPermission(sender);
		plugin.getCurrencyManager().addCurrency(currencyName);
		//sender.sendMessage("Waluta została dodana poprawnie");
		String message = ConfigMessages.CURRENCY_ADD.getMessage(plugin);
		message = message.replaceAll("%CURRENCY%", currencyName);
		sender.sendMessage(message);
		return true;
	}
	
	private boolean currencyRemove(CommandSender sender, String currencyName) {
		if (!sender.hasPermission(Permissions.CURRENCY_REMOVE.getPermission())) return noPermission(sender);
		Currency currency = Currency.getCurrencyFromName(currencyName);
		if (currency == null) {
			return currencyNotExists(sender, currencyName);
		}
		plugin.getCurrencyManager().removeCurrency(currencyName);
		//sender.sendMessage("Waluta została usunięta poprawnie");
		String message = ConfigMessages.CURRENCY_REMOVE.getMessage(plugin);
		message = message.replaceAll("%CURRENCY%", currencyName);
		sender.sendMessage(message);
		return true;
	}
	
	
	//Manage Player Currency
	
	private boolean playerCurrencyAdd(CommandSender sender, String currencyName, String playerName, String amount) {
		
		if (!sender.hasPermission(Permissions.PLAYER_ADD.getPermission())) return noPermission(sender);
		if (!sender.hasPermission(Permissions.PLAYER_REMOVE.getPermission()) && amount.startsWith("-")) return noPermission(sender);
		
		return handleAddCurrency(sender, currencyName, playerName, amount, "ADD");
	}
	
	private boolean playerCurrencyRemove(CommandSender sender, String currencyName, String playerName, String amount) {
		
		if (!sender.hasPermission(Permissions.PLAYER_REMOVE.getPermission())) return noPermission(sender);
		if (!sender.hasPermission(Permissions.PLAYER_ADD.getPermission()) && amount.startsWith("-")) return noPermission(sender);
		
		return handleAddCurrency(sender, currencyName, playerName, "-" + amount, "REMOVE");
	}
	
	@SuppressWarnings("deprecation")
	private boolean playerCurrencySet(CommandSender sender, String currencyName, String playerName, String amount) {
		if (!sender.hasPermission(Permissions.PLAYER_SET.getPermission())) return noPermission(sender);
		OfflinePlayer player = Bukkit.getOfflinePlayer(playerName);
		/*if (player.hasPlayedBefore()) {
			return notPlayedBefore(sender);
		}*/
		Currency currency = Currency.getCurrencyFromName(currencyName);
		if (currency == null) {
			return currencyNotExists(sender, currencyName);
		}
		float amountFloat;
		try {
		    amountFloat = Float.parseFloat(amount);
		} catch (NumberFormatException e) {
		    return notANumber(sender, amount);
		}
		CurrencyPlayerAccount account = new CurrencyPlayerAccount(plugin, player);
		account.setPlayerCurrency(currency, amountFloat);
		//sender.sendMessage("Nowa wartość waluty " + currencyName + " dla gracza " + playerName + " wynosi: " + amount);
		String message = ConfigMessages.PLAYER_SET.getMessage(plugin);
		message = formatPlayerMessage(message, "SETTED_POINTS", amount, playerName, currencyName, account.getFormatedCurrencyValue(currency));
		sender.sendMessage(message);
		return true;
	}
	
	@SuppressWarnings("deprecation")
	private boolean handleAddCurrency(CommandSender sender, String currencyName, String playerName, String amount, String type) {
		if (type == "ADD" && amount.startsWith("-")) type = "REMOVE";
		if (type == "REMOVE" && amount.startsWith("--")) type = "ADD";
		if (amount.startsWith("--")) amount = amount.substring(2);
		OfflinePlayer player = Bukkit.getOfflinePlayer(playerName);
		/*if (player.hasPlayedBefore()) {
			return notPlayedBefore(sender);
		}*/
		Currency currency = Currency.getCurrencyFromName(currencyName);
		if (currency == null) {
			return currencyNotExists(sender, currencyName);
		}
		float amountFloat;
		try {
		    amountFloat = Float.parseFloat(amount);
		} catch (NumberFormatException e) {
		    return notANumber(sender, amount);
		}
		CurrencyPlayerAccount account = new CurrencyPlayerAccount(plugin, player);
		account.addPlayerCurrency(currency, amountFloat);
		//sender.sendMessage("Nowa wartość waluty " + currencyName + " dla gracza " + playerName + " wynosi: " + account.getPlayerCurrency(currency));
		String message;
		if (type.toUpperCase() == "REMOVE") {
			message = ConfigMessages.PLAYER_REMOVE.getMessage(plugin);
			message = formatPlayerMessage(message, "REMOVED_POINTS", amount, playerName, currencyName, account.getFormatedCurrencyValue(currency));
		} else {
			message = ConfigMessages.PLAYER_ADD.getMessage(plugin);
			message = formatPlayerMessage(message, "ADDED_POINTS", amount, playerName, currencyName, account.getFormatedCurrencyValue(currency));
		}
		sender.sendMessage(message);
		return true;
	}
	//ADDED_POINTS, PLAYER, CURRENCY, ALL_POINTS
	private String formatPlayerMessage(String message, String placeholder, String amount, String playerName, String currencyName, String allPoints) {
		message = message.replaceAll("%" + placeholder.toUpperCase() + "%", amount);
		message = message.replaceAll("%PLAYER%", playerName);
		message = message.replaceAll("%CURRENCY%", currencyName);
		message = message.replaceAll("%ALL_POINTS%", allPoints);
		return message;
	}
	
	//--Messages--
	
	private boolean wrongArgument(CommandSender sender) {
		if (!hasPermissionOrChild(sender, Permissions.HELP)) return noPermission(sender);
		//sender.sendMessage(ChatColor.RED + "Złe Argument! wszystkie komendy: /hc help");
		sender.sendMessage(ConfigMessages.WRONG_ARG.getMessage(plugin));
		return false;
	}
	
	private boolean wrongShowCmd(CommandSender sender) {
		if (!hasPermissionOrChild(sender, Permissions.SHOW_CURRENCY)) return noPermission(sender);
		//sender.sendMessage(ChatColor.RED + "Złe Argumenty! poprawne użycie: /hc show [Currency] [Player]");
		sender.sendMessage(ConfigMessages.WRONG_SHOW_CMD.getMessage(plugin));
		return false;
	}
	
	private boolean wrongCurrencyCmd(CommandSender sender) {
		if (!hasPermissionOrChild(sender, Permissions.CURRENCY)) return noPermission(sender);
		//sender.sendMessage(ChatColor.RED + "Złe Argumenty! poprawne użycie: /hc currency <add / remove / list> <name>");
		sender.sendMessage(ConfigMessages.WRONG_CURRENCY_CMD.getMessage(plugin));
		return false;
	}
	
	private boolean wrongPlayersCmd(CommandSender sender) {
		if (!hasPermissionOrChild(sender, Permissions.PLAYER)) return noPermission(sender);
		//sender.sendMessage(ChatColor.RED + "Złe Argumenty! poprawne użycie: /hc player <add / remove / set> <Currency> <Player> <amount>");
		sender.sendMessage(ConfigMessages.WRONG_PLAYER_CMD.getMessage(plugin));
		return false;
	}
	
	private boolean onlyPlayer(CommandSender sender) {
		//sender.sendMessage(ChatColor.RED + "Tylko gracz może wykonać tą komendę!");
		sender.sendMessage(ConfigMessages.ONLY_PLAYER.getMessage(plugin));
		return false;
	}
	
	private boolean noPermission(CommandSender sender) {
		//sender.sendMessage(ChatColor.RED + "Nie masz uprawnień do tej komendy!");
		sender.sendMessage(ConfigMessages.NO_PERMISSION.getMessage(plugin));
		return false;
	}
	
	/*private boolean notPlayedBefore(CommandSender sender) {
		sender.sendMessage("Taki Gracz nigdy nie grał na tym serwerze");
		return false;
	}*/
	
	private boolean currencyNotExists(CommandSender sender, String currencyName) {
		String message = ConfigMessages.CURRENCY_NOT_EXISTS.getMessage(plugin);
		message = message.replaceAll("%CURRENCY%", currencyName);
		sender.sendMessage(message);
		return false;
	}
	
	private boolean notANumber(CommandSender sender, String value) {
		String message = ConfigMessages.NOT_FLOAT.getMessage(plugin);
		message = message.replaceAll("%VALUE%", value);
		sender.sendMessage(message);
		return false;
	}
	
	private boolean hasPermissionOrChild(CommandSender sender, Permissions permission) {
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
