package csu.soc.xwz.musicplayer.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import csu.soc.xwz.musicplayer.R;
import csu.soc.xwz.musicplayer.pojo.Music;

public class MusicPlayerHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "music.db";
    private static final int DB_VER = 1;

    static final String CREATE_SQL[] = {
            "CREATE TABLE SELF_BUILD_MUSIC_MENU (_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "USER_NAME TEXT," +
                    "MENU_NAME TEXT," +
                    "MENU_IMAGE_RESOURCE_ID INTEGER"+
                    ")",
            "CREATE TABLE RECENTLY_MUSIC (_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "USER_NAME TEXT," +
                    "MUSIC_ID TEXT" +
                    ")"
    };
    public MusicPlayerHelper(Context context) {
        super(context,DB_NAME,null,DB_VER);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.e("sqlite____","create Database");
        //执行sql语句
        for (int i = 0;i < CREATE_SQL.length;i++){
            db.execSQL(CREATE_SQL[i]);
        }
//        db.execSQL("create table stu_info(id INTEGER primary key autoincrement,sno varchar(10),name varchar(10),sex varchar(4),professional varchar(10),deparment varchar(20) )");

        Log.e("sqlite____", "数据库创建成功");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public static void insertSelfBuildMusicMenu(SQLiteDatabase db,String userName,String menuName,int resourceId){
        ContentValues selfValues = new ContentValues();
        selfValues.put("USER_NAME",userName);
        selfValues.put("MENU_NAME",menuName);
        selfValues.put("MENU_IMAG_RESOURCE_ID",resourceId);
        long result = db.insert("SELF_BUILD_MUSIC_MENU",null,selfValues);
        Log.d("sqlite", "insertSelfBuildMusicMenu: username: "+userName +
                " menuName: " + menuName + "_id" + result);
    }
    public static void insertRecentlyMusic(SQLiteDatabase db, String userName,String musicId){
        ContentValues selfValues = new ContentValues();
        selfValues.put("USER_NAME",userName);
        selfValues.put("MUSIC_ID",musicId);
        long result = db.insert("RECENTLY_MUSIC",null,selfValues);
        Log.d("sqlite", "insertRecentlyMusic: username " + userName +
                " musicId "+ musicId);
    }


}
