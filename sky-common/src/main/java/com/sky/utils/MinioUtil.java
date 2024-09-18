package com.sky.utils;

import io.minio.GetObjectArgs;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.MinioException;
import io.minio.http.Method;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Data
@AllArgsConstructor
@Slf4j
public class MinioUtil {

    private String endpoint;
    private String accessKey;
    private String secretKey;
    private String bucketName;
    private int expiryTime;


    private MinioClient minioClient;

    /**
     * 创建客户端实例
     */
    public MinioUtil(String endpoint, String accessKey, String secretKey, String bucketName, int expiryTime) {
        this.endpoint = endpoint;
        this.accessKey = accessKey;
        this.secretKey = secretKey;
        this.bucketName = bucketName;
        this.expiryTime = expiryTime;
        try {
            minioClient = MinioClient.builder()
                    .endpoint(endpoint)
                    .credentials(accessKey, secretKey)
                    .build();
            log.info("创建客户端实例成功");
        } catch (Exception e) {
            log.error("创建客户端实例失败", e);
        }
    }

    /**
     * 上传文件
     * @param fileName
     * @param is
     * @return
     */
    public String upload(String fileName, InputStream is, long size) {
        log.info("开始上传文件: {}", fileName);

        try {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(fileName)
                            .stream(is, size, -1)
                            .build());
            log.info("文件{}上传成功", fileName);
            return fileName;
        } catch (MinioException | IOException | InvalidKeyException | NoSuchAlgorithmException e) {
            log.error("上传文件{}失败", fileName, e);
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                log.error("关闭输入流失败", e);
            }
        }

        return null;
    }

    /**
     * 下载文件（尽量不要使用，严重影响服务器性能，建议使用更简单的预签名）
     * @param fileName
     * @return
     */
    public InputStream downloadObject(String fileName) {
        log.info("开始下载文件:{}", fileName);

        try {
            return minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(bucketName)
                            .object(fileName)
                            .build()
            );
        } catch (MinioException | InvalidKeyException | IOException | NoSuchAlgorithmException e) {
            log.error("下载文件{}失败", fileName, e);
        }

        return null;
    }

    /**
     * 生成文件url访问地址
     * @param fileName
     * @return
     */
    public String getUrl(String fileName) {
        log.info("生成文件{}的URL", fileName);

        try {
            String url = minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .bucket(bucketName)
                            .object(fileName)
                            .method(Method.GET)
                            .expiry(expiryTime)
                            .build());
            return url;
        } catch (MinioException | InvalidKeyException | IOException | NoSuchAlgorithmException e) {
            log.error("生成文件{}的URL失败", fileName, e);
        }

        return null;
    }
}
