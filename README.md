# Progetto Biblioteca - Ingegneria del Software
Applicazione desktop in **Java** con interfaccia **JavaFX** per la gestione di una biblioteca.
## Realizzato da:
- Graziuso Franco (f.graziuso2@studenti.unisa.it)
- Di Gregorio Mario (m.digregorio22@studenti.unisa.it)
- Alfano Gabriele (g.alfano79@studenti.unisa.it)
- Ambrosio Angelo (a.ambriosio92@stundenti.unisa.it)

## Requisiti

Per eseguire il progetto sul tuo PC servono:

- **Git** (per clonare la repository)
- **JDK 17** (consigliato) oppure una versione compatibile con `release 17`
- **Maven** (da terminale: `mvn -v`)

> Nota: le dipendenze JavaFX vengono scaricate da Maven automaticamente, non serve installare JavaFX a parte.



## Download del progetto

### Opzione A — Clona con Git
```bash
git clone https://github.com/digregoriomario/bibliosoft.git
cd bibliosoft
```
### Opzione B — Scarica ZIP
1. Vai sulla pagina GitHub del progetto
2. Clicca su **Code** → **Download ZIP**
3. Estrai lo ZIP in una cartella a scelta
4. Apri un terminale nella cartella estratta (quella dove c’è `pom.xml`)

Esempio (Linux/macOS):
```bash
cd ~/Scaricati/bibliosoft-main
```



## Compilazione 
Per compilare:

```bash
mvn compile
```




## Test
Per eseguire i test automatici:

```bash
mvn test
```



## Esecuzione
Per avviare l’applicazione:

```bash
mvn javafx:run
```

Se vuoi forzare anche una build pulita prima dell’avvio:

```bash
mvn clean javafx:run
```
## Generazione documenti Doxygen
```bash
doxygen Doxyfile
```
