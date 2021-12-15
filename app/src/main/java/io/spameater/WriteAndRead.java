package io.spameater;

import android.app.Application;
import android.content.Context;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class WriteAndRead {
    public static void save(String sender, String body, int position, Context context) throws FileNotFoundException, JSONException {
        creatSpamFolder(context);
        String filename = "";
        String fileContents = "";
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("addr",sender);
        jsonObject.put("body", Base64.encodeToString(body.getBytes(StandardCharsets.UTF_8),Base64.DEFAULT));
        filename=context.getFilesDir()+"/spam/"+ position;
        fileContents=jsonObject.toString();
        try (FileOutputStream fos = new FileOutputStream(filename)) {
            fos.write(fileContents.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static String[] read(int position,Context context) {
        String filename = "";
        filename=context.getFilesDir()+"/spam/"+ position;
        try (FileInputStream fis = new FileInputStream(filename)) {
            StringBuilder stringBuilder = new StringBuilder();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fis));
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            JSONObject jsonObject=new JSONObject(stringBuilder.toString());
            String[] result=new String[2];
            result[0]=jsonObject.getString("addr");
            result[1]=new String(Base64.decode(jsonObject.getString("body"),Base64.DEFAULT));
            return result;
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return null;
    }
    public static void save(String keyword, Context context) throws FileNotFoundException {
        creatSpamFolder(context);
        String filename = "";
        String fileContents = "";

        filename=context.getFilesDir()+"/keywords";
        fileContents=keyword+"\n";
        try (FileOutputStream fos = new FileOutputStream(filename,true)) {
            fos.write(fileContents.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static String[] read(Context context) {
        String filename = "";
        filename=context.getFilesDir()+"/keywords";
        try (FileInputStream fis = new FileInputStream(filename)) {
            StringBuilder stringBuilder = new StringBuilder();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fis));
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line+"\n");
            }

            return stringBuilder.toString().split("\n");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
    public static void creatSpamFolder(Context context){
        File directory = context.getFilesDir();
        File file = new File(directory+"/spam/");
        if (!file.exists())
            file.mkdir();
    }
}
