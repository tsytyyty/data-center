package com.data.center.pool;

import com.data.center.service.Impl.OpenSqlServiceImpl;
import com.data.center.utils.DbUtil;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@Data
public class ConnectionPool implements IConnectionPool {

    /**
     * 空闲连接池
     */
    private LinkedBlockingQueue<Connection> freeConnectPool = new LinkedBlockingQueue<>();
    /**
     * 活跃连接池
     */
    private LinkedBlockingQueue<Connection> busyConnectPool = new LinkedBlockingQueue<>();
    /**
     * 当前正在被使用的连接数
     */
    private AtomicInteger activeSize = new AtomicInteger(0);

//    @Value("${connection.pool.maxSize}")
//    private static int maxSize;
//    @Value("${connection.pool.initSize}")
//    private static int initSize;
//    @Value("${connection.pool.timeout}")
//    private static long timeout;

    private static int maxSize = 10;
    private static int initSize = 3;
    private static long timeout = 10000;


//    private volatile static ConnectionPool instance;    //避免指令重拍

//    public static ConnectionPool getInstance() {
//        if (instance == null) {                         //双重判断保证效率
//            synchronized (ConnectionPool.class) {
//                if (instance == null) {
//                    instance = new ConnectionPool();
//                }
//            }
//        }
//        return instance;
//    }

    @Autowired
    private ConnectionPool() {
        init();
    }

    private void init() {
        for (int i = 0; i < initSize; i++) {
            Connection connection = DbUtil.createConnection();
            freeConnectPool.offer(connection);
        }
    }

    @Override
    public Connection getConnection() {
        // 从free池中取出一个连接
        Connection connection = freeConnectPool.poll();
        while (connection != null) {
            try {
                if (connection.isValid(2000)) {
                    busyConnectPool.offer(connection);
//                    connection.setCallTime(System.currentTimeMillis());
                    return connection;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (activeSize.get() > 0) activeSize.decrementAndGet();
            connection = freeConnectPool.poll();
            createConnectionAsync();
        }


        // 通过 activeSize.incrementAndGet() <= maxSize 这个判断
        // 解决 if(activeSize.get() < maxSize) 存在的线程安全问题
        if (activeSize.incrementAndGet() <= maxSize) {
            connection = DbUtil.createConnection();// 创建新连接
            busyConnectPool.offer(connection);
            return connection;
        }


        // 如果空闲池中连接数达到maxSize， 则阻塞等待归还连接
        try {
            System.out.println("排队等待连接");
            connection = freeConnectPool.poll(timeout, TimeUnit.MILLISECONDS);// 阻塞获取连接，如果10秒内有其他连接释放
            if (connection == null) {
                System.out.println("等待超时");
                throw new RuntimeException("等待连接超时");
            }
            System.out.println("等待到了一个连接");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return connection;
    }

    /**
     * 异步创建连接
     */
    public void createConnectionAsync() {
        new Thread(() -> {
            Connection connection = DbUtil.createConnection();
            freeConnectPool.offer(connection);
            activeSize.incrementAndGet();
        }).start();
    }

    @Override
    public void release(Connection connection) {
        if (connection != null) {
            busyConnectPool.remove(connection);
            freeConnectPool.offer(connection);
        }
    }

    /**
     * 定时对连接进行健康检查
     * 注意：只能对idle连接池中的连接进行健康检查，
     * 不可以对busyConnectPool连接池中的连接进行健康检查，因为它正在被客户端使用;
     */
    @Scheduled(fixedRate = 60 * 1000)
    public void check() {
        for (int i = 0; i < freeConnectPool.size(); i++) {
            Connection connection = freeConnectPool.poll();
//            long l = System.currentTimeMillis() - connection.getCallTime();
            try {
                boolean valid = connection.isValid(2000);
                // 如果连接失效，并且连接数大于3，则删除连接
                if (!valid && activeSize.get() > 3) {
                    activeSize.decrementAndGet();
                    continue;
                }
                // 如果连接失效，并且连接数小于等于3，则重新创建一个连接
                if (!valid && activeSize.get() <= 3) {
                    connection = DbUtil.createConnection();
                }
                freeConnectPool.offer(connection);// 放进一个可用的连接
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
