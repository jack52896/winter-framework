package com.framework.scroll.winter.v1.webmvc.bean;

import java.io.File;

/**
 * @author http://zouyujie.top
 * @date 2021/11/15 22:27
 */
public class WinterViewResolver {
    private final String DEFAULT_SUFFIX=".html";
    private File templateRootDir;
    private String viewName;

    public WinterViewResolver(String templateRoot) {
        this.templateRootDir = new File(templateRoot);
    }
    public WinterView resolverViewName(String viewName){
        if("".equals(viewName) || null == viewName){
            return  null;
        }
        viewName = viewName.endsWith(DEFAULT_SUFFIX)? viewName : viewName+DEFAULT_SUFFIX;
        File file = new File((templateRootDir.getPath()+"/"+viewName).replaceAll("/+", "/"));
        return new WinterView(file);
    }
}
