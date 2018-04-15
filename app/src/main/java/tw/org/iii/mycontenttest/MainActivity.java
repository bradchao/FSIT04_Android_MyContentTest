package tw.org.iii.mycontenttest;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private ContentResolver contentResolver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_CALL_LOG)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_CONTACTS,
                            Manifest.permission.READ_CALL_LOG},
                    0);
        }else{
            init();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            init();
        }
    }

    private void init(){
        contentResolver = getContentResolver();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (!Settings.System.canWrite(this)){
                Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
                intent.setData(Uri.parse("package:" + getPackageName()));
                startActivity(intent);
            }
        }
    }


    // read settings
    public void test1(View view) {
//        Uri uri = Settings.System.CONTENT_URI;
//        Log.v("brad", uri.toString());
//        //Uri.parse("content://settings/system");
//
//        Cursor cursor = contentResolver.query(uri,
//                null,null,
//                null,null);
//
//        while (cursor.moveToNext()){
//            String name = cursor.getString(cursor.getColumnIndex("name"));
//            String value = cursor.getString(cursor.getColumnIndex("value"));
//            Log.v("brad", name + " => " + value);
//        }
//        cursor.close();

        Log.v("brad", "==> " +
                getSettingValue(Settings.System.FONT_SCALE));


    }

    private String getSettingValue(String name){
        String ret = "";
        Cursor cursor = contentResolver.query(Settings.System.CONTENT_URI,
                new String[]{"name", "value"},
                "name = ?",
                new String[]{name}, null);
        if (cursor.getCount()>0){
            cursor.moveToFirst();
            ret = cursor.getString(cursor.getColumnIndex("value"));
        }

        return ret;
    }

    public void test2(View view) {
        Settings.System.putInt(contentResolver,
                Settings.System.SCREEN_BRIGHTNESS, 120);
        contentResolver.notifyChange(Settings.System.CONTENT_URI,
                null);

        Log.v("brad", "==> " +
                getSettingValue(Settings.System.SCREEN_BRIGHTNESS));

    }

    public void test3(View view) {
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        Cursor cursor =
                contentResolver.query(uri,
                        null,null,
                        null,null);

        String[] fields = cursor.getColumnNames();
        for (String field : fields){
            Log.v("brad", field);
        }

        Log.v("brad", "---------------------------");
        Log.v("brad", ContactsContract.CommonDataKinds.Phone.NUMBER);

//        while (cursor.moveToNext()){
//            String id = cursor.getString(
//                    cursor.getColumnIndex(
//                            ContactsContract.CommonDataKinds.Phone._ID));
//            String name = cursor.getString(
//                    cursor.getColumnIndex(
//                    ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
//            String number = cursor.getString(
//                    cursor.getColumnIndex(
//                            ContactsContract.CommonDataKinds.Phone.NUMBER));
//
//            Log.v("brad", name + ":" + number);
//        }


    }


    public void test4(View view) {
        Uri uri = CallLog.Calls.CONTENT_URI;
        Cursor cursor = contentResolver.query(uri,
                null,null,
                null,null);

        while (cursor.moveToNext()){
            String name = cursor.getString(
                    cursor.getColumnIndex(
                    CallLog.Calls.CACHED_NAME));
            String number = cursor.getString(
                    cursor.getColumnIndex(
                            CallLog.Calls.NUMBER));
            String type = cursor.getString(
                    cursor.getColumnIndex(
                            CallLog.Calls.TYPE));

            SimpleDateFormat sdf = new SimpleDateFormat(
                    "yyyy-MM-dd HH:mm:ss");
            Date date = new Date(
                    cursor.getLong(cursor.getColumnIndex(CallLog.Calls.DATE)));
            String calldate = sdf.format(date);

            int duration = cursor.getInt(cursor.getColumnIndex(CallLog.Calls.DURATION));

            Log.v("brad", calldate+ ":" + name + ":" + number + ":" + duration);

        }

        // MediaStore.Images.Media.EXTERNAL_CONTENT_URI

    }
}
