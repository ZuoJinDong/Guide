package com.zjd.guide;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private GuideView guideView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        guideView= (GuideView) findViewById(R.id.guideView);
        guideView.setVisibility(View.VISIBLE);

        List<ViewBean> viewBeanList=new ArrayList<>();
        viewBeanList.add(new ViewBean(findViewById(R.id.tv1),"这是一个TextView"));
        viewBeanList.add(new ViewBean(findViewById(R.id.btn),"这是一个按钮"));
        viewBeanList.add(new ViewBean(findViewById(R.id.iv),"这是一张图片"));
        viewBeanList.add(new ViewBean(findViewById(R.id.et),"这是一个输入框"));
        viewBeanList.add(new ViewBean(findViewById(R.id.ib),"这是一个图片按键"));
        viewBeanList.add(new ViewBean(findViewById(R.id.cb),"这是一个选择框"));

        guideView.setViews(viewBeanList);

        guideView.setOnFinishListener(new GuideView.OnFinishListener() {
            @Override
            public void isFinish(boolean isFinish) {
                Toast.makeText(getApplicationContext(),"结束",Toast.LENGTH_SHORT).show();
                guideView.setVisibility(View.GONE);
            }
        });
    }

    public void onClick(View view) {
        guideView.setReStart();
        guideView.setVisibility(View.VISIBLE);
    }
}
