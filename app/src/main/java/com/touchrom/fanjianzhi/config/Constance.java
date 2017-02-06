package com.touchrom.fanjianzhi.config;

import android.os.Environment;

/**
 * Created by lyy on 2016/6/1.
 */
public class Constance {

    public static class ADAPTER {
        public static final int BANNER_ITEM = 0x00;
        public static final int IMG_CONTENT_ITEM = 0x01;
        public static final int IMG_TEXT_CONTENT_ITEM = 0x02;
        public static final int TEXT_CONTENT_ITEM = 0x03;
        public static final int VIDEO_CONTENT_ITEM = 0x04;
        public static final int CHANNEL_CONTENT_ITEM = 0x05;
        public static final int ART_CONTENT_ITEM = 0x06;
        public static final int MSG_SYS = 0x07;
        public static final int MSG_REPLAY = 0x08;
        public static final int MSG_CALL = 0x09;
        public static final int MSG_COLLECT = 0x0a;
        public static final int ART_BAR = 0x0b;
        public static final int COMMENT = 0x0c;
        public static final int TEXT_ITEM = 0x0d;
    }


    public static class PATH {
        public static final String ROOT_PATH = Environment.getExternalStorageDirectory().getPath();
        public static final String BASE_DIR = Environment.getExternalStorageDirectory().getPath() + "/fjz/";
        public static final String IMG_DIR = BASE_DIR + "/img/";
        public static final String TEMP = BASE_DIR + "/temp/";
        public static final String LAUNCHER_IMG_PATH = BASE_DIR + "launcher/launcher.dat";  //启动图
        public static final String NEW_APK_PATH = BASE_DIR + "update/gaoShouYou.apk";       //版本升级apk
        public static final String GUIDE_SETTING_TEMP_PATH = "/guideTemp.dat";  //引导版本的差异文件
        public static final String DEFAULT_DOWNLOAD_LOCATION = BASE_DIR + "download/";  //apk下载位置
        public static final String TEMP_HEAD_IMG_PATH = BASE_DIR + "temp/t_hi.jpg";  //头像上传缓存文件
    }

    public static class KEY {
        public static final String USER = "USER";
        public static final String CURRENT_COLOR = "CURRENT_COLOR";
        public static final String STATE_BAR_COLOR = "STATE_BAR_COLOR";
        public static final String LOGIN_STATE = "LOGIN_STATE";
        public static final String THEME = "THEME";
        public static final String LAUNCHER_ENTITY = "LAUNCHER_ENTITY";
        public static final String PARCELABLE_ENTITY = "PARCELABLE_ENTITY";
        public static final String BOOLEAN = "BOOLEAN";
        public static final String SETTING = "SETTING";
        public static final String FIND_APK_TIME = "FIND_APK_TIME";
        public static final String ACCOUNT = "ACCOUNT";
        public static final String INT = "INT";
        public static final String STRING = "STRING";
        public static final String LIST = "LIST";
        public static final String ID = "ID";
        public static final String TYPE = "TYPE";
        public static final String TURN = "TURN";
        public static final String USER_ID = "USER_ID";
    }

    public static class APP {
        public static final String NAME = "FAN_JIAN_ZHI";
        public static final String SEED = "@!*&$123456";
    }

    public static class URL {
        public static final String PROTOCOL = "http://www.fanjian.net/info/contract";   //注册协议
        public static final String OFFICIAL = "http://www.fanjian.net";   //官方网址
        public static final String DISCLAIMER = "http://www.fanjian.net/info/disclaimer"; //免责声明
        public static final String WEIBO = "http://weibo.com/p/1005051752014931?is_hot=1"; //新浪微博
        public static final String FEEDBACK = "https://jinshuju.net/f/esp1sS";  //意见反馈
    }

    public static class NOTIFY_FLAG {
        public static final int LOGIN = 0xd1;
        public static final int ART_COMMENT = 0xd2;
    }

    public static class NOTIFY_ACTION {
        public static final int LOGIN_ACTION = 0xe1;
        public static final int ART_COMMENT_UPDATE = 0xe2;
        public static final int UPDATE_SETTING = 0xe2;
    }

    public static class NOTIFICATION {
        public static final int VERSION_APK_ID = 0x01; //版本升级APK
        public static final int VERSION_NORMAL_MSG_ID = 0x02; //版本升级APK
    }
}
