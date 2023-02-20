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
package com.shunyi.cloud.pandanus.jcr.controller;

import com.shunyi.cloud.pandanus.jcr.config.JackRabbitRepositoryBuilder;
import com.shunyi.cloud.pandanus.jcr.model.FileResponse;
import com.shunyi.cloud.pandanus.jcr.model.RabbitNode;
import com.shunyi.cloud.pandanus.jcr.service.JackRabbitService;
import com.shunyi.cloud.pandanus.jcr.util.JackRabbitUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.jcr.Node;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import java.net.URLConnection;
import java.security.Principal;
import java.util.List;

/**
 * JackRabbitService Rest Endpoint
 *
 * @author Shunyi Chen
 */
@RestController
@RequestMapping("/jcr")
public class RabbitController {

//    @GetMapping("/viewRepository")
//    public String viewRepository(Principal principal) {
//        System.out.println("*****View repository");
//        return String.format("View repository");
//    }
//
//    @PostMapping("/createRepository")
//    public String createRepository(Principal principal) {
//        System.out.println("*************Repository was created by "+principal.getName());
//        return String.format("Repository was created.");
//    }


    private Repository repo = null;

    public RabbitController(JackRabbitService jackRabbitService) {
        this.jackRabbitService = jackRabbitService;
        repo = JackRabbitRepositoryBuilder.getRepo("localhost", 27017);
    }

    @Autowired
    JackRabbitService jackRabbitService;

    @PostMapping("/createRoot")
    public String createRoot() throws RepositoryException {
        Session session = JackRabbitUtils.getSession(repo);
        System.out.println("createRoot called!");
        RabbitNode input = new RabbitNode("/", "oak", "", "");
        Node node = jackRabbitService.createFolderNode(session, input);
        String identifier = node.getIdentifier();
        JackRabbitUtils.cleanUp(session);
        return identifier;
    }

    @PostMapping("/createFolder")
    public String createFolderNode(@RequestBody RabbitNode input) throws RepositoryException {
        Session session = JackRabbitUtils.getSession(repo);
        System.out.println("createFolderNode called!");
        System.out.println("parentId: " + input.getParentId());
        System.out.println("filePath: " + input.getFileName());
        System.out.println("mimeType: " + input.getMimeType());
        System.out.println("fileId: " + input.getFileId());
        Node node = jackRabbitService.createFolderNode(session, input);
        String identifier = node.getIdentifier();
        JackRabbitUtils.cleanUp(session);
        return identifier;
    }

    @PostMapping("/createFile")
    public String createNode(@RequestParam(value = "parent") String parent, @RequestParam(value = "file") MultipartFile file) throws RepositoryException {
        Session session = JackRabbitUtils.getSession(repo);
        RabbitNode input = new RabbitNode(parent, file.getOriginalFilename(), URLConnection.guessContentTypeFromName(file.getName()), "");
        System.out.println("createNode called!");
        System.out.println("parentId: " + input.getParentId());
        System.out.println("filePath: " + input.getFileName());
        System.out.println("mimeType: " + input.getMimeType());
        System.out.println("fileId: " + input.getFileId());
        Node node = jackRabbitService.createNode(session, input, file);
        String identifier = node.getIdentifier();
        session.getNodeByIdentifier(input.getParentId());
        return identifier;
    }

    @PostMapping("/deleteFile")
    public boolean deleteNode(@RequestBody RabbitNode input) {
        Session session = JackRabbitUtils.getSession(repo);
        System.out.println("deleteNode called!");
        System.out.println("parentId: " + input.getParentId());
        System.out.println("filePath: " + input.getFileName());
        System.out.println("mimeType: " + input.getMimeType());
        System.out.println("fileId: " + input.getFileId());
        boolean result = jackRabbitService.deleteNode(session, input);
        JackRabbitUtils.cleanUp(session);
        return result;
    }

    @PostMapping("/getVersions")
    public List<String> getVersionHistory(@RequestBody RabbitNode input) {
        Session session = JackRabbitUtils.getSession(repo);
        System.out.println("getVersionHistory called!");
        System.out.println("parentId: " + input.getParentId());
        System.out.println("filePath: " + input.getFileName());
        System.out.println("mimeType: " + input.getMimeType());
        System.out.println("fileId: " + input.getFileId());
        return jackRabbitService.getVersionHistory(session, input);
    }

    @PostMapping("/getFile/{versionId}")
    public FileResponse getNode(@PathVariable String versionId, @RequestBody RabbitNode input) {
        Session session = JackRabbitUtils.getSession(repo);
        FileResponse response = null;
        System.out.println("getNode called!");
        System.out.println("parentId: " + input.getParentId());
        System.out.println("filePath: " + input.getFileName());
        System.out.println("mimeType: " + input.getMimeType());
        System.out.println("fileId: " + input.getFileId());
        response = jackRabbitService.getNode(session, versionId, input);
        return response;
    }
}
