package test.aston.bank.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import test.aston.bank.enums.TransactionType;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Data
@NoArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column
    private Long accountId;
    @Column
    private Long recipientId;
    @Column
    private Double amount;
    @Column
    @CreationTimestamp
    private Instant createdOn;
    @Column
    @Enumerated(EnumType.STRING)
    private TransactionType type;

    public Transaction(Long accountId, Double amount, TransactionType type) {
        this.accountId = accountId;
        this.amount = amount;
        this.type = type;
    }

    public Transaction(Long accountId, Long recipientId, Double amount, TransactionType type) {
        this.accountId = accountId;
        this.recipientId = recipientId;
        this.amount = amount;
        this.type = type;
    }
}
