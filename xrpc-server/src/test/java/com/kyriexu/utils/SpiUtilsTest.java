package com.kyriexu.utils;

import com.kyriexu.registry.ServiceDiscovery;
import com.kyriexu.service.PublishServiceTest;
import org.junit.jupiter.api.Test;

/**
 * @author KyrieXu
 * @since 2020/9/10 14:16
 **/
class SpiUtilsTest {

    @Test
    void getServiceDiscovery() throws NoSuchMethodException {
        ServiceDiscovery discovery = SpiUtils.getServiceDiscovery(PublishServiceTest.class);
        discovery.discoverService("serviceName");
    }
}