package UI.SIgnAuth;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class Users {
    public HashMap<String, String> getUserMap() {
        return userMap;
    }

    HashMap<String,String > userMap;

    public HashMap<String, String> getRootMap() {
        return rootMap;
    }

    public  HashMap<String,String>rootMap;
    public HashMap<String, String> getAdminMap() {
        return adminMap;
    }

    HashMap<String,String>  adminMap;

    public File getUserFile() {
        return userFile;
    }

    File userFile;

    public File getAdminFile() {
        return adminFile;
    }

    public File getRootFile() {
        return rootFile;
    }

    File rootFile;

    File adminFile;
    PasswordCipher cipher;

    public boolean isRootFlag() {
        return rootFlag;
    }

    boolean rootFlag = true;


    public Users() {
        userMap = new HashMap<>();
        adminMap = new HashMap<>();
        rootMap = new HashMap<>();
        userFile =new File("src/Userlist/users.txt");
        adminFile = new File("src/Userlist/admins.txt");
        rootFile = new File("src/Userlist/root.txt");
        cipher = new PasswordCipher("Wearemenmanlymen");

    }

    private boolean checkPass(String password){
        return !password.contains(" ") && password.length() >= 1 &&password.length() <=255 && !password.contains("/");


    }
    private  boolean checkLogin(String login){
        return !login.contains(" ") && login.length() >= 1 && login.length() < 255 && !login.contains("/");

    }

    public CHECKER CheckAuth(String Login, String password){
        if(!Login.equals("root")) {
            rootFlag= false;
            if (!adminMap.containsKey(Login)) {
                if (!userMap.containsKey(Login)){
                    return CHECKER.INCORRECT_ALL;

                }
                if (!userMap.get(Login).equals(password)) {
                    return CHECKER.INCORRECT_ALL;

                }
            }
            if (!adminMap.get(Login).equals(password)) {
                return CHECKER.INCORRECT_ALL;

            }

        } else {
            if (!rootMap.containsKey(Login)) {
                return CHECKER.INCORRECT_ALL;
            }
            if (!rootMap.get(Login).equals(password)) {
                return CHECKER.INCORRECT_ALL;

            }
        }

        return CHECKER.CORRECT;
    }

    public CHECKER addUsers(HashMap<String,String> map,String login, String password,File curFile) throws IOException, IllegalBlockSizeException, java.security.InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException {
        boolean loginFlag = checkLogin(login);
        boolean passwordFlag = checkPass(password);
        boolean incorectALL = !loginFlag & !passwordFlag;
        if(incorectALL){
            return CHECKER.INCORRECT_ALL;
        }
        if(!loginFlag){
            return CHECKER.INCORRECT_LOGIN;
        }
        if(!passwordFlag){
            return CHECKER.INCORRECT_PASSWORD;
        }



        if(adminMap.containsKey(login) || userMap.containsKey(login)){
            return CHECKER.USER_EXIST;
        }


        map.put(login,password);
        Writer magicWriter = new FileWriter(curFile,true);

        magicWriter.write(login+"///"+cipher.encrypt(password)+"\n");
        magicWriter.close();
        return CHECKER.CORRECT;



    }
    public void putDataInFile(HashMap<String,String>map, String pathname) throws IOException, IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException {
        File file = new File(pathname);
        FileWriter magicWriter = new FileWriter(file,false);
        magicWriter.close();

        FileWriter writer = new FileWriter(file,true);
        for(Map.Entry<String, String> entry : map.entrySet()) {
            String login = entry.getKey();
            String  password = entry.getValue();
            writer.write(login+"///"+cipher.encrypt(password)+"\n");


        }
        writer.close();



    }


    public   void getDataFromFile(HashMap<String,String>map, String pathname) throws FileNotFoundException, IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException {
        File file = new File(pathname);
        Scanner magicReader = new Scanner(file);
        String delimeter = "///";
        String []userdata;
        while (magicReader.hasNext()) {
            String aux = magicReader.next();
            userdata = aux.split(delimeter);
            userdata[1] = cipher.decrypt(userdata[1]);
            map.put(userdata[0], userdata[1]);
            System.out.println(userdata[0] + " " + userdata[1]);
        }
        magicReader.close();

    }

}

 class PasswordCipher{
     private  SecretKey secretKey;
     private  Base64.Encoder encoder;
     private  Base64.Decoder decoder;
     public PasswordCipher(String key) {
         secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES");
         encoder = Base64.getEncoder();
         decoder = Base64.getDecoder();

     }

     public String encrypt(String plainText) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {

             byte[] plainTextByte = plainText.getBytes();

             Cipher cipher = Cipher.getInstance("AES");
             cipher.init(Cipher.ENCRYPT_MODE, secretKey);
             byte[] encryptedByte = cipher.doFinal(plainTextByte);

             return encoder.encodeToString(encryptedByte);
     }


     public String decrypt(String encrypted) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
             byte[] encryptedByte = decoder.decode(encrypted);

             Cipher cipher = Cipher.getInstance("AES");
             cipher.init(Cipher.DECRYPT_MODE, secretKey);
             byte[] decryptedByte = cipher.doFinal(encryptedByte);

             return new String(decryptedByte);


     }












 }
