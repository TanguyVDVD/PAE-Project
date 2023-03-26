package be.vinci.pae.api.filters;

import jakarta.ws.rs.NameBinding;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Annotation to mark a resource as requiring authorization of Mr Riez.
 */
@NameBinding
@Retention(RetentionPolicy.RUNTIME)
public @interface AuthorizeRiez {

}
