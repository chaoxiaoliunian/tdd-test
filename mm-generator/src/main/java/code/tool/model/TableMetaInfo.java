package code.tool.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author qishaojun
 */
@Getter
@AllArgsConstructor
public class TableMetaInfo {
    private String name;
    private String comment;
    private List<FieldMetaInfo> fields;

    public Map<String, FieldMetaInfo> getFieldMap() {
        Map<String, FieldMetaInfo> map = new LinkedHashMap<>(fields.size() * 2);
        for (FieldMetaInfo fieldMetaInfo : fields) {
            map.put(fieldMetaInfo.getName(), fieldMetaInfo);
        }
        return map;
    }
}
