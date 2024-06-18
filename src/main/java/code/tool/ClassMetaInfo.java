package code.tool;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

/**
 * @author qishaojun
 */
public record ClassMetaInfo(
        String name,
        String comment,
        List<FieldMetaInfo> fields) {
}
