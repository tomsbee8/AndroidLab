package cn.blinkdagger.androidLab.ui.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import cn.blinkdagger.androidLab.entity.MainItem;
import cn.blinkdagger.androidLab.R;

public class MainAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<MainItem> dataSourceList;
    private OnMainItemClickListener onItemClickListener;


    public MainAdapter(Context context, List<MainItem> dataSourceList) {
        this.context = context;
        this.dataSourceList = dataSourceList;
    }

    public void setOnItemClickListener(OnMainItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View convertView = LayoutInflater.from(context).inflate(R.layout.item_main_list_layout, parent, false);
        return new InnerViewHolder(convertView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final MainItem entity = dataSourceList.get(position);
        final InnerViewHolder viewHolder = (InnerViewHolder) holder;
        viewHolder.itemMainTV.setText(entity.getItemName());
        int colorPosition = viewHolder.getAdapterPosition() % 4;
        if (colorPosition == 1) {
            viewHolder.itemCardView.setCardBackgroundColor(context.getResources().getColor(R.color.main_color_1));
        } else if (colorPosition == 2) {
            viewHolder.itemCardView.setCardBackgroundColor(context.getResources().getColor(R.color.main_color_2));
        } else if (colorPosition == 3) {
            viewHolder.itemCardView.setCardBackgroundColor(context.getResources().getColor(R.color.main_color_3));
        } else if (colorPosition == 0) {
            viewHolder.itemCardView.setCardBackgroundColor(context.getResources().getColor(R.color.main_color_4));
        }
        viewHolder.itemCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(viewHolder.getAdapterPosition());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataSourceList.size();
    }

    class InnerViewHolder extends RecyclerView.ViewHolder {

        public CardView itemCardView;
        public TextView itemMainTV;


        public InnerViewHolder(View itemView) {
            super(itemView);
            itemCardView = (CardView) itemView.findViewById(R.id.item_main_card_view);
            itemMainTV = (TextView) itemView.findViewById(R.id.item_main_tv);
        }
    }

    public interface OnMainItemClickListener {
        void onItemClick(int position);
    }
}