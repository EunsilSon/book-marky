package com.eunsil.bookmarky;

import com.eunsil.bookmarky.service.passage.PassageService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class PassageServiceTest {

    @Autowired
    private PassageService passageService;

    @Test
    public void scheduledTest() {
        passageService.dailyCleanUpOfDeletedPassages();
    }
}
