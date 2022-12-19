package com.inov8.microbank.server.service.fileloader;

import java.io.File;
import java.io.IOException;

import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

public class ArbitraryResourceLoader implements ResourceLoaderAware {
    private ResourceLoader resourceLoader;

    public ResourceLoader getResourceLoader() {
		return resourceLoader;
	}

	public File loadImage(String path) throws IOException {
        Resource resource = resourceLoader.getResource(path);
        return resource.getFile();
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }
}