package com.test.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Log4j2
@Service
@Async("async_L")
public class FileDeletion {
    public void deleteFile(String path) {
        File file = new File(path);
        file.delete();
    }

    public void deleteFiles(String path, List<String> listOfFileNames) throws IOException {
        for (String fileName : listOfFileNames) {
            Path filePath = Paths.get(path, fileName);
            Files.delete(filePath);
        }
    }
    public void deleteFilesAndParentFolders(Collection<String> listOfFileNames) throws IOException {
        if (listOfFileNames == null || listOfFileNames.isEmpty()) return;
        try {
            for (String path : listOfFileNames) {
                log.debug("Filename {}", path);
                this.deleteFile(path);
                deleteFolder(Path.of(path).getParent());
            }
        } catch (SecurityException e) {
            log.warn("Not able to delete file.Permission denied.");
        }
    }
    public void deleteFolder(String path) {
        if (path == null || path.isEmpty() || path.isBlank()) return;
        try {
            File file = new File(path);
            for (File subfile : Objects.requireNonNull(file.listFiles())) {
                if (subfile.isDirectory()) {
                    deleteFolder(path);
                }
                subfile.delete();
            }
            file.delete();
        } catch (SecurityException e) {
            log.warn("Not able to delete file.Permission denied.");
        } catch (NullPointerException e) {
            log.warn("Null pointer exception while listing files.");
        }
    }
    public void deleteFolder(Path path) {
        if (path == null) return;
        this.deleteFolder(path.toString());
    }
    public void deleteFolders(Collection<String> path) {
        if (path == null || path.isEmpty()) return;
        path.forEach(this::deleteFolder);
    }
    public void deleteParentFolders(Collection<String> path) {
        if (path == null || path.isEmpty()) return;
        path.stream().map(Path::of).map(Path::getParent).forEach(this::deleteFolder);
    }
    public void deleteParentFolder(String path) {
        if (path == null || path.isEmpty()) return;
        deleteFolder(Path.of(path).getParent());
    }
    public void deleteParentFolder(Path path) {
        if (path == null) return;
        deleteFolder(path.getParent());
    }
}
