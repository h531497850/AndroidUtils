
package com.vivo.sdk.appinfo;

import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * @ClassName: AppInfo
 * @Description: TODO
 * @author: xushenghua
 * @date: 2017年3月22日 下午2:14:06
 * @version 1.0
 * @Copyright © 2017 广东维沃移动通信有限公司. All rights reserved.
 */
public class AppInfo implements Parcelable {

    public static final Creator<AppInfo> CREATOR = new Creator<AppInfo>() {
        @Override
        public AppInfo createFromParcel(Parcel in) {
            return new AppInfo(in);
        }

        @Override
        public AppInfo[] newArray(int size) {
            return new AppInfo[size];
        }
    };
    /**
     * @Description: 应用uid
     */
    private int uid;
    /**
     * @Description: 应用名
     */
    private String appName;
    /**
     * @Description: 包名
     */
    private String pkgName;
    /**
     * @Description: 版本名
     */
    private String versionName;
    /**
     * @Description: 版本号
     */
    private int versionCode;
    /**
     * @Description: 是否为系统应用
     */
    private boolean systemFlag;
    /**
     * @Description: 应用图标
     */
    private transient Drawable icon;

    public AppInfo() {
    }

    public AppInfo(AppInfo appInfo) {
        if (appInfo != null) {
            this.appName = appInfo.getAppName();
            this.icon = appInfo.getIcon();
            this.pkgName = appInfo.getPkgName();
            this.uid = appInfo.getUid();
            this.versionCode = appInfo.getVersionCode();
            this.versionName = appInfo.getVersionName();
            this.systemFlag = appInfo.isSystemFlag();
        }
    }

    public AppInfo(String pkgName) {
        this(AppInfoManager.getInstance().getAppInfo(pkgName));
    }

    protected AppInfo(Parcel in) {
        uid = in.readInt();
        appName = in.readString();
        pkgName = in.readString();
        versionName = in.readString();
        versionCode = in.readInt();
        systemFlag = in.readByte() != 0;
    }

    /**
     * @return the uid
     */
    public int getUid() {
        return uid;
    }

    /**
     * @param uid the uid to set
     */
    public void setUid(int uid) {
        this.uid = uid;
    }

    /**
     * @return the appName
     */
    public String getAppName() {
        return appName;
    }

    /**
     * @param appName the appName to set
     */
    public void setAppName(String appName) {
        this.appName = appName;
    }

    /**
     * @return the pkgName
     */
    public String getPkgName() {
        return pkgName;
    }

    /**
     * @param pkgName the pkgName to set
     */
    public void setPkgName(String pkgName) {
        this.pkgName = pkgName;
    }

    /**
     * @return the versionName
     */
    public String getVersionName() {
        return versionName;
    }

    /**
     * @param versionName the versionName to set
     */
    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    /**
     * @return the versionCode
     */
    public int getVersionCode() {
        return versionCode;
    }

    /**
     * @param versionCode the versionCode to set
     */
    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    /**
     * @return the systemFlag
     */
    public boolean isSystemFlag() {
        return systemFlag;
    }

    /**
     * @param systemFlag the systemFlag to set
     */
    public void setSystemFlag(boolean systemFlag) {
        this.systemFlag = systemFlag;
    }

    /**
     * @return the icon
     */
    public Drawable getIcon() {
        return icon;
    }

    /**
     * @param icon the icon to set
     */
    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    /**
     * @Title: toString
     * @Description: TODO
     * @return
     * @see Object#toString()
     */
    public String toString() {
        return "appName= " + appName + ", pkgName= " + pkgName + ", systemFlag= " + systemFlag;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(uid);
        dest.writeString(appName);
        dest.writeString(pkgName);
        dest.writeString(versionName);
        dest.writeInt(versionCode);
        dest.writeByte((byte) (systemFlag ? 1 : 0));
    }
}
