package com.zhm.entity;

import com.zhm.ListenerEntity.ListenerEntity;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * 中国行政区划（具体到村 2018-11月数据）
 * Created by 赵红明 on 2019/11/21.
 */
@Entity
@Table(name = "cn_area")
public class CnArea extends ListenerEntity{
    private Integer id;
    private Integer areaLevel;
    private String parentCode;
    private String areaCode;
    private String zipCode;
    private String cityCode;
    private String name;
    private String shortName;
    private String mergerName;
    private String pinyin;
    private BigDecimal lng;
    private BigDecimal lat;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Basic
    @Column(name = "area_level")
    public Integer getAreaLevel() {
        return areaLevel;
    }

    public void setAreaLevel(Integer areaLevel) {
        this.areaLevel = areaLevel;
    }

    @Basic
    @Column(name = "parent_code")
    public String getParentCode() {
        return parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }

    @Basic
    @Column(name = "area_code")
    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    @Basic
    @Column(name = "zip_code")
    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    @Basic
    @Column(name = "city_code")
    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    @Basic
    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "short_name")
    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    @Basic
    @Column(name = "merger_name")
    public String getMergerName() {
        return mergerName;
    }

    public void setMergerName(String mergerName) {
        this.mergerName = mergerName;
    }

    @Basic
    @Column(name = "pinyin")
    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    @Basic
    @Column(name = "lng")
    public BigDecimal getLng() {
        return lng;
    }

    public void setLng(BigDecimal lng) {
        this.lng = lng;
    }

    @Basic
    @Column(name = "lat")
    public BigDecimal getLat() {
        return lat;
    }

    public void setLat(BigDecimal lat) {
        this.lat = lat;
    }

    @Override
    public String toString() {
        return "CnArea{" +
                "id=" + id +
                ", parentCode=" + parentCode +
                ", areaCode=" + areaCode +
                ", zipCode=" + zipCode +
                ", cityCode='" + cityCode + '\'' +
                ", name='" + name + '\'' +
                ", shortName='" + shortName + '\'' +
                ", mergerName='" + mergerName + '\'' +
                ", pinyin='" + pinyin + '\'' +
                ", lng=" + lng +
                ", lat=" + lat +
                '}';
    }
}
