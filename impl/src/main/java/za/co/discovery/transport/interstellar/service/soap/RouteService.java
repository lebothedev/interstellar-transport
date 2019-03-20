package za.co.discovery.transport.interstellar.service.soap;

import za.co.discovery.transport.interstellar.service.api.TransportService;
import za.co.discovery.transport.interstellar.service.api.exception.TransportSystemException;
import za.co.discovery.transport.interstellar.service.api.xsd.RouteRequestDocument;
import za.co.discovery.transport.interstellar.service.api.xsd.RouteResponseDocument;
import za.co.discovery.transport.interstellar.service.helper.ValidationHelper;

import javax.inject.Inject;
import javax.jws.WebMethod;
import javax.jws.WebService;

/**
 * SOAP web service to expose the shortest path algorithm
 */
@WebService(name = "RouteService")
public class RouteService {

	@Inject
	private TransportService transportService;

	@WebMethod(operationName = "Route")
	public RouteResponseDocument findOptimalRoute(RouteRequestDocument request) {
		if (ValidationHelper.isInvalidRequest(request)) {
			throw new TransportSystemException("Invalid web service request received");
		}
		return transportService.determineShortestPath(request.getOrigin(), request.getDestination());
	}
}
