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

public class RegisterActivity extends Activity {

    private TextView mTitle;    //标题
    private TextView mBack;     //返回按钮
    private Button mRegister;     //注册按钮
    private EditText mName, mPwd, mPwdAgain;     //用户名，密码，再次输入的密码
    private String userName, pwd, pwdAgain;    //用户名，密码，再次输入的密码 值

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initView();   //初始化界面

        initEvent();  //初始化事件
    }

    private void initView(){
        mTitle = this.findViewById(R.id.tv_main_title);
        mBack = this.findViewById(R.id.tv_back);
        mRegister = this.findViewById(R.id.btn_register);
        mName = this.findViewById(R.id.et_user_name);
        mPwd = this.findViewById(R.id.et_psw);
        mPwdAgain = this.findViewById(R.id.et_psw_again);

        mTitle.setText("注册");
    }

    private void initEvent(){

        //返回按钮
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RegisterActivity.this.finish();
            }
        });

        //注册按钮
        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //获取输入框中的值
                userName = mName.getText().toString().trim();
                pwd = mPwd.getText().toString().trim();
                pwdAgain = mPwdAgain.getText().toString().trim();

                //先判断输入框是否有值
                if (TextUtils.isEmpty(userName)) {
                    Toast.makeText(RegisterActivity.this, "请输入用户名", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(pwd)) {
                    Toast.makeText(RegisterActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(pwdAgain)) {
                    Toast.makeText(RegisterActivity.this, "请再次输入密码", Toast.LENGTH_SHORT).show();
                    return;
                }

                //判断两次输入的密码是否相同
                if (!pwd.equals(pwdAgain)) {
                    Toast.makeText(RegisterActivity.this, "输入两次的密码不一样", Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    Toast.makeText(RegisterActivity.this, "注册成功！", Toast.LENGTH_SHORT).show();
                    RegisterActivity.this.finish();
                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    intent.putExtra("userName",userName);   //把用户信息传递到主界面
                    //跳转到主界面，状态传递到 MainActivity 中
                    startActivity(intent);
                }

                //查询数据库看是否已经存在？
            }
        });
    }
}
