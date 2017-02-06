package com.touchrom.fanjianzhi.config;

/**
 * Created by lyy on 2015/11/12.
 * 网络接口
 */
public class ServerConstance {

    public static final String BASE_URL = "http://api.fanjian.net/api/v2/";      //接入接口
    public static final String TEST_BASE_URL = "http://121.41.99.141:8803/api/v2/"; //外网接口
//    public static final String TEST_BASE_URL = "http://test.fanjian.net/api/v2/"; //测试接口
    /**
     * 验证注册
     */
    public static final String IS_REG = "user/is_reg";
    /**
     * 热门评论
     */
    public static final String GET_HOT_COMMENT = "article/hotComment";
    /**
     * 获取收藏列表
     */
    public static final String GET_COLLECT_DATA = "user/collect";
    /**
     * 删除消息
     */
    public static final String DEL_MSG = "msg/delete";
    /**
     * 收到的回复
     */
    public static final String MSG_REPLAY = "msg/reply";
    /**
     * @我的
     */
    public static final String MSG_CALL_ME = "msg/atme";
    /**
     * 系统消息
     */
    public static final String MSG_SYS = "msg/system";
    /**
     * 打赏
     */
    public static final String REWARD = "user/reward";
    /**
     * 取消收藏
     */
    public static final String CANCEL_COLLECT = "user/delete_collect";
    /**
     * 投诉
     */
    public static final String COMPLAINTS = "article/tousu";
    /**
     * 关注
     */
    public static final String FOLLOW = "user/follow";
    /**
     * 签到
     */
    public static final String SIGN = "user/sign";
    /**
     * 获取消息中心数据
     */
    public static final String GET_MSG_CENTER_DATA = "msg/index";
    /**
     * 获取分享数据
     */
    public static final String GET_SHARE_DATA = "welcome/share";
    /**
     * 收藏
     */
    public static final String COLLECT = "user/add_collect";
    /**
     * 获取收藏状态
     */
    public static final String GET_COLLECT_STATE = "article/zt_collect";
    /**
     * 回复
     */
    public static final String ART_COMMENT = "comment/send_comment";
    /**
     * 评论列表
     */
    public static final String GET_COMMENT = "comment/index";
    /**
     * 文章推荐
     */
    public static final String ART_RECOMMEND = "Article/recommend";
    /**
     * 踩
     */
    public static final String UN_STAR = "article/unding";
    /**
     * 点赞
     */
    public static final String STAR = "article/ding";
    /**
     * 修改密码
     */
    public static final String MODIFY_PW = "user/update";
    /**
     * 第三方登录
     */
    public static final String LIB_LOGIN = "user/third";
    /**
     * 文章详情
     */
    public static final String ART_INFO = "article/info";
    /**
     * 频道下文章列表
     */
    public static final String CHANNEL_ART_LIST = "article/pd_lists";
    /**
     * 首页文章列表
     */
    public static final String MAIN_TAB_CONTENT_LIST = "article/index";
    /**
     * 获取首页tab栏目
     */
    public static final String MAIN_TAB = "article/tab";
    /**
     * 启动实体
     */
    public static final String LAUNCHER = "welcome/index";
    /**
     * 版本升级实体
     */
    public static final String UPDATE = "welcome/banben";
    /**
     * 登录
     */
    public static final String LOGIN = "user/login";
    /**
     * 获取短信验证码
     */
    public static final String GET_MSG_CODE = "ajax/sms";
    /**
     * 验证短信验证码
     */
    public static final String CONFIRM_MSG_CODE = "ajax/sms_code";
    /**
     * 验证图片验证码
     */
    public static final String CONFIRM_IMG_CODE = "ajax/img_code";
    /**
     * 注册
     */
    public static final String REG = "user/reg";
    /**
     * 搜索
     */
    public static final String SEARCH_KEY = "search/keyword";
    /**
     * 搜索内容
     */
    public static final String SEARCH_CONTENT = "search/index";
}
