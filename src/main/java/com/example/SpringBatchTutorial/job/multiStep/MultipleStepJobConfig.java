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
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class MultipleStepJobConfig {

    @Bean
    public Job multipleStepJob(
            JobRepository jobRepository
            , Step multipleFirstStep
            , Step multipleSecondStep
            , Step multipleThirdStep) {
        return new JobBuilder("multipleStepJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(multipleFirstStep)
                .next(multipleSecondStep)
                .next(multipleThirdStep)
                .build();
    }

    @Bean
    public Step multipleFirstStep(JobRepository jobRepository
            , PlatformTransactionManager transactionManager
            , Tasklet multipleFirstTasklet) {
        return new StepBuilder("multipleFirstStep", jobRepository)
                .tasklet(multipleFirstTasklet, transactionManager)
                .build();
    }

    @Bean
    public Tasklet multipleFirstTasklet() {
        return new Tasklet() {
            @Override
            public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext)
                    throws Exception {
                System.out.println("첫번째 tasklet");
                return RepeatStatus.FINISHED;
            }
        };
    }

    @Bean
    public Step multipleSecondStep(JobRepository jobRepository
            , PlatformTransactionManager transactionManager
            , Tasklet multipleSecondTasklet) {
        return new StepBuilder("multipleSecondStep", jobRepository)
                .tasklet(multipleSecondTasklet, transactionManager)
                .build();
    }

    @Bean
    public Tasklet multipleSecondTasklet() {
        return new Tasklet() {
            @Override
            public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext)
                    throws Exception {
                System.out.println("두번째 tasklet");

                //다음 step으로 데이터 넘길시에는 ExecutionContext에 데이터를 담아서 다음 step에서 사용
                ExecutionContext executionContext = chunkContext
                        .getStepContext()
                        .getStepExecution()
                        .getJobExecution()
                        .getExecutionContext();

                executionContext.put("someKey", "hello!");
                return RepeatStatus.FINISHED;
            }
        };
    }

    @Bean
    public Step multipleThirdStep(JobRepository jobRepository
            , PlatformTransactionManager transactionManager
            , Tasklet multipleThirdTasklet) {
        return new StepBuilder("multipleThirdStep", jobRepository)
                .tasklet(multipleThirdTasklet, transactionManager)
                .build();
    }

    @Bean
    public Tasklet multipleThirdTasklet() {
        return new Tasklet() {
            @Override
            public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext)
                    throws Exception {
                System.out.println("세번째 tasklet");

                ExecutionContext executionContext = chunkContext
                        .getStepContext()
                        .getStepExecution()
                        .getJobExecution()
                        .getExecutionContext();

                System.out.println(executionContext.get("someKey"));
                return RepeatStatus.FINISHED;
            }
        };
    }
}
