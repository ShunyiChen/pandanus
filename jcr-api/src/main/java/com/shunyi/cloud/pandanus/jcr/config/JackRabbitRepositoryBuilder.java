/**
 * MIT License
 *
 * Copyright (c) 2023 Shunyi Chen
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.shunyi.cloud.pandanus.jcr.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.jackrabbit.oak.Oak;
import org.apache.jackrabbit.oak.jcr.Jcr;
import org.apache.jackrabbit.oak.plugins.document.DocumentNodeStore;
import org.apache.jackrabbit.oak.plugins.document.mongo.MongoDocumentNodeStoreBuilder;
import org.springframework.context.annotation.Configuration;

import javax.jcr.Repository;

/**
 * JackRabbit Repository Builder
 *
 * @author Shunyi Chen
 */
@Slf4j
@Configuration
public class JackRabbitRepositoryBuilder {
    static Repository repo = null;
    static DocumentNodeStore ns = null;
    public static Repository getRepository() {
        try {
            ns = new MongoDocumentNodeStoreBuilder()
                    .setMongoDB("mongodb://root:password123@localhost:27017", "oak", 0).build();
            repo = new Jcr(new Oak(ns)).createRepository();
        } catch (Exception e) {
            log.error("Exception caught: " + e.getLocalizedMessage());
            e.printStackTrace();
        }
        return repo;
    }

    public static void dispose() {
        try {
            ns.dispose();
        } catch (Exception e) {
            log.error("Exception caught: " + e.getLocalizedMessage());
            e.printStackTrace();
        }
    }
}
