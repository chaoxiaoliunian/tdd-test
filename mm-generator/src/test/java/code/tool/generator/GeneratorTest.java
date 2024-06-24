package code.tool.generator;

import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class GeneratorTest {
    @Test
    public void testGenerateClass() throws IOException {
        String fileName = this.getClass().getResource("/")
                .getPath().replaceAll("target/test-classes/", "src/main/java/code/tool/model/ExampleModel.java");
        System.out.println(Files.readString(Path.of(fileName)));
    }
}
