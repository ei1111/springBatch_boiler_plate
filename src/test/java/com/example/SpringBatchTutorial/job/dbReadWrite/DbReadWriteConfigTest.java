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
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBatchTest
@ActiveProfiles("test")
@SpringBootTest(classes = {SpringBatchTestConfig.class, DbReadWriteConfig.class})
class DbReadWriteConfigTest {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

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
    void sucess_noData() throws Exception {
        //when
        //HellowWorldJobConfig 실행
        JobExecution execution = jobLauncherTestUtils.launchJob();

        //then
        Assertions.assertEquals(execution.getExitStatus(), ExitStatus.COMPLETED);
        Assertions.assertEquals(0, accountRepository.count());
    }

    @DisplayName("데이터가 주어졌을때")
    @Test
    void sucess_existData() throws Exception {
        //given
        Order orders1 = new Order(null, "kakao gift", 15000, LocalDate.now());
        Order orders2 = new Order(null, "naver gift", 15000, LocalDate.now());

        orderRepository.saveAll(Arrays.asList(orders1, orders2));

        //when
        JobExecution execution = jobLauncherTestUtils.launchJob();

        //then
        Assertions.assertEquals(execution.getExitStatus(), ExitStatus.COMPLETED);
        Assertions.assertEquals(2, accountRepository.count());
    }

}