package csu.soc.xwz.musicplayer.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import csu.soc.xwz.musicplayer.pojo.SelfBuildMusicMenu;

/*
 *获取存在self_build_music_menu表中的信息
 */
public class SelfBuildMusicMenuDaoImpl implements SelfBuildMusicMenuDao {
    @Override
    public List<SelfBuildMusicMenu> getSelfMenuList(Connection connection) throws SQLException {
        ArrayList<SelfBuildMusicMenu> sbmmList = new ArrayList<SelfBuildMusicMenu>();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        if (connection!=null) {
            String sql = "select * from music";
            Object[] params = {};
            resultSet = BaseDao.execute(connection, sql, params, resultSet, preparedStatement);
            while(resultSet.next()){
                SelfBuildMusicMenu selfBuildMusicMenu = new SelfBuildMusicMenu();
                selfBuildMusicMenu.setMenuName(resultSet.getString("self_name"));
//               selfBuildMusicMenu.setImage(resultSet.);


                sbmmList.add(selfBuildMusicMenu);
            }
            BaseDao.closeResource(null,preparedStatement,resultSet);
        }
        return sbmmList;
    }
}
