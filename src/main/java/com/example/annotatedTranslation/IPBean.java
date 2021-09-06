package com.example.annotatedTranslation;

import com.tencentcloudapi.vpc.v20170312.models.Ip6Rule;

/**
 * @author Yu Shaoqing
 * @date 2021/9/6/10:33
 */
public class IPBean {


    private String ip;
    private int port;
    // type = 0 表示http 协议，type = 1 表示https协议
    private int type;

    public IPBean(){}
    public IPBean(IPBean bean){
        ip = bean.getIp();
        port = bean.getPort();
        type = bean.getType();
    }

    public IPBean(String ip, int port, int type) {
        this.ip = ip;
        this.port = port;
        this.type = type;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
