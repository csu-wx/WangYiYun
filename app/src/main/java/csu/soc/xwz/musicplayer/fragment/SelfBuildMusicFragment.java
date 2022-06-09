package csu.soc.xwz.musicplayer.fragment;


import static java.security.AccessController.getContext;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

import csu.soc.xwz.musicplayer.Adapter.SelfListAdapter;
import csu.soc.xwz.musicplayer.R;
import csu.soc.xwz.musicplayer.pojo.SelfList_Content;


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
        selfListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0:
                        setMenuName();
                        break;
                    case 1:
                        Log.d("print", "onItemClick: 导入外部歌单");
                        break;
                }

            }
        });
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
    private void setMenuName(){
        //保存用户输入
        final String[] menuName = new String[1];
        View view1 = getLayoutInflater().inflate(R.layout.set_self_music_menu, null);
        final EditText editText = (EditText) view1.findViewById(R.id.self_dialog_edit);
        AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setIcon(R.drawable.default_record_album)//设置标题的图片
                .setTitle("设置歌单名称")//设置对话框的标题
                .setView(view1)
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String content = editText.getText().toString();
                        if (content.equals("")){
                            Toast.makeText(getActivity(), "歌单名称不能为空", Toast.LENGTH_SHORT).show();
                        }else {
//                            Toast.makeText(getActivity(), "歌单名为：" + content, Toast.LENGTH_SHORT).show();
                            SelfList_Content selfList_content = new SelfList_Content(content,R.drawable.night);
                            selfList_contentList.add(selfList_content);
                        }
                        dialog.dismiss();
                    }
                }).create();
        dialog.show();


    }
}
