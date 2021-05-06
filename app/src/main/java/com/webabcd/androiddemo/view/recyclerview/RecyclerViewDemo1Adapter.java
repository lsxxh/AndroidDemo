/**
 * 自定义 RecyclerView.Adapter
 *
 * 适配器中包含了数据和项模板
 */

package com.webabcd.androiddemo.view.recyclerview;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.webabcd.androiddemo.R;

import java.util.List;

public class RecyclerViewDemo1Adapter extends RecyclerView.Adapter<RecyclerViewDemo1Adapter.ViewHolder>
{
    private final String LOG_TAG = "RecyclerView.Adapter";

    private List<MyData> _myDataList;

    public  RecyclerViewDemo1Adapter(List<MyData> myDataList) {
        _myDataList = myDataList;
    }

    // 创建自定义 ViewHolder 实例
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // 构造自定义 ViewHolder
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_recyclerview_recyclerviewdemo1, parent, false);
        ViewHolder holder = new ViewHolder(view);

        // item 中的 imgLogo 控件的点击事件
        holder.imgLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                Toast.makeText(view.getContext(), "image click: " + position, Toast.LENGTH_SHORT).show();
            }
        });

        // item 本身的点击事件
        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                Toast.makeText(view.getContext(), "item click: " + position, Toast.LENGTH_SHORT).show();
            }
        });

        // item 本身的长按事件
        holder.container.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                int position = holder.getAdapterPosition();
                Toast.makeText(view.getContext(), "item long click: " + position, Toast.LENGTH_SHORT).show();

                return true;
            }
        });

        return holder;
    }

    // 每次绘制 item 时都会调用 onBindViewHolder()，需要在此为 item 中的控件赋值
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // 多多上下滚动 RecyclerView 来了解一下调用 onBindViewHolder() 的时机
        Log.d(LOG_TAG, String.format("onBindViewHolder: %d", position));

        MyData myData = _myDataList.get(position);
        holder.imgLogo.setImageResource(myData.getLogoId());
        holder.txtName.setText(myData.getName());
        holder.txtComment.setText(myData.getComment());
    }

    // 需要呈现的 item 的总数
    @Override
    public int getItemCount() {
        return _myDataList.size();
    }

    // 自定义 RecyclerView.ViewHolder
    class ViewHolder extends RecyclerView.ViewHolder{

        private View container;
        private ImageView imgLogo;
        private TextView txtName;
        private TextView txtComment;

        public ViewHolder (View view)
        {
            super(view);

            container = view;
            imgLogo = view.findViewById(R.id.imgLogo);
            txtName = view.findViewById(R.id.txtName);
            txtComment = view.findViewById(R.id.txtComment);
        }

    }
}
