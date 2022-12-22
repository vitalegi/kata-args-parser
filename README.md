Implementare una classe `Args` che realizzi un parser.

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

- `LocalDate getDate(String name)`: restituisce un LocalDate. Il `type` corrispondente è `%`. Formato atteso del valore: `yyyy-MM-dd`
- `BigDecimal getBigDecimal(String name)`: restituisce un BigDecimal. Il `type` corrispondente è `$`. Formato atteso del valore: `123.456`

# Fase 3

TBD
