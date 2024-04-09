import com.data.center.TestApplication;
import com.data.center.pool.ConnectionPool;
import com.data.center.service.Impl.OpenDataServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.ExecutionException;

@SpringBootTest(classes = TestApplication.class)
@Slf4j
public class ConnectTest {


    @Autowired
    private ConnectionPool connectionPool;

    @Test
    public void test() throws ExecutionException, InterruptedException, SQLException {
        OpenDataServiceImpl dbUtil = new OpenDataServiceImpl();
        Connection connection = connectionPool.getConnection();


    }




}
