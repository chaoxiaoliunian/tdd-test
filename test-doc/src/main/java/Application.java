import java.util.Arrays;

public class Application {
    public static void main(String[] args) {
        ExcelGenerator excelGenerator = new ExcelGenerator();
        String user = get(args, 2);
        String date = get(args, 3);
        String version = get(args, 4);
        excelGenerator.generatorExcel(Arrays.asList(args[0].split(",")), args[1], user, date, version);
    }

    private static String get(String[] args, int index) {
        if (args.length >= (index + 1)) {
            return args[index];
        }
        return "";
    }
}
