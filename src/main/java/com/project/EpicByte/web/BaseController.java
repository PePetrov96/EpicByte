package com.project.EpicByte.web;

import org.springframework.ui.Model;

import java.util.LinkedHashMap;
import java.util.Map;

public class BaseController {

    protected void addProductBreadcrumb(Model model, String pageUrl, String... pageNames) {
        Map<String, String> breadcrumbs = new LinkedHashMap<>();
        breadcrumbs.put("Home", "/");

        if (pageUrl == null) {
            for (int i = 0; i < pageNames.length; i++) {
                if (i == 0) {
                    breadcrumbs.put(pageNames[i], "/products/" + pageNames[i].replace(" ", "").toLowerCase());
                } else {
                    breadcrumbs.put(pageNames[i], "/" + pageNames[i].replace(" ", "").toLowerCase());
                }
            }
        } else {
            breadcrumbs.put(pageNames[0], pageUrl);
        }

        model.addAttribute("breadcrumbs", breadcrumbs);
    }
}
