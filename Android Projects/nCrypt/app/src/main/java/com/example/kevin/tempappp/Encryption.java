package com.example.kevin.tempappp;

import android.content.Context;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PublicKey;

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


    /**
     * String to hold the name of the private key file.
     */
    //"/src/main/assets""

    public static final String PRIVATE_KEY_FILE = "/storage/emulated/0/private.key";

    /**
     * String to hold name of the public key file.
     */
    public static final String PUBLIC_KEY_FILE = "/storage/emulated/0/public.key";


    // CONSTRUCTOR
    public Encryption(Context context) {
        this.mContext = context;

        try {
            InputStream is = null;
            ObjectInputStream os;

            // Get private key
            is = context.getResources().openRawResource(R.raw.privatekey);
            os = new ObjectInputStream(is);
            privateKey = (Key) os.readObject();

            // get public key
            is = context.getResources().openRawResource(R.raw.publickey);
            os = new ObjectInputStream(is);
            publicKey = (Key) os.readObject();
        }
        catch(Exception e){e.printStackTrace();}
/*
        // Check if the pair of keys are present else generate those.
        if (!areKeysPresent()) {
            // Method generates a pair of keys using the RSA algorithm and stores it
            // in their respective files
            //GenerateKey();
        }
        else
        {
            // LOAD KEYS

            try {
                ObjectInputStream inputStream = null;
                inputStream = new ObjectInputStream(new FileInputStream(PUBLIC_KEY_FILE));
                publicKey = (Key) inputStream.readObject();

                inputStream = new ObjectInputStream(new FileInputStream(PRIVATE_KEY_FILE));
                privateKey = (Key) inputStream.readObject();
            }
            catch(Exception e) {e.printStackTrace();}

        }
        */
    }

    /**
     * The method checks if the pair of public and private key has been generated.
     *
     * @return flag indicating if the pair of keys were generated.
     */
    public static boolean areKeysPresent() {

        File privateKey = new File(PRIVATE_KEY_FILE);
        File publicKey = new File(PUBLIC_KEY_FILE);

        if (privateKey.exists() && publicKey.exists()) {
            return true;
        }
        return false;
    }

    public void GenerateKey(){

        try {
            KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
            kpg.initialize(1024);
            KeyPair kp = kpg.genKeyPair();

            publicKey = kp.getPublic();
            privateKey = kp.getPrivate();

            File privateKeyFile = new File(PRIVATE_KEY_FILE);
            File publicKeyFile = new File(PUBLIC_KEY_FILE);

            // Create files to store public and private key
            if (privateKeyFile.getParentFile() != null) {
                privateKeyFile.getParentFile().mkdirs();
            }
            privateKeyFile.createNewFile();

            if (publicKeyFile.getParentFile() != null) {
                publicKeyFile.getParentFile().mkdirs();
            }
            publicKeyFile.createNewFile();

            // Saving the Public key in a file
            ObjectOutputStream publicKeyOS = new ObjectOutputStream(
                    new FileOutputStream(publicKeyFile));
            publicKeyOS.writeObject(kp.getPublic());
            publicKeyOS.close();
            // Saving the Private key in a file
            ObjectOutputStream privateKeyOS = new ObjectOutputStream(
                    new FileOutputStream(privateKeyFile));
            privateKeyOS.writeObject(kp.getPrivate());
            privateKeyOS.close();

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

    public boolean isEncrypted(String message){
        if(message.length() == 175)
            return true;
        else
            return false;
    }
}
