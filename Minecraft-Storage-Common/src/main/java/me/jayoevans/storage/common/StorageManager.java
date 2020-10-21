package me.jayoevans.storage.common;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.File;

public class StorageManager
{
    /*
        Reference:
        - https://github.com/awsdocs/aws-doc-sdk-examples/blob/master/javav2/example_code/s3/src/main/java/com/example/s3/PutObject.java
        - https://github.com/awsdocs/aws-doc-sdk-examples/blob/master/javav2/example_code/s3/src/main/java/com/example/s3/GetObjectData.java
        - https://github.com/awsdocs/aws-doc-sdk-examples/blob/master/javav2/example_code/s3/src/main/java/com/example/s3/DeleteObjects.java
     */

    public static final String BUCKET_NAME = "10413316-minecraft-server";

    private final S3Client client;

    public static void main(String[] args)
    {
        new StorageManager();
    }

    public StorageManager()
    {
        this.client = S3Client.builder()
                .region(Region.AP_SOUTHEAST_2)
                .build();

        try
        {
            CreateBucketRequest request = CreateBucketRequest.builder()
                    .bucket(BUCKET_NAME)
                    .createBucketConfiguration(CreateBucketConfiguration.builder()
                            .locationConstraint(Region.AP_SOUTHEAST_2.id())
                            .build())
                    .build();

            this.client.createBucket(request);
        }
        catch (Exception ignored)
        {
        }
    }

    public void getObject(String key, File destination)
    {
        GetObjectRequest request = GetObjectRequest.builder()
                .bucket(BUCKET_NAME)
                .key(key)
                .build();

        this.client.getObject(request, destination.toPath());
    }

    public void putObject(String key, File source)
    {
        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(BUCKET_NAME)
                .key(key)
                .build();

        this.client.putObject(request, RequestBody.fromFile(source));
    }

    public void deleteObject(String key)
    {
        DeleteObjectRequest request = DeleteObjectRequest.builder()
                .bucket(BUCKET_NAME)
                .key(key)
                .build();

        this.client.deleteObject(request);
    }

    public String getKey(String serverId)
    {
        return BUCKET_NAME + "-" + serverId;
    }
}
