package src.main.java.encryptors;

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
        int ret;
        if (x < 0.5) {
            ret = (int) (4 - r / 4 * Math.sin(3.14159 * x) + r / 2 * x);
        } else {
            ret = (int) ((4 - r) * x * (1 - x) + r / 2 * (1 - x));
        }
        return ret;
    }

    @Override
    public double initialConditionY() {
        return 0;
    }
}
