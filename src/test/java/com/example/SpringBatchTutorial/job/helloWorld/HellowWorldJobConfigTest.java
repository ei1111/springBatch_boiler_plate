package com.example.SpringBatchTutorial.job.helloWorld;

import com.example.SpringBatchTutorial.job.SpringBatchTestConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;


@SpringBatchTest
@ActiveProfiles("test")
@SpringBootTest(classes = {SpringBatchTestConfig.class, HellowWorldJobConfig.class})
class HellowWorldJobConfigTest {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @DisplayName("성공 케이스")
    @Test
    void success() throws Exception {
        //when
        //HellowWorldJobConfig 실행
        JobExecution execution = jobLauncherTestUtils.launchJob();

        //then
        Assertions.assertEquals(execution.getExitStatus(), ExitStatus.COMPLETED);
    }
}