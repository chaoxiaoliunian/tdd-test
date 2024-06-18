package code.tool;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author qishaojun
 */
public record TableMetaInfo(
        String name,
        String comment,
        List<FieldMetaInfo> fields) {
    public Map<String, FieldMetaInfo> getFieldMap() {
        Map<String, FieldMetaInfo> map = new LinkedHashMap<>(fields.size() * 2);
        for (FieldMetaInfo fieldMetaInfo : fields) {
            map.put(fieldMetaInfo.name(), fieldMetaInfo);
        }
        return map;
    }
}
