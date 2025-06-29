package nl.gerimedica.assignment.Utils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Utils {

    private static int usageCounter = 0;

    public static void recordUsage(String context) {
        usageCounter++;
        log.info("HospitalUtils used. Counter: {} | Context: {}", usageCounter, context);
    }

    public static boolean isNull(Object o) {
        return o == null;
    }

    public static boolean isEmpty(Object o) {
        if (o instanceof String) {
            return ((String) o).trim().isEmpty();
        }
        if (o instanceof java.util.Collection) {
            return ((java.util.Collection<?>) o).isEmpty();
        }
        if (o instanceof java.util.Map) {
            return ((java.util.Map<?, ?>) o).isEmpty();
        }
        if (o.getClass().isArray()) {
            return java.lang.reflect.Array.getLength(o) == 0;
        }
        return false;
    }

    public static boolean isNullOrEmpty(Object o) {
        return isNull(o) || isEmpty(o);
    }
}
