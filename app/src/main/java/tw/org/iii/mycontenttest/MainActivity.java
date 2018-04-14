package tw.org.iii.mycontenttest;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    private ContentResolver contentResolver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

}
