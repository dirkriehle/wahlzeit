package org.wahlzeit.testEnvironmentProvider;

import org.junit.rules.ExternalResource;
import org.wahlzeit.services.SysConfig;
import org.wahlzeit.webparts.WebPartTemplateService;

/**
 * A test setup class.
 * 
 * @review
 */
public class SysConfigProvider extends ExternalResource {

	@Override
	protected void before() throws Throwable {
		SysConfig.dropInstance();
		SysConfig sysConfig = new SysConfig("src/main/webapp");
		SysConfig.setInstance(sysConfig);
		WebPartTemplateService.getInstance().setTemplatesDir(SysConfig.getTemplatesDir());
	}

	@Override
	protected void after() {
		SysConfig.dropInstance();
	}
}
