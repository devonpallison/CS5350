package src.main.java.encryptors;

/**
 * Implementation of the altered-sine logistic-based tent map image encryption algorithm.
 * This system is chaotic for r values in the range [0,4].
 * The mapping is defined by the piecewise function
 * f(x) = {
 *  4 - 0.25r * sin(pi * x) + 0.5rx        if x < 0.5
 *  (4-r)(x-x^2) + 0.5r(1-x)               otherwise
 * } where 0 <= x <= 1
 */
public class AlteredSineLogisticBasedTentMap extends EncryptionAlgorithm {
    public static final float r = 3;//r is the control parameter in the range [0,4]

    private static final AlteredSineLogisticBasedTentMap instance = new AlteredSineLogisticBasedTentMap();

    private AlteredSineLogisticBasedTentMap() {
    }

    public static AlteredSineLogisticBasedTentMap getInstance() {
        return instance;
    }

    @Override
    public double applyMap(double x) {
        if (x < 0.5) {
            return 4 - (r / 4) * Math.sin(3.14159 * x) + (r / 2 * x);
        } else {
            return (4 - r) * x * (1 - x) + ((r / 2) * (1 - x));
        }
    }
}
