package com.emrys.lotterysystem.logger;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 *
 * @author Emrys
 * content:
 */
@SpringBootTest
@Slf4j
public class Logger {
    @Test
    public void test(){
        log.info("test");
    }
}
