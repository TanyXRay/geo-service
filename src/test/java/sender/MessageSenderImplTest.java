package sender;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.netology.entity.Country;
import ru.netology.entity.Location;
import ru.netology.geo.GeoService;
import ru.netology.i18n.LocalizationService;
import ru.netology.sender.MessageSender;
import ru.netology.sender.MessageSenderImpl;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class MessageSenderImplTest {

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Mock
    private GeoService geoService;

    @Mock
    private LocalizationService localizationService;

    @Mock
    private MessageSender messageSender;

    /**
     * Проверка, что MessageSenderImpl всегда отправляет только русский текст, если ip относится к российскому сегменту адресов.
     */
    @Test
    public void sentOnlyRussianLanguageMassage() {
        Map<String, String> headers = Map.of(MessageSenderImpl.IP_ADDRESS_HEADER, "172.0.32.11");
        String ipAddressExpected = "172.0.32.11";
        Mockito.when(messageSender.send(Mockito.eq(headers))).thenReturn(ipAddressExpected);
        String IpActualResult = messageSender.send(headers);

        Location locationExpected = new Location("Moscow", Country.RUSSIA, "Lenina", 15);
        Mockito.when(geoService.byIp(Mockito.eq(ipAddressExpected))).thenReturn(locationExpected);
        Location locationActualResult = geoService.byIp(ipAddressExpected);

        String textOnRussianLanguageExpected = "Добро пожаловать";
        Mockito.when(localizationService.locale(Mockito.eq(locationExpected.getCountry()))).thenReturn(textOnRussianLanguageExpected);
        String textActualResult = localizationService.locale(locationExpected.getCountry());
        assertEquals(textOnRussianLanguageExpected, textActualResult);
    }

    /**
     * * Проверка, что MessageSenderImpl всегда отправляет только английский текст, если ip относится к американскому сегменту адресов.
     */
    @Test
    public void sentOnlyAmericanLanguageMassage() {
        Map<String, String> headers = Map.of(MessageSenderImpl.IP_ADDRESS_HEADER, "96.44.183.149");
        String ipAddressExpected = "96.44.183.149";
        Mockito.when(messageSender.send(Mockito.eq(headers))).thenReturn(ipAddressExpected);
        String IpActualResult = messageSender.send(headers);

        Location locationExpected = new Location("New York", Country.USA, " 10th Avenue", 32);
        Mockito.when(geoService.byIp(Mockito.eq(ipAddressExpected))).thenReturn(locationExpected);
        Location locationActualResult = geoService.byIp(ipAddressExpected);

        String textOnAmericanLanguageExpected = "Welcome";
        Mockito.when(localizationService.locale(Mockito.eq(locationExpected.getCountry()))).thenReturn(textOnAmericanLanguageExpected);
        String textActualResult = localizationService.locale(locationExpected.getCountry());
        assertEquals(textOnAmericanLanguageExpected, textActualResult);
    }

    /**
     * Проверка работы метода public Location byIp(String ip), если IP из России.
     */
    @Test
    public void checkIpRussiaLocation() {
        String ipAddressForRussiaCountry = "172.0.32.11";
        Location locationExpected = new Location("Moscow", Country.RUSSIA, "Lenina", 15);
        Mockito.when(geoService.byIp(Mockito.argThat(ip -> ip != null && ip.startsWith("172.")))).thenReturn(locationExpected);
        Location locationActualResult = geoService.byIp(ipAddressForRussiaCountry);
        assertEquals(locationExpected, locationActualResult);
    }

    /**
     * Проверка работы метода public Location byIp(String ip), если IP из Америки.
     */
    @Test
    public void checkIpAmericaLocation() {
        String ipAddressForAmericaCountry = "96.44.183.149";
        Location locationExpected = new Location("New York", Country.USA, " 10th Avenue", 32);
        Mockito.when(geoService.byIp(Mockito.argThat(ip -> ip != null && ip.startsWith("96.")))).thenReturn(locationExpected);
        Location locationActualResult = geoService.byIp(ipAddressForAmericaCountry);
        assertEquals(locationExpected, locationActualResult);
    }

    /**
     * Проверка работы метода public Location byIp(String ip), если IP - null.
     */
    @Test
    public void checkIpNullLocation() {
        NullPointerException exception = new NullPointerException("IP is null!");
        String ipNull = null;
        Mockito.when(geoService.byIp(ipNull)).thenThrow(exception);
        assertThrows(NullPointerException.class, () -> geoService.byIp(ipNull));
        exception.printStackTrace();
    }

    /**
     * Проверка работы метода public String locale(Country country), вывод текста на русском, если страна Россия.
     */
    @Test
    public void checkLocaleForRussiaCountry() {
        String welcomeTextExpected = "Добро пожаловать";
        Mockito.when(localizationService.locale(Mockito.argThat(country -> country == Country.RUSSIA))).thenReturn(welcomeTextExpected);
        String welcomeTextActual = localizationService.locale(Country.RUSSIA);
        assertEquals(welcomeTextExpected, welcomeTextActual);
    }

    /**
     * Проверка работы метода public String locale(Country country), вывод текста на русском, если страна Америка.
     */
    @Test
    public void checkLocaleForAmericanCountry() {
        String welcomeTextExpected = "Welcome";
        Mockito.when(localizationService.locale(Mockito.argThat(country -> country == Country.USA))).thenReturn(welcomeTextExpected);
        String welcomeTextActual = localizationService.locale(Country.USA);
        assertEquals(welcomeTextExpected, welcomeTextActual);
    }
}
