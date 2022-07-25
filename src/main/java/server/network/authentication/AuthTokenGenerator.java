package server.network.authentication;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Date;

public class AuthTokenGenerator {
    private SecureRandom secureRandomGenerator;
    private Base64.Encoder encoder;

    public AuthTokenGenerator() {
        secureRandomGenerator = new SecureRandom();
        encoder = Base64.getEncoder();
    }

    public String generateAuthToken() {
        byte[] currentDateBytes = getCurrentDateString();
        byte[] randomBytes = getRandomBytes();
        byte[] concatenatedBytes = concatenateByteArrays(currentDateBytes, randomBytes);
        return encoder.encodeToString(concatenatedBytes);
    }

    private byte[] concatenateByteArrays(byte[] firstByteArray, byte[] secondByteArray) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        try {
            stream.write(firstByteArray);
            stream.write(secondByteArray);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stream.toByteArray();
    }

    private byte[] getRandomBytes() {
        byte[] randomBytes = new byte[8];
        secureRandomGenerator.nextBytes(randomBytes);
        return randomBytes;
    }

    private byte[] getCurrentDateString() {
        Date currentDate = new Date();
        String currentDateString = currentDate.getTime() + "";
        return currentDateString.getBytes();
    }
}