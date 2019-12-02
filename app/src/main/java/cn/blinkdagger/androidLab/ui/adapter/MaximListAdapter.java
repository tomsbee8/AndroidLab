package cn.blinkdagger.androidLab.ui.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cn.blinkdagger.androidLab.entity.RemarkItem;
import cn.blinkdagger.androidLab.R;

import java.util.List;

public class MaximListAdapter extends  RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private Context context;
    private List<RemarkItem> dataSourceList;

    public MaximListAdapter(Context context, List<RemarkItem> dataSourceList) {
        this.context = context;
        this.dataSourceList = dataSourceList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View convertView = LayoutInflater.from(context).inflate(R.layout.item_maxim_list_layout, parent,false);
        return new InnerViewHolder(convertView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final RemarkItem entity = dataSourceList.get(position);
        InnerViewHolder viewHolder = (InnerViewHolder) holder;
        viewHolder.position = position;
        viewHolder.keyTV.setText(entity.getTypeId()+"：");
        viewHolder.valueTV.setText(entity.getTypeName());
    }

    @Override
    public int getItemCount() {
        return dataSourceList.size();
    }

    class InnerViewHolder extends RecyclerView.ViewHolder {

        public int position;
        public TextView keyTV;
        public TextView valueTV;


        public InnerViewHolder(View itemView) {
            super(itemView);
            keyTV = (TextView) itemView.findViewById(R.id.item_maxim_id_tv);
            valueTV = (TextView) itemView.findViewById(R.id.item_maxim_name_tv);
        }
    }
}