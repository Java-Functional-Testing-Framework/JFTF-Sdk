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
    private static final String OUTPUT_DIRECTORY = "jftf-sdk/src/main/java/jftf/gen/BasicTest";
    private static final String PACKAGE_NAME = "jftf.sdk.BasicTest";
    private static final String CLASS_NAME = "JftfDemosBasicTest";
    private static final String BOOTSTRAPPER_CLASS_NAME = "JftfTestBootstrapper";
    private static final String TEST_GROUP = "examples"; // Set the desired test group value here

    public static void main(String[] args) {
        try {
            Velocity.setProperty(RuntimeConstants.RESOURCE_LOADERS, "classpath");
            Velocity.setProperty("resource.loader.classpath.class", ClasspathResourceLoader.class.getName());
            Velocity.init();

            generateTestCase();
            generateBootstrapper();
        } catch (ResourceNotFoundException e) {
            System.out.println("Template file not found.");
        } catch (Exception e) {
            System.out.println("An error occurred during template generation: " + e.getMessage());
        }
    }

    private static void generateTestCase() throws IOException {
        Template template = Velocity.getTemplate(TEST_CASE_TEMPLATE_FILE);

        VelocityContext context = new VelocityContext();
        context.put("packageName", PACKAGE_NAME);
        context.put("className", CLASS_NAME);
        context.put("testGroup", TEST_GROUP); // Pass the test group as a variable

        StringWriter writer = new StringWriter();
        template.merge(context, writer);

        String generatedCode = writer.toString();

        System.out.println("Generated Test Case:\n" + generatedCode);

        saveGeneratedCode(generatedCode, CLASS_NAME + ".java");
    }

    private static void generateBootstrapper() throws IOException {
        Template template = Velocity.getTemplate(BOOTSTRAPPER_TEMPLATE_FILE);

        VelocityContext context = new VelocityContext();
        context.put("packageName", PACKAGE_NAME);
        context.put("className", CLASS_NAME);
        context.put("bootstrapperClassName", BOOTSTRAPPER_CLASS_NAME);
        context.put("testClass", CLASS_NAME + ".class"); // Pass the test class as an argument
        context.put("testGroup", TEST_GROUP); // Pass the test group as a variable

        StringWriter writer = new StringWriter();
        template.merge(context, writer);

        String generatedCode = writer.toString();

        System.out.println("Generated Bootstrapper:\n" + generatedCode);

        saveGeneratedCode(generatedCode, BOOTSTRAPPER_CLASS_NAME + ".java");
    }

    private static void saveGeneratedCode(String generatedCode, String fileName) {
        File outputDirectory = new File(OUTPUT_DIRECTORY);
        if (!outputDirectory.exists()) {
            outputDirectory.mkdirs();
        }

        File outputFile = new File(outputDirectory, fileName);

        try (FileWriter writer = new FileWriter(outputFile)) {
            // Apply code formatting using Google Java Format
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
