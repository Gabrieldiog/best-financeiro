package utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class Hash {

    private static final SecureRandom random = new SecureRandom();

    public static String gerarHash(String senha) {
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        byte[] hash = aplicarSha256(senha, salt);
        return Base64.getEncoder().encodeToString(salt) + ":" + Base64.getEncoder().encodeToString(hash);
    }

    public static boolean verificar(String senha, String armazenado) {
        if (armazenado == null || !armazenado.contains(":")) {
            return false;
        }
        String[] partes = armazenado.split(":");
        byte[] salt = Base64.getDecoder().decode(partes[0]);
        byte[] hashSalvo = Base64.getDecoder().decode(partes[1]);
        byte[] hashTentativa = aplicarSha256(senha, salt);
        return MessageDigest.isEqual(hashSalvo, hashTentativa);
    }

    private static byte[] aplicarSha256(String senha, byte[] salt) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt);
            return md.digest(senha.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException | java.io.UnsupportedEncodingException e) {
            throw new RuntimeException("Erro ao gerar hash", e);
        }
    }
}
