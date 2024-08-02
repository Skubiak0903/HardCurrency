package eu.hardmc.skubiak.hardcurrency.utils;

import java.util.List;

import eu.hardmc.skubiak.hardcurrency.HardCurrency;
import net.md_5.bungee.api.ChatColor;

public enum ConfigMessages {
	// main args
    PREFIX("messages.prefix"),
    ONLY_PLAYER("messages.onlyPlayer"),
    NO_PERMISSION("messages.noPermission"),
    WRONG_ARG("messages.wrongArg"),
    CURRENCY_NOT_EXISTS("messages.currencyNotExists"),
    NO_CURRENCIES_FOUND("messages.noCurrenciesFound"),
    NOT_FLOAT("messages.notFloat"),

    //cmd
    HELP("messages.commands.help"),
    
    SHOW("messages.commands.show"),
    SHOW_CURRENCY("messages.commands.showCurrency"),
    SHOW_PLAYER("messages.commands.showPlayer"),
    WRONG_SHOW_CMD("messages.commands.wrongShowCmd"),
    
    CURRENCY_ADD("messages.commands.currencyAdd"),
    CURRENCY_REMOVE("messages.commands.currencyRemoved"),
    CURRENCY_LIST("messages.commands.currencyList"),
    WRONG_CURRENCY_CMD("messages.commands.wrongCurrencyCmd"),
    
    PLAYER_ADD("messages.commands.playerAdd"),
    PLAYER_REMOVE("messages.commands.playerRemove"),
    PLAYER_SET("messages.commands.playerSet"),
    WRONG_PLAYER_CMD("messages.commands.wrongPlayerCmd");

    private final String path;

    ConfigMessages(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
    
    public String getMessage(HardCurrency plugin) {
    	String message = plugin.getConfig().getString(this.path);
    	return formatString(plugin, message);
    }
    
    public List<String> getList(HardCurrency plugin) {
    	return plugin.getConfig().getStringList(this.path);
    }
    
    public static String formatString(HardCurrency plugin, String message) {
    	String prefix = plugin.getConfig().getString(ConfigMessages.PREFIX.getPath());
    	prefix = ChatColor.translateAlternateColorCodes('&', prefix);
    	message = message.replaceAll("%PREFIX%", prefix);
    	message = ChatColor.translateAlternateColorCodes('&', message);
    	return message;
    }
}
