package com.ironhack.ironbankapi.accounts.service;

import com.google.firebase.auth.FirebaseAuthException;
import com.ironhack.ironbankapi.accounts.dto.CreateCheckingAccountDto;
import com.ironhack.ironbankapi.auth.exceptions.IronbankAuthException;
import com.ironhack.ironbankapi.auth.service.UserService;
import com.ironhack.ironbankapi.core.model.account.Account;
import com.ironhack.ironbankapi.core.model.common.Money;
import com.ironhack.ironbankapi.core.model.transaction.TransactionResult;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

@SpringBootTest
class TransactionServiceTest {

    @MockBean
    FirebaseRepository firebaseRepository;

    @Autowired
    UserService userService;

    @Autowired
    AccountService accountService;

    @Autowired
    TransactionService transactionService;

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
    User admin;
    User thirdParty;
    Account account;
    Account account2;

    @SneakyThrows
    @BeforeEach
    void setUp() {
        transactionRepository.deleteAllInBatch();
        checkingAccountRepository.deleteAllInBatch();
        creditAccountRepository.deleteAllInBatch();
        savingsAccountRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
        accountHolder = createUser(UserRole.ACCOUNT_HOLDER, "accountHolder", null);
        admin = createUser(UserRole.ADMIN, "admin", null);
        thirdParty = createUser(UserRole.THIRD_PARTY, "thirdParty", "thisisahashShshshsh");
        account = createAccount();
        account2 = createAccount();
    }

    private User createUser(UserRole userRole, String name, String externalAcc)
            throws FirebaseAuthException, IronbankAuthException {

        var user = User
                .builder()
                .dateOfBirth(LocalDate.of(1922, 4, 8))
                .userRole(userRole)
                .name(name)
                .email(name + "@email.com")
                .externalAccount(externalAcc)
                .build();

        when(firebaseRepository.createUser(user.getEmail(), user.getName(), user.getUserRole()))
                .thenReturn(UUID.randomUUID().toString());

        return userService.createUser(user);
    }

    @SneakyThrows
    private Account createAccount() {

        Money balance = new Money(new BigDecimal("500"));
        String secretKey = "123456";
        CreateCheckingAccountDto createCheckingAccountDto = CreateCheckingAccountDto
                .builder()
                .balance(balance)
                .primaryOwnerId(accountHolder.getId())
                .secretKey(secretKey)
                .build();
        return accountService.createCheckingAccount(admin, createCheckingAccountDto);
    }

    @Test
    void updateBalance_ok() {

        Money balance = new Money(new BigDecimal("500"));
        Money newBalance = new Money(new BigDecimal("3000"));
        Exception exc = null;
        try {
            var accountBefore = accountService.getAccountByAccountNumber(account.getAccountNumber().getIban());
            assertEquals(balance.getAmount(), accountBefore.getBalance());
            var traRes = transactionService.updateBalance(account, newBalance.getAmount(), admin);
            var accountAfter = accountService.getAccountByAccountNumber(account.getAccountNumber().getIban());
            assertEquals(TransactionResult.EXECUTED, traRes.getTransactionResult());
            assertEquals(newBalance.getAmount(), accountAfter.getBalance());
        } catch (Exception e) {
            exc = e;
        }
        assertNull(exc);
    }

    @Test
    void withdraw_ok() {

        Money withdraw = new Money(new BigDecimal("50"));
        Money balance = new Money(new BigDecimal("500"));
        Money newBalance = new Money(new BigDecimal("450"));
        Exception exc = null;
        try {
            var accountBefore = accountService.getAccountByAccountNumber(account.getAccountNumber().getIban());
            assertEquals(balance.getAmount(), accountBefore.getBalance());

            var traRes = transactionService.withdraw(account, withdraw.getAmount(), accountHolder,
                    account.getSecretKey());

            var accountAfter = accountService.getAccountByAccountNumber(account.getAccountNumber().getIban());
            assertEquals(TransactionResult.EXECUTED, traRes.getTransactionResult());
            assertEquals(newBalance.getAmount(), accountAfter.getBalance());
        } catch (Exception e) {
            exc = e;
        }
        assertNull(exc);
    }

