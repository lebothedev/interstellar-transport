package za.co.discovery.transport.interstellar.service.helper;

import org.apache.commons.lang3.StringUtils;
import za.co.discovery.transport.interstellar.service.api.xsd.RouteRequestDocument;

import java.math.BigDecimal;

public class ValidationHelper {

	public static boolean isInvalidRequest(String... arguments) {
		try {
			for (String argument : arguments) {
				ValidationHelper.validateArgument(argument);
			}
			return false;
		} catch (IllegalArgumentException ile) {
			return true;
		}
	}

	public static boolean isInvalidRequest(Long routeId, BigDecimal newDistance) {
		try {
			ValidationHelper.validateArgument(routeId);
			ValidationHelper.validateArgument(newDistance);
			return false;
		} catch (IllegalArgumentException ile) {
			return true;
		}
	}

	public static boolean isInvalidRequest(String originNode, String destinationNode, BigDecimal distance) {
		try {
			ValidationHelper.validateArgument(originNode);
			ValidationHelper.validateArgument(destinationNode);
			ValidationHelper.validateArgument(distance);
			return false;
		} catch (IllegalArgumentException ile) {
			return true;
		}
	}

	public static boolean isInvalidRequest(RouteRequestDocument request) {
		if (request == null || StringUtils.isBlank(request.getDestination()) || StringUtils.isBlank(request.getOrigin())) {
			return true;
		}
		return false;
	}

	private static void validateArgument(String argument) {
		if (StringUtils.isBlank(argument)) {
			throw new IllegalArgumentException(String.format("Invalid value supplied [%s]", argument));
		}
	}

	private static void validateArgument(BigDecimal argument) {
		if (argument == null) {
			throw new IllegalArgumentException("Null decimal value supplied");
		}
	}

	private static void validateArgument(Long argument) {
		if ((argument == null) || argument <= 0) {
			throw new IllegalArgumentException("Invalid value supplied for number");
		}
	}
}
