package code.tool.datasource;

import lombok.Getter;

import java.util.LinkedHashMap;

/**
 * @author qishaojun
 */
@Getter
public record TableMetaInfo(String tableName, LinkedHashMap<String, String> columnMap) {
}
