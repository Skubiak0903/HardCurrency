package eu.hardmc.skubiak.hardcurrency.cmds;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import eu.hardmc.skubiak.hardcurrency.HardCurrency;
import eu.hardmc.skubiak.hardcurrency.account.Currency;
import eu.hardmc.skubiak.hardcurrency.account.CurrencyPlayerAccount;

public class HardCurrencyCMD implements CommandExecutor {
	private HardCurrency plugin;
	
	public HardCurrencyCMD(HardCurrency plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args.length == 1) {
			/*
			 * hc show
			 * hc currency
			 * hc player
			 */
			
			switch (args[0].toLowerCase()) {
			case "show":
				return showCurrencies(sender);
			case "currency":
				return wrongCurrencyCmd(sender);
			case "player":
				return wrongPlayersCmd(sender);
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
		player.sendMessage("Twoje waluty:");
		for (Currency currency : this.getCurrenciesWithShowPermission(sender)) {
			player.sendMessage(currency.getName() + " - " + account.getPlayerCurrency(currency));
		}
		return true;
	}
	
	private boolean showCurrencies(CommandSender sender, String currencyName) {
		if (!hasShowPermission(sender, currencyName)) return noPermission(sender);
		if (!(sender instanceof Player)) {
			onlyPlayer(sender);
			return false;
		}
		Player player = (Player) sender;
		CurrencyPlayerAccount account = new CurrencyPlayerAccount(plugin, player);
		player.sendMessage(currencyName + " - " + account.getPlayerCurrency(Currency.getCurrencyFromName(currencyName)));
		return true;
	}
	
	@SuppressWarnings("deprecation")
	private boolean showCurrencies(CommandSender sender, String currencyName, String playerName) {
		if (!hasShowPermission(sender, currencyName) || !hasShowPermission(sender, "player")) return noPermission(sender);
		CurrencyPlayerAccount account = new CurrencyPlayerAccount(plugin, Bukkit.getOfflinePlayer(playerName));
		sender.sendMessage("Waluta: " + currencyName + " Gracza " + playerName + " wynosi: " + account.getPlayerCurrency(Currency.getCurrencyFromName(currencyName)));
		return true;
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
		sender.sendMessage("Wszystkie waluty:");
		for (String currency : plugin.getCurrencyManager().getAllCurrencies()) {
			sender.sendMessage(currency);
		}
		return true;
	}
	
	private boolean currencyAdd(CommandSender sender, String currencyName) {
		if (!sender.hasPermission(Permissions.CURRENCY_ADD.getPermission())) return noPermission(sender);
		plugin.getCurrencyManager().addCurrency(currencyName);
		sender.sendMessage("Waluta została dodana poprawnie");
		return true;
	}
	
	private boolean currencyRemove(CommandSender sender, String currencyName) {
		if (!sender.hasPermission(Permissions.CURRENCY_REMOVE.getPermission())) return noPermission(sender);
		plugin.getCurrencyManager().removeCurrency(currencyName);
		sender.sendMessage("Waluta została usunięta poprawnie");
		return true;
	}
	
	
	//Manage Player Currency
	
	private boolean playerCurrencyAdd(CommandSender sender, String currencyName, String playerName, String amount) {
		
		if (!sender.hasPermission(Permissions.PLAYER_ADD.getPermission())) return noPermission(sender);
		if (!sender.hasPermission(Permissions.PLAYER_REMOVE.getPermission()) && amount.startsWith("-")) return noPermission(sender);
		
		return handleAddCurrency(sender, currencyName, playerName, amount);
	}
	
	private boolean playerCurrencyRemove(CommandSender sender, String currencyName, String playerName, String amount) {
		
		if (!sender.hasPermission(Permissions.PLAYER_REMOVE.getPermission())) return noPermission(sender);
		if (!sender.hasPermission(Permissions.PLAYER_ADD.getPermission()) && amount.startsWith("-")) return noPermission(sender);
		
		return handleAddCurrency(sender, currencyName, playerName, "-" + amount);
	}
	
	@SuppressWarnings("deprecation")
	private boolean playerCurrencySet(CommandSender sender, String currencyName, String playerName, String amount) {
		if (!sender.hasPermission(Permissions.PLAYER_SET.getPermission())) return noPermission(sender);
		OfflinePlayer player = Bukkit.getOfflinePlayer(playerName);
		Currency currency = Currency.getCurrencyFromName(currencyName);
		CurrencyPlayerAccount account = new CurrencyPlayerAccount(plugin, player);
		account.setPlayerCurrency(currency, Float.valueOf(amount));
		sender.sendMessage("Nowa wartość waluty " + currencyName + " dla gracza " + playerName + " wynosi: " + amount);
		return true;
	}
	
	@SuppressWarnings("deprecation")
	private boolean handleAddCurrency(CommandSender sender, String currencyName, String playerName, String amount) {
		if (amount.startsWith("--")) amount = amount.substring(2);
		OfflinePlayer player = Bukkit.getOfflinePlayer(playerName);
		Currency currency = Currency.getCurrencyFromName(currencyName);
		CurrencyPlayerAccount account = new CurrencyPlayerAccount(plugin, player);
		account.addPlayerCurrency(currency, Float.valueOf(amount));
		sender.sendMessage("Nowa wartość waluty " + currencyName + " dla gracza " + playerName + " wynosi: " + account.getPlayerCurrency(currency));
		return true;
	}
	
	//--Messages--
	
	private boolean wrongShowCmd(CommandSender sender) {
		if (!hasPermissionOrChild(sender, Permissions.SHOW_CURRENCY)) return noPermission(sender);
		sender.sendMessage(ChatColor.RED + "Złe Argumenty! poprawne użycie: /hc show [Currency] [Player]");
		return false;
	}
	
	private boolean wrongCurrencyCmd(CommandSender sender) {
		if (!hasPermissionOrChild(sender, Permissions.CURRENCY)) return noPermission(sender);
		sender.sendMessage(ChatColor.RED + "Złe Argumenty! poprawne użycie: /hc currency <add / remove / list> <name>");
		return false;
	}
	
	private boolean wrongPlayersCmd(CommandSender sender) {
		if (!hasPermissionOrChild(sender, Permissions.PLAYER)) return noPermission(sender);
		sender.sendMessage(ChatColor.RED + "Złe Argumenty! poprawne użycie: /hc player <add / remove / set> <Currency> <Player> <amount>");
		return false;
	}
	
	private boolean onlyPlayer(CommandSender sender) {
		sender.sendMessage(ChatColor.RED + "Tylko gracz może wykonać tą komendę!");
		return false;
	}
	
	private boolean noPermission(CommandSender sender) {
		sender.sendMessage(ChatColor.RED + "Nie masz uprawnień do tej komendy!");
		return false;
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
