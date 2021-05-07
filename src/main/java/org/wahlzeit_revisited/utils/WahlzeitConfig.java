package org.wahlzeit_revisited.utils;

import org.wahlzeit_revisited.config.ConfigDir;

/**
 * A basic set of system configuration data
 */
public interface WahlzeitConfig {

    String getRootDirAsString();

    ConfigDir getPhotosDir();

    ConfigDir getScriptsDir();

    ConfigDir getTemplatesDir();

    Directory getBackupDir();

    Directory getTempDir();

    String getStaticFileMappingPath();

    String getDbDriverAsString();

    String getDbConnectionAsString();

    String getDbUserAsString();

    String getDbPasswordAsString();

}
