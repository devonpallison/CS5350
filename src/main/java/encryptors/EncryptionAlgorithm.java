package src.main.java.encryptors;

public interface EncryptionAlgorithm {

    byte[] encrypt(final byte[] plainText);

    byte[] decrypt(final byte[] cypherText);
}
