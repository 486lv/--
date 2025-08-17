package org.example.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.servlet.http.HttpServletResponse;
import org.example.utils.HadoopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/files")
public class FileController {
    @Autowired
    private HadoopUtils hadooputils;

    @Value("${app.file.hadoop-dir}")
    String hadoop;

    @Value("${app.file.upload-dir}")
    private String uploadDir;

    @Value("#{'${app.file.allowed-extensions}'.split(',')}")
    private List<String> allowedExtensions;

    @Value("${app.file.max-file-size-mb}")
    private int maxFileSizeMb;

    // 上传接口
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<FileInfo> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            validateFile(file);
            Path uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();
            Files.createDirectories(uploadPath);
            String originalFilename = Objects.requireNonNull(file.getOriginalFilename());
            Path targetPath = uploadPath.resolve(originalFilename);
            Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("文件上传成功，文件地址：" +  targetPath);
            String hadooppath=hadoop+"/"+originalFilename;
            hadooputils.uploadFileToHadoop(targetPath.toString(),hadooppath);

            return ResponseEntity.ok(new FileInfo(originalFilename));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    // 文件列表接口
    // 文件列表接口，支持文件名模糊搜索
    @GetMapping
    public ResponseEntity<Map<String, Object>> listFiles(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "5") int pageSize,
            @RequestParam(required = false) String filename) throws IOException {
        Path uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();
        Map<String, Object> result = new HashMap<>();
        if (!Files.exists(uploadPath)) {
            result.put("items", Collections.emptyList());
            result.put("total", 0);
            return ResponseEntity.ok(result);
        }
        List<FileInfo> allFiles = Files.list(uploadPath)
                .filter(Files::isRegularFile)
                .map(path -> new FileInfo(path.getFileName().toString()))
                .filter(info -> filename == null || filename.isEmpty() || info.getFilename().contains(filename))
                .collect(Collectors.toList());
        int total = allFiles.size();
        int fromIndex = Math.max(0, (pageNum - 1) * pageSize);
        int toIndex = Math.min(fromIndex + pageSize, total);
        List<FileInfo> pageList = fromIndex > total ? Collections.emptyList() : allFiles.subList(fromIndex, toIndex);
        result.put("items", pageList);
        result.put("total", total);
        return ResponseEntity.ok(result);
    }


    //
    @GetMapping("/{filename}")
    public void downloadFile(@PathVariable String filename, HttpServletResponse response) throws IOException {
        Path uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();
        Path filePath = uploadPath.resolve(filename);
        if (!Files.exists(filePath)) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        String encodedName = URLEncoder.encode(filename, StandardCharsets.UTF_8).replaceAll("\\+", "%20");
        String contentType = Files.probeContentType(filePath);
        if (contentType == null) {
            contentType = "application/octet-stream";
        }
        response.setContentType(contentType);
        response.setHeader("Content-Disposition", "attachment; filename*=UTF-8''" + encodedName);
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");

        try (InputStream is = Files.newInputStream(filePath); OutputStream os = response.getOutputStream()) {
            byte[] buffer = new byte[8192];
            int len;
            while ((len = is.read(buffer)) != -1) {
                os.write(buffer, 0, len);
            }
            os.flush();
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            e.printStackTrace();
        }
    }
    private void validateFile(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        String extension = getFileExtension(originalFilename);
        if (file.getSize() > maxFileSizeMb * 1024 * 1024) {
            throw new IllegalArgumentException("文件过大");
        }
        if (!allowedExtensions.contains(extension.toLowerCase())) {
            throw new IllegalArgumentException("不支持的文件类型");
        }
    }

    private String getFileExtension(String filename) {
        if (filename == null || !filename.contains(".")) return "";
        return filename.substring(filename.lastIndexOf("."));
    }

    @DeleteMapping("/{filename}")
    public ResponseEntity<Void> deleteFile(@PathVariable String filename) {
        Path uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();
        Path filePath = uploadPath.resolve(filename);
        try {
            if (!Files.exists(filePath)) {
                return ResponseEntity.notFound().build();
            }
            Files.delete(filePath);
            hadooputils.deleteFromHadoop(filePath.toString(), hadoop + "/" + filename);
            return ResponseEntity.noContent().build();
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    // FileController.java
    public static class FileInfo {
        private String filename;

        public FileInfo(String filename) {
            this.filename = filename;
        }

        public String getFilename() {
            return filename;
        }
    }
    // 在 FileController.java 中添加
    @GetMapping("/preview/{filename}")
    public void previewFile(@PathVariable String filename, HttpServletResponse response) throws IOException {
        Path uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();
        Path filePath = uploadPath.resolve(filename);
        if (!Files.exists(filePath)) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        String contentType = Files.probeContentType(filePath);
        if (contentType == null) {
            contentType = "application/octet-stream";
        }
        response.setContentType(contentType);
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        try (InputStream is = Files.newInputStream(filePath); OutputStream os = response.getOutputStream()) {
            byte[] buffer = new byte[8192];
            int len;
            while ((len = is.read(buffer)) != -1) {
                os.write(buffer, 0, len);
            }
            os.flush();
        }
    }

}