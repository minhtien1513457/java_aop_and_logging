package com.tienpm.aop.config;

import lombok.Getter;
import org.springframework.stereotype.Service;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

@Service
@Getter
public class Config {
    public static String APP_ID;
    public static Config INSTANCE;
    public static String SERVICE_NAME = "AOP";
    public static String EXTERNAL_URL = "http://localhost:8080/api/v1";

    private Config() {
        super();
        INSTANCE = this;
        APP_ID = String.format("%s:%s", SERVICE_NAME, getIpAddress());
    }

    private String getIpAddress() {
        String ip = "127.0.0.1";
        Enumeration<NetworkInterface> networkInterfaces;
        List<String> ipAddress = new ArrayList<>();
        try {
            networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = networkInterfaces.nextElement();
                Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress addr = addresses.nextElement();
                    if (addr instanceof Inet4Address && !addr.isLoopbackAddress()) {
                        ipAddress.add(addr.getHostAddress());
                    }
                }
            }
            if (ipAddress.isEmpty()) {
                return ip;
            }
            Collections.sort(ipAddress);

            return ipAddress.get(0);
        } catch (Exception e) {
        }

        return ip;
    }

}
