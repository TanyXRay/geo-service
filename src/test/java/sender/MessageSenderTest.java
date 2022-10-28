package sender;

import org.junit.jupiter.api.Assertions;
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
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class MessageSenderTest {

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Mock
    private GeoService geoService;

    @Mock
    private LocalizationService localizationService;

    @Test
    public void returnRuMessageTest() {
        String ipAddressExpected = "172.0.32.11";
        Location locationExpected = new Location("Moscow", Country.RUSSIA, "Lenina", 15);
        Mockito.when(geoService.byIp(Mockito.eq(ipAddressExpected))).thenReturn(locationExpected);

        String textRuExpected = "Добро пожаловать";
        Mockito.when(localizationService.locale(Mockito.eq(locationExpected.getCountry())))
                .thenReturn(textRuExpected);

        Map<String, String> headers = Map.of(MessageSenderImpl.IP_ADDRESS_HEADER, ipAddressExpected);
        MessageSender messageSender = new MessageSenderImpl(geoService, localizationService);
        String message = messageSender.send(headers);

        assertEquals(textRuExpected, message);
    }

    @Test
    public void returnEngMessageTest() {
        String ipAddressExpected = "96.44.183.149";
        Location locationExpected = new Location("New York", Country.USA, " 10th Avenue", 32);
        Mockito.when(geoService.byIp(Mockito.eq(ipAddressExpected))).thenReturn(locationExpected);

        String textEngExpected = "Welcome";
        Mockito.when(localizationService.locale(Mockito.eq(locationExpected.getCountry())))
                .thenReturn(textEngExpected);

        Map<String, String> headers = Map.of(MessageSenderImpl.IP_ADDRESS_HEADER, ipAddressExpected);
        MessageSender messageSender = new MessageSenderImpl(geoService, localizationService);
        String message = messageSender.send(headers);

        assertEquals(textEngExpected, message);
    }

    @Test
    public void throwNPEMessageTest() {
        MessageSender messageSender = new MessageSenderImpl(null, null);
        NullPointerException thrown = Assertions.assertThrows(
                NullPointerException.class,
                () -> messageSender.send(null),
                "Expected send() to throw"
        );
        assertTrue(thrown.getMessage().contains("because \"headers\" is null"));
    }
}
