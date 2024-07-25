package eu.hardmc.skubiak.hardcurrency.account;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import eu.hardmc.skubiak.hardcurrency.HardCurrency;
import eu.hardmc.skubiak.hardcurrency.database.queries.QueryManager;
import eu.hardmc.skubiak.hardcurrency.database.queries.QueryResult;

public class CurrencyPlayerAccount {
	
	private HardCurrency plugin;
	private UUID uuid;
	
	public CurrencyPlayerAccount(HardCurrency plugin, Player player) {
		this.plugin = plugin;
		this.uuid = player.getUniqueId();
	}
	
	public CurrencyPlayerAccount(HardCurrency plugin, OfflinePlayer player) {
		this.plugin = plugin;
		this.uuid = player.getUniqueId();
	}
	
	public CurrencyPlayerAccount(HardCurrency plugin, UUID uuid) {
		this.plugin = plugin;
		this.uuid = uuid;
	}
	
	public void addPlayerCurrency(Currency currency, float amount) {
		Optional<Float> currentAmount = getPlayerCurrencyOrNull(currency);
		if (!currentAmount.isPresent()) {
			insertPlayerCurrency(currency, amount);
			return;
		}
		float newAmount = currentAmount.get() + amount;
		updatePlayerCurrency(currency, newAmount);
	}
	
	public void setPlayerCurrency(Currency currency, float amount) {
		Optional<Float> currentAmount = getPlayerCurrencyOrNull(currency);
		if (!currentAmount.isPresent()) {
			insertPlayerCurrency(currency, amount);
			return;
		}
		updatePlayerCurrency(currency, amount);
	}
	
	public float getPlayerCurrency(Currency currency) {
		Optional<Float> currentAmount = getPlayerCurrencyOrNull(currency);
		if (currentAmount.isPresent()) {
			return currentAmount.get();
		}
		return 0;
	}
	
	public Optional<Float> getPlayerCurrencyOrNull(Currency currency) {
		//get player money in amount
		QueryManager query = plugin.getQueryManager();
		try(QueryResult queryResult = query.executeQuery(HardCurrency.getQuery().getPlayerCurrencyAmount(), uuid.toString(), currency.getId())) {
			ResultSet result = queryResult.getResultSet();
            if (result.next()) {
                return Optional.of(result.getFloat("currency_amount"));
            }
		} catch (SQLException e) {}
		return Optional.empty();
	}
	
	
	private void insertPlayerCurrency(Currency currency, float amount) {
		QueryManager query = plugin.getQueryManager();
		query.execute(HardCurrency.getQuery().insertCurrencyToPlayer(), uuid.toString(), currency.getId(), amount);
	}
	
	private void updatePlayerCurrency(Currency currency, float amount) {
		QueryManager query = plugin.getQueryManager();
		query.execute(HardCurrency.getQuery().setPlayerCurrency(), amount, uuid.toString(), currency.getId());
	}
}
