package trash;

import com.macys.un.smartmonkey.configuration.ResourceType;
import com.macys.un.smartmonkey.configuration.validation.ScenarioValidationException;
import com.macys.un.smartmonkey.rest.bindings.ResourceHandlerResponse;
import com.macys.un.smartmonkey.rest.util.CommonWebConstants;
import com.macys.un.smartmonkey.service.ResourceAlreadyExistsException;
import com.macys.un.smartmonkey.service.ResourceService;
import com.macys.un.smartmonkey.util.FileUtil;
import com.macys.un.smartmonkey.util.MonkeyLogger;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.URI;
import java.util.Map;

/**
 * REST-based service for resources managing: listing/uploading/deleting/validating
 *
 * @author sfedosov
 */
@Path("/v1/resource")
public class ResourceHandler {
    private static final Logger logger = MonkeyLogger.getTechLog(ResourceHandler.class);

    private ResourceService resourceService;

    private static final String RESOURCE_UPLOADED = "Resource was successfully uploaded";
    private static final String RESOURCE_DELETED = "Resource successfully deleted";
    private static final String RESOURCE_NOT_DELETED = "Access denied.";
    private static final String RESOURCE_WAS_NOT_FOUND = "Requested file was not found";
    private static final String ERROR_UPLOADING_RESOURCE = "Error uploading resource %s " +
            "- resource with such name already exists";
    private static final String VALIDATION_EXCEPTION = "Uploaded file can not be accepted. " +
            "It may have invalid name or doesn't pass service-side validation: %s";
    private static final String OVERRIDE_TEMPLATES_ONLY = "Override functionality allowed only for template resources";
    private static final String GENERATED_SUFFIX = ".generated.xml";

    @Required
    public void setResourceService(ResourceService resourceService) {
        this.resourceService = resourceService;
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response getAllAvailableResources() {
        return Response
                .ok(ResourceHandlerResponse
                        .createOk()
                        .withResources(resourceService.getAllAvailableResources()))
                .build();
    }

    @GET
    @Path("/{resourceType}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getAvailableResources(@PathParam("resourceType") ResourceType resourceType) {
        return Response
                .ok(ResourceHandlerResponse
                        .createOk()
                        .withResources(resourceService.getAvailableResourcesWithType(resourceType)))
                .build();
    }

    @GET
    @Path("/{resourceType}/{fileName}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response downloadResource(@PathParam("resourceType") ResourceType resourceType,
                                     @PathParam("fileName") String fileName) {
        try {

            File requestedFile = resourceService.downloadResource(resourceType, fileName);

            return Response
                    .ok(requestedFile)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                    .header(HttpHeaders.CONTENT_TYPE, "application/octet-stream")
                    .build();
        } catch (FileNotFoundException e) {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity(ResourceHandlerResponse
                            .createError()
                            .withDetails(RESOURCE_WAS_NOT_FOUND))
                    .build();
        } catch (Exception e) {
            logger.error("Exception caught", e);
            return Response
            .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ResourceHandlerResponse
                            .createError()
                            .withDetails(
                                String.format(CommonWebConstants.TECHNICAL_EXCEPTION, ExceptionUtils.getMessage(e))))
                    .build();
        }
    }

