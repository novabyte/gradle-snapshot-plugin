/*
 * Copyright 2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package me.cmoz.gradle.snapshot;

import org.gradle.api.Project;
import org.gradle.api.logging.Logger;

import javax.annotation.Nullable;
import java.io.File;

/**
 * A helper class with utility methods to integrate with an SCM repository.
 */
final class SCMUtil {

    /**
     * Recursively walks up the directory tree starting from {@code
     * Project#getProjectDir()} looking for the {@code repoDirName}. This code
     * mirrors the lookup strategy used by most SCM tools.
     *
     * @param project The Gradle project for this plugin.
     * @param dirName The name of the configuration folder for SCM metadata.
     * @return The file with the location of the repository or {@code null} if
     *         one cannot be found.
     */
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
