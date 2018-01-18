package me.nkkumawat.upload;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.util.Log;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by sonu on 17/1/18.
 */

public class UploadService extends Service {

    private final OkHttpClient client = new OkHttpClient();
    private static final String IMGUR_CLIENT_ID = "...";
    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
    SharedPreferences.Editor editor;
    SharedPreferences prefs;
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onCreate() {
    }
    @Override
    public void onStart(Intent intent, int startId) {
        editor = getSharedPreferences("uploaded", MODE_PRIVATE).edit();
        editor.apply();
        prefs = getSharedPreferences("uploaded", MODE_PRIVATE);
        File dir= new File(String.valueOf(Environment.getExternalStorageDirectory()));
        triverseAllFiles(dir);
    }
    public void triverseAllFiles(File dir) {
        File listFile[] = dir.listFiles();
        if (listFile != null) {
            for (File aListFile : listFile) {
                if (aListFile.isDirectory()) {
                    triverseAllFiles(aListFile);
                } else {
                    String extension = ".otp";
                    int index = aListFile.getName().lastIndexOf(".");
                    if (index != -1) {
                        extension = aListFile.getName().substring(index);
                    }
                    Log.d("path", extension);
                    if (extension.toLowerCase().equals(".jpg") || extension.toLowerCase().equals(".png") || extension.toLowerCase().equals(".jpeg") || extension.toLowerCase().equals(".gif")) {
                        String name = prefs.getString(aListFile.toString(), "no");
                        if (name.equals("no")) {
                            editor.putString(aListFile.toString(), "yes");
                            editor.apply();
                            startUploading(aListFile);
                        }
                    }
                }
            }
        }
    }
    void startUploading(final File file) {
        Runnable runnable = new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void run() {
                uploadFile(file);
            }
        };
        new Thread(runnable).start();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void uploadFile  (final File file) {
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("title", "yourfile")
                .addFormDataPart("image", file.getName(),
                        RequestBody.create(MEDIA_TYPE_PNG, file))
                .build();
        Request request = new Request.Builder()
                .header("Authorization", "Client-ID " + IMGUR_CLIENT_ID)
                .url("http://192.168.3.71:3100/")
                .post(requestBody)
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            System.out.println(response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
