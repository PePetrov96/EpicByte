package com.project.EpicByte.model.entity;

import com.project.EpicByte.model.entity.productEntities.OrderItem;
import jakarta.persistence.*;

import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "customer_order")
@Getter @Setter @NoArgsConstructor
public class Order extends BaseEntity {
    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    private UserEntity user;

    @Column(name = "date_ordered")
    private LocalDate orderDate;

    @Column(name = "city")
    private String city;

    @Column(name = "neighborhood")
    private String neighborhood;

    @Column(name = "address")
    private String address;

    @Column(name = "total_cost")
    private BigDecimal totalCost;

    @Column(name = "is_complete")
    private boolean isComplete;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();
}