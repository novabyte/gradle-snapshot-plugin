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

import java.util.ArrayList;
import java.util.List;

import org.gradle.api.Project;

import lombok.*;

/**
 * Analyses the project directories and creates a {@code ScmProvider} that
 * handles the SCM repository found (if there is one).
 */
class ScmProviderFactory {

    /** The project this plugin is being applied to. */
    private final @NonNull Project project;

    /** The list of available {@code ScmProvider}(s). */
    private final @NonNull List<ScmProvider> providers;

    /**
     * Creates a factory to create an {@code ScmProvider} that handles the SCM
     * repository in use in the Gradle project.
     *
     * @param project The (Gradle) project this plugin is being applied to.
     */
    public ScmProviderFactory(final @NonNull Project project) {
        this.project = project;

        providers = new ArrayList<ScmProvider>();
        registerProviders();
    }

    /**
     * Scans the buildscript classpath searching for available {@code
     * ScmProvider} libraries and registers them with this factory.
     */
    private void registerProviders() {
        val msg = "  {} library not found in buildscript dependencies, {} " +
                "SCM support disabled.";
        if (classpathContains("org.tmatesoft.hg.repo.HgRepository")) {
            providers.add(new MercurialScmProvider(project));
        } else {
            project.getLogger().info(msg, "Hg4j", "Mercurial");
        }

        if (classpathContains("org.eclipse.jgit.lib.Repository")) {
            providers.add(new GitScmProvider(project));
        } else {
            project.getLogger().info(msg, "JGit", "Git");
        }
    }

    /**
     * Attempts to create an {@code ScmProvider} by trying each supported
     * provider to verify that it's 
     *
     * @return The compatible SCM provider or {@code null} if one couldn't be
     *         found.
     */
    public ScmProvider createScmProvider() {
        project.getLogger().info("Available SCM providers are: {}.", providers);
        for (val provider : providers) {
            if (provider.isFound()) {
                return provider;
            }
        }
        return null;
    }

    /**
     * Attempts to load the supplied {@code fqClassName} from the buildscript
     * classpath.
     *
     * @param fqClassName The fully qualified classname to try to load.
     * @return {@code true} if the file was found on the classpath.
     */
    private static boolean classpathContains(final @NonNull String fqClassName) {
        try {
            Thread.currentThread().getContextClassLoader().loadClass(fqClassName);
        } catch (final ClassNotFoundException e) {
            return false;
        } catch (final NoClassDefFoundError e) {
            return false;
        }
        return true;
    }

}
