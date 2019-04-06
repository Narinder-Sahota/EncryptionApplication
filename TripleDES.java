package Source;

import java.security.spec.KeySpec;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;//Only operate on (symmetric) Keys
import javax.crypto.spec.DESedeKeySpec;
import java.util.Base64;

public class TripleDES {
    protected static final String UNICODE_FORMAT = "UTF8";//Character encoding scheme, a=2,b=7 etc..
    protected static final String DESEDE_ENCRYPTION_SCHEME = "DESede";
    protected static final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    protected static final int randomStringSize = 256;
    private KeySpec ks;
    private SecretKeyFactory skf;
    private Cipher cipher;
    byte[] arrayBytes;
    private String myEncryptionKey;
    private String myEncryptionScheme;
    SecretKey key;

    private static final int REPEAT = 10;
    private static int RESTART = 1000000;


    public static void main(String args []) throws Exception
    {
        TripleDES td= new TripleDES();

        String target="I agree that the most common thing that is likely to occur is leave.";
        String encrypted=td.encrypt(target);
        String decrypted=td.decrypt(encrypted);

        //System.out.println("String To Encrypt: "+ target);
        //System.out.println("Encrypted String:" + encrypted);
        //System.out.println("Decrypted String:" + decrypted);
        System.out.println("There are  = "+ stringCompare(target,decrypted)+ " = Differences between strings tested" );

        long totalTime;
        for(int i = 0; i < REPEAT;i++){
            long startTime = System.currentTimeMillis();
            for (int j=0;j < RESTART;j++){
                if(stringCompare(target,decrypted)== 0){
                    //System.out.println("success");
                }else{
                    System.out.println("incorrect");
                }
            }
            long endTime = System.currentTimeMillis();
            totalTime = endTime - startTime;
            System.out.println(totalTime);
        }

    }
    public static int stringCompare(String str1, String str2) {
        int strlength1 = str1.length();
        int strlength2 = str2.length();
        int lmin = Math.min(strlength1,strlength2);
        //Math.min returns the smaller of two int values, if argument has same value,the result is that same value.
        for (int i = 0 ; i < lmin; i++){
            int str1_char = str1.charAt(i);
            int  str2_char = str2.charAt(i);

            if (str1_char != str2_char){
                return str1_char - str2_char;
            }
        }

        if (strlength1 != strlength2){//if string are not equal to each other
            return strlength1 - strlength2;
        }else {
            return 0;
        }
    }

    public TripleDES() throws Exception {
        String randomString = randomAlphaNumeric(randomStringSize);
        myEncryptionKey = randomString;//adds another encryption key specifically for 3DES
        myEncryptionScheme = DESEDE_ENCRYPTION_SCHEME;
        arrayBytes = myEncryptionKey.getBytes(UNICODE_FORMAT);
        ks = new DESedeKeySpec(arrayBytes);
        skf = SecretKeyFactory.getInstance(myEncryptionScheme);
        cipher = Cipher.getInstance(myEncryptionScheme);
        key = skf.generateSecret(ks);
    }

    public String encrypt(String unencryptedString) {
        String encryptedString = null;
        try {
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] plainText = unencryptedString.getBytes(UNICODE_FORMAT);
            byte[] encryptedText = cipher.doFinal(plainText);
            Base64.Encoder encoder = Base64.getEncoder();
            encryptedString = encoder.encodeToString(encryptedText);
            //encryptedString = new String(Base64.encodeBase64(encryptedText));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encryptedString;
    }

    public String decrypt(String encryptedString) {
        String decryptedText=null;
        try {
            cipher.init(Cipher.DECRYPT_MODE, key);
            Base64.Decoder decoder = Base64.getDecoder();
            byte[] encryptedText = decoder.decode(encryptedString);
            byte[] plainText = cipher.doFinal(encryptedText);
            decryptedText= new String(plainText);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return decryptedText;
    }

    public static String randomAlphaNumeric(int sizeOfString){//To generate random key values.
        StringBuilder builder = new StringBuilder();
        while (sizeOfString -- != 0){
            int character = (int)(Math.random()*ALPHA_NUMERIC_STRING.length());
            builder.append(ALPHA_NUMERIC_STRING.charAt(character));
        }
        return builder.toString();
    }
}
