package com.data.center.factory;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.data.center.factory.DataSource;
import com.data.center.utils.AcquisitionMinioUtil;
import com.data.center.utils.DataSourceCache;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Callable;

@Slf4j
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MinioDataSource implements DataSource {
    //id
    private int id;

    //名称
    private String name;

    //地址
    private String endpoint;

    //账号
    private String accessKey;

    //密码
    private String secretKey;

    //桶名
    private String bucketName;

    //文件名-数据类型
    Map<String, String> fileName;

    @Override
    public AmazonS3 getConnection(long timeout) {
        // 创建Minio客户端
        AWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);
        AwsClientBuilder.EndpointConfiguration endpointConfiguration =
                new AwsClientBuilder.EndpointConfiguration(endpoint, null);
        return AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .withEndpointConfiguration(endpointConfiguration)
                .build();
    }

    @Override
    public boolean testConnect() {
        try {
            AmazonS3 connection = getConnection(0);
            DataSourceCache.getInstance().putConnection(getHashCode(), connection);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public Callable<Object> dataAcquisition() {
        return () -> {
            AmazonS3 client = (AmazonS3) DataSourceCache.getInstance().getConnection(this);
            return AcquisitionMinioUtil.acquisitionToList(client, bucketName, fileName);
        };
    }

    @Override
    public Integer getHashCode() {
        return Objects.hash(id, name, endpoint, accessKey, secretKey, bucketName);
    }
}
