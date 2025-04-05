package com.example.SpringBatchTutorial.job.dbReadWrite;

import com.example.SpringBatchTutorial.job.SpringBatchTestConfig;
import com.example.SpringBatchTutorial.job.dbReadWrite.account.AccountRepository;
import com.example.SpringBatchTutorial.job.dbReadWrite.order.Order;
import com.example.SpringBatchTutorial.job.dbReadWrite.order.OrderRepository;
import java.time.LocalDate;
import java.util.Arrays;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestPropertySource;

@TestPropertySource(properties = {"spring.batch.job.name=dbConnectionJob"})
class DbReadWriteConfigTest extends SpringBatchTestConfig {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private AccountRepository accountRepository;


    @AfterEach
    public void cleanUpEach() {
        orderRepository.deleteAll();
        accountRepository.deleteAll();
    }

    @DisplayName("데이터 없을때 테스트")
    @Test
    void sucess_noData(@Autowired Job dbConnectionJob) throws Exception {
        //when
        //HellowWorldJobConfig 실행
        jobLauncherTestUtils.setJob(dbConnectionJob);
        JobExecution execution = jobLauncherTestUtils.launchJob();

        //then
        Assertions.assertEquals(execution.getExitStatus(), ExitStatus.COMPLETED);
        Assertions.assertEquals(0, accountRepository.count());
    }

    @DisplayName("데이터가 주어졌을때")
    @Test
    void sucess_existData(@Autowired Job dbConnectionJob) throws Exception {
        //given
        Order orders1 = new Order(null, "kakao gift", 15000, LocalDate.now());
        Order orders2 = new Order(null, "naver gift", 15000, LocalDate.now());

        orderRepository.saveAll(Arrays.asList(orders1, orders2));

        //when
        jobLauncherTestUtils.setJob(dbConnectionJob);
        JobExecution execution = jobLauncherTestUtils.launchJob();

        //then
        Assertions.assertEquals(execution.getExitStatus(), ExitStatus.COMPLETED);
        Assertions.assertEquals(2, accountRepository.count());
    }

}