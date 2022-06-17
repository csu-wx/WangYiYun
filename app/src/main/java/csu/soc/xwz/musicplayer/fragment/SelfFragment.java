package csu.soc.xwz.musicplayer.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

import csu.soc.xwz.musicplayer.Adapter.SelfAdapter;
import csu.soc.xwz.musicplayer.R;
import csu.soc.xwz.musicplayer.activity.SongMenuActivity;
import csu.soc.xwz.musicplayer.pojo.SelfList_Content;

public class SelfFragment extends Fragment {
    private View root;
    private TextView textview;
    private Button button;
    private ArrayList<SelfList_Content> selfArrayList = new ArrayList<>();
    private RecyclerView recyclerview;
    private SelfAdapter adapter;
    private String content;

    //监听回调
    private SelfFragment.FragmentCallBack mFragmentCallBack;

    //定义一个监听，用于fragment向activity传递数据
    public  interface FragmentCallBack {
        //定义监听方法，这里主要是需要传递当前播放歌曲的item的position
        void getPosition(int position);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    //创建fragment视图
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (root == null){
            root = inflater.inflate((R.layout.fragment_self),container,false);
        }
//        initRecyclerview();
//        initData();
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initRecyclerview();
        initData();
    }

    private void initData() {
        selfArrayList.clear();
        SelfList_Content list_content1 = new SelfList_Content("创建歌单",R.drawable.mine_create_song_menu_button);
        SelfList_Content list_content2 = new SelfList_Content("导入外部歌单",R.drawable.mine_import_song_menu_button);
        selfArrayList.add(list_content1);
        selfArrayList.add(list_content2);
    }

    private void initRecyclerview() {
        recyclerview = (RecyclerView) root.findViewById(R.id.self_recycler_view);
        adapter = new SelfAdapter(getActivity(),selfArrayList);
        recyclerview.setAdapter(adapter);
        //选择RecylerView显示方式
        recyclerview.setLayoutManager(new LinearLayoutManager(this.getContext(),LinearLayoutManager.VERTICAL,false));
        adapter.setMyOnItemClickListener(new SelfAdapter.MyOnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void OnItemClickListener(View itemView) {
                int i = recyclerview.getChildAdapterPosition(itemView);
                switch (i){
                    case 0:
                        setMenuName();
                        break;
                    case 1:
                        Log.d("print", "onItemClick: 导入外部歌单");
                        break;
                    case 2:
                        Intent intent = new Intent(getActivity(),SongMenuActivity.class);
                        intent.putExtra("songMenuName",content);
                        SimpleDateFormat formatter = new SimpleDateFormat("MM-dd"); //设置时间格式
                        formatter.setTimeZone(TimeZone.getTimeZone("GMT+08")); //设置时区
                        Date curDate = new Date(System.currentTimeMillis()); //获取当前时间
                        String createDate = formatter.format(curDate);   //格式转换
                        intent.putExtra("time",createDate);
                        startActivity(intent);
                        break;
                }
            }
        });
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
                        content = editText.getText().toString();
                        if (content.equals("")){
                            Toast.makeText(getActivity(), "歌单名称不能为空", Toast.LENGTH_SHORT).show();
                        }else {
//                            Toast.makeText(getActivity(), "歌单名为：" + content, Toast.LENGTH_SHORT).show();
                            SelfList_Content selfList_content = new SelfList_Content(content,R.drawable.night);
                            selfArrayList.add(selfList_content);
                        }
                        Intent intent = new Intent(getActivity(),SongMenuActivity.class);
                        intent.putExtra("songMenuName",content);

                        SimpleDateFormat formatter = new SimpleDateFormat("MM-dd"); //设置时间格式
                        formatter.setTimeZone(TimeZone.getTimeZone("GMT+08")); //设置时区
                        Date curDate = new Date(System.currentTimeMillis()); //获取当前时间
                        String createDate = formatter.format(curDate);   //格式转换

                        intent.putExtra("time",createDate);
                        dialog.dismiss();
                        startActivity(intent);

                    }
                }).create();
        dialog.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }
}
