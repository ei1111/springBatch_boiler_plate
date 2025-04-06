package com.example.SpringBatchTutorial.scheduler;

import java.text.SimpleDateFormat;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
@RequiredArgsConstructor
public class SampleScheduler {
    private final Job helloWorldJob;
    private final Job dbConnectionJob;
    private final Job fileReadWriteJob;
    private final JobLauncher jobLauncher;

    //초 분 시 일 월 주
    public void helloWorldJobRun() throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        //넣을 파라미터
        JobParameters jobParameters = new JobParametersBuilder()
                //파라미터에 값을 안넣고 실행하면 동일한 잡을 실행한다고 생각하여 작동을 안함
                .addString("date", String.valueOf(System.currentTimeMillis()))
                .toJobParameters();

        jobLauncher.run(helloWorldJob, jobParameters);
    }

   // @Scheduled(cron = "*/10 * * * * *", zone = "Asia/Seoul")
    public void dbConnectionJobRun() throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        //넣을 파라미터
        JobParameters jobParameters = new JobParametersBuilder()
                //파라미터에 값을 안넣고 실행하면 동일한 잡을 실행한다고 생각하여 작동을 안함
                .addString("date", String.valueOf(System.currentTimeMillis()))
                .toJobParameters();

        jobLauncher.run(dbConnectionJob, jobParameters);
    }

    @Scheduled(cron = "10 * * * * *", zone = "Asia/Seoul")
    public void fileReadWriteJobRun() throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        //넣을 파라미터
        JobParameters jobParameters = new JobParametersBuilder()
                //파라미터에 값을 안넣고 실행하면 동일한 잡을 실행한다고 생각하여 작동을 안함
                .addString("date", String.valueOf(System.currentTimeMillis()))
                .toJobParameters();

        jobLauncher.run(fileReadWriteJob, jobParameters);
    }
}
