package org.wahlzeit_revisited.utils;

import org.wahlzeit_revisited.database.config.ConfigDir;
import org.wahlzeit_revisited.database.config.Directory;

/**
 * A basic set of system configuration data
 */
public interface WahlzeitConfig {

    String getRootDirAsString();

    ConfigDir getStaticDir();

    ConfigDir getScriptsDir();

    ConfigDir getTemplatesDir();

    Directory getPhotosDir();

    Directory getBackupDir();

    Directory getTempDir();

    String getStaticFileMappingPath();

    String getDbDriverAsString();

    String getDbConnectionAsString();

    String getDbUserAsString();

    String getDbPasswordAsString();

}
