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
package com.shunyi.cloud.pandanus.jcr.service;

import com.shunyi.cloud.pandanus.jcr.model.FileResponse;
import com.shunyi.cloud.pandanus.jcr.model.RabbitNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.jcr.*;
import javax.jcr.version.Version;
import javax.jcr.version.VersionHistory;
import javax.jcr.version.VersionIterator;
import javax.jcr.version.VersionManager;
import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * JackRabbit Service
 *
 * @author Shunyi Chen
 */
@Slf4j
@Service
public class JackRabbitService {

    public Node createNode(Session session, RabbitNode input, MultipartFile uploadFile) {
        Node node = null;
        File file = new File(uploadFile.getOriginalFilename());
        try {
            Node parentNode = session.getNodeByIdentifier(input.getParentId());
            if (parentNode != null && parentNode.hasNode(file.getName())) {
                log.error(file.getName() + " node already exists!");
                return editNode(session, input, uploadFile);
            } else {
                try {
                    node = parentNode.addNode(file.getName(), "nt:file");
                    node.addMixin("mix:versionable");
                    node.addMixin("mix:referenceable");
                    Node content = node.addNode("jcr:content", "nt:resource");
                    InputStream inputStream = uploadFile.getInputStream();
                    Binary binary = session.getValueFactory().createBinary(inputStream);
                    content.setProperty("jcr:data", binary);
                    content.setProperty("jcr:mimeType", input.getMimeType());
                    Date now = new Date();
                    now.toInstant().toString();
                    content.setProperty("jcr:lastModified", now.toInstant().toString());
                    inputStream.close();
                    session.save();
                    VersionManager vm = session.getWorkspace().getVersionManager();
                    vm.checkin(node.getPath());
                    log.error("File saved!");
                } catch (Exception e) {
                    log.error("Exception caught!");
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            log.error("Exception caught!");
            e.printStackTrace();
        }
        return node;
    }

    public boolean deleteNode(Session session, RabbitNode input) {
        try {
            Node node = session.getNodeByIdentifier(input.getFileId());
            if (node != null) {
                node.remove();
                session.save();
                return true;
            }
        } catch (Exception e) {
            log.error("Exception caught!");
            e.printStackTrace();
        }
        return false;
    }

    public List<String> getVersionHistory(Session session, RabbitNode input) {
        List<String> versions = new ArrayList<>();
        try {
            VersionManager vm = session.getWorkspace().getVersionManager();
            Node node = session.getNodeByIdentifier(input.getFileId());
            String filePath = node.getPath();
            if (session.itemExists(filePath)) {
                VersionHistory versionHistory = vm.getVersionHistory(filePath);
                Version currentVersion = vm.getBaseVersion(filePath);
                log.error("Current version: " + currentVersion.getName());
                VersionIterator versionIterator = versionHistory.getAllVersions();
                while (versionIterator.hasNext()) {
                    versions.add(((Version) versionIterator.next()).getName());
                }
            }
        } catch (Exception e) {
            log.error("Exception caught!");
            e.printStackTrace();
        }
        return versions;
    }

    public Node editNode(Session session, RabbitNode input, MultipartFile uploadFile) {
        File file = new File(uploadFile.getOriginalFilename());
        Node returnNode = null;
        try {
            Node parentNode = session.getNodeByIdentifier(input.getParentId());
            if (parentNode != null && parentNode.hasNode(file.getName())) {
                VersionManager vm = session.getWorkspace().getVersionManager();
                Node fileNode = parentNode.getNode(file.getName());
                vm.checkout(fileNode.getPath());
                Node content = fileNode.getNode("jcr:content");
                InputStream is = uploadFile.getInputStream();
                Binary binary = session.getValueFactory().createBinary(is);
                content.setProperty("jcr:data", binary);
                session.save();
                is.close();
                vm.checkin(fileNode.getPath());
                returnNode = fileNode;
            }
        } catch (Exception e) {
            log.error("Exception caught");
            e.printStackTrace();
        }
        return returnNode;
    }

    public Node createFolderNode(Session session, RabbitNode input) {
        Node node = null;
        Node parentNode = null;
        try {
            parentNode = session.getNodeByIdentifier(input.getParentId());
            if (session.nodeExists(parentNode.getPath())) {
                if (!parentNode.hasNode(input.getFileName())) {
                    node = parentNode.addNode(input.getFileName(), "nt:folder");
                    node.addMixin("mix:referenceable");
                    session.save();
                    System.out.println("Folder created: " + input.getFileName());
                }
            } else {
                log.error("Node already exists!");
            }
        } catch (Exception e) {
            log.error("Exception caught!");
            e.printStackTrace();
        }
        return node;
    }

    public FileResponse getNode(Session session, String versionId, RabbitNode input) {
        FileResponse response = new FileResponse();
        try {
            Node file = session.getNodeByIdentifier(input.getFileId());
            if (file != null) {
                VersionManager vm = session.getWorkspace().getVersionManager();
                VersionHistory history = vm.getVersionHistory(file.getPath());
                for (VersionIterator it = history.getAllVersions(); it.hasNext(); ) {
                    Version version = (Version) it.next();
                    if (versionId.equals(version.getName())) {
                        file = version.getFrozenNode();
                        break;
                    }
                }
                log.error("Node retrieved: " + file.getPath());
                Node fileContent = file.getNode("jcr:content");
                Binary bin = fileContent.getProperty("jcr:data").getBinary();
                InputStream stream = bin.getStream();
                byte[] bytes = IOUtils.toByteArray(stream);
                bin.dispose();
                stream.close();
                response.setBytes(bytes);
                //  response.setContentType(fileContent.getProperty("jcr:mimeType").getString());
                response.setContentType(input.getMimeType());
                return response;
            } else {
                log.error("Node does not exist!");
            }
        } catch (Exception e) {
            log.error("Exception caught!");
            e.printStackTrace();
        }
        return response;
    }
}
