package eu.hardmc.skubiak.hardcurrency.hooks;

import java.util.List;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import eu.hardmc.skubiak.hardcurrency.account.Currency;
import eu.hardmc.skubiak.hardcurrency.account.CurrencyPlayerAccount;
import eu.hardmc.skubiak.hardcurrency.api.HardCurrencyAPI;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;

public class PlaceholderAPIHook extends PlaceholderExpansion {

	@Override
	public @NotNull String getIdentifier() {
		return "hardcurrency";
	}

	@Override
	public @NotNull String getAuthor() {
		return "Skubiak";
	}

	@Override
	public @NotNull String getVersion() {
		return "1.0.0";
	}
	
	@Override
	public @Nullable String onRequest(OfflinePlayer offlinePlayer, @NotNull String params) {
		if (offlinePlayer == null || !offlinePlayer.isOnline() ) return null;
		Player player = offlinePlayer.getPlayer();
		List<String> currencies = HardCurrencyAPI.getAllCurrencies();
		for(String currencyName : currencies) {
			if (params.equalsIgnoreCase(currencyName)) {
				Currency currency = Currency.getCurrencyFromName(currencyName);
				CurrencyPlayerAccount playerAccount = new CurrencyPlayerAccount(HardCurrencyAPI.plugin, player);
				
				return playerAccount.getFormatedCurrencyValue(currency);
				
			}
		}
		return null;
	}
	
	public static void registerHook() {
		new PlaceholderAPIHook().register();
	}

}