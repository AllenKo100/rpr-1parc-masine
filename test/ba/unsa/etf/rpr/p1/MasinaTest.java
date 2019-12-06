package ba.unsa.etf.rpr.p1;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class MasinaTest {
    Fabrika fabrika;

    Masina kupljena;
    Masina domaca;

    @BeforeEach
    void setUp() {
        fabrika = new Fabrika();

        kupljena = fabrika.dodajKupljenuMasinu("kupljena", 521);
        domaca = fabrika.dodajDomacuMasinu("domaca", 1251);

        fabrika.dodajMaterijal("kupljena", "m1", 1);
        fabrika.dodajMaterijal("kupljena", "m5", 5);

        fabrika.dodajMaterijal("domaca", "m1", 1);
        fabrika.dodajMaterijal("domaca", "m3", 3);
        fabrika.dodajMaterijal("domaca", "m5", 5);
    }

    void upaliSve() {
        assertDoesNotThrow(() -> {
            kupljena.upali();
            domaca.upali();
        });
    }

    @Test
    void testValidacijaNazivaAlfabet() {
        assertThrows(IllegalArgumentException.class, () -> fabrika.dodajDomacuMasinu("123", 1));
    }

    @Test
    void testValidacijaNazivaDuzina() {
        assertThrows(IllegalArgumentException.class, () -> fabrika.dodajDomacuMasinu("a", 1));
    }


    @Test
    void testValidacijaNazivaSerijskiDonjaGranica() {
        assertThrows(IllegalArgumentException.class, () -> fabrika.dodajDomacuMasinu("aa", 0));
    }

    @Test
    void testValidacijaNazivaSerijskiGornjaGranica() {
        assertThrows(IllegalArgumentException.class, () -> fabrika.dodajDomacuMasinu("ba", 100000));
    }

    @Test
    void upali() {
        assertDoesNotThrow(kupljena::upali);
    }

    @Test
    void upaliBacaIzuzetak() {
        assertDoesNotThrow(kupljena::upali);
        assertThrows(WrongMachineState.class, kupljena::upali);
    }

    @Test
    void ugasi() {
        assertDoesNotThrow(() -> {
            kupljena.upali();
            kupljena.ugasi();
        });
    }

    @Test
    void ugasiBacaIzuzetak2() {
        assertThrows(WrongMachineState.class, kupljena::ugasi);
    }

    @Test
    void ugasiBacaIzuzetak() {
        assertDoesNotThrow(() -> {
            kupljena.upali();
            kupljena.ugasi();
        });

        assertThrows(WrongMachineState.class, kupljena::ugasi);
    }

    @Test
    void resetuj() {
        assertDoesNotThrow(kupljena::upali);
        assertDoesNotThrow(kupljena::resetuj);
        assertDoesNotThrow(kupljena::ugasi);
    }


    @Test
    void resetuj2() {
        assertThrows(WrongMachineState.class, kupljena::resetuj);
    }


    @Test
    void cijena() {
        upaliSve();

        Map<Masina, Integer> map = fabrika.cijenaZaMaterijal("m1");

        assertEquals(2, map.size());

        assertAll(map.entrySet().stream().map(entry -> () -> {
            assertNotNull(entry.getKey());

            assertEquals(Integer.valueOf(1), entry.getValue());
        }));
    }

    @Test
    void proizvedi() {
        upaliSve();

        assertEquals(8, domaca.preostaloSati());

        domaca.proizvedi("m1");

        assertEquals(7, domaca.preostaloSati());

        domaca.proizvedi("m5");

        assertEquals(2, domaca.preostaloSati());
    }

    @Test
    void masinaEquals() {
        assertNotEquals(kupljena, domaca);
    }

    @Test
    void masinaEqualsAlias() {
        assertEquals(kupljena, kupljena);
    }

    @Test
    void masinaEquals2() {
        Masina other = fabrika.dodajDomacuMasinu("domaca", 1251);
        assertEquals(other, domaca);
    }

    @Test
    void proizvedi2() {
        upaliSve();

        assertEquals(8, domaca.preostaloSati());

        domaca.proizvedi("m5");

        assertEquals(3, domaca.preostaloSati());

        assertThrows(IllegalArgumentException.class, () -> domaca.proizvedi("m5"));
    }



    @Test
    void preostaloSati() {
        upaliSve();

        assertEquals(8, domaca.preostaloSati());
    }

    @Test
    void registrujMaterijal() {
        upaliSve();

        assertDoesNotThrow(() -> {
            fabrika.dodajMaterijal("domaca", "mt", 2);
        });
    }


    @Test
    void registrujMaterijal2() {
        upaliSve();
        assertDoesNotThrow(kupljena::ugasi);

        fabrika.dodajMaterijal("domaca", "mt", 4);
        fabrika.dodajMaterijal("domaca", "mt", 2);

        Map<Masina, Integer> map = fabrika.cijenaZaMaterijal("mt");

        assertEquals(1, map.size());

        Integer cijena = map.values().iterator().next();

        assertEquals(cijena, Integer.valueOf(4));
    }


    @Test
    void registrujMaterijal3() {
        upaliSve();

        assertThrows(IllegalArgumentException.class, () -> {
            fabrika.dodajMaterijal("domaca", "mt", 0);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            fabrika.dodajMaterijal("domaca", "mt", 6);
        });
    }

    @Test
    void dajMaterijaleMoguceZaProizvesti() {
        upaliSve();

        Set<String> set = domaca.dajMaterijaleMoguceZaProizvesti();

        assertEquals(3, set.size());
    }


    @Test
    void dajMaterijaleMoguceZaProizvesti2() {
        upaliSve();

        Set<String> set = domaca.dajMaterijaleMoguceZaProizvesti();

        assertEquals(3, set.size());

        domaca.proizvedi("m5");

        set = domaca.dajMaterijaleMoguceZaProizvesti();

        assertEquals(2, set.size());
    }

    @Test
    void dajMogucnostProizvodnje() {
        upaliSve();

        Map<String, Integer> map = domaca.dajMogucnostProizvodnje();

        assertEquals(3, map.size());

        assertAll(map.entrySet().stream().map(entry -> () -> {
            switch (entry.getKey()) {
                case "m1":
                    assertEquals(Integer.valueOf(8), entry.getValue());
                    break;
                case "m3":
                    assertEquals(Integer.valueOf(2), entry.getValue());
                    break;
                case "m5":
                    assertEquals(Integer.valueOf(1), entry.getValue());
                    break;
                default:
                    fail();
                    break;
            }
        }));
    }
}