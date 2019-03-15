package com.atlassian.tutorial.macro;

import com.atlassian.confluence.content.render.xhtml.ConversionContext;
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
        String imageLink = map.get("Name");
        Map context = MacroUtils.defaultVelocityContext();
        String Alpha = "1";
        try {
            if ((map.get("Alpha") != null) || (Integer.parseInt(map.get("Alpha")) <= 1))
                Alpha = map.get("Alpha");
            context.put("link", imageLink);
            context.put("opacity", Alpha);
            context.put("user", getUsername());
            return VelocityUtils.getRenderedTemplate("templates/content", context);
        } catch (NumberFormatException e) {
            context.put("opacity", "1");
            return VelocityUtils.getRenderedTemplate("templates/content", context);
        }

    }

    public BodyType getBodyType() {
        return BodyType.NONE;
    }

    public OutputType getOutputType() {
        return OutputType.BLOCK;
    }
}