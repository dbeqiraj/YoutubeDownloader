package com.dbeqiraj.youtubedownloader.utilities;

import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.media.RingtoneManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;

import com.dbeqiraj.youtubedownloader.R;

import java.io.File;

/**
 * Created by d.beqiraj on 3/27/2018.
 */

public class Utils {

    public static boolean permissionIsGranted(Context context, String permission) {
        int res = ContextCompat.checkSelfPermission(context, permission);
        return ( res == PackageManager.PERMISSION_GRANTED);
    }

    public static void isMusic(Context context, File file, String title){
        ContentValues values = new ContentValues();
        values.put(MediaStore.MediaColumns.DATA,        file.getAbsolutePath());
        values.put(MediaStore.MediaColumns.TITLE,       title);
        values.put(MediaStore.MediaColumns.MIME_TYPE,   "audio/mp3");
        values.put(MediaStore.Audio.Media.ARTIST,       context.getString(R.string.app_name));
        values.put(MediaStore.Audio.Media.IS_MUSIC, true);

        Uri uri = MediaStore.Audio.Media.getContentUriForPath(file.getAbsolutePath());

        try {
            context.getContentResolver().delete(uri, MediaStore.MediaColumns.DATA + "=\"" + file.getAbsolutePath() + "\"", null);
            Uri newUri = context.getContentResolver().insert(uri, values);
            RingtoneManager.setActualDefaultRingtoneUri(context, 0, newUri);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Typeface fontQuicksandBold(Context   context){
        return Typeface.createFromAsset(context.getApplicationContext().getAssets(),"font/Quicksand-Bold.otf");
    }
}
