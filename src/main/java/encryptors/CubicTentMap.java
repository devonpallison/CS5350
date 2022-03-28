package src.main.java.encryptors;

import src.main.java.util.MathOperations;

/**
 * Implementation of a complex tent map image encryption
 */
public class CubicTentMap extends EncryptionAlgorithm {
    public static final float r = 3;//r is the control parameter in the range [0,4]

    private static final CubicTentMap instance = new CubicTentMap();

    private CubicTentMap() {
    }

    public static CubicTentMap getInstance() {
        return instance;
    }

    @Override
    public double applyMap(final double x) {
        if (x < 0.5) {
            return MathOperations.nonNegativeMod((4.0 - 0.75 * r) * x * (1 - x * x) + (r / 2.0) * x, 1);
        } else {
            return MathOperations.nonNegativeMod((4.0 - 0.75 * r) * x * (1 - x * x) + (r / 2.0) * (1 - x), 1);
        }
    }

    @Override
    public double initialConditionY() {
        return 0;
    }
}
