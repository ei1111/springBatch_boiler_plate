package com.example.SpringBatchTutorial.job.helloWorld;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class HellowWorldJobConfig {
    @Bean
    public Job helloWorldJob(JobRepository jobRepository, Step hellowWorldStep) {
        //이름 설정
        return new JobBuilder("helloWorldJob", jobRepository)
                //job 실행시 id를 구현(시퀀스 순차적 구현)
                .incrementer(new RunIdIncrementer())
                .start(hellowWorldStep)
                .build();
    }

    @Bean
    public Step hellowWorldStep(JobRepository jobRepository
            , PlatformTransactionManager platformTransactionManager
            , Tasklet helloWorldTasklet) {
        return new StepBuilder("hellowWorldStep", jobRepository)
                //reader, processor, writer를 안사용할려면 tasklet을 사용하면 된다.
                .tasklet(helloWorldTasklet, platformTransactionManager)
                .build();
    }

    @Bean
    public Tasklet helloWorldTasklet() {
        return new Tasklet() {
            @Override
            public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext)
                    throws Exception {
                System.out.println("hello World Tasklet " + LocalDateTime.now());
                return RepeatStatus.FINISHED;
            }
        };
    }
}
