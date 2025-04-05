package com.example.SpringBatchTutorial.job.validatoredParam;

import com.example.SpringBatchTutorial.job.validator.FileParamEmptyCheck;
import com.example.SpringBatchTutorial.job.validator.FileParamValidator;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import org.hibernate.id.IncrementGenerator;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.JobParametersValidator;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.job.CompositeJobParametersValidator;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class ValidatedParam {

    @Bean
    public Job validateParamJob(JobRepository jobRepository
    , Step vaildateParamStep) {
        return new JobBuilder("validateParamJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .validator(multipleValidator())
                .start(vaildateParamStep)
                .build();
    }

    private CompositeJobParametersValidator multipleValidator() {
        CompositeJobParametersValidator validator = new CompositeJobParametersValidator();
        //여러개 검증 validator 등록이 가능하다
        validator.setValidators(Arrays.asList(new FileParamValidator(), new FileParamEmptyCheck()));
        return validator;
    }

    @Bean
    public Step vaildateParamStep(JobRepository jobRepository
    , PlatformTransactionManager platformTransactionManager
    , Tasklet vaildateParamTasklet) {
        return new StepBuilder("vaildateParamStep", jobRepository)
                .tasklet(vaildateParamTasklet, platformTransactionManager)
                .build();
    }

    @Bean
    @JobScope
    public Tasklet vaildateParamTasklet(
            @Value("#{jobParameters['fileName']}")String fileName
    ) {
        return new Tasklet() {
            @Override
            public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext)
                    throws Exception {
                System.out.println("fileName = " + fileName);
                System.out.println("vaildate Param Tasklet");
                return RepeatStatus.FINISHED;
            }
        };
    }
}
