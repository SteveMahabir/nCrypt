package com.example.kevin.tempappp;

import android.content.Context;
import android.util.Base64;
import android.util.Log;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;

import javax.crypto.Cipher;

public class Encryption {

    // Whoami
    static final String TAG = "EncryptionAlgorithms";

    // saving the context for later use
    //private final Context mContext;

    // Generate key pair for 1024-bit RSA encryption and decryption
    private Key publicKey = null;
    private Key privateKey = null;

    // Physical Location of Files
    public static final String PRIVATE_KEY_FILE = "/storage/emulated/0/private.key";
    public static final String PUBLIC_KEY_FILE = "/storage/emulated/0/public.key";
    //"/src/main/assets""

    // Encrypted / Decrypted Data
    byte[] encodedBytes;
    byte[] decodedBytes;


    // CONSTRUCTOR
    public Encryption() {
    }

    // Loads keys from file - Generates new keys if not found
    public void PrepareKeys() {

        // Check if the pair of keys are present else generate new ones
        if (!areKeysPresent()) {
            // generates keys using the RSA algorithm and stores it on file
            GenerateNewKey();
        }

        else
        {
            try {
                // LOAD KEYS FROM FILE
                ObjectInputStream inputStream = null;
                inputStream = new ObjectInputStream(new FileInputStream(PUBLIC_KEY_FILE));
                this.publicKey = (Key) inputStream.readObject();

                inputStream = new ObjectInputStream(new FileInputStream(PRIVATE_KEY_FILE));
                this.privateKey = (Key) inputStream.readObject();
            }
            catch(Exception e) {e.printStackTrace();}
        }
    }

    public void SaveKeys(KeyPair kp) {
        try{
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

    public void GenerateNewKey(){
        try {
            KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
            kpg.initialize(1024);
            KeyPair kp = kpg.genKeyPair();

            this.publicKey = kp.getPublic();
            this.privateKey = kp.getPrivate();

            this.SaveKeys(kp);
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


    // Helper Method
    public boolean isEncryptedMessage(String message){
        if(message.length() == 175)
            return true;
        else
            return false;
    }

    // Helper Method
    public boolean isEncryptedPublicKey(String message){
        if(message.length() == 539)
            return true;
        else
            return false;
    }


    // Helper Method
    public static boolean areKeysPresent() {
        File privateKey = new File(PRIVATE_KEY_FILE);
        File publicKey = new File(PUBLIC_KEY_FILE);

        if (privateKey.exists() && publicKey.exists()) {
            return true;
        }
        return false;
    }

    // Used for Sending a Public Key
    public String sendPublicKey(Key public_key)
    {
        try {
            // Serialize the Key into a byte[]
            byte[] serialized_public_key = DBAdapter.Serialize(public_key);
            // Encode the byte[] into String
            String serialized_public_key_text = Base64.encodeToString(serialized_public_key, Base64.DEFAULT);
            // Text the String to a person
            return serialized_public_key_text;
        }
        catch (Exception e) {
            Log.e(TAG, "RSA encryption error");
        }

        return "";
    }

    public Key recievePublicKey(String serialized_public_key_text)
    {
        try{
            // Decode the string into a byte[]
            byte[] serialized_public_key = Base64.decode(serialized_public_key_text, Base64.DEFAULT);
            // Deserialize the byte[] into a Key
            Key public_key = (Key) DBAdapter.Deserialize(serialized_public_key);

            return public_key;
        }
        catch (Exception e) {
            Log.e(TAG, "RSA encryption error");
        }

        return null;
    }




    // Getter sand Setters
    public Key getPublicKey(){
        return publicKey;
    }
    public void setPublicKey(Key public_key){
        this.publicKey = public_key;
    }
    public Key getPrivateKey(){
        return privateKey;
    }
    public void setPrivateKey(Key private_key){
        this.privateKey = private_key;
    }

    // If you just need the  encoded data
    public byte[] GetEncodedData(){return encodedBytes;}
    public byte[] GetEncodedData(String input){
        Encrypt(input);
        return encodedBytes;
    }

    // If you just need the decoded data
    public byte[] GetDecodedData(){return decodedBytes;}
    public byte[] GetDecodedData(String input){
        Decrypt(input);
        return decodedBytes;}
}
