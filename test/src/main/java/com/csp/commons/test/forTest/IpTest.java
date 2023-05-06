package com.csp.commons.test.forTest;

import com.alibaba.fastjson2.JSON;
import lombok.SneakyThrows;
import org.apache.tomcat.util.net.IPv6Utils;
import sun.net.util.IPAddressUtil;

import java.net.Inet6Address;
import java.net.InetAddress;

/**
 * @description:
 * @author: chish
 * @date: 2023/4/29 13:56
 */
public class IpTest {
    @SneakyThrows
    public static void main(String[] args) {
        InetAddress[] cccsps = Inet6Address.getAllByName("cccsp");
        for (InetAddress cccsp : cccsps) {
            System.out.println(cccsp.getHostAddress());
            System.out.println(IPAddressUtil.isIPv6LiteralAddress(cccsp.getHostAddress()));
        }
    }
}