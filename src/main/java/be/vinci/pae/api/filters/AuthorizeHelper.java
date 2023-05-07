package be.vinci.pae.api.filters;

import be.vinci.pae.utils.views.Views;
import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.ws.rs.NameBinding;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Annotation to mark a resource as requiring authorization of a helper.
 */
@NameBinding
@Retention(RetentionPolicy.RUNTIME)
@JacksonAnnotationsInside
@JsonView(Views.Helper.class)
public @interface AuthorizeHelper {

}
