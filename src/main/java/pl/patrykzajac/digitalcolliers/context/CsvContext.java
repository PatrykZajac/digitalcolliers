package pl.patrykzajac.digitalcolliers.context;

import com.opencsv.bean.CsvToBeanBuilder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import pl.patrykzajac.digitalcolliers.entity.csv.FeeWages;
import pl.patrykzajac.digitalcolliers.entity.csv.Transaction;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Getter
@Slf4j
public class CsvContext {

    private List<Transaction> transactions;

    private List<FeeWages> feeWages;

    private Map<String, List<Transaction>> transactionsGroupedByCustomerId;
    private List<String> customerIds;

    @PostConstruct
    public void init() throws URISyntaxException, IOException {
        loadData();
        processRawData();
    }

    private void loadData() throws URISyntaxException, IOException {
        this.transactions = new CsvToBeanBuilder<Transaction>(new BufferedReader( new InputStreamReader(getClass().getClassLoader().getResourceAsStream("csv/transactions.csv"))))
                .withType(Transaction.class).build().parse();
        this.feeWages = new CsvToBeanBuilder<FeeWages>(new BufferedReader( new InputStreamReader(getClass().getClassLoader().getResourceAsStream("csv/fee_wages.csv"))))
                .withType(FeeWages.class).build().parse().stream().sorted(Comparator.comparing(FeeWages::getTransactionValueLessThen)).collect(Collectors.toList());
    }

    private void processRawData() {
        this.customerIds = transactions.stream().map(Transaction::getCustomerId).distinct().collect(Collectors.toList());
        this.transactionsGroupedByCustomerId = transactions.stream().collect(Collectors.groupingBy(Transaction::getCustomerId));
    }

    public Map<String, List<Transaction>> getTransactionsData(List<String> customersId) {
        return this.getTransactionsGroupedByCustomerId().entrySet().stream().filter(item -> customersId.contains(item.getKey())).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public FeeWages getFeeWagesForTransactionAmount(BigDecimal transactionAmount) {
        for (FeeWages feeWage : this.getFeeWages()) {
            if (feeWage.getTransactionValueLessThen().compareTo(transactionAmount) >= 0) {
                return feeWage;
            }
        }
        return new FeeWages(null, BigDecimal.ZERO);

    }
}
