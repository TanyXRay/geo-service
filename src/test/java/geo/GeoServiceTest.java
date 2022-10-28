package geo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.entity.Country;
import ru.netology.entity.Location;
import ru.netology.geo.GeoServiceImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class GeoServiceTest {

    private GeoServiceImpl geoService;

    @BeforeEach
    public void setUp() {
        geoService = new GeoServiceImpl();
    }

    @Test
    public void checkLocationByLocalhostIp() {
        Location locationExpected = new Location(null, null, null, 0);
        Location locationActual = geoService.byIp(GeoServiceImpl.LOCALHOST);

        assertEquals(locationExpected, locationActual);
    }

    @Test
    public void checkLocationByMoscowIp() {
        Location locationExpected = new Location("Moscow", Country.RUSSIA, "Lenina", 15);
        Location locationActual = geoService.byIp(GeoServiceImpl.MOSCOW_IP);

        assertEquals(locationExpected, locationActual);
    }

    @Test
    public void checkLocationByNewYorkIp() {
        Location locationExpected = new Location("New York", Country.USA, " 10th Avenue", 32);
        Location locationActual = geoService.byIp(GeoServiceImpl.NEW_YORK_IP);

        assertEquals(locationExpected, locationActual);
    }

    @Test
    public void checkLocationByIpStartsWith172() {
        String ipAddress = "172.1.45.78";
        Location locationExpected = new Location("Moscow", Country.RUSSIA, null, 0);
        Location locationActual = geoService.byIp(ipAddress);

        assertEquals(locationExpected, locationActual);
    }

    @Test
    public void checkLocationByIpStartsWith96() {
        String ipAddress = "96.6.15.63";
        Location locationExpected = new Location("New York", Country.USA, null, 0);
        Location locationActual = geoService.byIp(ipAddress);

        assertEquals(locationExpected, locationActual);
    }

    @Test
    public void checkLocationByNullIp() {
        NullPointerException thrown = assertThrows(
                NullPointerException.class,
                () -> geoService.byIp(null)
        );
        Assertions.assertTrue(thrown.getMessage().contains("because \"ip\" is null"));
    }

    @Test
    public void checkLocationByEmptyIp() {
        String ipAddress = " ";
        Location locationExpected = null;
        Location locationActual = geoService.byIp(ipAddress);

        assertEquals(locationExpected, locationActual);
    }
}
