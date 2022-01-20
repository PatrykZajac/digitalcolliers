package pl.patrykzajac.digitalcolliers.repository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.patrykzajac.digitalcolliers.context.CsvContext;
import pl.patrykzajac.digitalcolliers.dto.Customer;
import pl.patrykzajac.digitalcolliers.entity.csv.FeeWages;
import pl.patrykzajac.digitalcolliers.entity.csv.Transaction;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class UserRepository {

    private final CsvContext csvContext;

    @Nullable
    public List<Customer> allUserData() {
        List<String> customerIds = csvContext.getCustomerIds();
        Map<String, List<Transaction>> transactions = csvContext.getTransactionsData(customerIds);
        return processTransactionsList(transactions);
    }

    @Nullable
    public List<Customer> userDataById(@NotNull List<String> ids) {
        List<String> customerIds = csvContext.getCustomerIds().stream().filter(ids::contains).collect(Collectors.toList());
        Map<String, List<Transaction>> transactions = csvContext.getTransactionsData(customerIds);
        return processTransactionsList(transactions);
    }


    private List<Customer> processTransactionsList(Map<String, List<Transaction>> transactionsGroupedByCustomerId) {
        List<Customer> customers = new ArrayList<>();
        transactionsGroupedByCustomerId.entrySet().parallelStream().forEach(element -> {
            String customerId = element.getKey();
            List<Transaction> transactions = element.getValue().stream().sorted(Comparator.comparing(Transaction::getTransactionDate)).collect(Collectors.toList());
            Collections.reverse(transactions);
            BigDecimal transactionsValue = transactions.stream().map(Transaction::getTransactionAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
            FeeWages feeWages = csvContext.getFeeWagesForTransactionAmount(transactionsValue);
            BigDecimal commissionsValue = feeWages.getFeePercentageOfTransactionValue().multiply(transactionsValue).divide(new BigDecimal(100), 2, RoundingMode.HALF_UP);
            Transaction lastTransaction = transactions.iterator().next();
            int numberOfTransactions = transactions.size();
            Customer customer = new Customer(customerId, lastTransaction.getCustomerFirstName(), lastTransaction.getCustomerLastName(), numberOfTransactions, transactionsValue, commissionsValue, lastTransaction.getTransactionDate());
            customers.add(customer);
        });

        return customers.stream().sorted(Comparator.comparing(Customer::getCustomerId)).collect(Collectors.toList());
    }
}
