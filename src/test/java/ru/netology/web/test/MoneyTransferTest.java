package ru.netology.web.test;

import dev.failsafe.internal.util.Assert;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.web.data.DataHelper;
import ru.netology.web.page.DashboardPage;
import ru.netology.web.page.LoginPageV2;
import ru.netology.web.page.MoneyTransfer;

import static com.codeborne.selenide.Selenide.open;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
    void  shouldTransferMoneyTo1Card500 () {// Перевод денежных средств на 1 карту 500 рублей
        int balanceFirstCard = dashboardPage.getFirstCardBalance();
        int balanceSecondCard = dashboardPage.getSecondCardBalance();
        val moneyTransfer = dashboardPage.firstCardButton();
        val infoCard = DataHelper.getSecondCardNumber();
        String amount = "500";
        moneyTransfer.validTransfer(amount, infoCard);
        int expectedBalanceFirstCard = balanceFirstCard + Integer.parseInt(amount);
        int expectedBalanceSecondCard = balanceSecondCard - Integer.parseInt(amount);
        assertEquals(expectedBalanceFirstCard, dashboardPage.getFirstCardBalance());
        assertEquals(expectedBalanceSecondCard, dashboardPage.getSecondCardBalance());
    }
    @Test
    void   shouldTransferMoneyTo1Card9000 () {// Перевод 9000 рублей
        int balanceFirstCard = dashboardPage.getFirstCardBalance();
        int balanceSecondCard = dashboardPage.getSecondCardBalance();
        val moneyTransfer = dashboardPage.firstCardButton();
        val infoCard = DataHelper.getSecondCardNumber();
        String amount = "9000";
        moneyTransfer.validTransfer(amount, infoCard);
        int expectedBalanceFirstCard = balanceFirstCard + Integer.parseInt(amount);
        int expectedBalanceSecondCard = balanceSecondCard - Integer.parseInt(amount);
        assertEquals(expectedBalanceFirstCard, dashboardPage.getFirstCardBalance());
        assertEquals(expectedBalanceSecondCard, dashboardPage.getSecondCardBalance());
    }
    @Test
    void  shouldTransferMoneyTo1CardMoreThanBalance () {
        // Необходимо перевести 12000 рублей, что больше баланса карты
        int balanceFirstCard = dashboardPage.getFirstCardBalance();
        int balanceSecondCard = dashboardPage.getSecondCardBalance();
        val moneyTransfer = dashboardPage.firstCardButton();
        val infoCard = DataHelper.getSecondCardNumber();
        String amount = "12000";
        moneyTransfer.validTransfer(amount, infoCard);
        int expectedBalanceFirstCard = balanceFirstCard + Integer.parseInt(amount);
        int expectedBalanceSecondCard = balanceSecondCard - Integer.parseInt(amount);
        assertEquals(expectedBalanceFirstCard, dashboardPage.getFirstCardBalance());
        assertEquals(expectedBalanceSecondCard, dashboardPage.getSecondCardBalance());
    }
    @Test

    void  shouldErrorMessageWithoutSpecifyingWhereTransferCameFrom () {
        // Сообщение об ошибке без указания источника перевода
        val moneyTransfer = dashboardPage.firstCardButton();
        val infoCard = DataHelper.getFirstCardNumber();
        String amount = "1000";
        moneyTransfer.transferError( amount, infoCard );
        moneyTransfer.getErrorNotification();
    }

    @Test
    void  shouldTransfer0Rur(){//Необходимо перевести 0 рублей
        int balanceFirstCard = dashboardPage.getFirstCardBalance();
        int balanceSecondCard = dashboardPage.getSecondCardBalance();
        val moneyTransfer = dashboardPage.firstCardButton();
        val infoCard = DataHelper.getSecondCardNumber();
        String amount = "0";
        moneyTransfer.validTransfer(amount, infoCard);
        int expectedBalanceFirstCard = balanceFirstCard + Integer.parseInt(amount);
        int expectedBalanceSecondCard = balanceSecondCard - Integer.parseInt(amount);
        assertEquals(expectedBalanceFirstCard, dashboardPage.getFirstCardBalance());
        assertEquals(expectedBalanceSecondCard, dashboardPage.getSecondCardBalance());
    }


    }

