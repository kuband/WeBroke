package com.backend.common.util;

import jakarta.servlet.http.HttpServletRequest;

public class RequestUtil {
    public static String getAppUrl(HttpServletRequest request) {
        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }
}
