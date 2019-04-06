package Source;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.util.Base64;

public class test {
    //AES , ARCFOUR (RC4), Blowfish ,
    protected static final String AlgorithmType_UNICODE_FORMAT = "AES";
    protected static Cipher cipher;
    private static final int REPEAT = 10;
    private static int RESTART = 1000000;

    public static void main(String[] args) throws Exception{
        System.out.println("The algorithm being used is: " +AlgorithmType_UNICODE_FORMAT);
        KeyGenerator keyGenerator = KeyGenerator.getInstance(AlgorithmType_UNICODE_FORMAT);
        //keyGenerator.init(40);//Adjust key size depending on the algorithm used.
        SecretKey secretKey = keyGenerator.generateKey();
        cipher = Cipher.getInstance(AlgorithmType_UNICODE_FORMAT);

        String plainText = "I agree that the most common thing that is likely to occur is leave.";
        System.out.println("Plain Text Before Encryption: " + plainText);

        String encryptedText = encrypt(plainText, secretKey);
        System.out.println("Encrypted Text After Encryption: " + encryptedText);

        String decryptedText = decrypt(encryptedText, secretKey);
        System.out.println("Decrypted Text After Decryption: " + decryptedText + "\n");

        System.out.println("There are  = "+ stringCompare(plainText,decryptedText)+ " = Differences between strings tested" );

        //In order to calculate run time of algorithm
        long totalTime;
        for(int i = 0; i < REPEAT;i++){
            long startTime = System.currentTimeMillis();
            for (int j=0;j < RESTART;j++){
                if(stringCompare(plainText,decryptedText)== 0){
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
        //This method compares each character of the plaintext to decrypted text
        int firstStringLength = str1.length();
        int secondStringLength = str2.length();
        //To iterate through the size of the string length
        int strLength = Math.min(firstStringLength,secondStringLength);
        //Math.min returns the smaller of two int values, if argument has same value,the result is that same value.
        for (int i = 0 ; i < strLength; i++){
            int str1_char = str1.charAt(i);
            int  str2_char = str2.charAt(i);

            if (str1_char != str2_char){
                return str1_char - str2_char;
            }
        }
        if (firstStringLength != secondStringLength){//if string are not equal to each other
            return firstStringLength - secondStringLength;
        }else {
            return 0;//If they are equal returns 0 as identifier.
        }
    }
    public static String encrypt(String plainText, SecretKey secretKey) throws Exception {
        byte[] plainTextByte = plainText.getBytes();
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedByte = cipher.doFinal(plainTextByte);
        Base64.Encoder encoder = Base64.getEncoder();
        String encryptedText = encoder.encodeToString(encryptedByte);
        return encryptedText;
    }

    public static String decrypt(String encryptedText, SecretKey secretKey) throws Exception {

        Base64.Decoder decoder = Base64.getDecoder();
        byte[] encryptedTextByte = decoder.decode(encryptedText);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decryptedByte = cipher.doFinal(encryptedTextByte);
        String decryptedText = new String(decryptedByte);
        return decryptedText;
    }

}
