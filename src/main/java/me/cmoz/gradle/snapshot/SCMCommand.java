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

import javax.annotation.Nullable;
import java.io.File;

/**
 * A command to access a Source Control Management (SCM) Repository.
 */
public interface SCMCommand {

    /**
     * Returns a {@code File} of the directory for the repository of the SCM
     * tool.
     *
     * @return The directory of the SCM repository, may be {@code null} if no
     *         SCM directory could be found.
     */
    @Nullable
    File getRepositoryDir();

    /**
     * Analyses the latest commit in the SCM repository and returns it's
     * information as an {@code Commit}.
     *
     * @param dateFormat The format of the dates in the commit.
     * @return The {@code Commit} reported by the SCM for most recent commit
     *         information.
     */
    Commit getLatestCommit(final String dateFormat);

}
