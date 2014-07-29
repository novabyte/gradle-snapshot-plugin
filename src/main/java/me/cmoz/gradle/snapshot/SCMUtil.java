package me.cmoz.gradle.snapshot;

import org.gradle.api.Project;
import org.gradle.api.logging.Logger;

import javax.annotation.Nullable;
import java.io.File;

final class SCMUtil {

    @Nullable
    public static File scanDir(final Project project, final String dirName) {
        final Logger logger = project.getLogger();

        File repoDir;
        File baseDir = project.getProjectDir();
        do {
            repoDir = new File(baseDir, dirName);
            logger.info("Searching '{}' for a '{}' directory.", baseDir, dirName);
            if (repoDir.exists()) {
                return repoDir;
            }
            baseDir = baseDir.getParentFile();
        } while (baseDir != null);

        logger.info("Could not locate a '{}' directory.", dirName);
        return null;
    }

}
