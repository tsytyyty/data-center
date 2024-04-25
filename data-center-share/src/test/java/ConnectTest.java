import com.data.center.ShareApplication;
import com.data.center.controller.VisualController;
import com.data.center.pojo.Do.PortGoodsThroughput;
import com.data.center.pojo.result.Result;
import com.data.center.pool.ConnectionPool;
import com.data.center.service.Impl.OpenSqlServiceImpl;
import com.data.center.service.VisualService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@SpringBootTest(classes = ShareApplication.class)
@Slf4j
public class ConnectTest {


//    @Autowired
//    private ConnectionPool connectionPool;
//
//    @Autowired
//    private VisualService visualService;
//

    @Autowired
    private VisualController visualController;
    @Test
    public void test() throws ExecutionException, InterruptedException, SQLException {

        Result allVisualUrl = visualController.getAllVisualUrl();
        System.out.println(allVisualUrl);
    }




}
