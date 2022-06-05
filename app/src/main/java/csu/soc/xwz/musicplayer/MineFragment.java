package csu.soc.xwz.musicplayer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import csu.soc.xwz.musicplayer.MineFragmentFolder.CollectMusicFragment;
import csu.soc.xwz.musicplayer.MineFragmentFolder.SelfBuildMusicFragment;


public class MineFragment extends Fragment implements View.OnClickListener{
    private LayoutInflater inflater;

    private CollectMusicFragment collectMusicFragment;
    private SelfBuildMusicFragment selfBuildMusicFragment;
    private Context mainActivity;
    private Button logButton;
    private TextView textView;

    private RadioGroup twoGrop;
    protected RadioButton selfButton,collectButton;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mainActivity = getActivity();
        inflater = LayoutInflater.from(getActivity());
        //初始化控件
        twoGrop = (RadioGroup) getActivity().findViewById(R.id.redio);
        selfButton =(RadioButton) getActivity().findViewById(R.id.self_button);
        collectButton =(RadioButton) getActivity().findViewById(R.id.collect_button);

        selfButton.setOnClickListener(this);
        collectButton.setOnClickListener(this);

        setDefaultFragment();

        //设置登录按钮点击事件
        logButton = getActivity().findViewById(R.id.unLogTip);
        logButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });

        textView = getActivity().findViewById(R.id.userName);
        if (getActivity().getIntent().hasExtra("userName")){
            textView.setText("欢迎 admin!");
            logButton.setVisibility(View.INVISIBLE);
        }else {
            textView.setText(R.string.mine_un_log);
        }
    }
    // 设置默认fragment
    private void setDefaultFragment() {
        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        selfBuildMusicFragment = new SelfBuildMusicFragment();
        transaction.add(R.id.framelayout,selfBuildMusicFragment).commit();
    }


    @Override
    public void onClick(View v) {
        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();

        switch (v.getId()){
            case R.id.self_button:
                if (selfBuildMusicFragment == null){
                    selfBuildMusicFragment = new SelfBuildMusicFragment();
                }
                transaction.replace(R.id.framelayout,selfBuildMusicFragment);
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
}