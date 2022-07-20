package com.hyx.manager;

import java.util.HashMap;
import java.util.Map;

/**
 * @author hyx
 **/

public class JsonManager {
    private static final Map<String, Object> INSTANCE = new HashMap<>();
    
    public static Map<String, Object> instance() {
        return INSTANCE;
    }
}
