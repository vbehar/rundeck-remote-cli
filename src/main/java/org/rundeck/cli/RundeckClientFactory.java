/*
 * Copyright 2011 Vincent Behar
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.rundeck.cli;

import org.apache.commons.lang.StringUtils;
import org.rundeck.api.RundeckClient;
import com.beust.jcommander.Parameter;

/**
 * Factory for building {@link RundeckClient} instances, using environment variables or parameters values.
 * 
 * @author Vincent Behar
 */
public class RundeckClientFactory {

    /** Name of the environment variable for configuring the RunDeck URL */
    public static final transient String ENV_URL = "RUNDECK_URL";

    /** Name of the environment variable for configuring the RunDeck login */
    public static final transient String ENV_LOGIN = "RUNDECK_LOGIN";

    /** Name of the environment variable for configuring the RunDeck password */
    public static final transient String ENV_PASSWORD = "RUNDECK_PASSWORD";

    @Parameter(names = "--url", description = "The URL of your RunDeck instance")
    private String paramUrl;

    @Parameter(names = "--login", description = "The login to use to connect to your RunDeck instance")
    private String paramLogin;

    @Parameter(names = "--password", description = "The password to use to connect to your RunDeck instance", password = true)
    private String paramPassword;

    /**
     * Instantiate a new {@link RundeckClient} instance, using environment variables, or parameters values. Note that if
     * both are set, the parameter value will be used in favor of the environment variable.
     * 
     * @return a new {@link RundeckClient} instance
     */
    public RundeckClient instantiateRundeckClient() {
        String url = System.getenv(ENV_URL);
        if (StringUtils.isNotBlank(paramUrl)) {
            url = paramUrl;
        }

        String login = System.getenv(ENV_LOGIN);
        if (StringUtils.isNotBlank(paramLogin)) {
            login = paramLogin;
        }

        String password = System.getenv(ENV_PASSWORD);
        if (StringUtils.isNotBlank(paramPassword)) {
            password = paramPassword;
        }

        return new RundeckClient(url, login, password);
    }

}
