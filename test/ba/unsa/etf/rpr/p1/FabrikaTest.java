package ba.unsa.etf.rpr.p1;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class FabrikaTest {
    Fabrika fabrika;

    Masina kupljena;
    Masina domaca;

    @BeforeEach
    void setUp() {
        fabrika = new Fabrika();

        kupljena = fabrika.dodajKupljenuMasinu("kupljena", 521);
        domaca = fabrika.dodajDomacuMasinu("domaca", 1251);

        fabrika.dodajMaterijal("kupljena", "m5", 5);
        fabrika.dodajMaterijal("kupljena", "m1", 1);

        fabrika.dodajMaterijal("domaca", "m1", 1);
        fabrika.dodajMaterijal("domaca", "m5", 5);
        fabrika.dodajMaterijal("domaca", "m3", 3);

        assertDoesNotThrow(kupljena::upali);
        assertDoesNotThrow(domaca::upali);
    }

    @org.junit.jupiter.api.AfterEach
    void tearDown() {
    }

    @org.junit.jupiter.api.Test
    void najviseProizvoda() {
        Map<Masina, String> map = fabrika.najviseProizvoda();

        assertEquals(2, map.size());

        assertAll(map.entrySet().stream().map(entry -> () -> {
            assertEquals("m1", entry.getValue());
        }));
    }

    @org.junit.jupiter.api.Test
    void najviseProizvoda2() {
        assertDoesNotThrow(domaca::ugasi);

        Map<Masina, String> map = fabrika.najviseProizvoda();

        assertEquals(1, map.size());

        assertAll(map.entrySet().stream().map(entry -> () -> {
            assertEquals("m1", entry.getValue());
        }));
    }


    @org.junit.jupiter.api.Test
    void najviseProizvoda3() {
        fabrika.dodajMaterijal("domaca", "m0", 1);
        fabrika.dodajMaterijal("kupljena", "m0", 1);

        Map<Masina, String> map = fabrika.najviseProizvoda();

        assertEquals(2, map.size());

        assertAll(map.entrySet().stream().map(entry -> () -> {
            assertEquals("m0", entry.getValue());
        }));
    }


    @org.junit.jupiter.api.Test
    void dodajDomacuMasinu() {
        Masina t = fabrika.dodajDomacuMasinu("domaca", 111);
        assertEquals(111, t.getSerijski());
    }


    @org.junit.jupiter.api.Test
    void dodajDomacuMasinu2() {
        // metode ne treba da pali masinu

        Masina t = fabrika.dodajDomacuMasinu("domaca", 111);
        assertEquals(111, t.getSerijski());
        assertDoesNotThrow(t::upali);
    }

    @Test
    void dajMasine() {
        Set<Masina> masine = fabrika.dajMasine(null);

        assertEquals(2, masine.size());


        Iterator<Masina> iterator = masine.iterator();
        Masina prva = iterator.next();
        Masina druga = iterator.next();

        assertSame(prva, kupljena);
        assertSame(druga, domaca);
    }


    @Test
    void dajMasine2() {
        // potrosi sve sate
        for ( int i = 0; i < 8; ++ i ) {
            kupljena.proizvedi("m1");
        }

        Set<Masina> masine = fabrika.dajMasine(null);

        assertEquals(2, masine.size());

        Iterator<Masina> iterator = masine.iterator();
        Masina prva = iterator.next();
        Masina druga = iterator.next();

        assertSame(prva, kupljena);
        assertSame(druga, domaca);
    }


    @Test
    void dajMasine3() {
        Set<Masina> masine = fabrika.dajMasine(m -> false);

        assertEquals(0, masine.size());
    }

    @Test
    void dajMasine4() {
        Set<Masina> masine = fabrika.dajMasine(m -> true);

        assertEquals(2, masine.size());
    }


    @Test
    void dajMasine5() {

        Set<Masina> masine = fabrika.dajMasine(m -> m.getNaziv().equals("domaca"));

        assertEquals(1, masine.size());
    }


    @org.junit.jupiter.api.Test
    void ispisi() {
        String s = fabrika.toString();

        String expected =
                "1. Masina domaca je upaljena (preostalo 8 sati). Ona moze proizvesti materijale m1 (1), m3 (3), m5 (5).\n" +
                "2. Masina kupljena je upaljena (preostalo 8 sati). Ona moze proizvesti materijale m1 (1), m5 (5).\n";

        String expectedAlt =
                "1. Masina kupljena je upaljena (preostalo 8 sati). Ona moze proizvesti materijale m1 (1), m5 (5).\n" +
                "2. Masina domaca je upaljena (preostalo 8 sati). Ona moze proizvesti materijale m1 (1), m3 (3), m5 (5).\n";

        assertTrue(expected.equals(s) || expectedAlt.equals(s));
    }


    @org.junit.jupiter.api.Test
    void ispisi2() {
        assertDoesNotThrow(domaca::ugasi);

        String s = fabrika.toString();

        String expected =
                "1. Masina domaca je ugasena. Ona moze proizvesti materijale m1 (1), m3 (3), m5 (5).\n" +
                "2. Masina kupljena je upaljena (preostalo 8 sati). Ona moze proizvesti materijale m1 (1), m5 (5).\n";

        String expectedAlt =
                "1. Masina kupljena je upaljena (preostalo 8 sati). Ona moze proizvesti materijale m1 (1), m5 (5).\n" +
                "2. Masina domaca je ugasena. Ona moze proizvesti materijale m1 (1), m3 (3), m5 (5).\n";


        assertTrue(expected.equals(s) || expectedAlt.equals(s));
    }

    @Test
    void testIzbacivanjePrethodneMasine() {
        Masina novaDomaca = fabrika.dodajDomacuMasinu("domaca", 1251);

        assertNotSame(domaca, novaDomaca);
    }


    @org.junit.jupiter.api.Test
    void ispisi3() {
        assertDoesNotThrow(domaca::ugasi);
        assertDoesNotThrow(() -> kupljena.proizvedi("m5"));

        String s = fabrika.toString();

        String expected =
                "1. Masina domaca je ugasena. Ona moze proizvesti materijale m1 (1), m3 (3), m5 (5).\n" +
                        "2. Masina kupljena je upaljena (preostalo 3 sati). Ona moze proizvesti materijale m1 (1), m5 (5).\n";

        String expectedAlt =
                "1. Masina kupljena je upaljena (preostalo 3 sati). Ona moze proizvesti materijale m1 (1), m5 (5).\n" +
                        "2. Masina domaca je ugasena. Ona moze proizvesti materijale m1 (1), m3 (3), m5 (5).\n";


        assertTrue(expected.equals(s) || expectedAlt.equals(s));

    }


    @org.junit.jupiter.api.Test
    void cijenaZaMaterijal() {
        Map<Masina, Integer> map = fabrika.cijenaZaMaterijal("m1");

        assertEquals(2, map.size());
        assertAll(map.entrySet().stream().map(entry -> () -> {
            assertEquals(Integer.valueOf(1), entry.getValue());
        }));
    }


    @org.junit.jupiter.api.Test
    void cijenaZaMaterijal2() {
        Map<Masina, Integer> map = fabrika.cijenaZaMaterijal("m3");

        assertEquals(2, map.size());
        assertAll(map.entrySet().stream().map(entry -> () -> {
            if ( entry.getKey() == domaca ) {
                assertEquals(Integer.valueOf(3), entry.getValue());
            } else {
                assertEquals(Integer.valueOf(-1), entry.getValue());
            }
        }));
    }


    @Test
    void optimalnaMasinaZaMaterijal() {
        assertDoesNotThrow(domaca::ugasi);

        Map<Masina, Integer> map = fabrika.cijenaZaMaterijal("m5");

        assertEquals(1, map.size());
        assertAll(map.entrySet().stream().map(entry -> () -> {
            assertEquals(Integer.valueOf(5), entry.getValue());
        }));
    }
}