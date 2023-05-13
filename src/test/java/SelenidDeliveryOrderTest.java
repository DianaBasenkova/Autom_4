import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;

import java.time.Duration;
import java.time.*;
import java.time.format.DateTimeFormatter;


import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class SelenidDeliveryOrderTest {
    public String SetDateAfter() {

        return LocalDate.now().plusDays(5).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));

    }


    @BeforeEach
    void setupTest() {
        open("http://localhost:9999");

    }

    //positive
    @Test
    public void shouldSendFormWithTrueData() {

        $(By.cssSelector("[data-test-id='city'] input")).setValue("Москва");
        $(By.cssSelector("[data-test-id='date'] input")).doubleClick().sendKeys(Keys.DELETE);
        $(By.cssSelector("[data-test-id='date'] input")).setValue(SetDateAfter());
        $(By.cssSelector("[data-test-id='name'] input")).setValue("Петров-Иванов Иван");
        $(By.cssSelector("[data-test-id='phone'] input")).setValue("+79123456789");
        $(By.cssSelector("[data-test-id='agreement']")).click();
        $(By.className("button")).click();
        $("[data-test-id = notification]").shouldHave(text("Успешно!"),
                Duration.ofSeconds(15)).shouldBe(visible);
        $(".notification__content").shouldHave(text("Встреча успешно забронирована на " + SetDateAfter()),
                Duration.ofSeconds(15)).shouldBe(visible);

    }

    //negative tests with bad data
    @Test
    public void shouldTrySendFormWithWrongCity() {
        $(By.cssSelector("[data-test-id='city'] input")).setValue("Ялта");
        $(By.cssSelector("[data-test-id='date'] input")).doubleClick().sendKeys(Keys.DELETE);
        $(By.cssSelector("[data-test-id='date'] input")).setValue(SetDateAfter());
        $(By.cssSelector("[data-test-id='name'] input")).setValue("Петров-Иванов Иван");
        $(By.cssSelector("[data-test-id='phone'] input")).setValue("+79123456789");
        $(By.cssSelector("[data-test-id='agreement']")).click();
        $(By.className("button")).click();
        String expected = "Доставка в выбранный город недоступна";
        String actual = $(By.cssSelector("[data-test-id='city'].input_invalid .input__sub")).getText();
        assertEquals(expected, actual);
    }

    @Test
    public void shouldTrySendFormWithWrongDate() {
        $(By.cssSelector("[data-test-id='city'] input")).setValue("Москва");
        $(By.cssSelector("[data-test-id='date'] input")).doubleClick().sendKeys("14.05.2023");
        $(By.cssSelector("[data-test-id='name'] input")).setValue("Петров-Иванов Иван");
        $(By.cssSelector("[data-test-id='phone'] input")).setValue("+79123456789");
        $(By.cssSelector("[data-test-id='agreement']")).click();
        $(By.className("button")).click();
        $x("//span[@data-test-id='date']//span[contains(text(), 'Заказ на выбранную дату невозможен')]").should(appear);

    }

    @Test
    public void shouldTrySendFormWithWrongName() {
        $(By.cssSelector("[data-test-id='city'] input")).setValue("Москва");
        $(By.cssSelector("[data-test-id='date'] input")).doubleClick().sendKeys(Keys.DELETE);
        $(By.cssSelector("[data-test-id='date'] input")).setValue(SetDateAfter());
        $(By.cssSelector("[data-test-id='name'] input")).setValue("James Bond");
        $(By.cssSelector("[data-test-id='phone'] input")).setValue("+79123456789");
        $(By.cssSelector("[data-test-id='agreement']")).click();
        $(By.className("button")).click();
        String expected = "Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.";
        String actual = $(By.cssSelector("[data-test-id='name'].input_invalid .input__sub")).getText();
        assertEquals(expected, actual);

    }

    @Test
    public void shouldTrySendFormWithWrongPhone() {

        $(By.cssSelector("[data-test-id='city'] input")).setValue("Москва");
        $(By.cssSelector("[data-test-id='date'] input")).doubleClick().sendKeys(Keys.DELETE);
        $(By.cssSelector("[data-test-id='date'] input")).setValue(SetDateAfter());
        $(By.cssSelector("[data-test-id='name'] input")).setValue("Петров-Иванов Иван");
        $(By.cssSelector("[data-test-id='phone'] input")).setValue("111111");
        $(By.cssSelector("[data-test-id='agreement']")).click();
        $(By.className("button")).click();
        String expected = "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.";
        String actual = $(By.cssSelector("[data-test-id='phone'].input_invalid .input__sub")).getText();
        assertEquals(expected, actual);
    }

    @Test
    public void shouldTrySendFormIfCheckboxNotSelected() {
        $(By.cssSelector("[data-test-id='city'] input")).setValue("Москва");
        $(By.cssSelector("[data-test-id='date'] input")).doubleClick().sendKeys("17.05.2023");
        $(By.cssSelector("[data-test-id='name'] input")).setValue("Петров-Иванов Иван");
        $(By.cssSelector("[data-test-id='phone'] input")).setValue("+79123456789");
        $(By.cssSelector("[data-test-id='agreement']"));
        $(By.className("button")).click();
        Boolean expected = true;
        Boolean actual = $(By.cssSelector("[data-test-id='agreement'].input_invalid")).isDisplayed();
        assertEquals(expected, actual);
    }

    //negative tests with void field

    @Test
    public void shouldTrySendFormWithVoidCity() {
        $(By.cssSelector("[data-test-id='city'] input")).setValue("");
        $(By.cssSelector("[data-test-id='date'] input")).doubleClick().sendKeys(Keys.DELETE);
        $(By.cssSelector("[data-test-id='date'] input")).setValue(SetDateAfter());
        $(By.cssSelector("[data-test-id='name'] input")).setValue("Петров-Иванов Иван");
        $(By.cssSelector("[data-test-id='phone'] input")).setValue("+79123456789");
        $(By.cssSelector("[data-test-id='agreement']")).click();
        $(By.className("button")).click();
        String expected = "Поле обязательно для заполнения";
        String actual = $(By.cssSelector("[data-test-id='city'].input_invalid .input__sub")).getText();
        assertEquals(expected, actual);
    }

    @Test
    public void shouldTrySendFormWithVoidData() {
        $(By.cssSelector("[data-test-id='city'] input")).setValue("Москва");
        $(By.cssSelector("[data-test-id='date'] input")).doubleClick().sendKeys(Keys.DELETE);
        $(By.cssSelector("[data-test-id='name'] input")).setValue("Петров-Иванов Иван");
        $(By.cssSelector("[data-test-id='phone'] input")).setValue("+79123456789");
        $(By.cssSelector("[data-test-id='agreement']")).click();
        $(By.className("button")).click();
        $x("//span[@data-test-id='date']//span[contains(text(), 'Неверно введена дата')]").should(appear);
    }

    @Test
    public void shouldTrySendFormWithVoidName() {
        $(By.cssSelector("[data-test-id='city'] input")).setValue("Москва");
        $(By.cssSelector("[data-test-id='date'] input")).doubleClick().sendKeys(Keys.DELETE);
        $(By.cssSelector("[data-test-id='date'] input")).setValue(SetDateAfter());
        $(By.cssSelector("[data-test-id='name'] input")).setValue("");
        $(By.cssSelector("[data-test-id='phone'] input")).setValue("+79123456789");
        $(By.cssSelector("[data-test-id='agreement']")).click();
        $(By.className("button")).click();
        String expected = "Поле обязательно для заполнения";
        String actual = $(By.cssSelector("[data-test-id='name'].input_invalid .input__sub")).getText();
        assertEquals(expected, actual);
    }

    @Test
    public void shouldTrySendFormWithVoidPhone() {

        $(By.cssSelector("[data-test-id='city'] input")).setValue("Москва");
        $(By.cssSelector("[data-test-id='date'] input")).doubleClick().sendKeys("17.05.2023");
        $(By.cssSelector("[data-test-id='name'] input")).setValue("Петров-Иванов Иван");
        $(By.cssSelector("[data-test-id='phone'] input")).setValue("");
        $(By.cssSelector("[data-test-id='agreement']")).click();
        $(By.className("button")).click();
        String expected = "Поле обязательно для заполнения";
        String actual = $(By.cssSelector("[data-test-id='phone'].input_invalid .input__sub")).getText();
        assertEquals(expected, actual);

    }
}
