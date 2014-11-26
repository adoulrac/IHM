package IHM.utils;


import java.net.Inet4Address;
import java.util.Arrays;
import java.util.List;

public class StringUtil {

    public static final String SYSTEM_SEPARATOR = System.getProperty("line.separator");

    public static List<String> toList(String string, String separator) {
        return Arrays.asList(string.split(separator));
    }
}
