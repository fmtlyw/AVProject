package com.lyw.avproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 功能描述:
 * Created on 2021/6/18.
 *
 * @author lyw
 *
 * 1、 PartAdapter extends RecyclerView.Adapter
 * 2、创建PartViewHold 内部类 继承RecyclerView.ViewHolder
 */
public class PartAdapter extends RecyclerView.Adapter<PartAdapter.PartViewHold> {

    private Context mContext;
    private List<PartBeen> mPartList;
    private OnPartItemClickListener mListener;

    public PartAdapter(Context context, List<PartBeen> partList) {
        mContext = context;
        this.mPartList = partList;
    }

    @NonNull
    @Override
    public PartViewHold onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        /**
         * return new PartViewHold(LayoutInflater.from(mContext).inflate(R.layout.adapter_part_one,null));
         * 如果写成这样，item的布局不受控制，宽度明明设置match_parent，但却显示包裹状态
         */
        return new PartViewHold(LayoutInflater.from(mContext).inflate(R.layout.adapter_part_one,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull PartViewHold holder, int position) {
        final PartBeen partBeen = mPartList.get(position);
        holder.partName.setText(partBeen.getPartName());
        holder.partLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener!= null) {
                    mListener.onPartItem(partBeen.getPartName(),partBeen.getPath());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mPartList.size();
    }

    class PartViewHold extends RecyclerView.ViewHolder {
        private TextView partName;
        private LinearLayout partLl;
        public PartViewHold(@NonNull View itemView) {
            super(itemView);
            partName = itemView.findViewById(R.id.part_name);
            partLl = itemView.findViewById(R.id.part_ll);
        }
    }

    interface OnPartItemClickListener{
        void onPartItem(String partName,String path);
    }

    public void  setOnPartItemClickListener(OnPartItemClickListener mListener){
        this.mListener = mListener;
    }
}
