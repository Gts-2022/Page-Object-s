package ru.netology.web.test;

import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.web.data.DataHelper;
import ru.netology.web.page.DashboardPage;
import ru.netology.web.page.LoginPageV2;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.netology.web.data.DataHelper.getFirstCardNumber;
import static ru.netology.web.data.DataHelper.getSecondCardNumber;

class MoneyTransferTest {
    DashboardPage dashboardPage;

    @BeforeEach
    void setUp() {
        open("http://localhost:9999");
        var loginPage = new LoginPageV2();//подключаем класс LoginPageV2
        var authInfo = DataHelper.getAuthInfo();//Из класса DataHelper получаем данные пользователя
        var verificationPage = loginPage.validLogin(authInfo);//логин вводим
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);// получаем код из метода getVerificationCodeFor
        dashboardPage = verificationPage.validVerify(verificationCode);//Вводим код и открывается страница Личного кабинета
    }

    @Test
    void shouldTransferMoneyTo2Card500() {// Перевод 500 рублей
        String amount = "500";
        var firstCardInfo = getFirstCardNumber();
        var secondCardInfo = getSecondCardNumber();
        var balanceFirstCard = dashboardPage.getCardBalance(firstCardInfo);
        var balanceSecondCard = dashboardPage.getCardBalance(secondCardInfo);
        var moneyTransfer = dashboardPage.firstCardButton();
        var infoCard = DataHelper.getFirstCardNumber();
        moneyTransfer.validTransfer(amount, infoCard);
        var expectedBalanceFirstCard = balanceFirstCard - Integer.parseInt(amount);
        var expectedBalanceSecondCard = balanceSecondCard + Integer.parseInt(amount);
        var actualBalanceFirstCard = dashboardPage.getCardBalance(firstCardInfo);
        var actualBalanceSecondCard = dashboardPage.getCardBalance(secondCardInfo);
        assertEquals(expectedBalanceFirstCard, actualBalanceFirstCard);
        assertEquals(expectedBalanceSecondCard, actualBalanceSecondCard);

    }

    @Test
    void shouldErrorMessageWithoutSpecifyingWhereTransferCameFrom() {
        // Сообщение об ошибке без указания источника перевода
        val moneyTransfer = dashboardPage.firstCardButton();
        val infoCard = DataHelper.getFirstCardNumber();
        String amount = "1000";
        moneyTransfer.transferError(amount, infoCard);
        moneyTransfer.getErrorNotification();

    }

    @Test
    void shouldTransfer0Rur() {//Необходимо перевести 0 рублей
        String amount = "0";
        var firstCardInfo = getFirstCardNumber();
        var secondCardInfo = getSecondCardNumber();
        var balanceFirstCard = dashboardPage.getCardBalance(firstCardInfo);
        var balanceSecondCard = dashboardPage.getCardBalance(secondCardInfo);
        var moneyTransfer = dashboardPage.firstCardButton();
        var infoCard = DataHelper.getFirstCardNumber();
        moneyTransfer.validTransfer(amount, infoCard);
        var expectedBalanceFirstCard = balanceFirstCard - Integer.parseInt(amount);
        var expectedBalanceSecondCard = balanceSecondCard + Integer.parseInt(amount);
        var actualBalanceFirstCard = dashboardPage.getCardBalance(firstCardInfo);
        var actualBalanceSecondCard = dashboardPage.getCardBalance(secondCardInfo);
        assertEquals(expectedBalanceFirstCard, actualBalanceFirstCard);
        assertEquals(expectedBalanceSecondCard, actualBalanceSecondCard);
    }

    @Test
    void shouldTransferMoneyTo2CardMoreThanBalance() {//Должно появиться сообщение об ошибке
        String amount = "9800";
        var moneyTransfer = dashboardPage.firstCardButton();
        var infoCard = DataHelper.getFirstCardNumber();
        moneyTransfer.validTransfer(amount, infoCard);
        moneyTransfer.getErrorNotification();


    }

}