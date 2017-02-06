package com.touchrom.fanjianzhi.adapter.delegate.art_list_content;

import com.touchrom.fanjianzhi.entity.d_entity.ImgTextEntity;
import com.touchrom.fanjianzhi.widget.ItemBottomBar;

/**
 * Created by lyy on 2016/6/6.
 */
class BottomBarHelp {

    public static void handleBottomBar(ItemBottomBar bottomBar, ImgTextEntity entity){
        bottomBar.setTime(entity.getTime());
        bottomBar.setStar(entity.getStarNum() + "");
        bottomBar.setUnStar(entity.getUnStarNum() + "");
        bottomBar.setComment(entity.getCommentNum() + "");
    }
}
