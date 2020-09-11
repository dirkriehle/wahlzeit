package org.wahlzeit.testEnvironmentProvider;

import java.util.Optional;

import org.junit.rules.ExternalResource;
import org.wahlzeit.services.SysConfig;
import org.wahlzeit.webparts.WebPartTemplateService;

/**
 * A test setup class.
 * 
 * @review
 */
public class SysConfigProvider extends ExternalResource {
	private static final String DB_HOST = Optional.ofNullable(System.getenv("WAHLZEIT_DB_HOST")).orElse("localhost");

	@Override
	protected void before() throws Throwable {
		SysConfig.dropInstance();
		SysConfig sysConfig = new SysConfig("src/main/webapp", DB_HOST);
		SysConfig.setInstance(sysConfig);
		WebPartTemplateService.getInstance().setTemplatesDir(SysConfig.getTemplatesDir());
	}

	@Override
	protected void after() {
		SysConfig.dropInstance();
	}
}
