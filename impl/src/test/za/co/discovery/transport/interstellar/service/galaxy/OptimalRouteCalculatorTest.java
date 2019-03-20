package za.co.discovery.transport.interstellar.service.galaxy;

import lombok.extern.java.Log;
import org.junit.Test;
import org.junit.internal.runners.JUnit4ClassRunner;
import org.junit.runner.RunWith;
import za.co.discovery.transport.interstellar.service.api.dto.PlanetResponse;
import za.co.discovery.transport.interstellar.service.api.dto.RouteResponse;
import za.co.discovery.transport.interstellar.service.helper.OptimalRouteCalculator;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Log
@RunWith(JUnit4ClassRunner.class)
public class OptimalRouteCalculatorTest {

	private List<PlanetResponse> planets;
	private List<RouteResponse> routes;

	@Test
	public void testAlgorithm() {

		planets = new ArrayList<PlanetResponse>();
		routes = new ArrayList<RouteResponse>();

		loadPlanets();
		loadRoutes();

		Galaxy galaxy = new Galaxy(planets, routes);
		OptimalRouteCalculator calculator = new OptimalRouteCalculator(galaxy);
		calculator.begin(planets.get(0));
		LinkedList<PlanetResponse> optimalPath = calculator.getPath(planets.get(6));

		for (PlanetResponse p : optimalPath) {
			log.info(p.toString());
		}
	}

	private void loadRoutes() {
		addRoute(1, 0, 1, new BigDecimal(1));
		addRoute(2, 0, 3, new BigDecimal(4));
		addRoute(3, 0, 2, new BigDecimal(2));
		addRoute(4, 1, 2, new BigDecimal(4));
		addRoute(5, 2, 3, new BigDecimal(3));
		addRoute(6, 2, 5, new BigDecimal(1));
		addRoute(7, 3, 5, new BigDecimal(7));
		addRoute(8, 3, 4, new BigDecimal(2));
		addRoute(9, 4, 5, new BigDecimal(3));
		addRoute(10, 5, 6, new BigDecimal(6));
		addRoute(11, 4, 6, new BigDecimal(5));
	}

	private void loadPlanets() {
		PlanetResponse planet1 = new PlanetResponse("A", "Earth"); //0
		PlanetResponse planet2 = new PlanetResponse("B", "Mars");   //1
		PlanetResponse planet3 = new PlanetResponse("C", "Venus");  //2
		PlanetResponse planet4 = new PlanetResponse("D", "Mercury");    //3
		PlanetResponse planet5 = new PlanetResponse("E", "Jupiter");    //4
		PlanetResponse planet6 = new PlanetResponse("F", "Saturn"); //5
		PlanetResponse planet7 = new PlanetResponse("G", "Uranus"); //6

		planets.add(planet1);
		planets.add(planet2);
		planets.add(planet3);
		planets.add(planet4);
		planets.add(planet5);
		planets.add(planet6);
		planets.add(planet7);
	}

	private void addRoute(int routeId, int sourceLocation, int destinationLocation, BigDecimal distance) {
		RouteResponse route = new RouteResponse(planets.get(sourceLocation), planets.get(destinationLocation), distance);
		routes.add(route);
	}

}