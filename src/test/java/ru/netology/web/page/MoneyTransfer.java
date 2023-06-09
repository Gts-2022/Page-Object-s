package ru.netology.web.page;

import com.codeborne.selenide.SelenideElement;
import ru.netology.web.data.DataHelper;

import java.time.Duration;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static java.time.Duration.ofSeconds;

public class MoneyTransfer {
    private SelenideElement heading = $("h1");
    private SelenideElement amountTransfer = $("[data-test-id=amount] input");
    private SelenideElement from = $("[data-test-id=from] input");
    private SelenideElement transferButton = $("[data-test-id=action-transfer]");
    private final SelenideElement errorNotification = $("[data-test-id=error-notification]");

    public MoneyTransfer() {
        heading.shouldBe(visible);
    }

    public void transfer(String amount, DataHelper.CardInfo cardInfo) {
        amountTransfer.setValue(amount);
        from.setValue(cardInfo.getCardNumber());
        transferButton.click();


    }

    public DashboardPage validTransfer(String amount, DataHelper.CardInfo cardInfo) {
        transfer(String.valueOf(amount), cardInfo);
        return new DashboardPage();
    }

    public void transferError(String amount, DataHelper.CardInfo cardInfo) {
        amountTransfer.setValue(amount);

        transferButton.click();


    }

    public void getErrorNotification() {
        errorNotification.shouldBe(visible);
    }



}
