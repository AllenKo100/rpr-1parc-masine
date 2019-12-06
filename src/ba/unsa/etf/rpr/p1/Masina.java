package ba.unsa.etf.rpr.p1;

import java.util.*;


public class Masina implements Comparable<Masina> {
    private String naziv;

    private boolean upaljen = false;

    private int sati;
    private int serijski;

    private Map<String, Integer> materijali = new HashMap<>();

    public Masina(String naziv, int serijski) {
        // naziv - minimalno 2 karaktera
        if ( naziv.length() < 2 ) {
            throw new IllegalArgumentException("Neispravan naziv masine");
        }
        // naziv - samo slova alfabeta
        for ( char c : naziv.toCharArray() ) {
            if ( !Character.isAlphabetic(c) ) {
                throw new IllegalArgumentException("Neispravan naziv masine");
            }
        }
        // serijski - pet cifara, pozitivan
        if ( 0 >= serijski || serijski >= 100000 ) {
            throw new IllegalArgumentException("Neispravan serijski broj masine");
        }


        this.serijski = serijski;
        this.naziv = naziv;
        this.sati = 8;
    }

    public String getNaziv() {
        return naziv;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Masina masina = (Masina) o;
        return serijski == masina.serijski;
    }

    @Override
    public int hashCode() {
        return Objects.hash(serijski);
    }

    void upali() throws WrongMachineState {
        if ( upaljen ) {
            throw new WrongMachineState();
        }
        upaljen = true;
    }

    void ugasi() throws WrongMachineState {
        if ( !upaljen ) {
            throw new WrongMachineState();
        }
        upaljen = false;
    }

    boolean upaljena() {
        return upaljen;
    }

    void resetuj() throws WrongMachineState {
        if ( upaljen ) {
            sati = 8;
        } else {
            throw new WrongMachineState();
        }
    }

    int cijena(String materijal) {
        if ( !materijali.containsKey(materijal) ) {
            throw new IllegalArgumentException();
        }

        return materijali.get(materijal);
    }

    int proizvedi(String materijal) {
        int cijena = cijena(materijal);
        if ( cijena > sati ) {
            throw new IllegalArgumentException();
        }
        sati -= cijena;
        return cijena;
    }

    int preostaloSati() {
        return sati;
    }

    void registrujMaterijal(String naziv, int cijena) {
        if ( cijena < 1 || cijena > 5 ) {
            throw new IllegalArgumentException("Nedozvoljena vrijednost cijene!");
        }
        materijali.putIfAbsent(naziv, cijena);
    }

    Set<String> dajMaterijaleMoguceZaProizvesti() {
        Set<String> res = new TreeSet<>();
        for ( Map.Entry<String, Integer> entry : materijali.entrySet() ) {
            if ( entry.getValue() <= preostaloSati() ) {
                res.add(entry.getKey());
            }
        }
        return res;
    }

    Map<String, Integer> dajMogucnostProizvodnje() {
        Map<String, Integer> res = new HashMap<>();

        for ( Map.Entry<String, Integer> entry : materijali.entrySet() ) {
            res.put(entry.getKey(), preostaloSati() / entry.getValue());
        }

        return res;
    }

    int getSerijski() {
        return serijski;
    }

    // u sustini samo materijal sa minimalnom cijenom.
    String dajMaterijalKojiMoguNajviseProizvesti() {
        Map.Entry<String, Integer> min = null;
        for ( Map.Entry<String, Integer> mats : materijali.entrySet() ) {
            if ( min == null ) {
                min = mats;
            } else if ( min.getValue() > mats.getValue() ) {
                min = mats;
            }
        }
        return min == null ? null : min.getKey();
    }

    @Override
    public int compareTo(Masina o) {
        // bolja je masina koja moze proizvesti vise razlicitih materijala sa preostalim satima
        Integer c1 = dajMaterijaleMoguceZaProizvesti().size();
        Integer c2 = o.dajMaterijaleMoguceZaProizvesti().size();

        Integer poredak = c1 - c2;
        if ( poredak != 0 ) {
            return poredak;
        }

        return naziv.compareTo(o.naziv);
    }

    @Override
    public String toString() {
        String status = "";
        if ( upaljena() ) {
            status = String.format("upaljena (preostalo %d sati)", preostaloSati());
        } else {
            status = "ugasena";
        }

        String materijali = "";
        boolean first = true;
        for ( Map.Entry<String, Integer> entry : this.materijali.entrySet() ) {
            if ( first ) {
                first = false;
            } else {
                materijali += ", ";
            }

            materijali += entry.getKey() + " (" + entry.getValue() + ")";
        }

        return String.format("Masina %s je %s. Ona moze proizvesti materijale %s.", naziv, status, materijali);
    }
}

