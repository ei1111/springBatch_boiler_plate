package com.example.SpringBatchTutorial.job.dbReadWrite.account;

import com.example.SpringBatchTutorial.job.dbReadWrite.order.Order;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "accounts")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String orderItem;
    private int price;
    private LocalDate orderDate;
    private LocalDate accountDate;


    public Account(Order order) {
        this.orderItem = order.getOrderItem();
        this.price = order.getPrice();
        this.orderDate = order.getOrderDate();
        this.accountDate = LocalDate.now();
    }
}