    @PUT
    @Path("/{resourceType}/{fileName}")
    @Consumes({MediaType.TEXT_XML, MediaType.TEXT_PLAIN})
    @Produces({MediaType.APPLICATION_JSON})
    public Response uploadResource(@PathParam("resourceType") ResourceType resourceType,
                                   @PathParam("fileName") String fileName,
                                   @QueryParam("dryRun") @DefaultValue("false") boolean dryRun,
                                   String data) {
        try {
            if (!fileName.endsWith("." + resourceType.fileExtension)) {
                return Response
                        .status(Response.Status.BAD_REQUEST)
                        .entity(ResourceHandlerResponse.createError()
                                .withDetails(String.format(VALIDATION_EXCEPTION,
                                        resourceType + " resource file name should end with '." + resourceType.fileExtension + "'")))
                        .build();
            }

            final String encodedFileName = resourceService.uploadResource(resourceType, fileName, dryRun, data);

            return Response
                    .created(URI.create(resourceType + File.separator + encodedFileName))
                    .entity(ResourceHandlerResponse
                            .createOk()
                            .withDetails(RESOURCE_UPLOADED))
                    .build();
        } catch (ScenarioValidationException e) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(ResourceHandlerResponse
                            .createError()
                            .withDetails(String.format(VALIDATION_EXCEPTION, ExceptionUtils.getMessage(e))))
                    .build();
        } catch( ResourceAlreadyExistsException rae) {
            return Response.status(Response.Status.CONFLICT)
                    .entity(ResourceHandlerResponse.createError()
                            .withDetails(String.format(ERROR_UPLOADING_RESOURCE, fileName)))
                    .build();
        } catch (Exception e) {
            logger.error("Exception caught", e);
            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ResourceHandlerResponse
                            .createError()
                            .withDetails(
                                String.format(CommonWebConstants.TECHNICAL_EXCEPTION, ExceptionUtils.getMessage(e))))
                    .build();
        }

    }

    @POST
    @Path("/{resourceType}/{fileName}")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response applyOverrides(@PathParam("resourceType") ResourceType resourceType,
                                   @PathParam("fileName") String fileName,
                                   @QueryParam("dryRun") @DefaultValue("false") boolean dryRun,
                                   String request) {

        try {
            if (ResourceType.template != resourceType) {
                throw new WithHttpStatusException(Response.Status.METHOD_NOT_ALLOWED, OVERRIDE_TEMPLATES_ONLY);
            }

            final Map<String, Object> map = FileUtil.getObjectMapping(request);
            if (map == null || map.isEmpty()) {
                throw new WithHttpStatusException(Response.Status.BAD_REQUEST, CommonWebConstants.REQUEST_IS_EMPTY);
            }

            final String configContent;

            try {
                configContent = resourceService.applyOverrides(fileName, map);
            } catch (ScenarioValidationException e) {
                // wrap ScenarioValidationException
                throw new WithHttpStatusException(Response.Status.PRECONDITION_FAILED, e);
            } catch (FileNotFoundException e) {
                throw new WithHttpStatusException(Response.Status.NOT_FOUND, e);
            }
            if (dryRun) {
                return Response.ok()
                        .entity(ResourceHandlerResponse.createOk()
                                .withDetails(CommonWebConstants.VALIDATION_SUCCESSFUL))
                        .build();
            }

            String generatedConfigName = fileName + GENERATED_SUFFIX;
            return Response
                    .ok(configContent)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + generatedConfigName + "\"")
                    .header(HttpHeaders.CONTENT_TYPE, "application/octet-stream")
                    .build();
        } catch (WithHttpStatusException e) {
            return Response
                    .status(e.getHttpStatus())
                    .entity(ResourceHandlerResponse.createError().withDetails(ExceptionUtils.getMessage(e)))
                    .build();
        } catch (Exception e) {
            logger.error("Exception caught", e);
            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ResourceHandlerResponse
                            .createError()
                            .withDetails(
                                    String.format(CommonWebConstants.TECHNICAL_EXCEPTION, ExceptionUtils.getMessage(e))))
                    .build();
        }
    }


    @DELETE
    @Path("/{resourceType}/{fileName}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response deleteResource(@PathParam("resourceType") ResourceType resourceType,
                                   @PathParam("fileName") String fileName) {

        try {
            boolean deleted = resourceService.deleteResource(resourceType, fileName);
            return Response
                    .ok(ResourceHandlerResponse
                            .createOk()
                            .withDetails(deleted ? RESOURCE_DELETED : RESOURCE_NOT_DELETED))
                    .build();
        } catch(FileNotFoundException e) {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity(ResourceHandlerResponse
                            .createError()
                            .withDetails(RESOURCE_WAS_NOT_FOUND))
                    .build();
        }

    }


    /**
     * Wrap exception class with additional field to represent http status.
     */
    private static class WithHttpStatusException extends Exception {

        private Response.Status httpStatus = Response.Status.INTERNAL_SERVER_ERROR;

        WithHttpStatusException(Response.Status httpStatus, String message) {
            super(message);
            this.httpStatus = httpStatus;
        }

        WithHttpStatusException(Response.Status httpStatus, Throwable th) {
            this(httpStatus, ExceptionUtils.getMessage(th));
        }

        public Response.Status getHttpStatus() {
            return httpStatus;
        }
    }
}
