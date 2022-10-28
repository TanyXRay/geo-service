package i18n;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.entity.Country;
import ru.netology.i18n.LocalizationServiceImpl;

public class LocalizationServiceTest {

    private LocalizationServiceImpl localizationService;

    @BeforeEach
    public void setUp() {
        localizationService = new LocalizationServiceImpl();
    }

    @Test
    public void returnRuMessageIfCountryRussia() {
        String textExpected = "Добро пожаловать";
        String message = localizationService.locale(Country.RUSSIA);

        Assertions.assertEquals(textExpected, message);
    }

    @Test
    public void returnEngMessageIfCountryUsa() {
        String textExpected = "Welcome";
        String message = localizationService.locale(Country.USA);

        Assertions.assertEquals(textExpected, message);
    }
}
