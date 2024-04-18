package com.data.center.factory;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.data.center.utils.AcquisitionMinioUtil;
import com.data.center.utils.DataSourceCache;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;

import java.util.*;
import java.util.concurrent.Callable;

@Slf4j
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MinioDataSource implements DataSource {

    //UUID
    private String id;

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
    public int testConnect() {
        AmazonS3 client = null;
        try {
            //创建连接测试
            client = getConnection(0);
        } catch (Exception e) {
            //连接测试失败
            log.error(e.getMessage(), e);
            return 500;
        }
        //将连接放入缓存
        DataSourceCache.getInstance().putConnection(id, client);
        return 200;
    }

    @Override
    public Map<String, Object> testConnectBeforeAdd() {
        Map<String, Object> map = new HashMap<>();
        List<String> errorList = new ArrayList<>();
        AmazonS3 client = null;
        try {
            //创建连接测试
            client = getConnection(0);
        } catch (Exception e) {
            //连接测试失败
            log.error(e.getMessage(), e);
            map.put("code", 500);
            return map;
        }
        //检查数据源信息中文件是否存在
        ObjectListing objectListing = client.listObjects(bucketName);
        List<S3ObjectSummary> objectSummaries = objectListing.getObjectSummaries();

        Set<String> fileNameSet = new HashSet<>();
        objectSummaries.listIterator().forEachRemaining(s3ObjectSummary -> {
            fileNameSet.add(s3ObjectSummary.getKey());
        });

        fileName.keySet().forEach(f -> {
            if (!fileNameSet.contains(f)) {
                errorList.add(f);
            }
        });
        if (errorList.size() > 0) {
            map.put("code", 404);
            map.put("errorList", errorList);
            return map;
        }
        map.put("code", 200);
        map.put("connection",client);
        return map;
    }

    @Override
    public Callable<Map<String, List<Object>>> dataAcquisition() {
        return () -> {
            AmazonS3 client = (AmazonS3) DataSourceCache.getInstance().getConnection(this);
            return AcquisitionMinioUtil.acquisitionToList(name, client, bucketName, fileName);
        };
    }

    @Override
    public String getUUID() {
        return this.getId();
    }

}
