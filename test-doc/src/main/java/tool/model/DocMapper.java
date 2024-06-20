package tool.model;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author qishaojun
 */
@Mapper
public interface DocMapper {
    DocMapper INSTANCE = Mappers.getMapper(DocMapper.class);

    TestData toTestData(TestDoc testDoc);

    List<TestData> toTestDataList(List<TestDoc> testDocs);
}
