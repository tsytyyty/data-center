import com.data.center.AcquisitionApplication;
import com.data.center.domain.Do.LoadingTable;
import com.data.center.domain.Do.UnloadingTable;
import com.data.center.service.CustomerInformationService;
import com.data.center.service.LoadingTableService;
import com.data.center.service.UnloadingTableService;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest(classes = AcquisitionApplication.class)
public class Test {

    @Autowired
    private CustomerInformationService customerInformationService;
    @Autowired
    private LoadingTableService loadingTableService;
    @Autowired
    private UnloadingTableService unloadingTableService;

    @org.junit.jupiter.api.Test
    public void test() throws ClassNotFoundException, IllegalAccessException, InstantiationException {
//        unloadingTableService.importDataFromExcel("D:\\学习\\软件杯\\数据\\模拟数据2020.xlsx");
        String className = "com.data.center.domain.Do.LoadingTable";
        Class<?> clazz = Class.forName(className);
        List<T> list = new ArrayList<>();
        Object o = clazz.newInstance();


        System.out.println(o instanceof LoadingTable);
        System.out.println(o instanceof UnloadingTable);
//        List<Object> list = createList(className);
//        list.forEach(System.out::println);
    }

    // 根据类名创建 List<MyClass> 类型的列表
    public <T> List<T> createList(String className) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        // 根据类名加载类
        Class<?> clazz = Class.forName(className);

        // 创建 List<T> 类型的列表
        List<T> list = new ArrayList<>();

        // 使用反射创建对象并添加到列表中
        T obj = (T) clazz.newInstance();
        list.add(obj);

        return list;
    }

}
