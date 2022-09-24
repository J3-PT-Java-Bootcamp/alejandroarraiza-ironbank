package com.ironhack.ironbankapi.accounts.service;

import com.google.firebase.auth.FirebaseAuthException;
import com.ironhack.ironbankapi.accounts.dto.CreateCheckingAccountDto;
import com.ironhack.ironbankapi.accounts.dto.CreateCreditAccountDto;
import com.ironhack.ironbankapi.accounts.dto.CreateSavingsAccountDto;
import com.ironhack.ironbankapi.auth.exceptions.IronbankAuthException;
import com.ironhack.ironbankapi.auth.service.UserService;
import com.ironhack.ironbankapi.core.model.account.*;
import com.ironhack.ironbankapi.core.model.common.Money;
import com.ironhack.ironbankapi.core.model.user.User;
import com.ironhack.ironbankapi.core.model.user.UserRole;
import com.ironhack.ironbankapi.core.repository.account.CheckingAccountRepository;
import com.ironhack.ironbankapi.core.repository.account.CreditAccountRepository;
import com.ironhack.ironbankapi.core.repository.account.SavingsAccountRepository;
import com.ironhack.ironbankapi.core.repository.firebase.FirebaseRepository;
import com.ironhack.ironbankapi.core.repository.transaction.TransactionRepository;
import com.ironhack.ironbankapi.core.repository.user.UserRepository;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class AccountServiceTest {

    @MockBean
    FirebaseRepository firebaseRepository;

    @Autowired
    UserService userService;

    @Autowired
    AccountService accountService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    CheckingAccountRepository checkingAccountRepository;

    @Autowired
    SavingsAccountRepository savingsAccountRepository;

    @Autowired
    CreditAccountRepository creditAccountRepository;

    User accountHolder;
    User accountHolderStudent;
    User admin;
    User thirdParty;


    @SneakyThrows
    @BeforeEach
    void setUp() {
        transactionRepository.deleteAllInBatch();
        checkingAccountRepository.deleteAllInBatch();
        creditAccountRepository.deleteAllInBatch();
        savingsAccountRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
        accountHolder = createUser(UserRole.ACCOUNT_HOLDER, "accountHolder", null, null);
        accountHolderStudent = createUser(UserRole.ACCOUNT_HOLDER, "student", LocalDate.of(2010, 4, 3), null);
        admin = createUser(UserRole.ADMIN, "admin", null, null);
        thirdParty = createUser(UserRole.THIRD_PARTY, "thirdParty", null, "thisisahashShshshsh");
    }

    private User createUser(UserRole userRole, String name, LocalDate birthDate, String externalAcc) throws FirebaseAuthException, IronbankAuthException {

        var user = User
                .builder()
                .dateOfBirth(birthDate == null ? LocalDate.of(1922,4,8) : birthDate)
                .userRole(userRole)
                .name(name)
                .email(name + "@email.com")
                .externalAccount(externalAcc)
                .build();

        when(firebaseRepository.createUser(user.getEmail(), user.getName(), user.getUserRole())).thenReturn(UUID.randomUUID().toString());

        return userService.createUser(user);
    }

    @Test
    void createCheckingAccount_ok() {
        Money balance = new Money(new BigDecimal("500"));
        String secretKey = "123456";
        CreateCheckingAccountDto createCheckingAccountDto = CreateCheckingAccountDto
                .builder()
                .balance(balance)
                .primaryOwnerId(accountHolder.getId())
                .secretKey(secretKey)
                .build();
        Exception exc = null;
        try{
            var accountCreated = accountService.createCheckingAccount(admin, createCheckingAccountDto);
            assertEquals(balance.getAmount(), accountCreated.getBalance());
            assertEquals(CheckingAccountType.DEFAULT, accountCreated.getCheckingAccountType());
            assertEquals(accountHolder, accountCreated.getPrimaryOwner());
            assertEquals(AccountStatus.ACTIVE, accountCreated.getStatus());
            assertEquals(secretKey, accountCreated.getSecretKey());
            assertEquals(CheckingAccount.MINIMUM_BALANCE, accountCreated.getMinimumBalance());
            assertEquals(CheckingAccount.MONTHLY_MAINTENANCE_FEE, accountCreated.getMonthlyMaintenanceFee());
        }catch (Exception e){
            exc = e;
        }
        assertNull(exc);
    }

    @Test
    void createStudentCheckingAccount_ok() {
        Money balance = new Money(new BigDecimal("500"));
        String secretKey = "123456";
        CreateCheckingAccountDto createCheckingAccountDto = CreateCheckingAccountDto
                .builder()
                .balance(balance)
                .primaryOwnerId(accountHolderStudent.getId())
                .secretKey(secretKey)
                .build();
        Exception exc = null;
        try{
            var accountCreated = accountService.createCheckingAccount(admin, createCheckingAccountDto);
            assertEquals(balance.getAmount(), accountCreated.getBalance());
            assertEquals(CheckingAccountType.STUDENT, accountCreated.getCheckingAccountType());
            assertEquals(accountHolderStudent, accountCreated.getPrimaryOwner());
            assertEquals(AccountStatus.ACTIVE, accountCreated.getStatus());
            assertEquals(secretKey, accountCreated.getSecretKey());
            assertEquals(new Money(new BigDecimal(0)), accountCreated.getMinimumBalance());
            assertEquals(new Money(new BigDecimal(0)), accountCreated.getMonthlyMaintenanceFee());
        }catch (Exception e){
            exc = e;
        }
        assertNull(exc);
    }

    @Test
    void createCreditAccount_ok() {
        Money balance = new Money(new BigDecimal("50"));
        String secretKey = "123456";
        CreateCreditAccountDto createCreditAccountDto = CreateCreditAccountDto
                .builder()
                .balance(balance)
                .primaryOwnerId(accountHolderStudent.getId())
                .secretKey(secretKey)
                .build();
        Exception exc = null;
        try{
            var accountCreated = accountService.createCreditAccount(admin, createCreditAccountDto);
            assertEquals(balance.getAmount(), accountCreated.getBalance());
            assertEquals(accountHolderStudent, accountCreated.getPrimaryOwner());
            assertEquals(AccountStatus.ACTIVE, accountCreated.getStatus());
            assertEquals(secretKey, accountCreated.getSecretKey());
            assertEquals(CreditAccount.DEFAULT_CREDIT_LIMIT, accountCreated.getCreditLimit());
            assertEquals(CreditAccount.DEFAULT_INTEREST_RATE, accountCreated.getInterestRate());
        }catch (Exception e){
            exc = e;
        }
        assertNull(exc);
    }

    @Test
    void createSavingsAccount_ok() {
        Money balance = new Money(new BigDecimal("5000"));
        String secretKey = "123456";
        CreateSavingsAccountDto createSavingsAccountDto = CreateSavingsAccountDto
                .builder()
                .balance(balance)
                .primaryOwnerId(accountHolderStudent.getId())
                .secretKey(secretKey)
                .build();
        Exception exc = null;
        try{
            var accountCreated = accountService.createSavingsAccount(admin, createSavingsAccountDto);
            assertEquals(balance.getAmount(), accountCreated.getBalance());
            assertEquals(accountHolderStudent, accountCreated.getPrimaryOwner());
            assertEquals(AccountStatus.ACTIVE, accountCreated.getStatus());
            assertEquals(secretKey, accountCreated.getSecretKey());
            assertEquals(SavingsAccount.DEFAULT_INTEREST_RATE, accountCreated.getInterestRate());
            assertEquals(SavingsAccount.DEFAULT_MINIMUM_BALANCE, accountCreated.getMinimumBalance());
        }catch (Exception e){
            exc = e;
        }
        assertNull(exc);
    }
}