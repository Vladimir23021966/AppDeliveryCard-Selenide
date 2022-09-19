package ru.netology;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class AppCardDeliveryTest {

    public String generateDate(int days) {
        return LocalDate.now().plusDays(days).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }

    @BeforeEach
    void setUp() {
        open("http://localhost:9999");
    }

    @Test
    void shouldSubmitRequest() {
        String planningDate = generateDate(3);
        SelenideElement form = $("form");
        form.$("[data-test-id=city] input.input__control").setValue("Москва");
        SelenideElement date = form.$("[data-test-id=date] input.input__control");
        date.doubleClick();
        date.sendKeys(Keys.BACK_SPACE);
        date.setValue(planningDate);
        form.$("[data-test-id=name] input").setValue("Колянников Владимир");
        form.$("[data-test-id=phone] input").setValue("+79066666666");
        form.$("[data-test-id=agreement]").click();
        form.$$(".button__text").find(exactText("Забронировать")).click();
        $(byText("Успешно!")).shouldBe(visible, Duration.ofSeconds(15));
        $(".notification__content")
                .shouldHave(Condition.text("Встреча успешно забронирована " +
                        "на " + planningDate), Duration.ofSeconds(15))
                .shouldBe(Condition.visible);
    }

    @Test
    void shouldSubmitForm() {
        String planningDate = generateDate(10);
        SelenideElement form = $("form");
        form.$("[data-test-id=city] input.input__control").sendKeys("Мо");
        $(byText("Москва")).click();
        SelenideElement date = form.$("[data-test-id=date] input.input__control");
        date.doubleClick();
        date.sendKeys(Keys.BACK_SPACE);
        date.setValue(planningDate);
        form.$("[data-test-id=name] input").setValue("Колянников Владимир");
        form.$("[data-test-id=phone] input").setValue("+79066666666");
        form.$("[data-test-id=agreement]").click();
        form.$$("button").find(exactText("Забронировать")).click();
        $(byText("Успешно!")).shouldBe(visible, Duration.ofSeconds(15));
        $(".notification__content")
                .shouldHave(Condition.text("Встреча успешно забронирована " +
                        "на " + planningDate), Duration.ofSeconds(15))
                .shouldBe(Condition.visible);
    }

    @Test
    void shouldBeChangeDataWhenLess3() {
        String planningDate = generateDate(1);
        SelenideElement form = $("form");
        form.$("[data-test-id=city] input.input__control").sendKeys("Мо");
        $(byText("Москва")).click();
        SelenideElement date = form.$("[data-test-id=date] input.input__control");
        date.doubleClick();
        date.sendKeys(Keys.BACK_SPACE);
        date.setValue(planningDate);
        form.$("[data-test-id=name] input").setValue("Колянников Владимир");
        form.$("[data-test-id=phone] input").setValue("+79066666666");
        form.$("[data-test-id=agreement]").click();
        form.$$("button").find(exactText("Забронировать")).click();

        $("[data-test-id=\"date\"] .input__sub")
                .shouldHave(Condition.text("Заказ на выбранную дату невозможен"), Duration.ofSeconds(15))
                .shouldBe(Condition.visible);
    }

}

