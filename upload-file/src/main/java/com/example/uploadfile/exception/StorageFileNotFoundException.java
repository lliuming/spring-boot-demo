package com.example.uploadfile.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StorageFileNotFoundException extends RuntimeException {

    public static final Logger logger = LoggerFactory.getLogger(StorageFileNotFoundException.class);

    public StorageFileNotFoundException(String message) {
        super(message);
        logger.info("上传文件没有找到");
    }
}
