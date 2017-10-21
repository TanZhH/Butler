package com.smartbutler.tanhuihui.butler.network;

/**
 * 创建者：   TANHUIHUI
 * 项  目：   Butler
 * 包  名：   com.smartbutler.tanhuihui.butler.network
 * 创建日期： 2017/10/7
 * 描  述：
 */

public class HttpConstant {
    private static final String ROOT_URL = "http://www.mytestbutler.com";
    /**
     * 检查是否有patch文件更新
     */
    public static String UPDATE_PATCH_URL = ROOT_URL + "/tinker/update.php";

    /**
     * patch文件下载地址
     */
    public static String DOWNLOAD_PATCH_URL = ROOT_URL + "/tinker/download_patch.php";
}
