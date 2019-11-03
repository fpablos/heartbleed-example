package com.quemepongo.domain;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(schema = "quemepongo",name ="contracts")
public class Contract {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private Long customerId; //CustomerId == userId
    private String customerDocument;
    private String customerName;
    private String deliveryAddress;
    private String deliveryDate; //String!? Sehh, es mas facil asi :P
    private BigDecimal netUsdAmount;
    private BigDecimal vatAmount;
    private BigDecimal totalUsdAmount;


    //For JPA
    public Contract() {}

    public Contract(Long customerId, String customerDocument, String customerName, String deliveryAddress, LocalDate deliveryDate, BigDecimal netUsdAmount, BigDecimal vatAmount, BigDecimal totalUsdAmount) {
        this.customerId = customerId;
        this.customerDocument = customerDocument;
        this.customerName = customerName;
        this.deliveryAddress = deliveryAddress;
        this.deliveryDate = deliveryDate.toString();
        this.netUsdAmount = netUsdAmount;
        this.vatAmount = vatAmount;
        this.totalUsdAmount = totalUsdAmount;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public String getCustomerDocument() {
        return customerDocument;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public BigDecimal getNetUsdAmount() {
        return netUsdAmount;
    }

    public Long getId() {
        return id;
    }

    public BigDecimal getVatAmount() {
        return vatAmount;
    }

    public BigDecimal getTotalUsdAmount() {
        return totalUsdAmount;
    }
}
