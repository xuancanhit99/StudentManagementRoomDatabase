package com.xuancanh.studentmanager;

import android.view.View;


//Click for RecycleView
public interface ItemClickListener {
    void onClick(View view, int position, boolean isLongClick);
}
