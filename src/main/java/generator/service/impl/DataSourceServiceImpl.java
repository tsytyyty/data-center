package generator.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import generator.domain.DataSource;
import generator.service.DataSourceService;
import generator.mapper.DataSourceMapper;
import org.springframework.stereotype.Service;

/**
* @author 25656
* @description 针对表【data_source】的数据库操作Service实现
* @createDate 2024-04-09 17:35:30
*/
@Service
public class DataSourceServiceImpl extends ServiceImpl<DataSourceMapper, DataSource>
    implements DataSourceService{

}




