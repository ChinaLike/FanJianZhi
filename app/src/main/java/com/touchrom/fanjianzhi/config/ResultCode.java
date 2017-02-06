package com.touchrom.fanjianzhi.config;

/**
 * Created by lyy on 2015/11/9.
 * 方法返回码
 */
public class ResultCode {


    //ACTIVITY请求码
    public class ACTIVITY {
        public static final int GD_COMMENT = 0xcc0; //游戏详情bar请求码
        public static final int FIRST_LAUNCHER = 0xcc1; //游戏详情bar请求码
    }

    //对话框相关
    public class DIALOG {
        public static final int MSG_DIALOG_ENTER = 0xaa1;         //提示对话框确认
        public static final int MSG_DIALOG_CANCEL = 0xaa2;         //提示对话框确认
        public static final int AM_ITEM_FUNCTION = 0xaa3;         //应该管理点击弹出对话框
        public static final int UPGRADE = 0xaa4;         //升级对话框的
        public static final int GUIDE = 0xaa5;         //欢迎页面
        public static final int DOWNLOAD = 0xaa6;         //下载对话框
        public static final int IMG_CHOOSE = 0xaa7;         //图片旋转方式对话框
        public static final int ERROR_TEMP_DIALOG = 0xaa8;         //错误填充对话框
        public static final int IMG_CODE_DIALOG_ENTER = 0xaa9;  //图片验证码确认
        public static final int IMG_CODE_DIALOG_CANCEL = 0xaaa;  //图片验证码取消
    }


    //后台接口相关
    public class SERVER {
        public static final int GET_SHARE_DATA = 0xbb0; //分享数据
        public static final int GET_TAB_DATA = 0xbb1;   //首页tab数据
        public static final int GET_MAIN_ART_LIST = 0xbb3;   //首页列表数据
        public static final int GET_CHANNEL_ART_LIST = 0xbb4;   //频道列表数据
        public static final int GET_ART_DETAIL = 0xbb5;   //获取文章详情
        public static final int LOGIN = 0xbb6;   //登录
        public static final int CONFIRM_MSG_CODE = 0xbb7;   //验证短信验证码
        public static final int CONFIRM_IMG_CODE = 0xbb8;   //验证图片验证码
        public static final int REG = 0xbb9;   //注册
        public static final int LIB_LOGIN = 0xbba;   //第三方登录
        public static final int GET_SEARCH_KEY_DATA = 0xbbb;        //搜索
        public static final int SEARCH_HISTORY = 0xbbc;        //搜索记录
        public static final int MODIFY_PW = 0xbbd;        //搜索记录
        public static final int SEARCH_CONTENT = 0xbbe;     //搜索内容
        public static final int GET_ART_RECOMMEND = 0xbbf;      //文章推荐
        public static final int GET_COMMENT_LIST = 0xbc0;       //获取评论列表
        public static final int ART_COMMENT = 0xbc1;        //文章评论
        public static final int GET_COLLECT_STATE = 0xbc2;        //获取收藏状态
        public static final int GET_MSG_CENTER_DATA = 0xbc3;        //获取消息中心数据
        public static final int GET_MSG_DATA = 0xbc4;        //获取消息数据
        public static final int COLLECT = 0xbc5;        //收藏
        public static final int CANCEL_COLLECT = 0xbc6;        //取消收藏
        public static final int CHECK_VERSION = 0xbc7;        //检查版本
        public static final int GET_HOT_COMMENT = 0xbc8;        //热门评论实体
        public static final int IS_REG = 0xbc9;        //验证账号
    }

    //其它
    public class OTHER {
        public static final int LAUNCHER_IMG_FLOWS = 0xc1;   //启动页启动流程
    }

}
