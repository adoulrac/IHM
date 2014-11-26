package IHM.utils;

import IHM.validators.IPAddressValidator;
import com.google.common.collect.Lists;
import com.google.common.net.InetAddresses;

import java.net.Inet4Address;
import java.util.List;

public class IPAddressUtil {

    public static Inet4Address toInet4Address(final String ipAddress) {
        return (Inet4Address) InetAddresses.forString(ipAddress);
    }

    public static List<Inet4Address> toInet4AddressList(final String ipAddressList) {
        List<Inet4Address> list = Lists.newArrayList();

        String[] lines = ipAddressList.split(System.getProperty("line.separator"));
        for(String line : lines) {
            list.add(toInet4Address(line));
        }

        return list;
    }
}
