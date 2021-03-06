package org.clothocad.phagebook.security;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import org.apache.commons.lang.RandomStringUtils;

/**
 * A utility class to hash passwords and check passwords vs hashed values. It uses a combination of hashing and unique
 * salt. The algorithm used is PBKDF2WithHmacSHA1 which, although not the best for hashing password (vs. bcrypt) is
 * still considered robust and <a href="http://security.stackexchange.com/a/6415/12614"> recommended by NIST </a>.
 * The hashed value has 256 bits.
 */
public class EmailSaltHasher {

  private static final Random RANDOM = new SecureRandom();
  private static final int ITERATIONS = 10000;
  private static final int KEY_LENGTH = 256;

  /**
   * static utility class
   */
  private static EmailSaltHasher instance = null;
  protected EmailSaltHasher() { }
  
    public static EmailSaltHasher getEmailSaltHasher(){
        if (instance == null){
            instance = new EmailSaltHasher();
        }
        return instance; 
    }

  /**
   * Returns a random salt to be used to hash a password.
   *
   * @return a 16 bytes random salt
   */
  public byte[] getNextSalt() {
    byte[] salt = new byte[16];
    RANDOM.nextBytes(salt);
    return salt;
  }

  /**
   * Returns a salted and hashed password using the provided hash.<br>
   * Note - side effect: the password is destroyed (the char[] is filled with zeros)
   *
   * @param password the password to be hashed
   * @param salt     a 16 bytes salt, ideally obtained with the getNextSalt method
   *
   * @return the hashed password with a pinch of salt
   */
  public byte[] hash(char[] password, byte[] salt) {
    PBEKeySpec spec = new PBEKeySpec(password, salt, ITERATIONS, KEY_LENGTH);
    Arrays.fill(password, Character.MIN_VALUE);
    try {
      
      SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
      return skf.generateSecret(spec).getEncoded();
      
    } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
      throw new AssertionError("Error while hashing a password: " + e.getMessage(), e);
    }
    finally {
      spec.clearPassword();
    }
    
  }
  
  
    public static String generateSaltForUserAccount() {
        return RandomStringUtils.randomAscii(30);
    }
  
    private static char[] VALID_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456879".toCharArray();

    // cs = cryptographically secure
    public static String csRandomAlphaNumericString() {
        int numChars = 25;
        SecureRandom srand = new SecureRandom();
        Random rand = new Random();
        char[] buff = new char[numChars];

        for (int i = 0; i < numChars; ++i) {
            // reseed rand once you've used up all available entropy bits
            if ((i % 10) == 0) {
                rand.setSeed(srand.nextLong()); // 64 bits of random!
            }
            buff[i] = VALID_CHARACTERS[rand.nextInt(VALID_CHARACTERS.length)];
        }
        return new String(buff);
    }


  /**
   * Returns true if the given password and salt match the hashed value, false otherwise.<br>
   * Note - side effect: the password is destroyed (the char[] is filled with zeros)
   *
   * @param password     the password to check
   * @param salt         the salt used to hash the password
   * @param expectedHash the expected hashed value of the password
   *
   * @return true if the given password and salt match the hashed value, false otherwise
   */
  public boolean isExpectedPassword(char[] password, byte[] salt, byte[] expectedHash) {
   
          byte[] pwdHash = hash(password, salt);
          Arrays.fill(password, Character.MIN_VALUE);
          if (pwdHash.length != expectedHash.length) return false;
          for (int i = 0; i < pwdHash.length; i++) {
              if (pwdHash[i] != expectedHash[i]) return false;
          }
          return true;
      

  }

  /**
   * Generates a random password of a given length, using letters and digits.
   *
   * @param length the length of the password
   *
   * @return a random password
   */
  public String generateRandomPassword(int length) {
    StringBuilder sb = new StringBuilder(length);
    for (int i = 0; i < length; i++) {
      int c = RANDOM.nextInt(62);
      if (c <= 9) {
        sb.append(String.valueOf(c));
      } else if (c < 36) {
        sb.append((char) ('a' + c - 10));
      } else {
        sb.append((char) ('A' + c - 36));
      }
    }
    return sb.toString();
  }
}