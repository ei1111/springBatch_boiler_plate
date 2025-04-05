package com.example.SpringBatchTutorial.job.helloWorld;

import com.example.SpringBatchTutorial.job.SpringBatchTestConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.Job;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestPropertySource;

@TestPropertySource(properties = {"spring.batch.job.name=helloWorldJob"})
class HellowWorldJobConfigTest extends SpringBatchTestConfig {

    @DisplayName("helloWorldJob을 테스트 할 수 있다.")
    @Test
    void testJob(@Autowired Job helloWorldJob) throws Exception {
        //given
        //when
        //then
        jobLauncherTestUtils.setJob(helloWorldJob);
        jobLauncherTestUtils.launchJob();
    }
}