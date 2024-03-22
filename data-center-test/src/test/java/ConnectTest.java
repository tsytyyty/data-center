import com.data.center.TestApplication;
import com.data.center.pool.ConnectionPool;
import com.data.center.utils.DbUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.annotation.Async;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@SpringBootTest(classes = TestApplication.class)
@Slf4j
public class ConnectTest {


    @Autowired
    private ConnectionPool connectionPool;
    @Test
    public void test() throws ExecutionException, InterruptedException, SQLException {
        DbUtil dbUtil = new DbUtil();
        Connection connection = connectionPool.getConnection();

    }

    /**
     * 异步创建连接
     */
    public static void createConnectionAsync() throws ExecutionException, InterruptedException {
        Runnable runnable = () -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            log.info("end asyncMethod");
        };
        Thread thread = new Thread(runnable);
        thread.setDaemon(true);
        thread.start();

        // 在这里执行异步操作
//        CompletableFuture<Void> completableFuture = CompletableFuture.runAsync(() -> {
//            log.info("Start asyncMethod");
//            int i = 0;
//            while (true){
//                if (i++ == 1000000) {
//                    break;
//                }
//            }
//            log.info("End asyncMethod");
//        });


    }


}
