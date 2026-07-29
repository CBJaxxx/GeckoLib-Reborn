import freemarker.template.Configuration;
import freemarker.template.Template;

import java.io.FileReader;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

/**
 * Parses every .ftl under src/main/resources with FreeMarker so template syntax
 * errors (unbalanced #list/#if, bad directives) surface here instead of as a
 * broken code generator inside MCreator.
 *
 * <p>This only checks syntax. Directive and function calls such as
 * {@code <@procedureCode/>} or {@code hasProcedure()} are resolved when a template
 * is rendered, so they are accepted at parse time by design.
 *
 * <p>Run via tools/validate-templates.sh
 */
public class ValidateTemplates {

    public static void main(String[] args) throws Exception {
        Path root = Path.of(args.length > 0 ? args[0] : "src/main/resources");
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_34);

        List<Path> templates;
        try (Stream<Path> walk = Files.walk(root)) {
            templates = walk.filter(path -> path.toString().endsWith(".ftl")).sorted().toList();
        }

        int failed = 0;
        for (Path template : templates) {
            try (Reader reader = new FileReader(template.toFile())) {
                new Template(template.toString(), reader, configuration);
            } catch (Exception exception) {
                failed++;
                System.out.println("FAIL " + root.relativize(template));
                System.out.println("     " + exception.getMessage().lines().findFirst().orElse(""));
            }
        }

        System.out.println("Parsed " + templates.size() + " templates, " + failed + " failed.");
        if (failed > 0)
            System.exit(1);
    }
}
