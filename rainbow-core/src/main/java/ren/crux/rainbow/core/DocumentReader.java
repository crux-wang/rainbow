package ren.crux.rainbow.core;

import io.swagger.v3.oas.integration.SwaggerConfiguration;
import io.swagger.v3.oas.integration.api.OpenAPIConfiguration;
import io.swagger.v3.oas.integration.api.OpenApiReader;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import ren.crux.rainbow.core.module.CombinationModule;
import ren.crux.rainbow.core.module.Context;
import ren.crux.rainbow.core.option.RevisableConfig;
import ren.crux.rainbow.core.utils.SpringWebUtils;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author wangzhihui
 */
@Slf4j
public class DocumentReader implements OpenApiReader {

    protected OpenAPIConfiguration openApiConfiguration;
    private RevisableConfig config;
    private Map<String, String> implMap;
    private Context context;
    private CombinationModule combinationModule;
    private String basePath = "/";
    private OpenAPI openAPI;
    private Components components;
    private Paths paths;
    private Set<Tag> openApiTags;

    public DocumentReader() {
        this.openAPI = new OpenAPI();
        paths = new Paths();
        openApiTags = new LinkedHashSet<>();
        components = new Components();
    }

    public DocumentReader(OpenAPI openAPI) {
        this();
        setConfiguration(new SwaggerConfiguration().openAPI(openAPI));
    }

    public DocumentReader(OpenAPIConfiguration openApiConfiguration) {
        this();
        setConfiguration(openApiConfiguration);
    }

    public OpenAPI getOpenAPI() {
        return openAPI;
    }

    @Override
    public void setConfiguration(OpenAPIConfiguration openApiConfiguration) {
    }

    @Override
    public OpenAPI read(Set<Class<?>> classes, Map<String, Object> resources) {
        return null;
    }

    public OpenAPI read(Class<?> cls) {

        if (!SpringWebUtils.isController(cls)) {
            return openAPI;
        }
        return openAPI;
    }
}
