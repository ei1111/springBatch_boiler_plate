package com.example.SpringBatchTutorial.job.dbReadWrite;

import com.example.SpringBatchTutorial.job.dbReadWrite.account.Account;
import com.example.SpringBatchTutorial.job.dbReadWrite.account.AccountRepository;
import com.example.SpringBatchTutorial.job.dbReadWrite.order.Order;
import com.example.SpringBatchTutorial.job.dbReadWrite.order.OrderRepository;
import java.util.Arrays;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class DbReadWriteConfig {
    private final OrderRepository orderRepository;
    private final AccountRepository accountRepository;

    @Bean
    public Job dbConnectionJob(
            JobRepository jobRepository
            , Step dbConnectionStep) {
        return new JobBuilder("dbConnectionJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(dbConnectionStep)
                .build();
    }

    @Bean
    public Step dbConnectionStep(JobRepository jobRepository
            , PlatformTransactionManager transactionManager
            , ItemReader<Order> orderReader
            , ItemProcessor<Order, Account> orderToProcessor
            , ItemWriter<Account> orderWriter) {
        return new StepBuilder("dbConnectionStep", jobRepository)
                //어떤 데이터로 읽어 와서 어떤 데이터로 쓸것인지,chunk: 몇개의 단위로 데이터를 처리할것인지(처리할 트랜잭션 갯수)
                //순수자바는 반복문을 통해 갯수를 세어서 갯수를 끊어줘야 했지만 배치는 알아서 chunk로 알아서 끊어줌
                .<Order, Account>chunk(5, transactionManager)
                .reader(orderReader)
                .processor(orderToProcessor)
                .writer(orderWriter)
                .build();
    }

    @Bean
    public RepositoryItemReader<Order> orderReader() {
        return new RepositoryItemReaderBuilder<Order>()
                .name("orderReader")
                .repository(orderRepository)
                .methodName("findAll")
                //보통 chunk 사이즈와 읽을 사이즈를 같게 한다
                .pageSize(5)
                //파라미터 존재시 aruments 사용해서 입력
                .arguments(Arrays.asList())
                //정렬
                .sorts(Collections.singletonMap("id", Direction.ASC))
                .build();
    }

    //입력될 DB
 /*   @Bean
    public RepositoryItemWriter<Account> orderWriter() {
        return new RepositoryItemWriterBuilder<Account>()
                .repository(accountRepository)
                .methodName("save")
                .build();
    }
*/
    @Bean
    public ItemWriter<Account> orderWriter() {
            return new ItemWriter<Account>() {
                @Override
                public void write(Chunk<? extends Account> items) throws Exception {
                    items.forEach(item -> accountRepository.save(item));
                }
            };
    }

    @Bean
    public ItemProcessor<Order, Account> orderToProcessor() {
        return new ItemProcessor<Order, Account>() {
            @Override
            public Account process(Order order) throws Exception {
                return new Account(order);
            }
        };
    }
}
