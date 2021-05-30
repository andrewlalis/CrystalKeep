package nl.andrewlalis.crystalkeep.model.serialization;

import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import nl.andrewlalis.crystalkeep.model.Cluster;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Optional;

public class ClusterLoader {
	public static final Path CLUSTER_PATH = Path.of("clusters");
	public static final Path DEFAULT_CLUSTER = CLUSTER_PATH.resolve("default.cts");
	private static final byte[] SALT = "zf9i78vy".getBytes(StandardCharsets.UTF_8);
	private static final byte[] IV = "Fafioje;a324fsde".getBytes(StandardCharsets.UTF_8);

	public Optional<String> promptPassword() {
		Dialog<String> d = new Dialog<>();
		d.setTitle("Enter Password");
		d.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

		PasswordField pwField = new PasswordField();
		VBox content = new VBox(10);
		content.setAlignment(Pos.CENTER);
		content.getChildren().addAll(new Label("Enter password"), pwField);
		d.getDialogPane().setContent(content);
		d.setResultConverter(param -> {
			if (param == ButtonType.OK) {
				return pwField.getText();
			}
			return null;
		});
		return d.showAndWait();
	}

	public Cluster load(Path path, String password) throws Exception {
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		cipher.init(Cipher.DECRYPT_MODE, this.getSecretKey(password), new IvParameterSpec(IV));
		byte[] raw = Files.readAllBytes(path);
		return ClusterSerializer.readCluster(new ByteArrayInputStream(cipher.doFinal(raw)));
	}

	public void save(Cluster cluster, Path path, String password) throws IOException, GeneralSecurityException {
		Files.createDirectories(CLUSTER_PATH);
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ClusterSerializer.writeCluster(cluster, bos);
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, this.getSecretKey(password), new IvParameterSpec(IV));
		Files.write(path, cipher.doFinal(bos.toByteArray()));
		bos.close();
	}

	private SecretKey getSecretKey(String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
		SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
		KeySpec spec = new PBEKeySpec(password.toCharArray(), SALT, 65536, 256);
		return new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
	}
}
