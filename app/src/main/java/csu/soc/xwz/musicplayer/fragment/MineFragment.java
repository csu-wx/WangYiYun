package csu.soc.xwz.musicplayer.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import csu.soc.xwz.musicplayer.R;
import csu.soc.xwz.musicplayer.activity.ChangePwdActivity;
import csu.soc.xwz.musicplayer.activity.CollectActivity;
import csu.soc.xwz.musicplayer.activity.LocalActivity;
import csu.soc.xwz.musicplayer.activity.LoginActivity;
import csu.soc.xwz.musicplayer.activity.MainActivity;
import csu.soc.xwz.musicplayer.activity.RecentlyPlayerActivity;
import csu.soc.xwz.musicplayer.activity.VipServiceActivity;
import csu.soc.xwz.musicplayer.utils.MusicResolver;
import csu.soc.xwz.musicplayer.utils.StaticValue;


public class MineFragment extends Fragment implements View.OnClickListener{

    private LayoutInflater inflater;

    private Context mainActivity;
    private Button logButton;
    private Button recentButton;
    private Button shouCangButton;

    private TextView textView;
    private Button localButton;
    private ImageButton vipImageButton;
    private TextView musicNumText;
    private ImageButton setImageButton;
    private RadioGroup twoGrop;
    protected RadioButton selfButton,collectButton;
    private ImageButton likeCountButton;
    /**
     * 广播接收器对象
     */
    public static Login_Receiver login_receiver;
    public static LocalBroadcastManager localBroadcastManager;

    // 设置默认fragment
    private FragmentManager fm;
    private FragmentTransaction transaction;
    private CollectMusicFragment collectMusicFragment;
    private SelfFragment selfFragment;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mainActivity = getActivity();
        inflater = LayoutInflater.from(getActivity());

        login_receiver = new Login_Receiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("login");
        intentFilter.addAction("collect");
        //注册应用内广播接收器
        localBroadcastManager = LocalBroadcastManager.getInstance(this.getContext());
        localBroadcastManager.registerReceiver(login_receiver, intentFilter);

        //初始我的fragment内的选项卡控件
        twoGrop = (RadioGroup) getActivity().findViewById(R.id.redio);
        selfButton =(RadioButton) getActivity().findViewById(R.id.self_button);
        collectButton =(RadioButton) getActivity().findViewById(R.id.collect_button);

        Log.e("点击自建歌单","______");

        selfButton.setOnClickListener(this);
        collectButton.setOnClickListener(this);

        selfFragment = new SelfFragment();
        collectMusicFragment = new CollectMusicFragment();
        fm = getChildFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.add(R.id.framelayout,selfFragment).commit();
        //设置我的fragment内所有按钮点击响应事件
        setButtonClick();
    }

    private void setButtonClick() {
        //设置登录按钮点击事件
        logButton = getActivity().findViewById(R.id.unLogTip);
        logButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });

        //设置本地按钮点击事件
        localButton = getActivity().findViewById(R.id.local_music_btn);
        localButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), LocalActivity.class);
//                intent.putExtra("userName",u)
                startActivity(intent);
            }
        });

        //绑定我喜欢的音乐数目
        musicNumText = getActivity().findViewById(R.id.textView_my_like_music_count);


        //设置登录信息
        textView = getActivity().findViewById(R.id.userName);

        //点击会员信息按钮
        vipImageButton = getActivity().findViewById(R.id.vipImageButton);
        vipImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (textView.getText().equals("欢迎! admin")){
                    Log.d("print","是admin在登录");
                    Intent intent = new Intent(getActivity(), VipServiceActivity.class);
                    intent.putExtra("userName","admin");
                    startActivity(intent);
                }else {
                    Log.d("print", "还未登录，跳转到登录界面");
                    Intent intent = new Intent(getActivity(),LoginActivity.class);
                    startActivity(intent);
                    Toast.makeText(getActivity(), "请先登录您的账号", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //我的收藏按钮
        shouCangButton = getActivity().findViewById(R.id.button3);
        shouCangButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //跳转收藏界面
                if (textView.getText().equals("欢迎! admin")) {
                    Intent intent = new Intent(getActivity(), CollectActivity.class);
                    getActivity().startActivity(intent);
                } else {
                    Toast.makeText(getContext(),"请登录",Toast.LENGTH_SHORT).show();
                }
            }
        });
        //最近播放按钮
        recentButton = getActivity().findViewById(R.id.recent_button);
        recentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), RecentlyPlayerActivity.class);
                Log.e("recent","我进来了最近播放按钮。");
                if (textView.getText().equals("欢迎! admin")) {
                    intent.putExtra("userName","admin");
                    startActivity(intent);
                } else {
                    Toast.makeText(getContext(),"请登录",Toast.LENGTH_SHORT).show();
                }

            }
        });
        //点击我的喜欢跳转到我的收藏
        likeCountButton = getActivity().findViewById(R.id.imageButton3);
        likeCountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),CollectActivity.class);
                if (textView.getText().equals("欢迎! admin")) {
                    intent.putExtra("userName","admin");
                    startActivity(intent);
                } else {
                    Toast.makeText(getContext(),"请登录",Toast.LENGTH_SHORT).show();
                }
            }
        });
        //设置个人信息按钮（可修改密码）
        setImageButton = getActivity().findViewById(R.id.setImageButton);
        setImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ChangePwdActivity.class);
                if (textView.getText().equals("欢迎! admin")) {
                    intent.putExtra("userName","admin");
                    startActivity(intent);
                } else {
                    Toast.makeText(getContext(),"请先登录",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }




    @Override
    public void onClick(View v) {
        fm = getChildFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        switch (v.getId()){
        case R.id.self_button:
            if (selfFragment == null){
                selfFragment = new SelfFragment();
            }
            transaction.replace(R.id.framelayout,selfFragment);
            break;
        case R.id.collect_button:
                if (collectMusicFragment == null){
                    collectMusicFragment = new CollectMusicFragment();
                }
                transaction.replace(R.id.framelayout,collectMusicFragment);
                break;
        }
        transaction.commit();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine, null);
        return view;
    }

    /**
     *  广播接收器
     */
    public class Login_Receiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case "login" :
                    String name = intent.getStringExtra("userName");
                    textView.setText("欢迎! " + name);
                    logButton.setVisibility(View.INVISIBLE);
                    break;

                case "collect" :
                    musicNumText.setText(StaticValue.shouCangNum + " 首");
                    break;

                default:
                    break;
            }

        }
    }
}