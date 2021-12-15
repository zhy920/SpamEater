package io.spameater.ui.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import io.spameater.MsgVo;
import io.spameater.R;
import io.spameater.ui.SideBarScrollView;

public class AdapterSideBar extends RecyclerView.Adapter<AdapterSideBar.ViewHolder> implements SideBarScrollView.Callback {

    private Context context;
    private List<MsgVo> mDatas = new ArrayList<MsgVo>();

    private RecyclerView mRecyclerView;

    public AdapterSideBar(Context context, List<MsgVo> mDatas) {
            this.context = context;
            this.mDatas = mDatas;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_view_homeitem, viewGroup, false);
            return new ViewHolder(view);
    }

    /**
     * 将recyclerView绑定Slide事件
     *
     * @param recyclerView
     */
    @Override
    public void onAttachedToRecyclerView( RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mRecyclerView = recyclerView;
        mRecyclerView.addOnItemTouchListener(new SideBarScrollView(mRecyclerView.getContext(), this));
    }

    @Override
    public void onBindViewHolder(  ViewHolder holder, final int position) {
            /**
             * 消息状态
             */
        if (mDatas.get(position).isChecked()) {
            holder.msgRemindPoint.setBackgroundResource(R.drawable.shape_remind_point_gray);
        } else {
            holder.msgRemindPoint.setBackgroundResource(R.drawable.shape_remind_point_theme);
        }
        //消息标题
        holder.tvRemindTitle.setText(mDatas.get(position).getTitle());
        //消息内容
        holder.tvRemindContent.setText(mDatas.get(position).getContent());

        /**
         * -->特别注意，敲黑板了啊！！！在执行notify的时候，取position要取holder.getAdapterPosition()，
         * 消息被删除之后，他原来的position是final的，所以取到的值不准确，会报数组越界。
         */

        //消息主体监听，这里我是让他添加一条数据，替换成你需要的操作即可
        holder.llMsgRemindMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        //标记已读监听
        holder.tvMsgRemindCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDatas.get(holder.getAdapterPosition()).setChecked(true);
                notifyItemChanged(holder.getAdapterPosition());
            }
        });
        //删除监听
        holder.tvMsgRemindDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeData(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    /**
     * 此方法用来计算水平方向移动的距离
     *
     * @param holder
     * @return
     */
    @Override
    public int getHorizontalRange(RecyclerView.ViewHolder holder) {
        if (holder.itemView instanceof LinearLayout) {
            ViewGroup viewGroup = (ViewGroup) holder.itemView;
            //viewGroup包含3个控件，即消息主item、标记已读、删除，返回为标记已读宽度+删除宽度
            return viewGroup.getChildAt(1).getLayoutParams().width + viewGroup.getChildAt(2).getLayoutParams().width;
        }
        return 0;
    }

    @Override
    public RecyclerView.ViewHolder getChildViewHolder(View childView) {
        return mRecyclerView.getChildViewHolder(childView);
    }

    @Override
    public View findTargetView(float x, float y) {
        return mRecyclerView.findChildViewUnder(x, y);
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        public final View msgRemindPoint;
        public final TextView tvRemindTitle;
        public final TextView tvRemindContent;
        public final LinearLayout llMsgRemindMain;
        public final TextView tvMsgRemindCheck;
        public final TextView tvMsgRemindDelete;
        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            msgRemindPoint = view.findViewById(R.id.msg_remind_point);
            tvRemindTitle= view.findViewById(R.id.tv_remind_title);
            tvRemindContent= view.findViewById(R.id.tv_remind_content);
            llMsgRemindMain= view.findViewById(R.id.ll_msg_remind_main);
            tvMsgRemindCheck = view.findViewById(R.id.tv_msg_remind_check);
            tvMsgRemindDelete = view.findViewById(R.id.tv_msg_remind_delete);
        }
    }
    /**
     * 添加单条数据
     *
     * @param position
     */
    public void addData(int position,String addr,String body) {
        MsgVo vo = new MsgVo();

        vo.setChecked(true);
        vo.setTitle(addr);
        vo.setContent(body);
        if (position==-1)
            position=mDatas.size();
        mDatas.add(position, vo);
        notifyItemInserted(position);
    }

    /**
     * 删除单条数据
     *
     * @param position
     */
    public void removeData(int position) {
        mDatas.remove(position);
        notifyItemRemoved(position);
    }

}