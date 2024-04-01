package com.example.demo.model;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "feedbacks")
public class Feedback {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne()
    @JoinColumn(name = "user_id")
    @Nullable
    private User user;
    @OneToOne()
    @JoinColumn(name = "order_id")
    @Nullable
    private Order order;
    @Column
    private Integer score;
    @Column
    private String comment;
    @Column
    private Timestamp created;
}
