package com.example.SpringBatchTutorial.job.multiStep;

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
public class ConditionalStepJobConfig {

    @Bean
    public Job conditionalStepJob(
            JobRepository jobRepository,
            Step conditionalStartStep,
            Step conditionalFailStep,
            Step conditionalCompletedStep,
            Step conditionalAllStep

    ) {
        return new JobBuilder("conditionalStepJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(conditionalStartStep)
                //conditionalStartStep가 FAILED되면 conditionalFailStep가 실행된다.
                .on("FAILED").to(conditionalFailStep)
                .from(conditionalStartStep)
                //conditionalStartStep가 COMPLETED되면 conditionalCompletedStep가 실행된다.
                .on("COMPLETED").to(conditionalCompletedStep)
                .from(conditionalStartStep)
                //conditionalStartStep가 FAILED,COMPLETED가 아닌 다른 상태가되면 conditionalAllStep가 실행된다.
                .on("*").to(conditionalAllStep)
                .end()
                .build();
    }

    @Bean
    public Step conditionalStartStep(JobRepository jobRepository
            , PlatformTransactionManager transactionManager) {
        return new StepBuilder("conditionalStartStep", jobRepository)
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                       System.out.println("conditionalStartStep");
                        return RepeatStatus.FINISHED;
                        //throw new Exception();
                    }
                }, transactionManager)
                .build();
    }

    @Bean
    public Step conditionalFailStep(JobRepository jobRepository
            , PlatformTransactionManager transactionManager) {
        return new StepBuilder("conditionalFailStep", jobRepository)
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                        System.out.println("conditionalFailStep");
                        return RepeatStatus.FINISHED;
                    }
                }, transactionManager)
                .build();
    }

    @Bean
    public Step conditionalCompletedStep(JobRepository jobRepository
            , PlatformTransactionManager transactionManager) {
        return new StepBuilder("conditionalCompletedStep", jobRepository)
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                        System.out.println("conditionalCompletedStep");
                        return RepeatStatus.FINISHED;
                    }
                }, transactionManager)
                .build();
    }

    @Bean
    public Step conditionalAllStep(JobRepository jobRepository
            , PlatformTransactionManager transactionManager) {
        return new StepBuilder("conditionalAllStep", jobRepository)
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                        System.out.println("conditionalAllStep");
                        return RepeatStatus.FINISHED;
                    }
                }, transactionManager)
                .build();
    }
}
