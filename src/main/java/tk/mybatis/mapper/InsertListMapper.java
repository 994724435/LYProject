package tk.mybatis.mapper;

import org.apache.ibatis.annotations.InsertProvider;
import java.util.List;


public interface InsertListMapper<T> {

//    @Options(useGeneratedKeys = true, keyProperty = "id")
//    @SelectKey(before=true,resultType=String.class,keyProperty="id",statement="SELECT REPLACE(UUID(),'-','')")
    @InsertProvider(type = SpecialProvider.class, method = "dynamicSQL")
    int insertList(List<T> recordList);

}