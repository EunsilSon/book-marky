package com.eunsil.bookmarky;

import com.eunsil.bookmarky.service.passage.PassageService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Transactional
@SpringBootTest
public class PassageServiceTest {

    @Autowired
    private PassageService passageService;

    @Test
    public void scheduledTest() {
        passageService.dailyCleanUpOfDeletedPassages();
    }

}
