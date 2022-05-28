package com.example.myapplication.db;



import java.io.Serializable;


public class ShopLoc  implements Serializable {

    private String shopId;
    private String shopName;
   private String desc;
    private String lat;
    private String lon;
    public ShopLoc(String shopId, String shopName, String lat,String lon,String desc) {
        super();
        this.shopId=shopId;
        this.shopName = shopName;
        this.lat = lat;
        this.lon=lon;
        this.desc=desc;
    }
    public String getShopId(){return shopId;}
    public void setShopId(String shopId){this.shopId=shopId;}

    public String getShopName(){return shopName;}
    public void setShopName(String shopName){this.shopName = shopName;}

    public String getDesc(){return desc;}
    public void setDesc(String desc){this.desc = desc;}

    public String getLat(){return lat;}
    public void setLat(String lat){this.lat = lat;}

    public String getLon(){return lon;}
    public void setLon(String lon){this.lon = lon;}


}
