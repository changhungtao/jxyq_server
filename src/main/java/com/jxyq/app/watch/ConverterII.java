package com.jxyq.app.watch;

import com.jxyq.model.watch.Location;

public class ConverterII {
    public static final double pi = 3.141592653589793D;
    public static final double a = 6378245.0D;
    public static final double ee = 0.006693421622965943D;
    public static final double x_pi = 52.35987755982988D;

    public ConverterII() {
    }

    public static boolean outOfChina(double lat, double lon) {
        return lon >= 72.004D && lon <= 137.8347D ? lat < 0.8293D || lat > 55.8271D : true;
    }

    public static double transformLat(double x, double y) {
        double ret = -100.0D + 2.0D * x + 3.0D * y + 0.2D * y * y + 0.1D * x * y + 0.2D * Math.sqrt(x > 0.0D ? x : -x);
        ret += (20.0D * Math.sin(6.0D * x * 3.141592653589793D) + 20.0D * Math.sin(2.0D * x * 3.141592653589793D)) * 2.0D / 3.0D;
        ret += (20.0D * Math.sin(y * 3.141592653589793D) + 40.0D * Math.sin(y / 3.0D * 3.141592653589793D)) * 2.0D / 3.0D;
        ret += (160.0D * Math.sin(y / 12.0D * 3.141592653589793D) + 320.0D * Math.sin(y * 3.141592653589793D / 30.0D)) * 2.0D / 3.0D;
        return ret;
    }

    public static double transformLon(double x, double y) {
        double ret = 300.0D + x + 2.0D * y + 0.1D * x * x + 0.1D * x * y + 0.1D * Math.sqrt(x > 0.0D ? x : -x);
        ret += (20.0D * Math.sin(6.0D * x * 3.141592653589793D) + 20.0D * Math.sin(2.0D * x * 3.141592653589793D)) * 2.0D / 3.0D;
        ret += (20.0D * Math.sin(x * 3.141592653589793D) + 40.0D * Math.sin(x / 3.0D * 3.141592653589793D)) * 2.0D / 3.0D;
        ret += (150.0D * Math.sin(x / 12.0D * 3.141592653589793D) + 300.0D * Math.sin(x / 30.0D * 3.141592653589793D)) * 2.0D / 3.0D;
        return ret;
    }

    /**
     * 各地图API坐标系统比较与转换;
     * WGS84坐标系：即地球坐标系，国际上通用的坐标系。设备一般包含GPS芯片或者北斗芯片获取的经纬度为WGS84地理坐标系,
     * 谷歌地图采用的是WGS84地理坐标系（中国范围除外）;
     * GCJ02坐标系：即火星坐标系，是由中国国家测绘局制订的地理信息系统的坐标系统。由WGS84坐标系经加密后的坐标系。
     * 谷歌中国地图和搜搜中国地图采用的是GCJ02地理坐标系; BD09坐标系：即百度坐标系，GCJ02坐标系经加密后的坐标系;
     * 搜狗坐标系、图吧坐标系等，估计也是在GCJ02基础上加密而成的。
     */
    public static Location transformFromWGSToGCJ(Location wgLoc) {
        if (outOfChina(wgLoc.getLatitude(), wgLoc.getLongitude())) {
            return wgLoc;
        } else {
            double dLat = transformLat(wgLoc.getLongitude() - 105.0D, wgLoc.getLatitude() - 35.0D);
            double dLon = transformLon(wgLoc.getLongitude() - 105.0D, wgLoc.getLatitude() - 35.0D);
            double radLat = wgLoc.getLatitude() / 180.0D * 3.141592653589793D;
            double magic = Math.sin(radLat);
            magic = 1.0D - 0.006693421622965943D * magic * magic;
            double sqrtMagic = Math.sqrt(magic);
            dLat = dLat * 180.0D / (6335552.717000426D / (magic * sqrtMagic) * 3.141592653589793D);
            dLon = dLon * 180.0D / (6378245.0D / sqrtMagic * Math.cos(radLat) * 3.141592653589793D);
            Location mgLoc = new Location(wgLoc.getLongitude() + dLon, wgLoc.getLatitude() + dLat);
            return mgLoc;
        }
    }

    public static Location transformFromGCJToWGS(Location gcLoc) {
        if (outOfChina(gcLoc.getLatitude(), gcLoc.getLongitude())) {
            return gcLoc;
        } else {
            Location currGcLoc = transformFromWGSToGCJ(gcLoc);
            Location dLoc = new Location(gcLoc.getLongitude() - currGcLoc.getLongitude(), gcLoc.getLatitude() - currGcLoc.getLatitude());
            gcLoc.setLatitude(gcLoc.getLatitude() + dLoc.getLatitude());
            gcLoc.setLongitude(gcLoc.getLongitude() + dLoc.getLongitude());
            return gcLoc;
        }
    }

    public static Location bd_encrypt(Location gcLoc) {
        double x = gcLoc.getLongitude();
        double y = gcLoc.getLatitude();
        double z = Math.sqrt(x * x + y * y) + 2.0E-5D * Math.sin(y * 52.35987755982988D);
        double theta = Math.atan2(y, x) + 3.0E-6D * Math.cos(x * 52.35987755982988D);
        return new Location(z * Math.cos(theta) + 0.0065D, z * Math.sin(theta) + 0.006D);
    }

    public static Location bd_decrypt(Location bdLoc) {
        double x = bdLoc.getLongitude() - 0.0065D;
        double y = bdLoc.getLatitude() - 0.006D;
        double z = Math.sqrt(x * x + y * y) - 2.0E-5D * Math.sin(y * 52.35987755982988D);
        double theta = Math.atan2(y, x) - 3.0E-6D * Math.cos(x * 52.35987755982988D);
        return new Location(z * Math.cos(theta), z * Math.sin(theta));
    }
}
