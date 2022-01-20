package pl.patrykzajac.digitalcolliers.context;

import com.opencsv.bean.CsvToBeanBuilder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import pl.patrykzajac.digitalcolliers.entity.csv.FeeWages;
import pl.patrykzajac.digitalcolliers.entity.csv.Transaction;

import javax.annotation.PostConstruct;
import java.io.IOException;
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
        this.transactions = new CsvToBeanBuilder<Transaction>(Files.newBufferedReader(Paths.get(ClassLoader.getSystemResource("csv/transactions.csv").toURI())))
                .withType(Transaction.class).build().parse();
        log.info(String.format("transactions loaded: %s", transactions.size()));
        this.feeWages = new CsvToBeanBuilder<FeeWages>(Files.newBufferedReader(Paths.get(ClassLoader.getSystemResource("csv/fee_wages.csv").toURI())))
                .withType(FeeWages.class).build().parse().stream().sorted(Comparator.comparing(FeeWages::getTransactionValueLessThen)).collect(Collectors.toList());
        log.info(String.format("feeWages loaded: %s", feeWages.size()));
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
