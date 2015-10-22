package com.example.kevin.tempappp;

import android.content.Context;
import android.util.Base64;
import android.util.Log;

import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;

import javax.crypto.Cipher;

/**
 * Created by Steve on 2015-10-16.
 */

public class Encryption {

    // Whoami
    static final String TAG = "EncryptionAlgorithms";

    // saving the context for later use
    private final Context mContext;

    // Generate key pair for 1024-bit RSA encryption and decryption
    public Key publicKey = null;
    public Key privateKey = null;

    // Encrypted Data
    byte[] encodedBytes;

    // Decrypted Data
    byte[] decodedBytes;

    public Encryption(Context context) {
        this.mContext = context;
    }

    public void GenerateKey(){

        try {
            KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
            kpg.initialize(1024);
            KeyPair kp = kpg.genKeyPair();
            publicKey = kp.getPublic();
            privateKey = kp.getPrivate();
        }
        catch (Exception e) {
            Log.e(TAG, "RSA key pair error");
        }
    }

    public String Encrypt(String input){
        // Encode the original data with RSA private key
        encodedBytes = null;
        try {
            Cipher c = Cipher.getInstance("RSA");
            c.init(Cipher.ENCRYPT_MODE, privateKey);
            encodedBytes = c.doFinal(input.getBytes());
        }
        catch (Exception e) {
            Log.e(TAG, "RSA encryption error");
        }

        return Base64.encodeToString(encodedBytes, Base64.DEFAULT);
    }

    public String EncodedMessage() {
        if (encodedBytes != null) {
            return Base64.encodeToString(encodedBytes, Base64.DEFAULT);
        }
        return null;
    }

    public String Decrypt() {
        if(encodedBytes == null)
            return null;

        // Decode the encoded data with RSA public key
        decodedBytes = null;

        try {
            Cipher c = Cipher.getInstance("RSA");
            c.init(Cipher.DECRYPT_MODE, publicKey);
            decodedBytes = c.doFinal(encodedBytes);
        } catch (Exception e) {
            Log.e(TAG, "RSA decryption error");
        }
        return new String(decodedBytes);
    }

    public String Decrypt(String encrypted_message) {
        // Decode the encoded data with RSA public key
        decodedBytes = null;

        encodedBytes = Base64.decode(encrypted_message, Base64.DEFAULT);

        try {
            Cipher c = Cipher.getInstance("RSA");
            c.init(Cipher.DECRYPT_MODE, publicKey);
            decodedBytes = c.doFinal(encodedBytes);
        } catch (Exception e) {
            Log.e(TAG, "RSA decryption error");
        }
        return new String(decodedBytes);
    }

    public String DecodedMessage(){
        if (decodedBytes != null) {
            return new String(decodedBytes);
        }
        return null;
    }
}