    @Test
    void deposit_ok() {

        Money deposit = new Money(new BigDecimal("50"));
        Money balance = new Money(new BigDecimal("500"));
        Money newBalance = new Money(new BigDecimal("550"));
        Exception exc = null;
        try {
            var accountBefore = accountService.getAccountByAccountNumber(account.getAccountNumber().getIban());
            assertEquals(balance.getAmount(), accountBefore.getBalance());

            var traRes = transactionService.deposit(account, deposit.getAmount(), accountHolder,
                    account.getSecretKey());

            var accountAfter = accountService.getAccountByAccountNumber(account.getAccountNumber().getIban());
            assertEquals(TransactionResult.EXECUTED, traRes.getTransactionResult());
            assertEquals(newBalance.getAmount(), accountAfter.getBalance());
        } catch (Exception e) {
            exc = e;
        }
        assertNull(exc);
    }

    @Test
    void localTransfer_ok() {

        Money transfer = new Money(new BigDecimal("50"));
        Money originBalance = new Money(new BigDecimal("500"));
        Money originNewBalance = new Money(new BigDecimal("450"));
        Money destBalance = new Money(new BigDecimal("500"));
        Money destNewBalance = new Money(new BigDecimal("550"));
        Exception exc = null;
        try {
            var originBefore = accountService.getAccountByAccountNumber(account.getAccountNumber().getIban());
            var destBefore = accountService.getAccountByAccountNumber(account2.getAccountNumber().getIban());
            assertEquals(originBalance.getAmount(), originBefore.getBalance());
            assertEquals(destBalance.getAmount(), destBefore.getBalance());

            var traRes = transactionService.localTransfer(account, account2, transfer.getAmount(), accountHolder,
                    account.getSecretKey());

            assertEquals(TransactionResult.EXECUTED, traRes.getTransactionResult());
            var originAfter = accountService.getAccountByAccountNumber(account.getAccountNumber().getIban());
            var destAfter = accountService.getAccountByAccountNumber(account2.getAccountNumber().getIban());
            assertEquals(originNewBalance.getAmount(), originAfter.getBalance());
            assertEquals(destNewBalance.getAmount(), destAfter.getBalance());
        } catch (Exception e) {
            exc = e;
        }
        assertNull(exc);

    }

    @Test
    void thirdPartyTransferFrom_ok() {

        Money transfer = new Money(new BigDecimal("50"));
        Money destBalance = new Money(new BigDecimal("500"));
        Money destNewBalance = new Money(new BigDecimal("550"));
        Exception exc = null;
        try {
            var destBefore = accountService.getAccountByAccountNumber(account.getAccountNumber().getIban());
            assertEquals(destBalance.getAmount(), destBefore.getBalance());

            var traRes = transactionService.thirdPartyTransfer(thirdParty.getExternalAccount(),
                    null, account, transfer.getAmount(), thirdParty,
                    account.getSecretKey());

            assertEquals(TransactionResult.EXECUTED, traRes.getTransactionResult());
            var destAfter = accountService.getAccountByAccountNumber(account.getAccountNumber().getIban());
            assertEquals(destNewBalance.getAmount(), destAfter.getBalance());
        } catch (Exception e) {
            exc = e;
        }
        assertNull(exc);
    }

    @Test
    void thirdPartyTransferTo_ok() {

        Money transfer = new Money(new BigDecimal("50"));
        Money originBalance = new Money(new BigDecimal("500"));
        Money originNewBalance = new Money(new BigDecimal("450"));
        Exception exc = null;
        try {
            var originBefore = accountService.getAccountByAccountNumber(account.getAccountNumber().getIban());
            assertEquals(originBalance.getAmount(), originBefore.getBalance());

            var traRes = transactionService.thirdPartyTransfer(thirdParty.getExternalAccount(),
                    account, null, transfer.getAmount(), accountHolder,
                    account.getSecretKey());

            assertEquals(TransactionResult.EXECUTED, traRes.getTransactionResult());
            var originAfter = accountService.getAccountByAccountNumber(account.getAccountNumber().getIban());
            assertEquals(originNewBalance.getAmount(), originAfter.getBalance());
        } catch (Exception e) {
            exc = e;
        }
        assertNull(exc);
    }
}
