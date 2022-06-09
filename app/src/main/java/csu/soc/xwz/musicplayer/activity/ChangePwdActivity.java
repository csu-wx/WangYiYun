package csu.soc.xwz.musicplayer.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import csu.soc.xwz.musicplayer.R;


/**
 * 密码修改
 */
public class ChangePwdActivity extends Activity {

    private EditText mUserName;  //账户
    private EditText mPassword;  //密码
    private EditText mNewPwd;  //新密码
    private EditText mNewPwdAgain;  //重新输入的新密码
    private TextView mTitle;  //标题
    private Button mChangePwd;  //确认修改按钮
    private TextView mBack;  //返回按钮

    private String userName, password, newPwd, newPwdAgain;

    /**
     * 创建默认执行事件
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pwd);

        initView(); //初始化界面

        initEvent();  //初始化事件
    }

    /**
     * 初始化，绑定界面控件
     */
    private void initView(){
        mUserName = this.findViewById(R.id.et_user_name);
        mPassword = this.findViewById(R.id.et_psw);
        mNewPwd = this.findViewById(R.id.et_new_psw);
        mNewPwdAgain = this.findViewById(R.id.et_new_psw_again);
        mTitle = this.findViewById(R.id.tv_main_title);
        mChangePwd = this.findViewById(R.id.btn_change_psw);
        mBack = this.findViewById(R.id.tv_back);

        mTitle.setText("修改密码");
    }

    /**
     * 初始化事件，为按钮设置监听
     */
    private void initEvent(){
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChangePwdActivity.this.finish();
            }
        });

        mChangePwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userName = mUserName.getText().toString().trim();
                password = mPassword.getText().toString().trim();
                newPwd = mNewPwd.getText().toString().trim();
                newPwdAgain = mNewPwdAgain.getText().toString().trim();

                if (TextUtils.isEmpty(userName)) {
                    Toast.makeText(ChangePwdActivity.this, "请输入用户名", Toast.LENGTH_SHORT).show();
                    return;
                } else if(TextUtils.isEmpty(password)){
                    Toast.makeText(ChangePwdActivity.this, "请输入原密码", Toast.LENGTH_SHORT).show();
                    return;
                }else if (TextUtils.isEmpty(newPwd)){
                    Toast.makeText(ChangePwdActivity.this, "请输入新密码",Toast.LENGTH_SHORT).show();
                    return;
                }else if(TextUtils.isEmpty(newPwdAgain)){
                    Toast.makeText(ChangePwdActivity.this, "请再次输入新密码", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(!newPwd.equals(newPwdAgain)){
                    Toast.makeText(ChangePwdActivity.this, "两次新密码输入不一致", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(newPwd.equals(password)){
                    Toast.makeText(ChangePwdActivity.this, "新密码与原密码相同", Toast.LENGTH_SHORT).show();
                }

                changePwdThread(); //修改密码线程;
            }
        });
    }

    /**
     * 修改密码的线程（匿名创建）
     */
    private void changePwdThread(){
        new Thread(){
            public void run(){
                Toast.makeText(ChangePwdActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                ChangePwdActivity.this.finish();
                Intent intent = new Intent(ChangePwdActivity.this, MainActivity.class);
                intent.putExtra("userName",userName);   //把用户信息传递到主界面
                //跳转到主界面，状态传递到 MainActivity 中
                startActivity(intent);
            }
        }.start();
    }
}
