package msm.MemeSena.Utils;

import android.content.Context;
import android.os.AsyncTask;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Random;

import msm.MemeSena.Interface.OnImageDownloadTaskComplite;

public class DownloadStickerPackImage extends AsyncTask<Void, Void, String> {
    Context context;
    String stickerUrl;
    OnImageDownloadTaskComplite onImageDownloadTaskComplite;
    File saveImgFolder;

    public DownloadStickerPackImage(Context context, String stickerUrl, OnImageDownloadTaskComplite onImageDownloadTaskComplite) {
        this.context = context;
        this.stickerUrl = stickerUrl;
        saveImgFolder = new File(context.getDir("TempAllSticker", Context.MODE_PRIVATE).getAbsolutePath() + File.separator);
        this.onImageDownloadTaskComplite = onImageDownloadTaskComplite;
    }

    @Override
    protected String doInBackground(Void... voids) {
        try {
            String ext = stickerUrl.substring(stickerUrl.lastIndexOf("."));
            String saveStickerName = Math.abs(new Random().nextInt()) + "" + System.currentTimeMillis() + ext;

            String fileLocaiton = saveImgFolder.getAbsolutePath() + File.separator + saveStickerName;
            URLConnection ucon = new URL(stickerUrl).openConnection();
            InputStream in = ucon.getInputStream();
            OutputStream output = null;

            output = new FileOutputStream(fileLocaiton);
            byte[] buffer = new byte[1024];
            int bytesRead = 0;
            while ((bytesRead = in.read(buffer, 0, buffer.length)) >= 0) {
                output.write(buffer, 0, bytesRead);
            }
            output.close();
            in.close();
            return fileLocaiton;
        } catch (Exception e) {
            e.getMessage();
            return "";
        }
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        onImageDownloadTaskComplite.onFinish(result, saveImgFolder);
    }
}