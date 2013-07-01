// vi: set softtabstop=4 shiftwidth=4 expandtab:
/*
 * Copyright 2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.pieceof8.gradle.snapshot;

import java.io.File;

import org.gradle.api.Project;

import lombok.*;

/**
 * A provider to access a Source Control Management (SCM) Repository.
 */
abstract class ScmProvider {

    /** The project this plugin is being applied to. */
    private final @NonNull Project project;

    /** Tracks whether the SCM repository directory was found. */
    private final @NonNull boolean found;

    /** The location of the SCM repository. */
    protected final @NonNull File repoDir;

    /**
     * Create an SCM provider for the supplied (Gradle) {@code project} with the
     * supplied {@code repoDirName}.
     *
     * @param project The project this plugin is being applied to.
     * @param repoDirName The name of the folder where the SCM stores it's
     *        metadata (e.g. '.git').
     */
    public ScmProvider(final @NonNull Project project,
            final @NonNull String repoDirName) {
        this.project = project;
        repoDir = findRepoDir(repoDirName);
        found = (repoDir != null);
    }

    /**
     * Recursively walks up the directory tree starting from {@code
     * Project#getProjectDir()} looking for the {@code repoDirName}. This code
     * mirrors the lookup strategy used by most SCM tools.
     *
     * @param repoDirName The name of the configuration folder for SCM metadata.
     * @return The file with the location of the repository or {@code null} if
     *         one cannot be found.
     */
    private File findRepoDir(final @NonNull String repoDirName) {
        File repoDir;
        File baseDir = project.getProjectDir();
        do {
            repoDir = new File(baseDir, repoDirName);
            project.getLogger().info(
                    "Searching '{}' for a '{}' directory.", baseDir, repoDirName);
            if (repoDir.exists()) return repoDir;

            baseDir = baseDir.getParentFile();
        } while (baseDir != null);

        project.getLogger()
                .info("Could not locate a '{}' directory.", repoDirName);
        return null;
    }

    /**
     * Checks that this Gradle project uses an SCM repository supported by this
     * provider.
     *
     * @return {@code true} if the SCM repository for this provider exists.
     */
    public final boolean isFound() {
        return found;
    }

    /**
     * Analyses the latest commit in the SCM repository and returns it's
     * information as an {@code Commit}.
     *
     * @return The {@code Commit} reported by the SCM for most recent commit
     *         information.
     * @see com.pieceof8.gradle.snapshot.Commit
     */
    abstract Commit getCommit();

}
