package page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import data.DataHelper;

import java.time.Duration;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

public class TransferPage {
    private final SelenideElement transferAmount = $("[data-test-id='amount'] input"); //сумма перевода
    private final SelenideElement fromTransfer = $("[data-test-id='from'] input"); //откуда перевод
    private final SelenideElement buttonTransfer = $("[data-test-id='action-transfer']"); // кнопка перевод
    private final SelenideElement transferHead = $(byText("Пополнение карты")); // заголовок
    private final SelenideElement errorMessage = $("[data-test-id ='error-message']");

    public TransferPage() {
        transferHead.shouldBe(visible);
    }

    public DashboardPage validTransfer(String amount, DataHelper.CardInfo cardInfo) {
        transfer(amount, cardInfo);
        return new DashboardPage();
    }

    public void transfer(String amount, DataHelper.CardInfo cardInfo) {
        transferAmount.setValue(amount);
        fromTransfer.setValue(cardInfo.getCardNumber());
        buttonTransfer.click();
    }

    public void getErrorMassage(String expectedText) {
        errorMessage.shouldHave(Condition.text("Ошибка!"), Duration.ofSeconds(15)).shouldBe(visible);
    }
}
