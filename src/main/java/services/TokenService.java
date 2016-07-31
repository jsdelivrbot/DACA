package services;

import java.security.Key;
import java.util.Date;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.tomcat.util.codec.binary.Base64;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import models.User;

@Component
public class TokenService {

	private static String secret = "Ve7GLoDiKyX3f3XV";
	private static Long expirationSeconds = 86400L; // one day
	
	@Autowired
	private UserRepository repository;
		
	private String encrypt(String text) throws Exception {
		Key aesKey = new SecretKeySpec(secret.getBytes(), "AES");
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.ENCRYPT_MODE, aesKey);
		byte[] encrypted = cipher.doFinal(text.getBytes());
		return Base64.encodeBase64String(encrypted);
	}
	
	private String decrypt(String text) throws Exception {
		byte[] decodedText = Base64.decodeBase64(text);
		Key aesKey = new SecretKeySpec(secret.getBytes(), "AES");
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.DECRYPT_MODE, aesKey);
		byte[] decrypted = cipher.doFinal(decodedText);
		return new String(decrypted);
	}
	
	public String generateToken(User user) {
		long currentTime = new Date().getTime() / 1000;
		JSONObject json = new JSONObject();
		json.put("id", user.getId());
		json.put("expires", currentTime + expirationSeconds);
		String token = null;
		try {
			token = encrypt(json.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return token;
	}
	
	public User validateToken(String token)  {
		String value = token.replace("Token ", "");
		JSONObject json;
		String id;
		long expires;
		try {
			String decrypted = decrypt(value);
			json = new JSONObject(decrypted);
			id = json.getString("id");
			expires = json.getLong("expires");
		} catch (Exception e) {
			throw new AuthException("Token malformed.");
		}
		long currentTime = new Date().getTime() / 1000;
		if (currentTime > expires) {
			throw new AuthException("Token has expired.");
		}
		return repository.findById(id);
	}
	
	public User getUser(String token) {
		try {
			return validateToken(token);
		} catch (Exception e) {
			return null;
		}
	}
	
}
