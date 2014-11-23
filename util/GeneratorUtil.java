package IHM.util;

import java.rmi.server.UID;
import java.util.UUID;

public class GeneratorUtil {

    public static UUID generateUID() {
        return UUID.randomUUID();
    }
}
