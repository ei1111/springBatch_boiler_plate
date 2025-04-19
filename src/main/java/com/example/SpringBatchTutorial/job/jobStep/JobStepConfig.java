package com.example.SpringBatchTutorial.job.jobStep;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.job.DefaultJobParametersExtractor;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

//jobStep1 -> childJob -> step1 -> step2
@Configuration
public class JobStepConfig {

    @Bean
    public Job jobStepJob(JobRepository jobRepository, Step jobStep1, Step step2) {
        return new JobBuilder("jobStepJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(jobStep1)
                .next(step2)
                .build();
    }


    @Bean
    public Step jobStep1(JobRepository jobRepository
            , JobLauncher jobLauncher
            , Job childJob) {
        return new StepBuilder("jobStep1", jobRepository)
                .job(childJob)
                .launcher(jobLauncher)
                .parametersExtractor(jobParametersExtractor())
                .listener(new StepExecutionListener() {
                    @Override
                    public void beforeStep(StepExecution stepExecution) {
                        stepExecution.getExecutionContext().put("name", "user1");
                    }
                })
                .build();
    }

    private DefaultJobParametersExtractor jobParametersExtractor() {
        DefaultJobParametersExtractor extractor = new DefaultJobParametersExtractor();
        extractor.setKeys(new String[]{"name"});
        return extractor;
    }


    @Bean
    public Job childJob(JobRepository jobRepository, Step step1) {
        return new JobBuilder("jobStep1", jobRepository)
                .start(step1)
                .build();
    }

    @Bean
    public Step step1(
            JobRepository jobRepository
            , PlatformTransactionManager transactionManager) {
        return new StepBuilder("step1", jobRepository)
                .tasklet((a, b) -> {
                    System.out.println("childJob- step1 살행");
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }

    @Bean
    public Step step2(
            JobRepository jobRepository
            , PlatformTransactionManager transactionManager) {
        return new StepBuilder("step2", jobRepository)
                .tasklet((a, b) -> {
                    System.out.println("helloJob - step2 실행");
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }
}
