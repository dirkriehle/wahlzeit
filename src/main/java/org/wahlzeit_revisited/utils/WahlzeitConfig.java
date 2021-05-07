package org.wahlzeit_revisited.utils;

import java.io.File;
import java.nio.file.Path;

/**
 * A basic set of system configuration data
 */
public interface WahlzeitConfig {


    File getPhotosDir();

    Path getScriptsPath();

    Path getLanguagePath();

    String getDbDriverAsString();

    String getDbConnectionAsString();

    String getDbUserAsString();

    String getDbPasswordAsString();

}
