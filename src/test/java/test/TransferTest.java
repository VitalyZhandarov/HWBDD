package test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import page.DashboardPage;
import page.LoginPage;

import static com.codeborne.selenide.Selenide.open;
import static data.DataHelper.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TransferTest {
    DashboardPage dashboardPage;

    @BeforeEach
    void SetUp() {
        var loginPage = open("http://localhost:9999", LoginPage.class);
        var authInfo = getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = getVerificationCodeFor();
        dashboardPage = verificationPage.validVerify(verificationCode);
    }


    @Test
    void shouldTransferMoneyFromSecondToFirstCard() {
        var firstCardInfo = getCardInfoFirst();
        var secondCardInfo = getCardInfoSecond();
        var firstCardBalance = dashboardPage.getCardBalance(firstCardInfo);
        var secondCardBalance = dashboardPage.getCardBalance(secondCardInfo);
        var amount = createValidAmount(firstCardBalance);
        var expectedFirstCardBalance = firstCardBalance - amount;
        var expectedSecondCardBalance = secondCardBalance + amount;
        var transferPage = dashboardPage.selectCardToTransfer(secondCardInfo);
        dashboardPage = transferPage.validTransfer(String.valueOf(amount), firstCardInfo);
        var actualFirstCardBalance = dashboardPage.getCardBalance(firstCardInfo);
        var actualSecondCardBalance = dashboardPage.getCardBalance(secondCardInfo);
        assertEquals(expectedFirstCardBalance, actualFirstCardBalance);
        assertEquals(expectedSecondCardBalance, actualSecondCardBalance);

    }

    @Test
    void shouldTransferMoneyFromFirstToSecondCard() {
        var firstCardInfo = getCardInfoFirst();
        var secondCardInfo = getCardInfoSecond();
        var firstCardBalance = dashboardPage.getCardBalance(firstCardInfo);
        var secondCardBalance = dashboardPage.getCardBalance(secondCardInfo);
        var amount = createValidAmount(secondCardBalance);
        var expectedSecondCardBalance = secondCardBalance - amount;
        var expectedFirstCardBalance = firstCardBalance + amount;
        var transferPage = dashboardPage.selectCardToTransfer(firstCardInfo);
        dashboardPage = transferPage.validTransfer(String.valueOf(amount), secondCardInfo);
        var actualFirstCardBalance = dashboardPage.getCardBalance(firstCardInfo);
        var actualSecondCardBalance = dashboardPage.getCardBalance(secondCardInfo);
        assertEquals(expectedFirstCardBalance, actualFirstCardBalance);
        assertEquals(expectedSecondCardBalance, actualSecondCardBalance);

    }

    @Test
    void shouldNotTransferMoneyIfAmountMoreBalance() {
        var firstCardInfo = getCardInfoFirst();
        var secondCardInfo = getCardInfoSecond();
        var firstCardBalance = dashboardPage.getCardBalance(firstCardInfo);
        var secondCardBalance = dashboardPage.getCardBalance(secondCardInfo);
        var amount = createInvalidAmount(secondCardBalance);
        var transferPage = dashboardPage.selectCardToTransfer(firstCardInfo);
        transferPage.transfer(String.valueOf(amount), secondCardInfo);
        transferPage.getErrorMassage("Ошибка!");
        var actualFirstCardBalance = dashboardPage.getCardBalance(firstCardInfo);
        var actualSecondCardBalance = dashboardPage.getCardBalance(secondCardInfo);
        assertEquals(firstCardBalance, actualFirstCardBalance);
        assertEquals(secondCardBalance, actualSecondCardBalance);
    }
}
