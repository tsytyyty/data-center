import com.data.center.AcquisitionApplication;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = AcquisitionApplication.class)
public class AcqTest {

    @Test
    public void test() {
        String fileName = "1.2.3.csv";
        String[] split = fileName.split("\\.");
        String fileType = split[split.length - 1];
        System.out.println(fileType);
    }


}
