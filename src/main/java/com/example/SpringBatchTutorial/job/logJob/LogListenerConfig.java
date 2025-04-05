package com.example.SpringBatchTutorial.job.logJob;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class LogListenerConfig {

    @Bean
    public Job logJob(JobRepository jobRepository, Step logStep) {
        return new JobBuilder("logJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .listener(new JobLoggerListener())
                .start(logStep)
                .build();
    }

    @Bean
    public Step logStep(JobRepository jobRepository
            , PlatformTransactionManager transactionManager
            , Tasklet logTaskLet) {
        return new StepBuilder("logStep", jobRepository)
                .tasklet(logTaskLet, transactionManager)
                .build();
    }

    @Bean
    public Tasklet logTaskLet() {
        return new Tasklet() {
            @Override
            public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext)
                    throws Exception {
                System.out.println("호출 = logTaskLet");
                return RepeatStatus.FINISHED;
            }
        };
    }
}
