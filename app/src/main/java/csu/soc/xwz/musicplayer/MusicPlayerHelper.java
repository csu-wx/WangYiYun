package csu.soc.xwz.musicplayer;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MusicPlayerHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "music.db";
    private static final int DB_VER = 1;

    public MusicPlayerHelper(Context context) {
        super(context,DB_NAME,null,DB_VER);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE SELF_BUILD_MUSIC_MENU (_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "USER_NAME TEXT," +
                "MENU_NAME TEXT," +
                "MENU_IMAGE_RESOURCE_ID INTEGER);");
        insertSelfBuildMusicMenu(db,"admin","中文歌单",R.drawable.night);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    private static void insertSelfBuildMusicMenu(SQLiteDatabase db,String userName,String menuName,int resourceId){
        ContentValues selfValues = new ContentValues();
        selfValues.put("USER_NAME",userName);
        selfValues.put("MENU_NAME",menuName);
        selfValues.put("MENU_IMAG_RESOURCE_ID",resourceId);
        long result = db.insert("SELF_BUILD_MUSIC_MENU",null,selfValues);
        Log.d("sqlite", "insertSelfBuildMusicMenu: username: "+userName +
                " menuName: " + menuName + "_id" + result);
    }
}
