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

import lombok.Getter;
import lombok.NonNull;
import lombok.SneakyThrows;
import org.eclipse.jgit.lib.*;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.gradle.api.Project;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

class GitSCMCommand implements SCMCommand {

    private static final String REPO_DIRNAME = ".git";

    @Getter
    private final File repoDir;

    public GitSCMCommand(final Project project) {
        repoDir = SCMUtil.scanDir(project, REPO_DIRNAME);
    }

    @Override
    @Nullable
    public File getRepositoryDir() {
        return repoDir;
    }

    @Override
    @SneakyThrows(IOException.class)
    public Commit getLatestCommit(@NonNull final String dateFormat) {
        if (repoDir == null) {
            throw new IllegalStateException("'.git' folder could not be found.");
        }

        final FileRepositoryBuilder builder = new FileRepositoryBuilder();

        final Repository repo = builder.setGitDir(repoDir)
                .readEnvironment()
                .build();
        final StoredConfig conf = repo.getConfig();

        int abbrev = Commit.ABBREV_LENGTH;
        if (conf != null) {
            abbrev = conf.getInt("core", "abbrev", abbrev);
        }

        final SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);

        final Ref HEAD = repo.getRef(Constants.HEAD);
        if (HEAD.getObjectId() == null) {
            throw new RuntimeException("Could not find any commits from HEAD ref.");
        }

        final RevWalk revWalk = new RevWalk(repo);
        if (HEAD.getObjectId() == null) {
            throw new RuntimeException("Could not find any commits from HEAD ref.");
        }
        final RevCommit revCommit = revWalk.parseCommit(HEAD.getObjectId());
        revWalk.markStart(revCommit);

        try {
            // git commit time in sec and java datetime is in ms
            final Date commitTime = new Date(revCommit.getCommitTime() * 1000L);
            final PersonIdent ident = revCommit.getAuthorIdent();

            return Commit.builder()
                    .buildTime(sdf.format(new Date()))
                    .buildAuthorName(conf.getString("user", null, "name"))
                    .buildAuthorEmail(conf.getString("user", null, "email"))
                    .branchName(repo.getBranch())
                    .commitId(revCommit.getName())
                    .commitTime(sdf.format(commitTime))
                    .commitUserName(ident.getName())
                    .commitUserEmail(ident.getEmailAddress())
                    .commitMessage(revCommit.getFullMessage().trim())
                    .build();
        } finally {
            revWalk.dispose();
            repo.close();
        }
    }

}
