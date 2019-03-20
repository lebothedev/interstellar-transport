package za.co.discovery.transport.interstellar.service.converter;

import za.co.discovery.transport.interstellar.service.api.dto.PlanetResponse;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter("PlanetResponseConverter")
public class PlanetResponseConverter implements Converter {

	/**
	 * Creates a planet object from the node value
	 * @param context
	 * @param component
	 * @param value
	 * @return
	 */
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		PlanetResponse response = new PlanetResponse();
		response.setNode(value);
		return response;
	}

	/**
	 * Returns a string representation of this planet value to display on the front-end
	 * @param context
	 * @param component
	 * @param value
	 * @return
	 */
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		return value.toString();
	}
}
