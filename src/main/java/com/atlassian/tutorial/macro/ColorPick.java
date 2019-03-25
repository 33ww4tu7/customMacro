package com.atlassian.tutorial.macro;

import com.atlassian.confluence.content.render.xhtml.ConversionContext;
import com.atlassian.confluence.macro.Macro;
import com.atlassian.confluence.macro.MacroExecutionException;
import com.atlassian.confluence.renderer.radeox.macros.MacroUtils;
import com.atlassian.confluence.util.velocity.VelocityUtils;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.webresource.api.assembler.PageBuilderService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.inject.Named;
import java.util.Map;

@Named
public class ColorPick implements Macro {

    private final PageBuilderService pageBuilderService;

    private final static String name = "name";

    @Autowired
    public ColorPick(@ComponentImport PageBuilderService pageBuilderService) {
        this.pageBuilderService = pageBuilderService;
    }

    public String execute(Map<String, String> map, String s, ConversionContext conversionContext) throws MacroExecutionException {
        pageBuilderService.assembler().resources().requireWebResource("com.atlassian.tutorial.colorPickMacro:colorPickMacro-resources");

        Map context = MacroUtils.defaultVelocityContext();
        context.put(name, map.get("Name"));
        return VelocityUtils.getRenderedTemplate("templates/content", context);
    }


    public BodyType getBodyType() {
        return BodyType.NONE;
    }

    public OutputType getOutputType() {
        return OutputType.BLOCK;
    }
}