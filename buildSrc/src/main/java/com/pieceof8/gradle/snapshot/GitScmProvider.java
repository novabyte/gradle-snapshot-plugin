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

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.StoredConfig;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.gradle.api.Project;

import lombok.*;

/**
 * An {@code ScmProvider} for the Git Source Control Management (SCM) tool.
 */
class GitScmProvider extends ScmProvider {

    /** The project this plugin is being applied to. */
    private final @NonNull Project project;

    /** The configuration object for this task. */
    private final @NonNull SnapshotPluginExtension extension;

    /**
     * Constructs an {@code ScmProvider} for the Git SCM tool.
     *
     * @param project The (Gradle) project this plugin is being applied to.
     * @see org.gradle.api.Project
     */
    public GitScmProvider(final @NonNull Project project) {
        super(project, Constants.DOT_GIT);

        this.project = project;
        this.extension =  project.getExtensions()
                .getByType(SnapshotPluginExtension.class);
    }

    /** {@inheritDoc} */
    @Override
    @SneakyThrows(IOException.class)
    public Commit getCommit() {
        if (repoDir == null) {
            throw new IllegalArgumentException("'repoDir' must not be null");
        }
        FileRepositoryBuilder builder = new FileRepositoryBuilder();
        @Cleanup Repository repo = builder.setGitDir(repoDir)
                .readEnvironment()
                .findGitDir()
                .build();
        StoredConfig conf = repo.getConfig();

        int abbrev = Commit.ABBREV_LENGTH;
        if (conf != null) {
            abbrev = conf.getInt("core", "abbrev", abbrev);
        }

        val sdf = new SimpleDateFormat(extension.getDateFormat());

        val HEAD = repo.getRef(Constants.HEAD);
        if (HEAD == null) {
            val msg = "Could not get HEAD Ref, the repository may be corrupt.";
            throw new RuntimeException(msg);
        }

        RevWalk revWalk = new RevWalk(repo);
        RevCommit commit = revWalk.parseCommit(HEAD.getObjectId());
        revWalk.markStart(commit);

        try {
            // git commit time in sec and java datetime is in ms
            val commitTime = new Date(commit.getCommitTime() * 1000L);
            val ident = commit.getAuthorIdent();

            return new Commit(
                    sdf.format(new Date()), // build time
                    conf.getString("user", null, "name"),
                    conf.getString("user", null, "email"),
                    repo.getBranch(),
                    commit.getName(),
                    sdf.format(commitTime),
                    ident.getName(),
                    ident.getEmailAddress(),
                    commit.getFullMessage().trim());
        } finally {
            revWalk.dispose();
        }
    }

}
