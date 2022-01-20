package pl.patrykzajac.digitalcolliers.controller;

import org.bson.types.ObjectId;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import pl.patrykzajac.digitalcolliers.context.CsvContext;
import pl.patrykzajac.digitalcolliers.dto.Customer;
import pl.patrykzajac.digitalcolliers.entity.csv.FeeWages;
import pl.patrykzajac.digitalcolliers.entity.csv.Transaction;
import pl.patrykzajac.digitalcolliers.entity.mongo.CalculationLog;
import pl.patrykzajac.digitalcolliers.repository.LogRepository;
import pl.patrykzajac.digitalcolliers.repository.UserRepository;
import pl.patrykzajac.digitalcolliers.service.LogService;
import pl.patrykzajac.digitalcolliers.service.UserService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserControllerTest {

    private UserController userController;
    private UserService userService;
    private UserRepository userRepository;

    private LogService logService;
    @Mock
    private LogRepository logRepository;

    @Mock
    private CsvContext csvContext;

    @Before
    public void init() {
        CalculationLog calculationLog = new CalculationLog(new ObjectId(), "", BigDecimal.ZERO, LocalDateTime.now());
        when(logRepository.save(Mockito.any(CalculationLog.class))).thenReturn(calculationLog);
        when(csvContext.getFeeWages()).thenReturn(getFeeWages());
        when(csvContext.getTransactionsGroupedByCustomerId()).thenReturn(getMap());
        when(csvContext.getTransactionsData(Mockito.any(List.class))).thenCallRealMethod();
        when(csvContext.getFeeWagesForTransactionAmount(Mockito.any(BigDecimal.class))).thenCallRealMethod();
        when(csvContext.getCustomerIds()).thenReturn(getIds());
        userRepository = new UserRepository(csvContext);
        logService = new LogService(logRepository);
        userService = new UserService(userRepository, logService);
        userController = new UserController(userService);
    }

    private Map<String, List<Transaction>> getMap() {
        Map<String, List<Transaction>> result = new HashMap<>();
        result.put("1", Arrays.asList(
                new Transaction("1", new BigDecimal("100"), "Adam", "Nowak", "1", new Date()),
                new Transaction("2", new BigDecimal("200"), "Adam", "Nowak", "1", new Date()),
                new Transaction("3", new BigDecimal("500"), "Adam", "Nowak", "1", new Date())));
        result.put("2", Arrays.asList(
                new Transaction("4", new BigDecimal("500"), "Jan", "Nowak", "2", new Date()),
                new Transaction("5", new BigDecimal("700"), "Jan", "Nowak", "2", new Date()),
                new Transaction("6", new BigDecimal("100"), "Jan", "Nowak", "2", new Date())));
        result.put("3", Arrays.asList(
                new Transaction("7", new BigDecimal("900"), "Adam", "Kowalski", "3", new Date()),
                new Transaction("8", new BigDecimal("900"), "Adam", "Kowalski", "3", new Date()),
                new Transaction("9", new BigDecimal("900"), "Adam", "Kowalski", "3", new Date())));
        result.put("4", Arrays.asList(
                new Transaction("10", new BigDecimal("100"), "Paweł", "Nowak", "4", new Date()),
                new Transaction("11", new BigDecimal("10"), "Paweł", "Nowak", "4", new Date()),
                new Transaction("12", new BigDecimal("10"), "Paweł", "Nowak", "4", new Date())));
        result.put("5", Arrays.asList(
                new Transaction("13", new BigDecimal("1000"), "Adam", "Polak", "5", new Date()),
                new Transaction("14", new BigDecimal("3000"), "Adam", "Polak", "5", new Date()),
                new Transaction("15", new BigDecimal("2000"), "Adam", "Polak", "5", new Date())));


        return result;
    }

    private List<String> getIds() {
        return Arrays.asList("1", "2", "3", "4", "5");
    }

    private List<FeeWages> getFeeWages() {
        List<FeeWages> result = new ArrayList<>();
        result.add(new FeeWages(new BigDecimal(1000), new BigDecimal("5")));
        result.add(new FeeWages(new BigDecimal(2000), new BigDecimal("2")));
        result.add(new FeeWages(new BigDecimal(5000), new BigDecimal("1")));
        return result;
    }

    @Test
    public void test_getAllUsersCount() {

        List<Customer> customers = userController.getData(Collections.singletonList("ALL"));
        Assert.assertEquals(5, customers.size());
    }

    @Test
    public void test_get3UsersCount() {
        List<Customer> customers = userController.getData(Arrays.asList("1", "3", "4"));
        Assert.assertEquals(3, customers.size());
    }

    @Test
    public void test_checkIfCorrectCustomerSelected() {
        List<Customer> customers = userController.getData(Arrays.asList("1"));
        Assert.assertEquals("1", customers.get(0).getCustomerId());
        Assert.assertEquals("Adam", customers.get(0).getFirstName());
        Assert.assertEquals("Nowak", customers.get(0).getLastName());
    }

    @Test
    public void test_checkIfCorrectTransactionAmountAndCommissionAmountCalculated() {
        List<Customer> customers = userController.getData(Arrays.asList("1"));
        Assert.assertEquals(new BigDecimal(800), customers.get(0).getTotalValueOfTransactions());
        Assert.assertEquals(new BigDecimal("40.00"), customers.get(0).getTransactionFeeValue());
        Assert.assertEquals(3, customers.get(0).getNumberOfTransactions());
    }


}
