package com.example.SpringBatchTutorial.job.multiflow;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.job.flow.support.SimpleFlow;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

//FlowA: (multiStep1 -> multiStep2) -> multiStep3 -> FlowB : (multiStep4 -> multiStep5) -> multiStep6
@Configuration
public class MultiFlowConfiguration {

    @Bean
    public Job multiJob(JobRepository jobRepository
            , Flow flowA
            ,Flow flowB
            , Step multiStep3
            , Step multiStep6) {
        return  new JobBuilder("multiJob", jobRepository)
                .start(flowA)
                .next(multiStep3)
                .end()
                .start(flowB)
                .next(multiStep6)
                .end()
                .build();
    }

    @Bean
    public Flow flowA(Step multiStep1, Step multiStep2) {
        return new FlowBuilder<SimpleFlow>("flowA")
                .start(multiStep1)
                .next(multiStep2)
                .build();
    }

    @Bean
    public Flow flowB(Step multiStep4, Step multiStep5) {
        return new FlowBuilder<SimpleFlow>("flowB")
                .start(multiStep4)
                .next(multiStep5)
                .build();
    }


    @Bean
    public Step multiStep1(JobRepository jobRepository
            , PlatformTransactionManager transactionManager) {
        return new StepBuilder("multiStep1", jobRepository)
                .tasklet(
                        (contribution, chunkContext) -> {
                            System.out.println("multiStep1 입니다");
                            return RepeatStatus.FINISHED;
                        },
                        transactionManager
                )
                .build();
    }

    @Bean
    public Step multiStep2(JobRepository jobRepository
            , PlatformTransactionManager transactionManager) {
        return new StepBuilder("multiStep2", jobRepository)
                .tasklet(
                        (contribution, chunkContext) -> {
                            System.out.println("multiStep2 입니다");
                            return RepeatStatus.FINISHED;
                        },
                        transactionManager
                )
                .build();
    }

    @Bean
    public Step multiStep3(JobRepository jobRepository
            , PlatformTransactionManager transactionManager) {
        return new StepBuilder("multiStep3", jobRepository)
                .tasklet(
                        (contribution, chunkContext) -> {
                            System.out.println("multiStep3 입니다");
                            return RepeatStatus.FINISHED;
                        },
                        transactionManager
                )
                .build();
    }

    @Bean
    public Step multiStep4(JobRepository jobRepository
            , PlatformTransactionManager transactionManager) {
        return new StepBuilder("multiStep4", jobRepository)
                .tasklet(
                        (contribution, chunkContext) -> {
                            System.out.println("multiStep4 입니다");
                            return RepeatStatus.FINISHED;
                        },
                        transactionManager
                )
                .build();
    }

    @Bean
    public Step multiStep5(JobRepository jobRepository
            , PlatformTransactionManager transactionManager) {
        return new StepBuilder("multiStep5", jobRepository)
                .tasklet(
                        (contribution, chunkContext) -> {
                            System.out.println("multiStep5 입니다");
                            return RepeatStatus.FINISHED;
                        },
                        transactionManager
                )
                .build();
    }


    @Bean
    public Step multiStep6(JobRepository jobRepository
            , PlatformTransactionManager transactionManager) {
        return new StepBuilder("multiStep6", jobRepository)
                .tasklet(
                        (contribution, chunkContext) -> {
                            System.out.println("multiStep6 입니다");
                            return RepeatStatus.FINISHED;
                        },
                        transactionManager
                )
                .build();
    }
}