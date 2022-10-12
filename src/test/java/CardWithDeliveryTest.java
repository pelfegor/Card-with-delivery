import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class CardWithDeliveryTest {

    public String generateDate(int days) {
        return LocalDate.now().plusDays(days).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }

    @BeforeEach
    void setUp() {
        open("http://localhost:9999/");
    }

    @Test
    public void shouldTest() {
        String newDate = generateDate(6);
        $("[data-test-id='city'] input").setValue("Москва");
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT,Keys.HOME),Keys.BACK_SPACE);
        $("[data-test-id='date'] input").setValue(newDate);
        $("[data-test-id='name'] input").setValue("Иванов Иван");
        $("[data-test-id='phone'] input").setValue("+71231231234");
        $("[data-test-id='agreement'] .checkbox__box").click();
        $("button .button__text").click();
        $(".notification").shouldBe(visible, Duration.ofSeconds(15));
        $(".notification_visible .notification__title").shouldHave(exactText("Успешно!"));
        $(".notification_visible .notification__content").shouldHave(exactText("Встреча успешно забронирована на " + newDate));
        $("button.notification__closer").click();
        $("[data-test-id='notification']").should(hidden);
    }

    @Test
    public void shouldTestWithChoice() {
        String newDate = generateDate(7);
        LocalDate selected = LocalDate.now().plusDays(3);
        LocalDate required = LocalDate.now().plusDays(7);
        String calendarDate = String.valueOf(LocalDate.now().plusDays(7).getDayOfMonth());
        $("[data-test-id='city'] input").setValue("Мос");
        $("[data-test-id='city'] input").sendKeys(Keys.ARROW_DOWN, Keys.ENTER);
        $("button .icon-button__content").click();
        if (selected.getMonthValue() != required.getMonthValue()) {
            $("[data-step='1']").click();
        }
        $$("table.calendar__layout td").find(text(calendarDate)).click();
        $("[data-test-id='name'] input").setValue("Иванов Иван");
        $("[data-test-id='phone'] input").setValue("+71231231234");
        $("[data-test-id='agreement'] .checkbox__box").click();
        $("button .button__text").click();
        $(".notification").shouldBe(visible, Duration.ofSeconds(15));
        $(".notification_visible .notification__title").shouldHave(exactText("Успешно!"));
        $(".notification_visible .notification__content").shouldHave(exactText("Встреча успешно забронирована на " + newDate));
        $("button.notification__closer").click();
        $("[data-test-id='notification']").should(hidden);
    }

    @Test
    public void shouldWarningIfInvalidCity() {
        String newDate = generateDate(6);
        $("[data-test-id='city'] input").setValue("Piter");
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT,Keys.HOME),Keys.BACK_SPACE);
        $("[data-test-id='date'] input").setValue(newDate);
        $("[data-test-id='name'] input").setValue("Иванов Иван");
        $("[data-test-id='phone'] input").setValue("+71231231234");
        $("[data-test-id='agreement'] .checkbox__box").click();
        $("button .button__text").click();
        $("[data-test-id='city'].input_invalid .input__sub").shouldHave(exactText("Доставка в выбранный город недоступна"));
    }

    @Test
    public void shouldWarningIfInvalidDate() {
        $("[data-test-id='city'] input").setValue("Москва");
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT,Keys.HOME),Keys.BACK_SPACE);
        $("[data-test-id='date'] input").setValue("08.10.2022");
        $("[data-test-id='name'] input").setValue("Иванов Иван");
        $("[data-test-id='phone'] input").setValue("+71231231234");
        $("[data-test-id='agreement'] .checkbox__box").click();
        $("button .button__text").click();
        $("[data-test-id='date'] .input_invalid .input__sub").shouldHave(exactText("Заказ на выбранную дату невозможен"));
    }

    @Test
    public void shouldWarningIfEmptyName() {
        String newDate = generateDate(6);
        $("[data-test-id='city'] input").setValue("Москва");
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT,Keys.HOME),Keys.BACK_SPACE);
        $("[data-test-id='date'] input").setValue(newDate);
        $("[data-test-id='name'] input").setValue("");
        $("[data-test-id='phone'] input").setValue("+71231231234");
        $("[data-test-id='agreement'] .checkbox__box").click();
        $("button .button__text").click();
        $("[data-test-id='name'].input_invalid .input__sub").shouldHave(exactText("Поле обязательно для заполнения"));
    }

    @Test
    public void shouldWarningIfInvalidName() {
        String newDate = generateDate(6);
        $("[data-test-id='city'] input").setValue("Москва");
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT,Keys.HOME),Keys.BACK_SPACE);
        $("[data-test-id='date'] input").setValue(newDate);
        $("[data-test-id='name'] input").setValue("Ivan");
        $("[data-test-id='phone'] input").setValue("+71231231234");
        $("[data-test-id='agreement'] .checkbox__box").click();
        $("button .button__text").click();
        $("[data-test-id='name'].input_invalid .input__sub").shouldHave(exactText("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
    }

    @Test
    public void shouldWarningIfEmptyPhone() {
        String newDate = generateDate(6);
        $("[data-test-id='city'] input").setValue("Москва");
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT,Keys.HOME),Keys.BACK_SPACE);
        $("[data-test-id='date'] input").setValue(newDate);
        $("[data-test-id='name'] input").setValue("Иванов Иван");
        $("[data-test-id='phone'] input").setValue("");
        $("[data-test-id='agreement'] .checkbox__box").click();
        $("button .button__text").click();
        $("[data-test-id='phone'].input_invalid .input__sub").shouldHave(exactText("Поле обязательно для заполнения"));
    }

    @Test
    public void shouldWarningIfInvalidPhone() {
        String newDate = generateDate(6);
        $("[data-test-id='city'] input").setValue("Москва");
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT,Keys.HOME),Keys.BACK_SPACE);
        $("[data-test-id='date'] input").setValue(newDate);
        $("[data-test-id='name'] input").setValue("Иванов Иван");
        $("[data-test-id='phone'] input").setValue("+7123456789");
        $("[data-test-id='agreement'] .checkbox__box").click();
        $("button .button__text").click();
        $("[data-test-id='phone'].input_invalid .input__sub").shouldHave(exactText("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."));
    }

    @Test
    public void shouldWarningIfEmptyCheckbox() {
        String newDate = generateDate(6);
        $("[data-test-id='city'] input").setValue("Москва");
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT,Keys.HOME),Keys.BACK_SPACE);
        $("[data-test-id='date'] input").setValue(newDate);
        $("[data-test-id='name'] input").setValue("Иванов Иван");
        $("[data-test-id='phone'] input").setValue("+71231231234");
        $("button .button__text").click();
        $("[data-test-id='agreement'].input_invalid .checkbox__text").shouldHave(exactText("Я соглашаюсь с условиями обработки и использования моих персональных данных"));
    }

}
