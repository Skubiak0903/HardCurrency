package eu.hardmc.skubiak.hardcurrency.database.queries;

public class MySQLQuery implements Query {

	@Override
	public String selectAllCurrencies() {
	    return "SELECT * FROM `HardCurrency_Currencies`";
	}

	@Override
	public String addCurrency() {
	    return "INSERT INTO `HardCurrency_Currencies`(`name`) VALUES (?)";
	}

	@Override
	public String removeCurrencyUsingId() {
	    return "DELETE FROM `HardCurrency_Currencies` WHERE `id` = ?";
	}

	@Override
	public String removeCurrencyUsingName() {
	    return "DELETE FROM `HardCurrency_Currencies` WHERE `name` = ?";
	}
	
	@Override
	public String insertCurrencyToPlayer() {
		return "INSERT INTO `HardCurrency_Players`(`uuid`, `currency_id`, `currency_amount`) VALUES (?,?,?)";
	}

	@Override
	public String getPlayerCurrencyAmount() {
		return "SELECT `currency_amount` FROM `HardCurrency_Players` WHERE `uuid` = ? AND `currency_id` = ?";
	}
	
	@Override
	public String setPlayerCurrency() {
		return "UPDATE `HardCurrency_Players` SET `currency_amount`= ? WHERE `uuid` = ? AND `currency_id`=?";
	}
	
}
