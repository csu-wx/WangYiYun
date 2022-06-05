package csu.soc.xwz.musicplayer.MineFragmentFolder;


import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

import csu.soc.xwz.musicplayer.Adapter.SelfListAdapter;
import csu.soc.xwz.musicplayer.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class SelfBuildMusicFragment extends Fragment implements AdapterView.OnItemClickListener {

    /*
     * listView 绑定数据源与适配器
     */
    //初始化list
    private ListView selfListView;
    //设置适配器
    private SelfListAdapter selfListAdapter;

    private List<SelfList_Content> selfList_contentList = new ArrayList<SelfList_Content>();

    public SelfBuildMusicFragment() {
        // Required empty public constructor
    }

    /*
     *布局文件和fragment相关联
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_self_build_music, container, false);

        initListResource();//初始化selfList数据

        selfListView = view.findViewById(R.id.list_self_build_music);
        selfListAdapter = new SelfListAdapter(getContext(),R.layout.mine_self_menu,selfList_contentList);
        selfListView.setAdapter(selfListAdapter);
        return view;
    }

    private void initListResource() {
        selfList_contentList.clear();
        SelfList_Content list_content1 = new SelfList_Content("创建歌单",R.drawable.mine_create_song_menu_button);
        SelfList_Content list_content2 = new SelfList_Content("导入外部歌单",R.drawable.mine_import_song_menu_button);
        selfList_contentList.add(list_content1);
        selfList_contentList.add(list_content2);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //ToDo such as how to deal with click;
    }

}
