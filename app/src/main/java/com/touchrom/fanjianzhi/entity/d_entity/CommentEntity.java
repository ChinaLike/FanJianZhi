package com.touchrom.fanjianzhi.entity.d_entity;

import com.arialyy.absadapter.delegate.AbsDEntity;
import com.touchrom.fanjianzhi.base.BaseEntity;
import com.touchrom.fanjianzhi.config.Constance;

import java.util.List;

/**
 * Created by lyy on 2016/6/12.
 * 评论实体
 */
public class CommentEntity extends AbsDEntity {
    String imgUrl;
    String nikeName;
    String grade;
    String content;
    String time;
    int starNum;
    int commentNum;
    int userId;
    int commentId;
    String flow;
    boolean isStared = false;

    @Override
    public int getAbsType() {
        return Constance.ADAPTER.COMMENT;
    }

    List<ChildCommentEntity> comments;

    public void setStared(boolean stared) {
        isStared = stared;
    }

    public void setComments(List<ChildCommentEntity> comments) {
        this.comments = comments;
    }

    public boolean isStared() {
        return isStared;
    }

    public void setStarNum(int starNum) {
        this.starNum = starNum;
    }

    public void setCommentNum(int commentNum) {
        this.commentNum = commentNum;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public String getNikeName() {
        return nikeName;
    }

    public String getGrade() {
        return grade;
    }

    public String getContent() {
        return content;
    }

    public String getTime() {
        return time;
    }

    public int getStarNum() {
        return starNum;
    }

    public int getCommentNum() {
        return commentNum;
    }

    public int getUserId() {
        return userId;
    }

    public int getCommentId() {
        return commentId;
    }

    public String getFlow() {
        return flow;
    }

    public List<ChildCommentEntity> getComments() {
        return comments;
    }

    public class ChildCommentEntity extends BaseEntity {
        String childImgUrl;
        String childNikeName;
        String childGrade;
        String childContent;
        String childTime;
        int childCommentId;
        int childUserId;
        int childStarNum;
        int byReplayUserId;
        String byReplayUserName;
        boolean isStared;

        public boolean isStared() {
            return isStared;
        }

        public void setStared(boolean stared) {
            isStared = stared;
        }

        public void setChildStarNum(int childStarNum) {
            this.childStarNum = childStarNum;
        }

        public String getChildImgUrl() {
            return childImgUrl;
        }

        public String getChildNikeName() {
            return childNikeName;
        }

        public String getChildGrade() {
            return childGrade;
        }

        public String getChildContent() {
            return childContent;
        }

        public String getChildTime() {
            return childTime;
        }

        public int getChildCommentId() {
            return childCommentId;
        }

        public int getChildUserId() {
            return childUserId;
        }

        public int getChildStarNum() {
            return childStarNum;
        }

        public int getByReplayUserId() {
            return byReplayUserId;
        }

        public String getByReplayUserName() {
            return byReplayUserName;
        }
    }

}
