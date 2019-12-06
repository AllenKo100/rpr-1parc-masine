package ba.unsa.etf.rpr.p1;

public class DomacaMasina extends Masina {
    private boolean fastMode = false;

    public DomacaMasina(String naziv, int serijski) {
        super(naziv, serijski);
    }

    void pocniBrziRad() throws WrongMachineState {
        if ( fastMode ) {
            throw new WrongMachineState();
        }
        fastMode = true;
    }

    void zaustaviBrziRad() throws WrongMachineState {
        if ( !fastMode ) {
            throw new WrongMachineState();
        }
        fastMode = false;
    }

    @Override
    int cijena(String materijal) {
        if ( fastMode ) {
            int res = super.cijena(materijal) - 2;
            if ( res < 1 ) {
                return 1;
            } else {
                return res;
            }
        } else {
            return super.cijena(materijal);
        }
    }
}
