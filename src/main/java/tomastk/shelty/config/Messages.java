package tomastk.shelty.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;


@Component
@Configuration
public class Messages {
    public static String creationError = "error_de_creacion";
    public static String deletingError = "error_de_eliminacion";
    public static String editionError = "error_de_edicion";
    public static String nonAuthorizatedError = "error_de_autorizacion";
    public static String nonFoundError = "error_de_busqueda";

    public static String asignatingError = "error_de_asignacion";
    public static String conflictError = "error_de_conflicto";

    public static String detailsSuccessCreation(String entityName) {
        return "Se ha creado exitosamente la entidad " + entityName;
    }

    public static String detailsSuccessEdition(String entityName) {
        return "Se ha editado exitosamente la entidad " + entityName;
    }

    public static String detailsSuccessDeleting(String entityName) {
        return "Se ha editado borrado la entidad " + entityName;
    }

    public static String detailNullFieldError(String entityName, String fieldName) {
        return "No se puede modificar una entidad de " + entityName + " que no tenga un atributo " + fieldName + " o sea inválido.";
    }

    public static String detailCreationError(String entityName) {
        return "Ha ocurrido un error al crear un " + entityName;
    }

    public static String detailEditionError(String entityName) {
        return "Ha ocurrido un error al editar un " + entityName;
    }

    public static String detailDeletingError(String entityName) {
        return "Ha ocurrido un error al borrar un " + entityName;
    }

    public static String detailNonAuthorizatedError(String entityName) {
        return "No tienes permiso para modificar la entidad " + entityName;
    }

    public static String detailNotFoundError(String entityName) {
        return "No se ha encontrado a una entidad de " + entityName + " que cumpla con las características provistas";
    }

}
