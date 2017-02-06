package com.touchrom.fanjianzhi.adapter.delegate.common;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.arialyy.absadapter.delegate.AbsDEntity;
import com.arialyy.absadapter.delegate.recycler_view.AbsRvDAdapter;
import com.arialyy.absadapter.delegate.recycler_view.AbsRvDelegation;
import com.arialyy.absadapter.recycler_view.AbsRVHolder;
import com.arialyy.frame.http.HttpUtil;
import com.lyy.ui.widget.CircleImageView;
import com.lyy.ui.widget.IconText;
import com.touchrom.fanjianzhi.R;
import com.touchrom.fanjianzhi.entity.d_entity.CommentEntity;
import com.touchrom.fanjianzhi.help.ImgHelp;
import com.touchrom.fanjianzhi.module.ArtModule;
import com.touchrom.fanjianzhi.net.ServiceUtil;
import com.touchrom.fanjianzhi.widget.ReplayLayout;
import com.touchrom.fanjianzhi.widget.ReplayText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;

/**
 * Created by lyy on 2016/6/29.
 */
public class CommentDelegate extends AbsRvDelegation<CommentEntity, CommentDelegate.ArtCommentHolder> {
    private CommentClickListener mListener;

    public interface CommentClickListener {
        public void onCommentClick(int position, int cmtId, String nikeName);
    }


    public CommentDelegate(Context context, AbsRvDAdapter adapter, int itemType) {
        super(context, adapter, itemType);
    }

    @Override
    public ArtCommentHolder createHolder(View convertView) {
        return new ArtCommentHolder(convertView);
    }

    public void setCommentClickListener(CommentClickListener listener) {
        mListener = listener;
    }

    @Override
    public void bindData(final int position, ArtCommentHolder holder, CommentEntity item) {
        ImgHelp.setImg(getContext(), item.getImgUrl(), holder.img);
        holder.nikeName.setText(item.getNikeName());
        holder.time.setText(item.getTime());
        holder.leave.setText(item.getGrade());
        holder.star.setText(item.getStarNum() + "");
        holder.star.setLeftDrawable(item.isStared() ? R.mipmap.icon_stared : R.mipmap.icon_star);
        holder.follow.setText(item.getFlow());
        holder.comment.setVisibility(View.VISIBLE);
        holder.content.setText(item.getContent());
        holder.comment.setText(item.getCommentNum() + "");

        List<CommentEntity.ChildCommentEntity> replays = item.getComments();
        if (replays != null && replays.size() > 0) {
            holder.line.setVisibility(View.VISIBLE);
            holder.list.setVisibility(View.VISIBLE);
            holder.list.setContent(replays);
            ReplayLayout.OnItemClickListener listener = (ReplayLayout.OnItemClickListener) holder.list.getTag(holder.list.getId());
            if (listener == null) {
                listener = new ReplayLayout.OnItemClickListener() {
                    @Override
                    public void onItemClick(ReplayText text) {
                        if (mListener != null) {
                            CommentEntity.ChildCommentEntity entity = text.getEntity();
                            mListener.onCommentClick(position, entity.getChildCommentId(), entity.getChildNikeName());
                        }
                    }
                };
                holder.list.setTag(holder.list.getId(), listener);
            }
            holder.list.setOnItemClickListener(listener);
        } else {
            holder.line.setVisibility(View.GONE);
            holder.list.setVisibility(View.GONE);
        }

        ClickListener listener = (ClickListener) holder.nikeName.getTag(holder.nikeName.getId());
        if (listener == null) {
            listener = new ClickListener();
            holder.nikeName.setTag(holder.nikeName.getId(), listener);
        }
        listener.setData(position, item);
        holder.nikeName.setOnClickListener(listener);
        holder.content.setOnClickListener(listener);
        holder.star.setOnClickListener(listener);
    }

    @Override
    public int setLayoutId() {
        return R.layout.item_art_comment;
    }

    public void updateReplay(int position, CommentEntity entity) {
        AbsDEntity dEntity = (AbsDEntity) getAdapter().getData().get(position);

        if (dEntity instanceof CommentEntity) {
            CommentEntity commentEntity = ((CommentEntity) dEntity);
            List<CommentEntity.ChildCommentEntity> list = commentEntity.getComments();
            if (list == null) {
                list = new ArrayList<>();
                commentEntity.setComments(list);
            } else {
                list.clear();
            }
            list.addAll(entity.getComments());
            getAdapter().notifyDataSetChanged();
//            notifyItemChanged(position + 1);
        }

    }

    /**
     * 点赞回调
     */
    private class StarResponse extends HttpUtil.AbsResponse {

        CommentEntity entity;

        public StarResponse(CommentEntity entity) {
            this.entity = entity;
        }

        @Override
        public void onResponse(String data) {
            super.onResponse(data);
            boolean success = false;
            try {
                JSONObject obj = new JSONObject(data);
                if (obj.getInt("code") == 200) {
                    JSONObject content = obj.getJSONObject(ServiceUtil.DATA_KEY);
                    success = content.getBoolean("result");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (success) {
                    int num = entity.getStarNum() + 1;
                    entity.setStarNum(num);
                    entity.setStared(true);
                    getAdapter().notifyDataSetChanged();
                }
            }
        }
    }


    class ClickListener implements View.OnClickListener {
        private CommentEntity entity;
        private int position;

        public void setData(int position, CommentEntity entity) {
            this.entity = entity;
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            ArtModule module = new ArtModule(getContext());
            int id = v.getId();
            if (id == R.id.star) {
                module.star(entity.getCommentId(), 1, new StarResponse(entity));
            } else if (id == R.id.content) {
                if (mListener != null) {
                    mListener.onCommentClick(position, entity.getCommentId(), entity.getNikeName());
                }
            }
        }
    }

    class ArtCommentHolder extends AbsRVHolder {
        @InjectView(R.id.img)
        CircleImageView img;
        @InjectView(R.id.nike_name)
        TextView nikeName;
        @InjectView(R.id.leave)
        TextView leave;
        @InjectView(R.id.time)
        TextView time;
        @InjectView(R.id.star)
        IconText star;
        @InjectView(R.id.comment)
        IconText comment;
        @InjectView(R.id.flow)
        TextView follow;
        @InjectView(R.id.content)
        TextView content;
        @InjectView(R.id.line)
        View line;
        @InjectView(R.id.replay)
        ReplayLayout list;

        public ArtCommentHolder(View itemView) {
            super(itemView);
        }
    }
}
