# HardCurrency
Simple Minecraft Plugin to manage unlimited number of currencies
<br><br>

## Compatibility
#### Server
- Tested Versions: 1.8.x, 1.20.1
- Spigot 1.8-1.20.1
- Paper 1.8-1.20.1
#### Proxy
- Velocity-3.3.0-SNAPSHOT

## Features

- [Commands](#commands)
- [Permissions](#permissions)
- [Multiple Currencies](#currencies)
- [MySQL and SQLite Support](#storage)
- [Configurable messages](#messages)
- [PlaceholderAPI](#placeholderapi)
- [Simple API](#api)
<br><br>

## Commands
```
- /hardcurrency help - Shows help page
- /hardcurrency show - Displays all your currencies
- /hardcurrency show <Currency> - Displays specific currency
- /hardcurrency show <Currency> <Player> - Displays specific currency of specific player
- /hardcurrency currency <add/remove> <Currency> - Add or Remove currency
- /hardcurrency currency list - Lists all currencies
- /hardcurrency player <add/set/remove> <Currency> <Player> <Amount> - Manages player Currency values
```
<br>

## Permissions
```
- /hardcurrency help - hardcurrency.help
- /hardcurrency show - hardcurrency.show.<Currency>
- /hardcurrency show <Currency> - hardcurrency.show.<Currency>
- /hardcurrency show <Currency> <Player> - hardcurrency.show.player
- /hardcurrency currency add <Currency> - hardcurrency.currency.add
- /hardcurrency currency remove <Currency> - hardcurrency.currency.remove
- /hardcurrency currency list - hardcurrency.currency.list
- /hardcurrency player add <Currency> <Player> <Amount> - hardcurrency.player.add
- /hardcurrency player set <Currency> <Player> <Amount> - hardcurrency.player.set
- /hardcurrency player remove <Currency> <Player> <Amount>- hardcurrency.player.remove
```
<br>

## Currencies
Plugin can handle more than one currency and player can use more than one currency in one moment

Limiting the length of displayed currencies after the decimal point:
```yaml
currencies:
   # How many decimal places should be visible?
   # -1 to disable
   maxPlacesAfterDot: 2
```

<br>

## Storage
MySQL or SQLite Support

#### Storage Config:
```yaml
database:
  # Type of database SQLite or MySQL
  type: SQLite

  # MySQL settings only used when type is MySQL
  mysql:
    # MySQL database host
    host: localhost
    # MySQL database port (default 3306)
    port: 3306 
    # MySQL database database where create tables
    database: database
    # MySQL database username to log in
    username: root
    # MySQL database password to log in
    password: ''
    # MySQL connection settings
    connSettings: characterEncoding=UTF-8&useSSL=false
 
  # SQLite settings only used when type is SQLite
  sqlite:
    # Path to SQLite database file
    path: database.db
```

#### Currencies Auto Refresh
```yaml
currencies:
   # how often refresh currencies in seconds (player currencies are always up to date)
   # type 0 to disable! Only positive numbers
   autoRefresh: 60
```

<br>

## Messages
```yaml
messages:
    # Placeholders: %PREFIX% - prefix to use everywhere
    prefix: "&cHard&6Currencies &8>"
    onlyPlayer: "%PREFIX% &cOnly player can use that command!"
    noPermission: "%PREFIX% &cYou do not have permission for this command!"
    wrongArg: "%PREFIX% &cThere is no such argument! Use &7/hc help for help."
    currencyNotExists: "%PREFIX% &cCurrency %CURRENCY% does not exist!"
    noCurrenciesFound: '%PREFIX% &cNo displayable currencies found!'
    notFloat: '%PREFIX% &cThe value "%VALUE%" is not a number!'
    commands:
      # Help
      # Prefixe like [Help] for messages indicates permissions
      # Placeholders: %cmd% - command like /hardcurreny, /hc
      help:
      - '&8---------------%PREFIX%&8----------------'
      - ''
      - '[HELP]&7/%cmd% help &8- Shows This Page'
      - '[SHOW]&7/%cmd% show &8- Displays all your currencies'
      - '[SHOW]&7/%cmd% show <Currency> &8- Displays specific currency'
      - '[SHOW_PLAYER]&7/%cmd% show <Currency> <Player> &8- Displays specific currency of specific player'
      - '[CURRENCY_ADD]&7/%cmd% currrency add <Name> &8- Adds Currency'
      - '[CURRENCY_REMOVE]&7/%cmd% currency remove <Currency> &8- Remove specific Currency'
      - '[CURRENCY_LIST]&7/%cmd% currency list &8- Displays all currencies'
      - '[PLAYER_ADD]&7/%cmd% player add <Currency> <Player <Amount> &8- Adds Points to specific currency'
      - '[PLAYER_REMOVE]&7/%cmd% player remove <Currency> <Player <Amount> &8- Remove Points to specific currency'
      - '[PLAYER_SET]&7/%cmd% player set <Currency> <Player <Amount> &8- Set Points to specific currency'
      - ''
      - '&8---------------%PREFIX%&8----------------'
    
      # Show
      # Placeholders:
      # - %CURRENCY% - currency name
      # - %AMOUNT% - amount of selected currency
      # - %PLAYER% - 
    
      # for show special [CURRENCY_REPEAT] - message to repeat for all showed currencies
      show:
      - '%PREFIX% &8- &7Your Currencies'
      - '[CURRENCY_REPEAT]&6%CURRENCY% &7- &a%AMOUNT%'
      showCurrency:
      - '%PREFIX% &8- &7Your Currency'
      - '&6%CURRENCY% &7- &a%AMOUNT%'
      showPlayer:
      - '%PREFIX% &8- &7Currency of Player &8%PLAYER%&7:'
      - '&6%CURRENCY% &7- &a%AMOUNT%'
      wrongShowCmd: '%PREFIX% &cWrong arguments. Usage: /hc show [Currency] [Player]'
    
      # Currencies
      # Placeholders: %CURRENCY% - used currency
      currencyAdd: '%PREFIX% &aCurrency &2%CURRENCY% &ahas been added'
      currencyRemoved: '%PREFIX% &aCurrency &2%CURRENCY% &ahas been removed'
      currencyList:
      - '%PREFIX% &7All Currencies'
      - '[CURRENCY_REPEAT]&6%CURRENCY%'
      wrongCurrencyCmd: '%PREFIX% &cWrong arguments. Usage: /hc currency <add/remove/list> [name]'
    
      # Player
      playerAdd: "%PREFIX% &aAdded %ADDED_POINTS% points to &2%PLAYER%'s &a%CURRENCY% currency, now this currency has %ALL_POINTS% points"
      playerRemove: "%PREFIX% &aRemoved %REMOVED_POINTS% points from &2%PLAYER%'s &a%CURRENCY% currency, now this currency has %ALL_POINTS% points"
      playerSet: "%PREFIX% &2%PLAYER%'s &aplayer points in %CURRENCY% currency have been set to %SETTED_POINTS% points"
      wrongPlayerCmd: '%PREFIX% &cWrong arguments. Usage: /hc player <add/remove/set> <Currency> <Player> <Amount>'
```
<br>


## PlaceholderAPI
Placeholders to use in other plugins using PlaceholderAPI

#### Placeholders
- `%hardcurrency_<Currency>%` - Displays the value of a given player currency

<br>

## API

### API Supports:
- [Adding/Removing currencies](#adding-currencies)
- [Getting Currencies](#getting-currencies)
- [Adding/Removing/Setting Player Currency](#addremoveset-player-currency)
- [Getting Player Currency Value](#get-player-currency-value)
- [Refreshing Currencies](#refresh-currencies)
- [Getting All Currencies](#get-all-currencies)


### Adding Currencies
```java
HardCurrencyAPI.addCurrency("CurrencyName");
```

### Removing Currencies
```java
// Removing using Currency Id
HardCurrencyAPI.removeCurrency(0); // int

// Removing using Currency Name
HardCurrencyAPI.removeCurrency("Currency Name"); // String

// Removing using Currency
Currency currency = Currency.getCurrencyFromName("Curreny Name");
HardCurrencyAPI.removeCurrency(currency);
```

### Getting Currencies
```java
// Get Currency From Id
Currency currency = Currency.getCurrencyFromName(0); // int

// Get Currency From Name
Currency currency = Currency.getCurrencyFromName("Curreny Name"); // String
```

### Add/Remove/Set Player Currency
```java
// Get Currency
Currency currency = Currency.getCurrencyFromName("Curreny Name");

// Get Player Account
CurrencyPlayerAccount playerAccount = new CurrencyPlayerAccount(HardCurrencyAPI.plugin, player);


// Add Player Currency
playerAccount.addPlayerCurrency(currency, 15);

// Remove Player Currency
playerAccount.removePlayerCurrency(currency, 15);

// Set Player Currency
playerAccount.setPlayerCurrency(currency, 15);
```

### Get Player Currency Value
```java
// Get Currency
Currency currency = Currency.getCurrencyFromName("Curreny Name");

// Get Player Account
CurrencyPlayerAccount playerAccount = new CurrencyPlayerAccount(HardCurrencyAPI.plugin, player);


// Get Player Currency
float value = playerAccount.getPlayerCurrency(currency);
```

### Refresh Currencies
```java
// Updates All currencies from storage
HardCurrencyAPI.refreshCurrencies();
```

### Get All Currencies
```java
List<String> currencies = HardCurrencyAPI.getAllCurrencies();
```







