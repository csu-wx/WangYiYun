package com.example.pages.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;

import com.example.pages.entity.Album;
import com.example.pages.entity.Directory;
import com.example.pages.entity.Music;
import com.example.pages.entity.Singer;

import java.nio.channels.InterruptedByTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class MusicResolver {
    private ContentResolver resolver;
    private Cursor cursor;
    private Music musicInfo;
    private Album album;
    private Singer singer;
    private Directory directory;
    public static ArrayList<Music> musicArrayList;
    private static ArrayList<Album> albumArrayList;
    private static ArrayList<Singer> singerArrayList;
    private static ArrayList<Directory> directoryArrayList;

    //获取本地音乐的基本信息
    public ArrayList<Music> getMusic(Context context) {
        musicArrayList = new ArrayList<Music>();
        resolver = context.getContentResolver();
        cursor = resolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                null, null, null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                musicInfo = new Music();
                // 歌手名
                musicInfo.setAuthor(cursor.getString(cursor
                        .getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)));
                //歌曲名
                musicInfo.setName(cursor.getString(cursor
                        .getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)));

//                musicInfo.setSong_size(FormatChange.formatSize(cursor
//                        .getInt(cursor
//                                .getColumnIndex(MediaStore.Audio.Media.SIZE))));// 歌曲的大小
                //歌曲ID
                musicInfo.setMusicID(cursor.getString(cursor
                        .getColumnIndexOrThrow(MediaStore.Audio.Media._ID)));

                //专辑封面ID
                musicInfo.setAlbumID(cursor.getString(cursor
                        .getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID)));

                //专辑名称
                musicInfo.setAlbumName(cursor.getString(cursor
                        .getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)));

                // 歌曲时间
                musicInfo.setDuration(String.valueOf(cursor.getInt(cursor
                        .getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION))));

                musicInfo.setLength(cursor.getInt(cursor
                        .getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)));

                //存放路径
                musicInfo.setUrl(cursor.getString(cursor
                        .getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)));

                musicArrayList.add(musicInfo);
            }
        }

        cursor.close();
        return musicArrayList;
    }

    //根据获得的musicList，返回albumList
    public ArrayList<Album> getAlum() {
        albumArrayList = new ArrayList<>();

        HashMap<String, LinkedList<Music>> map = new HashMap<>(musicArrayList.size());

        for (Music music : musicArrayList) {
            String albumName = music.getAlbumName();
            //若包含这个album,说明已经放过了，直接追加到后面
            if(map.containsKey(albumName))
                map.get(albumName).add(music);
            //若没有包含，则添加进去
            else {
                LinkedList<Music> temp = new LinkedList<>();
                temp.add(music);
                map.put(albumName,temp);
            }
        }

        ArrayList<Music> musics;

        //遍历map，将内容放到albumlist
        for (String album_name : map.keySet()) {
            album = new Album();
            musics = new ArrayList<Music>(map.get(album_name));
            album.setAlbumID(musics.get(0).getAlbumID());
            album.setAlbumName(album_name);
            album.setMusicArrayList(musics);
            album.setNum(musics.size());
            album.setSinger(musics.get(0).getAuthor());
            albumArrayList.add(album);
        }

        return albumArrayList;
    }

    //根据获得的album list，返回singer list
    public ArrayList<Singer> getSinger() {
        singerArrayList = new ArrayList<>(musicArrayList.size());

        //记录歌手对应的music
        HashMap<String, ArrayList<Music>> map = new HashMap<>(albumArrayList.size());
        //记录歌手对应几个专辑
        HashMap<String,Integer> albumNumMap = new HashMap<>();

        for (Album album : albumArrayList) {
            String singerName = album.getSinger();
            //若包含这个singer,说明已经放过他的一个专辑了，直接追加到后面
            if(map.containsKey(singerName)) {
                int num = albumNumMap.get(singerName) + 1;
                albumNumMap.put(singerName,num);
                ArrayList<Music> temp = new ArrayList<>(album.getMusicArrayList().size());
                temp.addAll(album.getMusicArrayList());
                temp.addAll(map.get(singerName));
                map.put(singerName,temp);
            }
            //若没有包含，则添加进去
            else {
                map.put(singerName,album.getMusicArrayList());
                albumNumMap.put(singerName,1);
            }
        }

        //遍历map，将内容放到singerlist
        for (String singer_name : map.keySet()) {
            singer = new Singer();
            singer.setName(singer_name);
            singer.setAlbumNum(albumNumMap.get(singer_name));
            singer.setMusicArrayList(map.get(singer_name));
            singer.setTotalMusic(map.get(singer_name).size());
            singerArrayList.add(singer);
        }


        return singerArrayList;
    }

    //根据music list获取文件夹list
    public ArrayList<Directory> getDirectory() {
        directoryArrayList = new ArrayList<>(musicArrayList.size());

        HashMap<String,ArrayList<Music>> map = new HashMap<>(musicArrayList.size());

        ArrayList<Music> temp;

        for ( Music music : musicArrayList ) {
            String direcName = music.getDirectoryName();
            if ( map.containsKey(direcName) ) {
                map.get(direcName).add(music);
            }
            else {
                temp = new ArrayList<>();
                temp.add(music);
                map.put(direcName,temp);
            }
        }

        for (String direc_name : map.keySet()) {
            directory = new Directory();
            directory.setName(direc_name);
            directory.setMusicArrayList(map.get(direc_name));
            directory.setTotalMusic(map.get(direc_name).size());
            String[] str = map.get(direc_name).get(0).getUrl().split("/");
            StringBuilder direct_url = new StringBuilder();
            for (int i = 0; i < str.length - 2; i++) {
                direct_url.append(str[i] + "/");
            }
            //删除最后一个“/”
            direct_url.deleteCharAt(direct_url.length() - 1);
            directory.setUrl(direct_url.toString());
            directoryArrayList.add(directory);
        }

        return directoryArrayList;
    }

}
