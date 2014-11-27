package IHM.utils;


import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import java.net.Inet4Address;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StringUtil {

    public static final String SYSTEM_SEPARATOR = System.getProperty("line.separator");

    public static List<String> toList(String string, String separator) {
        return Strings.isNullOrEmpty(string) ? new ArrayList<String>() : Arrays.asList(string.split(separator));
    }
}
