package eu.hardmc.skubiak.hardcurrency;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import eu.hardmc.skubiak.hardcurrency.account.Currency;
import eu.hardmc.skubiak.hardcurrency.account.CurrencyPlayerAccount;

public class Test implements Listener {
	private HardCurrency plugin;
	
	
	// !!! Sprawdzanie poprawności wpisancyh argumentów np. czy amount (np."123a") to napewno int itp.
	// !!! Permisje do komend dodać
	// !! poprawić wyświetlanie pomocy
	// !! configurowalne wiadomości
	// ! Opisanie całego kodu
	
	// Gotowe
	
	// !!! refresh currencies after rreset
	// !!!Automatyczne refreshe co 10 sek (zmienna z configu) aby synchronizować dane z bazy danych ( które mogły zostać zmodyfikowane gdzieś indziej)
	
	
	// Może w kolejnej wersji:
	
	// api
	// coś w stylu jak jest w currency aby zapisywać w liście wszystkie waluty gracza itp
	// przy usuwaniu currency refreshowanie i gdy będzie flaga --records to aby usuwało wszystkie rekordy graczy z tą walutą. (to z flagą to opcjonalne
	// Nowa tabla `HardCurrency_Recovery` po usunięciu będą tam wszystkie dane na temat usuniętych walut przez 24h po ich usunięciu
	// nowa komenda do odzyskania danych (trzeba podać nazwe jeżeli było ich więcej
	
	
	
	public Test(HardCurrency plugin) {
		this.plugin = plugin;
		
		Currency pln = plugin.getCurrencyManager().addCurrency("PLN");
		Currency usd = plugin.getCurrencyManager().addCurrency("USD");
		
		plugin.getLogger().info("pln- name: " +pln.getName() + ", id: " + pln.getId());
		plugin.getLogger().info("usd- name: " +usd.getName() + ", id: " + usd.getId());
		
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		plugin.getLogger().info("Player join event");
		Player player = e.getPlayer();
		
		Currency pln = Currency.getCurrencyFromName("PLN");
		Currency usd = Currency.getCurrencyFromName("USD");
		if(pln == null || usd == null) return;
		
		CurrencyPlayerAccount playerAccount = new CurrencyPlayerAccount(plugin, player);
		playerAccount.addPlayerCurrency(pln, 10);
		plugin.getLogger().info("1. pln - player currency: " + playerAccount.getPlayerCurrency(pln));
		playerAccount.addPlayerCurrency(pln, 5);
		plugin.getLogger().info("2. pln - player currency: " + playerAccount.getPlayerCurrency(pln));
		
		playerAccount.setPlayerCurrency(usd, 5);
		plugin.getLogger().info("3. usd - player currency: " + playerAccount.getPlayerCurrency(usd));
		playerAccount.setPlayerCurrency(usd, 50);
		plugin.getLogger().info("4. usd - player currency: " + playerAccount.getPlayerCurrency(usd));
		
	}

}
