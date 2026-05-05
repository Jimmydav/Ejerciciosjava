package es.medac.soporte.aerocheck;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class TelemetryXPathQueriesTest {

    private Document doc;
    private TelemetryXPathQueries queries;

    @Before
    public void setUp() throws Exception {
        doc = new TelemetryDomLoader().load();
        queries = new TelemetryXPathQueries();
    }

    @After
    public void tearDown() {
        doc = null;
        queries = null;
    }

    @Test
    public void testNumeroAlertas() throws Exception {
        List<String> alertas = queries.alertasResumen(doc);
        assertEquals("Deben existir exactamente 2 alertas en el XML de prueba", 2, alertas.size());
    }

    @Test
    public void testDronesBateriaBaja() throws Exception {
        List<String> drones = queries.dronesConBateriaBaja(doc, 20);

        assertTrue("DR-07 debe aparecer (tiene batería 19)", drones.contains("DR-07"));
        assertFalse("DR-12 NO debe aparecer (no baja de 20)", drones.contains("DR-12"));
    }

    @Test
    public void testUltimoGpsPorDrone() throws Exception {
        Map<String, String> gps = queries.ultimoGpsPorDrone(doc);

        assertEquals("36.510,-5.607", gps.get("DR-07"));
        assertEquals("36.524,-5.591", gps.get("DR-12"));

    }
}
