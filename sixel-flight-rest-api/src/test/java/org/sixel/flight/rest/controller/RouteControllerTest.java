package org.sixel.flight.rest.controller;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;

/**
 * @author marcos
 *
 */
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class RouteControllerTest {

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	@SuppressWarnings("unchecked")
	@Test
	void findFliesGruOrlPositive() throws Exception {
		final List<Map<String, Object>> flies;
		flies = (List<Map<String, Object>>) this.restTemplate.getForObject("http://localhost:" + port + "/routes/GRU-ORL",
				List.class);
		
		assertEquals(3, flies.size());
		
		int sumPrices = 0;
		for (final Map<String, Object> fly: flies) {
			assertTrue(fly.containsKey("route"));
			assertTrue(fly.containsKey("price"));
			final Map<String, Object> route = (Map<String, Object>) fly.get("route");
			
			if (route.containsKey("from") && route.containsKey("to")) {
				final Map<String, Object> from = (Map<String, Object>) route.get("from");
				final Map<String, Object> to = (Map<String, Object>) route.get("to");
				
				if (from.getOrDefault("code", "N/A").equals("GRU") &&
						to.getOrDefault("code", "N/A").equals("ORL")) {
					sumPrices=sumPrices + ((Number) fly.get("price")).intValue();
				}
			}
		}
		assertEquals(131, sumPrices);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	void findBestFlyGruOrlPositive() throws Exception {
		final Map<String, Object> fly;
		fly = (Map<String, Object>) this.restTemplate.getForObject("http://localhost:" + port + "/routes/GRU-ORL/best",
				Map.class);
		
		assertNotNull(fly);
		
		assertTrue(fly.containsKey("route"));
		assertTrue(fly.containsKey("price"));
		final Map<String, Object> route = (Map<String, Object>) fly.get("route");
		
		assertTrue(route.containsKey("from"));
		assertTrue(route.containsKey("to"));
		
		final Map<String, Object> from = (Map<String, Object>) route.get("from");
		final Map<String, Object> to = (Map<String, Object>) route.get("to");
		
		assertEquals("GRU", from.getOrDefault("code", "N/A"));
		assertEquals("ORL", to.getOrDefault("code", "N/A"));

		assertEquals(35, ((Number) fly.get("price")).intValue());
	}	
	
	
	@SuppressWarnings("unchecked")
	@Test
	void findBestFlyGruOrlNegative() throws Exception {
		final Map<String, Object> fly;
		fly = (Map<String, Object>) this.restTemplate.getForObject("http://localhost:" + port + "/routes/ORL-GRU/best",
				Map.class);
		
		assertNull(fly);
	}	
	
}