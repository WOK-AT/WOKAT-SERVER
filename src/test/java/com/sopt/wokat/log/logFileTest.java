package com.sopt.wokat.log;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
public class logFileTest {

    private final Logger LOGGER = LogManager.getLogger(this.getClass());

    @Test
    void logInfoTest() {

        LOGGER.info("✨로그 테스트✨");
        LOGGER.error("✨로그 테스트✨");
        
    }
    
}
