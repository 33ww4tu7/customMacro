package com.atlassian.tutorial.macro;

import com.atlassian.confluence.content.render.xhtml.ConversionContext;
import com.atlassian.confluence.core.ContentEntityObject;
import com.atlassian.confluence.macro.Macro;
import com.atlassian.confluence.macro.MacroExecutionException;
import com.atlassian.confluence.renderer.radeox.macros.MacroUtils;
import com.atlassian.confluence.util.velocity.VelocityUtils;
import com.atlassian.plugin.spring.scanner.annotation.component.Scanned;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.webresource.api.assembler.PageBuilderService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

import static com.atlassian.confluence.user.AuthenticatedUserThreadLocal.getUsername;

@Scanned
public class colorPick implements Macro {

    private PageBuilderService pageBuilderService;

    @Autowired
    public colorPick(@ComponentImport PageBuilderService pageBuilderService) {
        this.pageBuilderService = pageBuilderService;
    }

    public String execute(Map<String, String> map, String s, ConversionContext conversionContext) throws MacroExecutionException {
        pageBuilderService.assembler().resources().requireWebResource("com.atlassian.tutorial.colorPickMacro:colorPickMacro-resources");

        ContentEntityObject contentEntityObject = conversionContext.getEntity();
        String idAsString = contentEntityObject.getIdAsString();
        Map context = MacroUtils.defaultVelocityContext();
        context.put("name", map.get("Name"));
        context.put("userID", getUsername());
        context.put("pageID", idAsString);
        return VelocityUtils.getRenderedTemplate("templates/content", context);
    }


    public BodyType getBodyType() {
        return BodyType.NONE;
    }

    public OutputType getOutputType() {
        return OutputType.BLOCK;
    }
}