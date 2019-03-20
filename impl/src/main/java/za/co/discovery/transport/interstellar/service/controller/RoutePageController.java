package za.co.discovery.transport.interstellar.service.controller;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.java.Log;
import za.co.discovery.transport.interstellar.service.api.TransportService;
import za.co.discovery.transport.interstellar.service.api.dto.PlanetResponse;
import za.co.discovery.transport.interstellar.service.api.dto.RouteResponse;
import za.co.discovery.transport.interstellar.service.api.xsd.PlanetHop;
import za.co.discovery.transport.interstellar.service.api.xsd.RouteResponseDocument;
import za.co.discovery.transport.interstellar.service.dto.OptimalRouteResult;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Bean that interacts with the JSF pages in the application
 * TODO: Move to own admin module. Remote EJB calls would be required to interact with TransportService
 */
@Log
@ManagedBean(name = "route")
@SessionScoped
public class RoutePageController implements Serializable {

	private static final long serialVersionUID = 6102766045196365564L;

	@EJB
	private TransportService transportService;

	@Getter @Setter
	private PlanetResponse origin;

	@Getter @Setter
	private PlanetResponse destination;

	@Getter
	private List<RouteResponse> routeList;

	@Getter
	private List<OptimalRouteResult> optimalPath;

	@Getter @Setter
	private String result;

	// Acts as a trigger to move to next page
	private static final String DONE_ACTION = "done";

	public RoutePageController() {}

	@PostConstruct
	public void init() {
		routeList = transportService.findAllRoutes();
	}

	public Map<PlanetResponse, String> getOriginList() {
		Map<PlanetResponse, String> originatingPlanets = new HashMap<PlanetResponse, String>();
		for (RouteResponse route : routeList) {
			originatingPlanets.put(route.getOrigin(), route.getOrigin().getNode());
		}

		return originatingPlanets;
	}

	public Map<PlanetResponse, String> getDestinationList() {
		Map<PlanetResponse, String> destinationPlanets = new HashMap<PlanetResponse, String>();
		for (RouteResponse route : routeList) {
			destinationPlanets.put(route.getOrigin(), route.getOrigin().getNode());
		}
		return destinationPlanets;
	}

	public String getOptimalRoute() {
		try {
			RouteResponseDocument response = transportService.determineShortestPath(origin.getNode(), destination.getNode());
			if (response != null && !response.getHop().isEmpty()) {
				optimalPath = new ArrayList<OptimalRouteResult>();
				int hopNumber = 1;
				for (PlanetHop hop : response.getHop()) {
					OptimalRouteResult route = new OptimalRouteResult(hopNumber, hop.getNode(), hop.getName());
					optimalPath.add(route);
					hopNumber++;
				}
			}
		} catch (Exception ex) {
			System.err.println("Exception: " + ex.getMessage());
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage(), ex.getMessage()));
			this.result = String.format("Failed to find optimal route. Reason: [%s]", ex.getMessage());
		}
		return DONE_ACTION;
	}

}
