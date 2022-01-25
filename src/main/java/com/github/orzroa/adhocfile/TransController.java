package com.github.orzroa.adhocfile;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Controller
public class TransController {

    private static final int MAX_WAIT_COUNT = 60;

    @GetMapping(value = "/")
    public String index() {
        return "index.html";
    }

    @GetMapping(value = "/down/{slot}")
    public void down(@PathVariable String slot, HttpServletResponse response) throws IOException, InterruptedException {

        MultipartFile file;
        int count = 0;
        do {
            file = FileContextHolder.open(slot);
            Thread.sleep(1000L);
            if (count++ > MAX_WAIT_COUNT) return;
        } while (file == null);

        response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
        response.setHeader("Content-Disposition", "attachment; filename="
                + URLEncoder.encode(file.getOriginalFilename(), StandardCharsets.UTF_8.name()));
        response.setHeader("Access-Control-Expose-Headers", "content-disposition");

        byte[] buffer = new byte[1024];
        int len;
        OutputStream out = response.getOutputStream();
        InputStream in = file.getInputStream();
        while ((len = in.read(buffer)) > 0) {
            out.write(buffer, 0, len);
        }

        FileContextHolder.close(slot);
    }

    @PostMapping(value = "/up")
    public String up(@RequestParam("upCode") String slot, @RequestParam("file") MultipartFile file) throws InterruptedException {
        int count = 0;
        FileContextHolder.up(slot, file);

        while (FileContextHolder.getStatus(slot) == FileStatus.WAITING) {
            Thread.sleep(1000L);
            if (count++ > MAX_WAIT_COUNT)         return "redirect:?result=f";
        }

        while (FileContextHolder.getStatus(slot) == FileStatus.STREAMING) {
            Thread.sleep(1000L);
        }

        return "redirect:?result=s";
    }
}
