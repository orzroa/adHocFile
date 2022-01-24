package com.github.orzroa.adhocfile;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

import static com.github.orzroa.adhocfile.FileStatus.*;

/**
 * NONE --up--> WAITING --trans--> STREAMING --close--> NONE
 */
@Data
public class FileContextHolder {

    private static Map<String, MultipartFile> fileMap = new HashMap<>();
    private static Map<String, FileStatus> statusMap = new HashMap<>();


    private FileContextHolder() {
    }

    // up and set file
    public static void up(String slot, MultipartFile file) {
        fileMap.put(slot.toUpperCase(), file);
        statusMap.put(slot.toUpperCase(), WAITING);
    }

    // before download
    public static MultipartFile open(String slot) {
        MultipartFile file = fileMap.get(slot.toUpperCase());
        if (file != null) {
            fileMap.remove(slot.toUpperCase());
            statusMap.put(slot.toUpperCase(), STREAMING);
            return file;
        } else {
            return null;
        }
    }

    // after downloaded
    public static void close(String slot) {
        statusMap.remove(slot.toUpperCase());
    }

    public static FileStatus getStatus(String slot) {
        FileStatus status = statusMap.get(slot.toUpperCase());
        return status == null ? NONE : status;
    }
}
