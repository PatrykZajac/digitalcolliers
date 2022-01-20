package pl.patrykzajac.digitalcolliers.entity.csv;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvDate;
import com.opencsv.bean.CsvNumber;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

//transaction_id,transaction_amount,customer_first_name,customer_id,customer_last_name,transaction_date
@Data
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {
    @CsvBindByName(column = "transaction_id")
    private String transactionId;

    @CsvBindByName(column = "transaction_amount")
    @CsvNumber("#,##")
    private BigDecimal transactionAmount;

    @CsvBindByName(column = "customer_first_name")
    private String customerFirstName;

    @CsvBindByName(column = "customer_last_name")
    private String customerLastName;

    @CsvBindByName(column = "customer_id")
    private String customerId;

    @CsvBindByName(column = "transaction_date")
    @CsvDate("dd.MM.yyyy HH:mm:ss")
    private Date transactionDate;
}
