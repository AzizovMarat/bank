package test.aston.bank.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Entity
@NoArgsConstructor
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column
    private String name;
    @Column
    private String pin;
    @Column
    private Double score;
//    @OneToMany(fetch = FetchType.LAZY, mappedBy = "account", cascade = CascadeType.ALL)
//    @JoinColumn(name = "account_id")
//    private Set<Transaction> transactions = new HashSet<>();

    public Account(String name, String pin, Double score) {
        this.name = name;
        this.pin = pin;
        this.score = score;
    }
}
