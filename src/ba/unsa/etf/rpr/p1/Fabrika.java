package ba.unsa.etf.rpr.p1;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Function;

public class Fabrika {
    private Set<Masina> masine = new TreeSet<>();

    Masina dodajDomacuMasinu(String naziv, int serijski) {
        Masina m = new DomacaMasina(naziv, serijski);

        masine.remove(m);
        masine.add(m);

        return m;
    }

    Masina dodajKupljenuMasinu(String naziv, int serijski) {
        Masina m = new Masina(naziv, serijski);

        masine.remove(m);
        masine.add(m);

        return m;
    }

    void dodajMaterijal(String nazivMasine, String nazivMaterijala, int cijena) {
        for ( Masina m : masine ) {
            if ( m.getNaziv().equals(nazivMasine) ) {
                m.registrujMaterijal(nazivMaterijala, cijena);
            }
        }
    }

    Map<Masina, String> najviseProizvoda() {
        Map<Masina, String> res = new HashMap<>();
        for ( Masina m : masine ) {
            if ( !m.upaljena() ) {
                continue;
            }
            String curr = m.dajMaterijalKojiMoguNajviseProizvesti();
            if ( curr != null ) {
                res.put(m, curr);
            }
        }
        return res;
    }

    Map<Masina, Integer> cijenaZaMaterijal(String naziv) {
        Map<Masina, Integer> res = new HashMap<>();

        for ( Masina m : masine ) {
            if ( !m.upaljena() ) {
                continue;
            }

            int cijena = -1;
            try {
                cijena = m.cijena(naziv);
            } catch ( IllegalArgumentException ex ) {}
            res.put(m, cijena);
        }
        return res;
    }

    Set<Masina> dajMasine(Function<Masina, Boolean> filter) {
        // Posto je uslov redoslijeda dinamican (broj sati se moze uvijek promjeniti, kao i materijali)
        // set set treba svaki put resortirati, a to dobivamo najlakse pomocu novog seta
        Set<Masina> set = new TreeSet<>();
        for ( Masina m : masine ) {
            if ( filter == null || filter.apply(m) ) {
                set.add(m);
            }
        }
        return set;
    }

    @Override
    public String toString() {
        String res = "";
        int i = 1;
        for ( Masina m : masine ) {
            res += i + ". " + m.toString() + "\n";
            ++ i;
        }
        return res;
    }
}
