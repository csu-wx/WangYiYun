package csu.soc.xwz.musicplayer.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import csu.soc.xwz.musicplayer.R;
import csu.soc.xwz.musicplayer.service.MusicService;


//public class LoginActivity extends AppCompatActivity {
public class LoginActivity extends Activity {


    private TextView mMainTitle;   //主标题
    private TextView mBack;   //返回
    private TextView mRegister;  //注册
    private TextView mChangePwd;  //修改密码
    private Button mLogin;   //登录按钮
    private EditText mUserName;  //输入账户
    private EditText mPwd;   //输入密码
    private String userName, pwd;     //登录所需的账户和密码
    private LocalBroadcastManager localBroadcastManager;

    /**
     * 界面创建自动执行
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();  //初始化控件

        initEvent();  //初始化相关事件

        //注册应用内广播接收器
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
    }

    /**
     * 为按钮点击事件创建监听
     */
    private void initEvent(){

        //监听返回键的点击事件
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //登录界面销毁
                LoginActivity.this.finish();
            }
        });

        //注册按钮
        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                //跳转到注册界面
                Intent intent=new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        //修改密码按钮
        mChangePwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //跳转到修改密码界面
                Intent intent = new Intent(LoginActivity.this, ChangePwdActivity.class);
                startActivity(intent);
            }
        });

        //登录按钮
        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userName=mUserName.getText().toString().trim();  //获取用户名。.trim是为了去除字符串两侧对于空格
                pwd=mPwd.getText().toString().trim();  //获取登录密码

                //检测是否为空字符串
                if(TextUtils.isEmpty(userName)){
                    Toast.makeText(LoginActivity.this, "请输入用户名", Toast.LENGTH_SHORT).show();
                    return;
                }else if(TextUtils.isEmpty(pwd)){
                    Toast.makeText(LoginActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
                    return;
                }else if (userName.equals("admin")&&pwd.equals("123")){
                    //密码正确
                    Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                    //传递数据
                    Intent intent = new Intent("login");
                    intent.putExtra("userName",userName);   //把用户信息传递到主界面
                    localBroadcastManager.sendBroadcast(intent);
                    //销毁登录界面
                    LoginActivity.this.finish();
                }

//                loginThread();
            }
        });
    }

    /**
     * 初始化界面，绑定控件
     */
    private void initView(){
        mMainTitle = this.findViewById(R.id.tv_main_title);
        mBack = this.findViewById(R.id.tv_back);
        mRegister = this.findViewById(R.id.tv_register);
        mChangePwd = this.findViewById(R.id.tv_change_psw);
        mLogin = this.findViewById(R.id.btn_login);
        mUserName = this.findViewById(R.id.et_user_name);
        mPwd = this.findViewById(R.id.et_psw);

        mMainTitle.setText("登录");
    }


}
