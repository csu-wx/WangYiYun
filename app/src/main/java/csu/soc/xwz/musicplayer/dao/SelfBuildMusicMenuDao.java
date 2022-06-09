package csu.soc.xwz.musicplayer.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import csu.soc.xwz.musicplayer.pojo.SelfBuildMusicMenu;

public interface SelfBuildMusicMenuDao {
    //获取所有自建歌单信息
    public List<SelfBuildMusicMenu> getSelfMenuList(Connection connection) throws SQLException;
}
