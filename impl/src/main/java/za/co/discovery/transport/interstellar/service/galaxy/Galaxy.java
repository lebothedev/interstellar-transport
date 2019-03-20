package za.co.discovery.transport.interstellar.service.galaxy;

import za.co.discovery.transport.interstellar.service.api.dto.PlanetResponse;
import za.co.discovery.transport.interstellar.service.api.dto.RouteResponse;

import java.util.List;

/**
 * Represents a graph objects including all the planets and routes in the galaxy
 */
public class Galaxy {

	private List<PlanetResponse> planets;
	private List<RouteResponse> routes;

	public Galaxy(List<PlanetResponse> planets, List<RouteResponse> routes) {
		this.planets = planets;
		this.routes = routes;
	}

	public List<PlanetResponse> getPlanets() {
		return planets;
	}

	public List<RouteResponse> getRoutes() {
		return routes;
	}
}
