package pl.patrykzajac.digitalcolliers.entity.csv;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvNumber;
import lombok.*;

import java.math.BigDecimal;

//transaction_value_less_than,fee_percentage_of_transaction_value
@Data
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class FeeWages {
    @CsvBindByName(column = "transaction_value_less_than")
    private BigDecimal transactionValueLessThen;

    @CsvBindByName(column = "fee_percentage_of_transaction_value")
    @CsvNumber("#,##")
    private BigDecimal feePercentageOfTransactionValue;
}
