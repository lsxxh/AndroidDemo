/**
 * RecyclerView 基础，各种排列方式（垂直排列，水平排列，标准网格排列，错列网格排列），响应单击事件和长按事件
 *
 * 本例演示
 * 1、通过 RecyclerView.Adapter 显示数据
 * 2、线性排列方式，可纵向可横向
 * 3、标准网格排列方式（每个格子的宽高是一样的），可纵向可横向
 * 4、错列网格排列方式（每个格子的宽高可能是不一样的），可纵向可横向
 * 5、响应单击事件和长按事件
 *
 *
 * 注：
 * 1、需要在 app 的 build.gradle 中配置好 implementation 'androidx.recyclerview:recyclerview:x.x.x'
 * 2、本例使用的自定义 RecyclerView.Adapter 请参见 RecyclerViewDemo1Adapter.java
 */

package com.webabcd.androiddemo.view.recyclerview;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.webabcd.androiddemo.R;

import java.util.List;

public class RecyclerViewDemo1 extends AppCompatActivity {

    private RecyclerView _recyclerView;
    private Button _button1;
    private Button _button2;
    private Button _button3;
    private Button _button4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_recyclerview_recyclerviewdemo1);

        _recyclerView = findViewById(R.id.recyclerView1);
        _button1 = findViewById(R.id.button1);
        _button2 = findViewById(R.id.button2);
        _button3 = findViewById(R.id.button3);
        _button4 = findViewById(R.id.button4);

        sample();
    }

    private void sample() {
        // 构造数据
        List<MyData> myDataList = MyData.getDataList();
        // 实例化自定义的 RecyclerView.Adapter
        RecyclerViewDemo1Adapter adapter = new RecyclerViewDemo1Adapter(myDataList);
        // 设置 RecyclerView 的 RecyclerView.Adapter
        _recyclerView.setAdapter(adapter);

        _button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 最经典的排列方式，线性垂直排列
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(RecyclerViewDemo1.this);
                _recyclerView.setLayoutManager(linearLayoutManager);
            }
        });

        _button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // LinearLayoutManager - 线性排列
                //   第 2 个参数 - 用于指定线性排列的方式，垂直或水平
                //   第 3 个参数 - 当需要显示更多数据时，需要滑动的方向
                //     false - 通过下滑或右滑来显示更多的数据
                //     true - 通过上滑或左滑来显示更多的数据
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(RecyclerViewDemo1.this, LinearLayoutManager.HORIZONTAL, false);
                _recyclerView.setLayoutManager(linearLayoutManager);
            }
        });

        _button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // GridLayoutManager - 标准网格排列
                //   第 2 个参数 - 用于指定网格的行数或列数
                //   第 3 个参数 - 用于指定网格是垂直排列还是水平排列
                //   第 4 个参数 - 当需要显示更多数据时，需要滑动的方向
                //     false - 通过下滑或右滑来显示更多的数据
                //     true - 通过上滑或左滑来显示更多的数据
                GridLayoutManager gridLayoutManager = new GridLayoutManager(RecyclerViewDemo1.this, 3, LinearLayoutManager.VERTICAL, false);
                _recyclerView.setLayoutManager(gridLayoutManager);
            }
        });

        _button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // StaggeredGridLayoutManager - 错列网格排列
                //   第 1 个参数 - 用于指定网格的行数或列数
                //   第 2 个参数 - 用于指定网格是垂直排列还是水平排列
                //
                // 什么是错列网格排列呢？用下面这个例子说明
                // 这个例子是固定 3 列，垂直排列的，在这个前提下使用错列网格排列后，他的每个网格的宽度是一样的，但是高度可以不一样（当 wrap_content 时，其可以根据内容的多少来决定高度）
                StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.HORIZONTAL);
                _recyclerView.setLayoutManager(staggeredGridLayoutManager);
            }
        });
    }
}