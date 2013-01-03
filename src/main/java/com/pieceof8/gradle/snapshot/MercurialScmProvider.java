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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import lombok.*;

import org.gradle.api.Project;
import org.tmatesoft.hg.core.HgChangeset;
import org.tmatesoft.hg.core.HgException;
import org.tmatesoft.hg.core.HgLogCommand;
import org.tmatesoft.hg.core.HgRepoFacade;
import org.tmatesoft.hg.core.HgRepositoryNotFoundException;

/**
 * An {@code ScmProvider} for the Mercurial Source Control Management (SCM)
 * tool.
 */
class MercurialScmProvider extends ScmProvider {

    /** The project this plugin is being applied to. */
    private final @NonNull Project project;

    /** The configuration object for this task. */
    private final @NonNull SnapshotPluginExtension extension;

    /**
     * Constructs an {@code ScmProvider} for the Mercurial SCM tool.
     *
     * @param project The (Gradle) project this plugin is being applied to.
     * @see org.gradle.api.Project
     */
    public MercurialScmProvider(final @NonNull Project project) {
        super(project, ".hg");

        this.project = project;
        this.extension =  project.getExtensions()
                .getByType(SnapshotPluginExtension.class);
    }

    /** {@inheritDoc} */
    @Override
    @SneakyThrows({HgRepositoryNotFoundException.class, HgException.class})
    public Commit getCommit() {
        if (repoDir == null) {
            throw new IllegalArgumentException("'repoDir' must not be null");
        }
        HgRepoFacade repo = new HgRepoFacade();
        repo.initFrom(repoDir);

        val sdf = new SimpleDateFormat(extension.getDateFormat());

        HgLogCommand hgLogCommand = repo.createLogCommand();
        hgLogCommand.limit(1);
        List<HgChangeset> changesets = hgLogCommand.execute();
        if (changesets.size() < 1) {
            val msg = "Could not find any changesets in Mercurial repository.";
            throw new RuntimeException(msg);
        }

        HgChangeset commit = changesets.get(0);

        return new Commit(
                sdf.format(new Date()), // build time
                "",   // FIXME: how to get hgrc user name?
                "",   // FIXME: how to get hgrc email?
                commit.getBranch(),
                commit.getNodeid().toString(),
                sdf.format(new Date(commit.getDate().getRawTime())),
                extractName(commit.getUser()),
                extractEmail(commit.getUser()),
                commit.getComment().trim());
    }

    /**
     * Extracts a name from the {@code HgChangeset#getUser()} string format.
     *
     * @param user The user string obtained from {@code HgChangeset#getUser()}.
     * @return The name from the user string or an empty string if one could not
     *         be found.
     */
    private static String extractName(final @NonNull String user) {
        final int endIndex = (user.indexOf('<') != -1)
                ? user.indexOf('<')
                : user.length();

        return user.substring(0, endIndex).trim();
    }

    /**
     * Extracts an email address from the {@code HgChangeset#getUser()} string
     * format.
     *
     * <p>This method attempts to handle the following malformed string formats:
     * <pre>
     * {@code 
     *   Firstname <
     *   Firstname Lastname <
     *   Firstname Lastname <>
     *   Firstname Lastname <Email>
     *   Firstname Lastname >
     *   Firstname >
     * }
     * </pre>
     *
     * @param user The user string obtained from {@code HgChangeset#getUser()}.
     * @return The email address from the user string or an empty string if one
     *         could not be found.
     */
    private static String extractEmail(final @NonNull String user) {
        final int beginIndex = ((user.indexOf('<') + 1) < user.length())
                ? (user.indexOf('<') + 1)
                : user.length();
        final int endIndex = (user.lastIndexOf('>') != -1)
                ? user.lastIndexOf('>')
                : user.length();

        return user.substring(beginIndex, endIndex).trim();
    }

}
