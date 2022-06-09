package csu.soc.xwz.musicplayer.utils;



import java.util.ArrayList;
import java.util.List;

import csu.soc.xwz.musicplayer.pojo.Music;
import csu.soc.xwz.musicplayer.service.MusicService;

public interface MyBinderInterface {
    //暂停
    void pause();
    //恢复
    void resume();
    //播放
    void play();
    //播放下一首
    void playNext();
    //播放上一首
    void playPrev();
    //释放
    void release();
    //是否正在播
    boolean isPlaying();
    //获取时长
    int getDuration();
    //当前位置
    int getCurrentPosition();
    //拖动位置
    void seekTo(int length);
    //获取当前索引
    int getCurrentIndex();
    //设置当前索引
    void setCurrentIndex(int currentIdx);

    void reset();

    void setPlayMode();

    void setMusicList(ArrayList<Music> musicList);

    ArrayList<Music> getMusicList();

    MusicService getService();

    List<String> getRecentlyMusic(String userName);
    void  addRecentlyMusic(String USER_NAME,String musicId);

    public List<Music> getRecentMusics(String userName);
}
