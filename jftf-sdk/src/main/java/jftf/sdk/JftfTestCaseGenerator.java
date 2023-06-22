package jftf.sdk;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

import com.google.googlejavaformat.java.Formatter;
import com.google.googlejavaformat.java.FormatterException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;

public class JftfTestCaseGenerator {
    private static final String TEST_CASE_TEMPLATE_FILE = "vtemplates/JftfTestCaseTemplate.vm";
    private static final String BOOTSTRAPPER_TEMPLATE_FILE = "vtemplates/JftfTestBootstrapperTemplate.vm";
    private static final String OUTPUT_DIRECTORY = "jftf-sdk/src/main/java/jftf/gen/";
    private static final String PACKAGE_BASE = "jftf.gen.";

    public static void main(String[] args) {
        try {
            Velocity.setProperty(RuntimeConstants.RESOURCE_LOADERS, "classpath");
            Velocity.setProperty("resource.loader.classpath.class", ClasspathResourceLoader.class.getName());
            Velocity.init();

            String className = System.getenv("CLASS_NAME");
            String testGroup = System.getenv("TEST_GROUP");

            if (className == null || testGroup == null) {
                System.out.println("Please set the CLASS_NAME and TEST_GROUP environment variables.");
                return;
            }

            String outputDirectory = OUTPUT_DIRECTORY + className;
            String packageName = PACKAGE_BASE + className;

            generateTestCase(className, testGroup, outputDirectory, packageName);
            generateBootstrapper(className, testGroup, outputDirectory, packageName);
        } catch (ResourceNotFoundException e) {
            System.out.println("Template file not found.");
        } catch (Exception e) {
            System.out.println("An error occurred during template generation: " + e.getMessage());
        }
    }

    private static void generateTestCase(String className, String testGroup, String outputDirectory, String packageName) throws IOException {
        Template template = Velocity.getTemplate(TEST_CASE_TEMPLATE_FILE);

        VelocityContext context = new VelocityContext();
        context.put("packageName", packageName);
        context.put("className", className);
        context.put("testGroup", testGroup);

        StringWriter writer = new StringWriter();
        template.merge(context, writer);

        String generatedCode = writer.toString();

        System.out.println("Generated Test Case:\n" + generatedCode);

        saveGeneratedCode(generatedCode, className + ".java", outputDirectory);
    }

    private static void generateBootstrapper(String className, String testGroup, String outputDirectory, String packageName) throws IOException {
        Template template = Velocity.getTemplate(BOOTSTRAPPER_TEMPLATE_FILE);

        VelocityContext context = new VelocityContext();
        context.put("packageName", packageName);
        context.put("className", className);
        context.put("bootstrapperClassName", "JftfTestBootstrapper");
        context.put("testClass", className + ".class");
        context.put("testGroup", testGroup);

        StringWriter writer = new StringWriter();
        template.merge(context, writer);

        String generatedCode = writer.toString();

        System.out.println("Generated Bootstrapper:\n" + generatedCode);

        saveGeneratedCode(generatedCode, "JftfTestBootstrapper.java", outputDirectory);
    }

    private static void saveGeneratedCode(String generatedCode, String fileName, String outputDirectory) {
        File outputDir = new File(outputDirectory);
        if (!outputDir.exists()) {
            outputDir.mkdirs();
        }

        File outputFile = new File(outputDir, fileName);

        try (FileWriter writer = new FileWriter(outputFile)) {
            Formatter formatter = new Formatter();
            String formattedCode = formatter.formatSource(generatedCode);

            writer.write(formattedCode);
            System.out.println("Generated code saved to: " + outputFile.getAbsolutePath());
        } catch (IOException e) {
            System.out.println("An error occurred while saving the generated code: " + e.getMessage());
        } catch (FormatterException e) {
            System.out.println("An error occurred while formatting the generated code: " + e.getMessage());
        }
    }
}
