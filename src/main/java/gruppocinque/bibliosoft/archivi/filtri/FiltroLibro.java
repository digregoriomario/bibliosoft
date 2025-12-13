    /**
     * @file FiltroLibro.java
     * @author gruppocinque
     * @version 1.0
     */
    package gruppocinque.bibliosoft.archivi.filtri;

    import gruppocinque.bibliosoft.modelli.Libro;

    /**
     * @brief Classe di utilità per la creazione di filtri applicabili ai Libri.
     * @details Fornisce metodi statici che restituiscono istanze di InterfacciaFiltro<Libro>
     * per effettuare ricerche basate su testo generico o campi specifici come ISBN.
     * Questa classe supporta il requisito funzionale di Ricerca Libro (3.1.1.5).
     *
     * @see InterfacciaFiltro
     */
    public class FiltroLibro {

        /**
         * @brief Genera un filtro per una ricerca testuale generica.
         * @details Cerca la corrispondenza della stringa fornita all'interno del
         * titolo, dell'autore o del codice ISBN del libro. La ricerca è
         * case-insensitive.Se il testo è nullo, vuoto o composto solo da spazi, 
         * il filtro accetta tutti gli elementi.
         *
         * @param[in] testo La stringa da ricercare.
         * @return Un'istanza di InterfacciaFiltro che restituisce true se trova
         * corrispondenze, false altrimenti.
         */
        public static InterfacciaFiltro<Libro> ricerca(String testo) {
            // Gestione del caso limite: se la stringa è nulla o vuota, non filtra nulla.
            if (testo == null || testo.trim().isEmpty()) {
                return l -> true; // Filtro che accetta tutti i libri
            }

            // Parte di filtraggio
            String t = testo.toLowerCase();                       //applico il lowercase per rendere la stringa case-insensitive
            return l ->  l.getTitolo().toLowerCase().contains(t)  //controllo per titolo
                        || l.contieneAutore(testo)                //controllo per autore
                        || l.getIsbn().toLowerCase().contains(t); //controllo per ISBN

        }

        /**
         * @brief Genera un filtro per la ricerca esatta tramite ISBN.
         * @details La ricerca è case-insensitive. Se il codice è nullo, vuoto o 
         * composto solo da spazi, il filtro accetta tutti gli elementi.
         *
         * @param[in] codice Il codice ISBN da cercare.
         *
         * @return Un filtro che restituisce true se l'ISBN corrisponde.
         */
        public static InterfacciaFiltro<Libro> ricercaIsbn(String codice) {
            // Gestione del caso limite: se la stringa è nulla o vuota, non filtra nulla.
            if (codice == null || codice.trim().isEmpty()) {
                return l -> true; // Filtro che accetta tutti i libri
            }
            // Parte di filtraggio
            return l -> l.getIsbn().equalsIgnoreCase(codice);  //controllo per ISBN
        }
    }
