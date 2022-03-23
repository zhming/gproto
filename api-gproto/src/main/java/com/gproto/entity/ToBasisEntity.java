package com.gproto.entity;


import java.util.Map;

/**
 * @desc Downlink data
 * @author yan.zhp
 * @since 2021/02/02
 */
public class ToBasisEntity {

    private String serviceid;

    private String servicename;

    private int requesttype; //0719 add 1.method 2.field

    private String methodname;

    private Integer provider;

    private Map<String, Object> param;

    private String vin;

    private String cb;

    private String cbdata;

    private String serviceversion;


    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public String getServicename() {
        return servicename;
    }

    public void setServicename(String servicename) {
        this.servicename = servicename;
    }

    public String getServiceid() {
        return serviceid;
    }

    public void setServiceid(String serviceid) {
        this.serviceid = serviceid;
    }



    public String getCb() {
        return cb;
    }

    public void setCb(String cb) {
        this.cb = cb;
    }

    public String getMethodname() {
        return methodname;
    }

    public void setMethodname(String methodname) {
        this.methodname = methodname;
    }

    public String getCbdata() {
        return cbdata;
    }

    public void setCbdata(String cbdata) {
        this.cbdata = cbdata;
    }

    public Integer getProvider() {
        return provider;
    }

    public void setProvider(Integer provider) {
        this.provider = provider;
    }

    public int getRequesttype() {
        return requesttype;
    }

    public void setRequesttype(int requesttype) {
        this.requesttype = requesttype;
    }

    public String getServiceversion() {
        return serviceversion;
    }

    public void setServiceversion(String serviceversion) {
        this.serviceversion = serviceversion;
    }

    public Map<String, Object> getParam() {
        return param;
    }

    public void setParam(Map<String, Object> param) {
        this.param = param;
    }

    public ToBasisEntity() {
    }

    public ToBasisEntity(String vin, String servicename, String serviceid, Map<String,Object> param, String serviceversion, String cb, String methodname, String cbdata, Integer provider) {
        this.vin = vin;
        this.servicename = servicename;
        this.serviceid = serviceid;
        this.param = param;
        this.serviceversion = serviceversion;
        this.cb = cb;
        this.methodname = methodname;
        this.cbdata = cbdata;
        this.provider = provider;
    }

    @Override
    public String toString() {
        return "ToBasisEntity{" +
                "vin='" + vin + '\'' +
                ", servicename='" + servicename + '\'' +
                ", serviceid='" + serviceid + '\'' +
                ", param=" + param +
                ", serviceversion='" + serviceversion + '\'' +
                ", cb='" + cb + '\'' +
                ", methodname='" + methodname + '\'' +
                ", cbdata='" + cbdata + '\'' +
                ", provider=" + provider +
                '}';
    }
}
