package com.example.SpringBatchTutorial.job.logJob;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

@Slf4j
public class JobLoggerListener implements JobExecutionListener {
    private static String BEFORE_MESSAGE = "{} Job is Running";
    private static String AFTER_MESSAGE = "{} Job is DONE. (Status: {} )";

    //잡이 실행 전 시작
    @Override
    public void beforeJob(JobExecution jobExecution) {
        log.info(BEFORE_MESSAGE, jobExecution.getJobInstance().getJobName());

    }

    //잡이 실행 후 시작
    @Override
    public void afterJob(JobExecution jobExecution) {
        log.info(AFTER_MESSAGE
                , jobExecution.getJobInstance().getJobName()
                , jobExecution.getStatus()
        );

        if (jobExecution.getStatus() == BatchStatus.FAILED) {
            //email, 메신저 받기
            log.info("Job is Failed");
        }
    }
}
