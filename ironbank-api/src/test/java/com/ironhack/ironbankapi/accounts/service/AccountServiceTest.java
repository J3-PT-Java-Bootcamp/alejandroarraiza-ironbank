package com.ironhack.ironbankapi.accounts.service;

import com.google.firebase.auth.FirebaseAuthException;
import com.ironhack.ironbankapi.accounts.dto.CreateCheckingAccountDto;
import com.ironhack.ironbankapi.auth.exceptions.IronbankAuthException;
import com.ironhack.ironbankapi.auth.service.UserService;
import com.ironhack.ironbankapi.core.model.account.AccountStatus;
import com.ironhack.ironbankapi.core.model.account.CheckingAccount;
import com.ironhack.ironbankapi.core.model.account.CheckingAccountType;
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
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class AccountServiceTest {

    @Autowired
    UserService userService;

    @MockBean
    FirebaseRepository firebaseRepository;

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
    void createCreditAccount_ok() {
    }

    @Test
    void createSavingsAccount_ok() {
    }

    @Test
    void getAllCheckingAccounts_ok() {
    }

    @Test
    void getAllSavingsAccounts_ok() {
    }

    @Test
    void getAllCreditAccounts_ok() {
    }

    @Test
    void getAllCheckingAccountsByUserId_ok() {
    }

    @Test
    void getAllSavingsAccountsByUserId_ok() {
    }

    @Test
    void getAllCreditAccountsByUserId_ok() {
    }

    @Test
    void getAccountByAccountNumber_ok() {
    }

    @Test
    void closeAccount_ok() {
    }
}