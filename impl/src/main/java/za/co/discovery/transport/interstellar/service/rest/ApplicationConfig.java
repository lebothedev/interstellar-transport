package za.co.discovery.transport.interstellar.service.rest;

import lombok.extern.java.Log;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

/**
 * Activates JAX-RS in the application
 */
@Log
@ApplicationPath("/api")
public class ApplicationConfig extends Application {
    public ApplicationConfig() {
        log.info("Creating Transport REST Application");
    }
}
