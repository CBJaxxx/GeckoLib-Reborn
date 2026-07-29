import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapperBuilder;
import freemarker.template.Template;
import net.nerdypuzzle.geckolib.element.types.AnimatedEntity;

import java.io.StringReader;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Exercises the controller-list plumbing end to end: the filtering helpers on
 * AnimatedEntity plus the registerControllers block, which is extracted from the
 * real livingentity.java.ftl of both generators and rendered against a real
 * AnimatedEntity instance. Reading the actual templates means this test cannot
 * drift away from what MCreator will generate.
 *
 * <p>Field exposure is enabled on the object wrapper because MCreator's templates
 * read public fields directly (data.mobName, data.lerp and friends have no
 * getters), so this mirrors how the generator reads the element.
 *
 * <p>Run via tools/test-controller-codegen.sh
 */
public class TestControllerCodegen {

    private static final Path RESOURCES = Path.of("src/main/resources");

    private static final String ACCESSORS = """
			<#list data.getValidControllers() as ctrl>
			ANIMATION_${ctrl.name?upper_case}
			</#list>""";

    private static int failures = 0;

    public static void main(String[] args) throws Exception {
        AnimatedEntity element = new AnimatedEntity(null);
        element.lerp = 4;
        element.animationControllers = new ArrayList<>(List.of(
                controller("background", true, 0),      // explicit: no transition ramp
                controller("stagger", false, null),     // inherits the entity's lerp
                controller("action", true, 2),
                controller("movement", true),      // reserved - always dropped
                controller("Movement", false),     // reserved regardless of case - dropped
                controller("ATTACKING", true),     // reserved regardless of case - dropped
                controller("background", false),   // duplicate name - dropped
                controller("BackGround", true),    // duplicate ignoring case - dropped
                controller("bad name", true),      // not an identifier - dropped
                controller("", true)               // blank - dropped
        ));

        check("reserved names are rejected case-insensitively",
                AnimatedEntity.isValidControllerName("Movement") + "/"
                        + AnimatedEntity.isValidControllerName("PROCEDURE") + "/"
                        + AnimatedEntity.isValidControllerName("stance"),
                "false/false/true");

        check("valid controllers are filtered and deduplicated",
                names(element.getValidControllers()), "[background, stagger, action]");
        check("non-additive controllers", names(element.getBaseControllers()), "[stagger]");
        check("additive controllers", names(element.getAdditiveControllers()), "[background, action]");

        check("synched accessor names are upper-cased",
                collapse(render(ACCESSORS, element)),
                "ANIMATION_BACKGROUND ANIMATION_STAGGER ANIMATION_ACTION");

        // GeckoLib 5: additive controllers get additiveAnimations() so their keyframes
        // are offsets added on top of the base pose. They are still registered last,
        // because a non-additive controller assigns and would drop the offset.
        // Also covers per-controller transition ticks: stagger inherits the entity's 4,
        // background overrides it with 0, action with 2, while the built-in movement and
        // procedure controllers always keep the global value.
        check("26.1.2 emits additiveAnimations() and per-controller transition ticks",
                collapse(render(registerControllersBody("neoforge-26.1.2"), element)),
                "data.add(new AnimationController<>(\"stagger\", 4, this::controllerPredicate_stagger)); "
                        + "data.add(new AnimationController<>(\"movement\", 4, this::movementPredicate)); "
                        + "data.add(new AnimationController<>(\"procedure\", 4, this::procedurePredicate)); "
                        + "data.add(new AnimationController<>(\"background\", 0, this::controllerPredicate_background).additiveAnimations()); "
                        + "data.add(new AnimationController<>(\"action\", 2, this::controllerPredicate_action).additiveAnimations());");

        // GeckoLib 4.9.2 has no additiveAnimations(); the flag only affects ordering.
        check("1.21.1 never emits additiveAnimations()",
                collapse(render(registerControllersBody("neoforge-1.21.1"), element)),
                "data.add(new AnimationController<>(this, \"stagger\", 4, this::controllerPredicate_stagger)); "
                        + "data.add(new AnimationController<>(this, \"movement\", 4, this::movementPredicate)); "
                        + "data.add(new AnimationController<>(this, \"procedure\", 4, this::procedurePredicate)); "
                        + "data.add(new AnimationController<>(this, \"background\", 0, this::controllerPredicate_background)); "
                        + "data.add(new AnimationController<>(this, \"action\", 2, this::controllerPredicate_action));");

        // A looping animation must never be released by the predicate: GeckoLib reports
        // "finished" for a moment at every cycle boundary, and clearing the slot there
        // drops the pose for a frame.
        for (String generator : List.of("neoforge-26.1.2", "neoforge-1.21.1")) {
            String predicates = render(predicateSection(generator), element);
            check(generator + " predicate guards the release with a loop check",
                    countOccurrences(predicates, "!currentAnimationLoops(") + " of "
                            + countOccurrences(predicates, "= \"empty\";\n\t\t\tthis.prevAnim_"),
                    "3 of 3");
            check(generator + " predicate returns STOP only for empty and finished one-shots",
                    countOccurrences(predicates, "return PlayState.STOP;"), 6);

            // Replaying the animation a controller already holds must not depend on the
            // slot having been cleared, because the predicate that clears it only runs
            // while the entity is rendered.
            String template = String.join("\n", templateLines(generator));
            check(generator + " setter tags every request",
                    template.contains("tagAnimationRequest(animation)"), true);
            check(generator + " predicate plays the name without the token",
                    template.contains("thenPlay(stripAnimationRequest("), true);
            check(generator + " getter hides the token from procedures",
                    template.contains("-> stripAnimationRequest(this.animation_"), true);
        }

        // An entity saved before this feature existed deserializes with a null list.
        AnimatedEntity legacy = new AnimatedEntity(null);
        legacy.lerp = 4;
        legacy.animationControllers = null;
        check("null controller list degrades to no controllers",
                names(legacy.getValidControllers()), "[]");
        check("null controller list renders no accessors", render(ACCESSORS, legacy).trim(), "");
        check("null controller list still registers the built-ins",
                collapse(render(registerControllersBody("neoforge-26.1.2"), legacy)),
                "data.add(new AnimationController<>(\"movement\", 4, this::movementPredicate)); "
                        + "data.add(new AnimationController<>(\"procedure\", 4, this::procedurePredicate));");

        System.out.println(failures == 0 ? "All checks passed." : failures + " check(s) FAILED.");
        if (failures > 0)
            System.exit(1);
    }

