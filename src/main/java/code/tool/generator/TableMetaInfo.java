package code.tool.generator;

import lombok.Getter;

import java.util.LinkedHashMap;

/**
 * @author qishaojun
 */
public record TableMetaInfo(String tableName, LinkedHashMap<String, String> columnMap) {
}
