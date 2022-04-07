package src.main.java.encryptors;

/**
 * Tent map implementation; this system is chaotic for r values close 2.
 * Map is the piecewise function
 * f(x) = {
 *  rx            if x < 0.5
 *  r(1-x)        otherwise
 * } where 0 <= x <= 1
 */
public class TentMap extends EncryptionAlgorithm {
    private final double r = 1.99999d;

    private static final TentMap instance = new TentMap();

    private TentMap() {
    }

    public static TentMap getInstance() {
        return instance;
    }

    @Override
    public double applyMap(double x) {
        if(x < 0.5) {
            return r * x;
        } else {
            return r * (1 - x);
        }
    }

    @Override
    public String getDescription() {
        return "TM";
    }
}
