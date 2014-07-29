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

import lombok.NonNull;
import lombok.SneakyThrows;
import org.gradle.api.Project;
import org.tmatesoft.hg.core.*;

import javax.annotation.Nullable;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

class HgSCMCommand implements SCMCommand {

    private static final String REPO_DIRNAME = ".hg";

    private File repoDir;

    public HgSCMCommand(final Project project) {
        repoDir = SCMUtil.scanDir(project, REPO_DIRNAME);
    }

    @Override
    @Nullable
    public File getRepositoryDir() {
        return repoDir;
    }

    @Override
    @SneakyThrows({HgRepositoryNotFoundException.class, HgException.class})
    public Commit getLatestCommit(@NonNull final String dateFormat) {
        if (repoDir == null) {
            throw new IllegalStateException("'.hg' folder could not be found.");
        }

        final HgRepoFacade repo = new HgRepoFacade();
        repo.initFrom(repoDir);

        final SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);

        final HgLogCommand hgLogCommand = repo.createLogCommand();
        hgLogCommand.limit(1);
        final List<HgChangeset> changesets = hgLogCommand.execute();
        if (changesets.size() < 1) {
            throw new RuntimeException("Could not find any changesets in Hg repository.");
        }

        final HgChangeset commit = changesets.get(0);
        return Commit.builder()
                .buildTime(sdf.format(new Date()))
                .buildAuthorName("")    // TODO: how to get hgrc user name?
                .buildAuthorEmail("")   // TODO: how to get hgrc email?
                .branchName(commit.getBranch())
                .commitId(commit.getNodeid().toString())
                .commitTime(sdf.format(new Date(commit.getDate().getRawTime())))
                .commitUserName(extractName(commit.getUser()))
                .commitUserEmail(extractEmail(commit.getUser()))
                .commitMessage(commit.getComment().trim())
                .build();
    }

    /**
     * Extracts a name from the {@code HgChangeset#getUser()} string format.
     *
     * @param user The user string obtained from {@code HgChangeset#getUser()}.
     * @return The name from the user string or an empty string if one could not
     *         be found.
     */
    private static String extractName(@NonNull final String user) {
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
    private static String extractEmail(@NonNull final String user) {
        final int beginIndex = ((user.indexOf('<') + 1) < user.length())
                ? (user.indexOf('<') + 1)
                : user.length();
        final int endIndex = (user.lastIndexOf('>') != -1)
                ? user.lastIndexOf('>')
                : user.length();

        return user.substring(beginIndex, endIndex).trim();
    }

}
