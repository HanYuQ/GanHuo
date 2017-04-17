package com.dasu.ganhuo.ui.home;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dasu.ganhuo.R;
import com.dasu.ganhuo.mode.logic.category.GanHuoEntity;
import com.dasu.ganhuo.mode.logic.home.SomedayGanHuoEntity;
import com.dasu.ganhuo.mode.okhttp.GankController;
import com.dasu.ganhuo.ui.view.OnItemClickListener;
import com.dasu.ganhuo.utils.TimeUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by suxq on 2017/4/17.
 */

public class HomeRecycleAdapter extends RecyclerView.Adapter<HomeRecycleAdapter.ViewHolder> {
    private static final String TAG = HomeRecycleAdapter.class.getSimpleName();

    private List<GanHuoEntity> mDataList;
    private OnItemClickListener<GanHuoEntity> mClickListener;
    private Context mContext;


    public HomeRecycleAdapter(SomedayGanHuoEntity data) {
        mDataList = new ArrayList<>();
        if (data.getResults() != null) {
            for (List<GanHuoEntity> l : data.getResults().getAllList()) {
                for (GanHuoEntity g : l) {
                    mDataList.add(g);
                }
            }
        }
    }

    public void setData(SomedayGanHuoEntity data) {
        mDataList = new ArrayList<>();
        for (List<GanHuoEntity> l : data.getResults().getAllList()) {
            for (GanHuoEntity g : l) {
                mDataList.add(g);
            }
        }
        notifyDataSetChanged();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final GanHuoEntity data = mDataList.get(position);
        final int posi = position;
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mClickListener != null) {
                    mClickListener.onItemClick(v, data, posi);
                }
            }
        });
        holder.mTitleTv.setText(data.getDesc());
        holder.mAuthorTv.setText(data.getWho());
        setSource(data.getUrl(), holder.mSourceTv);
        setType(data.getType(), holder.mTypeTv, holder.mSourceTv);
        setDate(data.getPublishedAt(), holder.mDateTv);
    }

    private void setType(String type, TextView tv, TextView sourceTv) {
        if (type.equals(GankController.TYPE_ANDROID)) {
            tv.setBackgroundColor(mContext.getResources().getColor(R.color.green));
        } else if (type.equals(GankController.TYPE_IOS)) {
            tv.setBackgroundColor(mContext.getResources().getColor(R.color.yellow));
        } else if (type.equals(GankController.TYPE_WEB)) {
            tv.setBackgroundColor(mContext.getResources().getColor(R.color.blue));
        } else if (type.equals(GankController.TYPE_APP)) {
            tv.setBackgroundColor(mContext.getResources().getColor(R.color.orange));
        } else if (type.equals(GankController.TYPE_MEIZI)) {
            tv.setBackgroundColor(mContext.getResources().getColor(R.color.red));
        } else if (type.equals(GankController.TYPE_RECOMMENT)) {
            tv.setBackgroundColor(mContext.getResources().getColor(R.color.pink));
        } else if (type.equals(GankController.TYPE_VIDEO)) {
            tv.setBackgroundColor(mContext.getResources().getColor(R.color.deep_blue));
        } else if (type.equals(GankController.TYPE_OTHER)) {
            tv.setBackgroundColor(mContext.getResources().getColor(R.color.brown));
        }
        tv.setText(type);
    }

    private void setSource(String url, TextView tv) {
        tv.setText("博客");
        tv.setBackgroundColor(mContext.getResources().getColor(R.color.teal));
        if (url.contains("github")) {
            tv.setBackgroundColor(mContext.getResources().getColor(R.color.black));
            tv.setText("Github");
        } else if (url.contains("jianshu")) {
            tv.setBackgroundColor(mContext.getResources().getColor(R.color.light_pink));
            tv.setText("简书");
        }
    }

    private void setDate(Date date, TextView tv) {
        String time = TimeUtils.howLongAgo(date.getTime());
        tv.setText(time);
    }


    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView mTitleTv;
        TextView mTypeTv;
        TextView mSourceTv;
        TextView mAuthorTv;
        TextView mDateTv;

        public ViewHolder(View itemView) {
            super(itemView);
            mTitleTv = (TextView) itemView.findViewById(R.id.tv_home_item_title);
            mTypeTv = (TextView) itemView.findViewById(R.id.tv_home_item_type);
            mAuthorTv = (TextView) itemView.findViewById(R.id.tv_home_item_author);
            mDateTv = (TextView) itemView.findViewById(R.id.tv_home_item_date);
            mSourceTv = (TextView) itemView.findViewById(R.id.tv_home_item_source);
        }
    }

    public void setOnItemClickListener(OnItemClickListener<GanHuoEntity> listener) {
        mClickListener = listener;
    }
}