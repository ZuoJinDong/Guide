package com.zjd.guide;

import android.view.View;

import java.io.Serializable;

/**
 * Created by 左金栋 on 2017/9/19.
 */

public class ViewBean implements Serializable {
    private View view;
    private String content;

    public ViewBean() {
    }

    public ViewBean(View view, String content) {
        this.view = view;
        this.content = content;
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
