package ru.simplex_software.arbat_baza;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.simplex_software.arbat_baza.model.Address;
import ru.simplex_software.arbat_baza.model.Street;

/**
 * .
 */

public class TestGeocodeService {
    private static final Logger LOG = LoggerFactory.getLogger(TestGeocodeService.class);
    @Test
    public void testIt() throws Exception{
        Address address = new Address();
        Street street = new Street();
        street.setStreetName("Дорожная");
        address.setStreet(street);
        new GeocodeService().getCoordinates(address);
        address.setHouse_str("12A");
        new GeocodeService().getCoordinates(address);

    }
}
