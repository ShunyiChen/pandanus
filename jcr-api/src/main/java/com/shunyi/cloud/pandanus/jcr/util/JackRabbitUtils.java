/**
 * MIT License
 * <p>
 * Copyright (c) 2023 Shunyi Chen
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.shunyi.cloud.pandanus.jcr.util;

import lombok.extern.slf4j.Slf4j;

import javax.jcr.*;

/**
 * JackRabbit Utils
 *
 * @author Shunyi Chen
 */
@Slf4j
public class JackRabbitUtils {

    public static Session getSession(Repository repo) {
        try {
            if (repo != null)
                return repo.login(new SimpleCredentials("admin", "admin".toCharArray()));
        } catch (LoginException le) {
            log.error("Exception caught: " + le.getLocalizedMessage());
            le.printStackTrace();
        } catch (RepositoryException re) {
            log.error("Exception caught: " + re.getLocalizedMessage());
            re.printStackTrace();
        }
        return null;
    }

    public static void cleanUp(Session session) {
        if (session != null) {
            session.logout();
        }
    }
}
