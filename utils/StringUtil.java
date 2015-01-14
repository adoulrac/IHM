package IHM.utils;


import com.google.common.base.Strings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The Class StringUtil.
 */
public class StringUtil {

    /** The Constant SYSTEM_SEPARATOR. */
    public static final String SYSTEM_SEPARATOR = "\\r?\\n";

    /**
     * To list.
     *
     * @param string the string
     * @param separator the separator
     * @return the list
     */
    public static List<String> toList(String string, String separator) {
        return Strings.isNullOrEmpty(string) ? new ArrayList<String>() : Arrays.asList(string.split(separator));
    }
}
