package com.avtech.recruitment.infrastructure.tenant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TenantContext {

    private static final Logger logger = LoggerFactory.getLogger(TenantContext.class);
    private static final ThreadLocal<String> currentTenant = new ThreadLocal<>();

    public static void setTenantId(String tenantId) {
        logger.debug("Setting tenantId to {}", tenantId);
        currentTenant.set(tenantId);
    }

    public static String getTenantId() {
        return currentTenant.get();
    }

    public static void clear() {
        logger.debug("Clearing tenantId");
        currentTenant.remove();
    }
}
