package com.example.SpringBatchTutorial.job.fileReadWrite;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class FileDataReadWriteConfig {

    @Bean
    public Job fileReadWriteJob(JobRepository jobRepository
            , Step fileReadWriteStep) {
        return new JobBuilder("fileReadWriteJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(fileReadWriteStep)
                .build();
    }

    @Bean
    public Step fileReadWriteStep(JobRepository jobRepository
            , PlatformTransactionManager transactionManager
            , FlatFileItemReader<Player> fileItemReader
            , ItemProcessor playerItemProcessor
            , ItemWriter fileItemWriter) {
        return new StepBuilder("fileReadWriteStep", jobRepository)
                .<Player, PlayerYears>chunk(5, transactionManager)
                .reader(fileItemReader)
                /*      .writer(new ItemWriter<Player>() {
                          @Override
                          public void write(Chunk<? extends Player> items) throws Exception {
                              items.forEach(System.out::println);
                          }
                      })*/
                .processor(playerItemProcessor)
                //다른 파일로 작성
                .writer(fileItemWriter)
                .build();
    }


    //FlatFileItemReader -> 파일에서 데이터를 읽어오는 Reader
    @Bean
    public FlatFileItemReader<Player> fileItemReader() {
        return new FlatFileItemReaderBuilder<Player>()
                .name("fileItemReader")
                .resource(new FileSystemResource(".data/players.csv"))
                //데이터를 어떤 기준으로 나눌건지(DelimitedLineTokenizer -> 토큰 단위)
                .lineTokenizer(new DelimitedLineTokenizer())
                //읽어온 데이터를 객체로 변경 할 수 있도록 mapper 필요
                .fieldSetMapper(new PlayerFieldSetMapper())
                //첫번째 헤더 스킵
                .linesToSkip(1)
                .build();
    }


    @Bean
    public ItemProcessor<Player, PlayerYears> playerItemProcessor() {
        return new ItemProcessor<Player, PlayerYears>() {

            @Override
            public PlayerYears process(Player player) throws Exception {
                return new PlayerYears(player);
            }
        };
    }

    @Bean
    public FlatFileItemWriter<PlayerYears> fileItemWriter() {
        BeanWrapperFieldExtractor<PlayerYears> fieldExtractor = new BeanWrapperFieldExtractor<>();
        //새로운 파일 생성
        fieldExtractor.setNames(new String[]{"ID", "lastName", "position", "firstName", "yearsExperience"});
        fieldExtractor.afterPropertiesSet();
        //어떤 기준으로 파일을 만들지 알려주는 것
        DelimitedLineAggregator<PlayerYears> lineAggregator = new DelimitedLineAggregator<>();
        //어떤 기준으로 만들지 우리는 csv 파일이기에 ,로 만든다
        lineAggregator.setDelimiter(",");
        //필드 추출
        lineAggregator.setFieldExtractor(fieldExtractor);

        FileSystemResource outputResource = new FileSystemResource(".data/Players_output.txt");

        return new FlatFileItemWriterBuilder<PlayerYears>()
                .name("fileItemWriter")
                .resource(outputResource)
                //구분자 콤마
                .lineAggregator(lineAggregator)
                .build();
    }
}