    /**
     * Pulls the per-controller predicate out of a generator's entity template: the
     * whole #list block that generates one predicate method per controller.
     */
    private static String predicateSection(String generator) throws Exception {
        List<String> lines = templateLines(generator);
        int at = -1;
        for (int i = 0; i < lines.size(); i++) {
            if (lines.get(i).contains("private PlayState controllerPredicate_")) {
                at = i;
                break;
            }
        }
        if (at < 0)
            throw new IllegalStateException("controllerPredicate_ not found for " + generator);

        int start = at;
        while (start > 0 && !lines.get(start).trim().startsWith("<#list"))
            start--;
        int end = at;
        while (end < lines.size() && !lines.get(end).trim().equals("</#list>"))
            end++;

        return String.join("\n", lines.subList(start, Math.min(end + 1, lines.size())));
    }

    /**
     * Pulls the body of registerControllers out of a generator's entity template:
     * everything between the method signature and its closing brace at one tab.
     */
    private static List<String> templateLines(String generator) throws Exception {
        return Files.readAllLines(
                RESOURCES.resolve(generator).resolve("templates/aentity/livingentity.java.ftl"));
    }

    private static String registerControllersBody(String generator) throws Exception {
        List<String> lines = templateLines(generator);

        int start = -1;
        for (int i = 0; i < lines.size(); i++) {
            if (lines.get(i).contains("public void registerControllers")) {
                start = i + 1;
                break;
            }
        }
        if (start < 0)
            throw new IllegalStateException("registerControllers not found for " + generator);

        StringBuilder body = new StringBuilder();
        for (int i = start; i < lines.size(); i++) {
            if (lines.get(i).equals("\t}"))
                return body.toString();
            body.append(lines.get(i)).append('\n');
        }
        throw new IllegalStateException("unterminated registerControllers for " + generator);
    }

    private static AnimatedEntity.ControllerEntry controller(String name, boolean additive) {
        return controller(name, additive, null);
    }

    private static AnimatedEntity.ControllerEntry controller(String name, boolean additive,
            Integer transitionTicks) {
        AnimatedEntity.ControllerEntry entry = new AnimatedEntity.ControllerEntry();
        entry.name = name;
        entry.additive = additive;
        entry.transitionTicks = transitionTicks;
        return entry;
    }

    private static String names(List<AnimatedEntity.ControllerEntry> controllers) {
        return controllers.stream().map(controller -> controller.name).toList().toString();
    }

    private static int countOccurrences(String haystack, String needle) {
        int count = 0;
        for (int at = haystack.indexOf(needle); at >= 0; at = haystack.indexOf(needle, at + 1))
            count++;
        return count;
    }

    private static void check(String what, int actual, int expected) {
        check(what, String.valueOf(actual), String.valueOf(expected));
    }

    private static void check(String what, boolean actual, boolean expected) {
        check(what, String.valueOf(actual), String.valueOf(expected));
    }

    private static String collapse(String text) {
        return text.replaceAll("\\s+", " ").trim();
    }

    private static String render(String source, AnimatedEntity element) throws Exception {
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_34);
        DefaultObjectWrapperBuilder wrapperBuilder = new DefaultObjectWrapperBuilder(Configuration.VERSION_2_3_34);
        wrapperBuilder.setExposeFields(true);
        configuration.setObjectWrapper(wrapperBuilder.build());

        Map<String, Object> model = new HashMap<>();
        model.put("data", element);
        model.put("name", "TestMob");

        StringWriter writer = new StringWriter();
        new Template("fragment", new StringReader(source), configuration).process(model, writer);
        return writer.toString();
    }

    private static void check(String what, String actual, String expected) {
        if (expected.equals(actual)) {
            System.out.println("ok   " + what);
        } else {
            failures++;
            System.out.println("FAIL " + what);
            System.out.println("     expected: " + expected);
            System.out.println("     actual:   " + actual);
        }
    }
}
