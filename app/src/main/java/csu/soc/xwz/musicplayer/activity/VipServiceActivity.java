package csu.soc.xwz.musicplayer.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import csu.soc.xwz.musicplayer.R;

public class VipServiceActivity extends AppCompatActivity {
    //顶部toolbar
    private Toolbar vipToolbar;
    private Button vipButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vip_service);
        getSupportActionBar().hide();

        vipToolbar = findViewById(R.id.vip_toolbar);

        vipToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName = getIntent().getStringExtra("userName");
                if (userName.equals(null)){
                    Intent intent = new Intent(VipServiceActivity.this,MainActivity.class);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(VipServiceActivity.this,MainActivity.class);
                    intent.putExtra("userName","admin");
                    VipServiceActivity.this.finish();
                    startActivity(intent);
                }

            }
        });
        vipButton = findViewById(R.id.vip_login);
        vipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(VipServiceActivity.this, "服务还在开发中~", Toast.LENGTH_SHORT).show();

            }
        });

    }

}