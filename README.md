Implementare una classe `Args` che realizzi un parser.

# Operative info

## Requirements

- JDK 8
- maven

## Build

```
mvn clean package

or (to use the configured mvn):

./mvnw clean package
```

## Run tests

```
mvn test

or (to use the configured mvn):

./mvnw test
```


# Fase 1

Deve implementare i seguenti metodi:

## `Args(String format, String args)`

Parametri:

- `format`: è una stringa contenente la definizione del parser. è costituita da una serie di token nel formato `<name><type>[,<name><type>]*`.
- `<name>` è un carattere alfabetico
- `<type>` rappresenta il tipo di quel campo, valori possibili:
  - `!`: il campo `<name>` è un boolean (`true` &rarr; `true`, `false` &rarr; `false`, altri valori &rarr; `eccezione`)
  - `#`: il campo `<name>` è un integer
  - `&`: il campo `<name>` è una stringa
  - `<stringa vuota>`: il campo `<name>` è una stringa

Esempio: `a!,b#,c` &rarr; `a` è un boolean, `b` un intero, `c` una stringa

- `args`: è una stringa contenente il testo da parsare

Esempio: `-a true -b -53 -c hello`, col format `a!,b#,c`, identifica a=true, b=-53, c="hello"

Il metodo lancia `ArgsException` se si verifica un errore. Esempi di errore possono essere:

- format contiene caratteri non validi
- args contiene campi non riconosciuti
- il valore in `args` non è compatibile col tipo atteso

## `int getInt(String name)`

Restituisce il valore del campo `name` della stringa `args` passata al costruttore.

Eccezione se il campo non è un intero o non esiste nella definizione.

## `boolean getBoolean(String name)`

Restituisce il valore del campo `name` della stringa `args` passata al costruttore.

Eccezione se il campo non è un boolean o non esiste.

## `String getString(String name)`

Restituisce il valore del campo `name` della stringa `args` passata al costruttore.

Eccezione se il campo non esiste.

# Fase 2

Hai fatto un lavoro eccellente nel reinventare l'acqua calda, ora tutti vogliono usare la tua classe e ti si chiede di estenderla:

- `LocalDate getLocalDate(String name)`: restituisce un LocalDate. Il `type` corrispondente è `%`. Formato atteso del valore: `yyyy-MM-dd`
- `BigDecimal getBigDecimal(String name)`: restituisce un BigDecimal. Il `type` corrispondente è `$`. Formato atteso del valore: `123.456`

# Fase 3

- i valori di `args` possono contenere degli spazi, se la stringa inizia/finisce con doppi apici. `-c "hello world"` è una stringa valida, `-c hello world` no.
- i valori in `format` possono avere un nome più lungo di un carattere
- in `args` ogni `name` può avere più valori.
    - Dati i precedenti getter, prevedi l'equivalente che restituisca la lista di valori, ad esempio `List<String> getStrings(String name)`
    - I vecchi getter restituiscono il primo elemento della lista
    - L'ordine della lista è quello dato dall'utente

Esempio:

```
new Args("bool!,b#,text", "-bool true -bool false -b -53 -text hello -text world -text \"foo bar\"")
getString("text"); // `"hello"`
getStrings("text"); // ["hello", "world", "foo bar"]`
getBoolean("bool"); // `true`
getBooleans("bool"); // [true, false]`
```
