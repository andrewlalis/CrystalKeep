package nl.andrewlalis.crystalkeep.io;

import nl.andrewlalis.crystalkeep.io.serialization.ByteUtils;
import nl.andrewlalis.crystalkeep.io.serialization.ClusterSerializer;
import nl.andrewlalis.crystalkeep.model.Cluster;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

/**
 * This class is responsible for reading and writing clusters to files, which
 * may be encrypted with a password-based AES-256 CBC.
 *
 * <p>
 *     All saved files start with a single byte with value 1 to indicate that
 *     the file is encrypted, or 0 if it is not encrypted. If encrypted, the
 *     next 8 bytes will contain the salt that was used to encrypt the file.
 *     Encrypted files will then contain a 16 byte initialization vector.
 *     The remaining part of the file is simply the contents.
 * </p>
 *
 * @see nl.andrewlalis.crystalkeep.io.serialization.ClusterSerializer
 */
public class ClusterIO {
	public static final Path CLUSTER_PATH = Path.of("clusters");
	public static final int FILE_VERSION = 1;

	private final SecureRandom random;

	public ClusterIO() {
		try {
			this.random = SecureRandom.getInstanceStrong();
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("Could not initialize secure random.", e);
		}
	}

	/**
	 * Loads an encrypted cluster from the given path, using the given password
	 * as the secret key value.
	 * @param path The path of the file to load from.
	 * @param password The password to use to decrypt the contents.
	 * @return The cluster that was loaded.
	 * @throws Exception If the file could not be found, or could not be
	 * decrypted and read properly.
	 */
	public Cluster load(Path path, char[] password) throws Exception {
		try (var is = Files.newInputStream(path)) {
			int version = ByteUtils.readInt(is);
			if (version < FILE_VERSION) {
				System.err.println("Warning! Reading older file version: " + version);
			}
			int encryptionFlag = is.read();
			if (encryptionFlag == 0) throw new IOException("File is not encrypted.");
			byte[] salt = is.readNBytes(8);
			byte[] iv = is.readNBytes(16);
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, this.getSecretKey(password, salt), new IvParameterSpec(iv));
			try (CipherInputStream cis = new CipherInputStream(is, cipher)) {
				return ClusterSerializer.readCluster(cis);
			}
		}
	}

	/**
	 * Saves a cluster to an encrypted file, using the given password as the
	 * secret key.
	 * @param cluster The cluster to save.
	 * @param path The path to the file to save the cluster at.
	 * @param password The password to use when saving.
	 * @throws IOException If an error occurs when writing the file.
	 * @throws GeneralSecurityException If we could not obtain a secret key.
	 */
	public void save(Cluster cluster, Path path, char[] password) throws IOException, GeneralSecurityException {
		Files.createDirectories(path.getParent());
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		byte[] iv = new byte[16];
		this.random.nextBytes(iv);
		byte[] salt = new byte[8];
		this.random.nextBytes(salt);
		cipher.init(Cipher.ENCRYPT_MODE, this.getSecretKey(password, salt), new IvParameterSpec(iv));
		try (OutputStream fos = Files.newOutputStream(path)) {
			ByteUtils.writeInt(fos, FILE_VERSION);
			fos.write(1);
			fos.write(salt);
			fos.write(iv);
			try (CipherOutputStream cos = new CipherOutputStream(fos, cipher)) {
				ClusterSerializer.writeCluster(cluster, cos);
			}
		}
	}

	/**
	 * Saves an unencrypted cluster file.
	 * @param cluster The cluster to save.
	 * @param path The file to save the cluster to.
	 * @throws IOException If the file could not be saved.
	 */
	public void saveUnencrypted(Cluster cluster, Path path) throws IOException {
		Files.createDirectories(path.getParent());
		try (var os = Files.newOutputStream(path)) {
			ByteUtils.writeInt(os, FILE_VERSION);
			os.write(0);
			ClusterSerializer.writeCluster(cluster, os);
		}
	}

	/**
	 * Loads an unencrypted cluster from a file.
	 * @param path The path to load the cluster from.
	 * @return The cluster that was loaded.
	 * @throws IOException If the file could not be loaded.
	 */
	public Cluster loadUnencrypted(Path path) throws IOException {
		try (var is = Files.newInputStream(path)) {
			int version = ByteUtils.readInt(is);
			if (version < FILE_VERSION) {
				System.err.println("Warning! Reading older file version: " + version);
			}
			int encryptionFlag = is.read();
			if (encryptionFlag == 1) throw new IOException("File is encrypted.");
			return ClusterSerializer.readCluster(is);
		}
	}

	private SecretKey getSecretKey(char[] password, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
		SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
		KeySpec spec = new PBEKeySpec(password, salt, 65536, 256);
		return new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
	}
}
