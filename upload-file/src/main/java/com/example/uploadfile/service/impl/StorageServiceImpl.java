package com.example.uploadfile.service.impl;

import com.example.uploadfile.exception.StorageFileNotFoundException;
import com.example.uploadfile.property.StorageProperties;
import com.example.uploadfile.service.StorageService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.stream.Stream;

@Service
public class StorageServiceImpl implements StorageService {

    private final Path rootLocation;

    public StorageServiceImpl(StorageProperties storageProperties) {
        this.rootLocation = Paths.get(storageProperties.getLocation());
    }

    @Override
    public void init() {
        try {
//            创建目录
            Files.createDirectory(rootLocation);
        } catch (IOException e) {
            throw new StorageFileNotFoundException("不能initial storage");
        }
    }

    @Override
    public void store(MultipartFile file) {
        if (file.isEmpty()) {
            throw new StorageFileNotFoundException("文件是空的");
        }
        Path destPath = this.rootLocation.resolve(Paths.get(Objects.requireNonNull(file.getOriginalFilename()))).normalize().toAbsolutePath();
        if (!destPath.getParent().equals(this.rootLocation.toAbsolutePath())) {
//          这是个安全检查
            throw new StorageFileNotFoundException("不能存储在当前目录外面");
        }
        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, destPath,
                    StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new StorageFileNotFoundException("失败去存储文件");
        }
    }

    @Override
    public Stream<Path> loadAll() {
        try {
//          加载所有文件
            return Files.walk(this.rootLocation, 1)
                    .filter(path -> !path.equals(this.rootLocation))
                    .map(this.rootLocation::relativize);
        } catch (IOException e) {
            throw new StorageFileNotFoundException("文件读取失败");
        }
    }

    @Override
    public Path load(String filename) {
//      解析文件名并返回Path对象
        return rootLocation.resolve(filename);
    }

    @Override
    public Resource loadAsResource(String filename) {
        Path file = load(filename);
        try {
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new StorageFileNotFoundException("不能读取文件：" + filename);
            }
        } catch (MalformedURLException e) {
            throw new StorageFileNotFoundException("不能读取文件：" + filename);
        }
    }

    @Override
    public void deleteAll() {
//      递归删除所有文件，包括根目录
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }
}
