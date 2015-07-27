package io.ingenieux.sample;

import com.amazonaws.services.lambda.runtime.Context;

import java.io.*;
import java.util.*;

public class Handlers {
    public static void rawHandler(final InputStream is, final OutputStream os, final Context ctx) throws Exception {
        File tempFile = File.createTempFile("cmds-", ".sh");

        String inputText = asString(is) + "\n";

        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            fos.write(inputText.getBytes());
        }

        ctx.getLogger().log("Input Text: " + inputText);

        runScriptFile(tempFile, os);
    }

    public static String arrayHandler(List<String> commands, final Context ctx) throws Exception {
        File tempFile = File.createTempFile("cmds-", ".sh");

        try (PrintWriter pw = new PrintWriter(tempFile)) {
            for (String s : commands) {
                pw.println(s);
            }
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        runScriptFile(tempFile, baos);

        return new String(baos.toByteArray());
    }

    public static class CommandArgs {
        List<String> commands = new ArrayList<>();

        public List<String> getCommands() {
            return commands;
        }

        public void setCommands(List<String> commands) {
            this.commands = commands;
        }
    }

    public static String pojoHandler(CommandArgs cmdArgs, final Context ctx) throws Exception {
        File tempFile = File.createTempFile("cmds-", ".sh");

        try (PrintWriter pw = new PrintWriter(tempFile)) {
            for (String s : cmdArgs.getCommands()) {
                pw.println(s);
            }
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        runScriptFile(tempFile, baos);

        return new String(baos.toByteArray());
    }

    public static Map<String, String> displayPropertiesHandler() throws Exception {
        Map<String, String> results = new LinkedHashMap<>();

        Properties p = new Properties();

        p.load(Handlers.class.getClassLoader().getResourceAsStream("default.properties"));

        for (Map.Entry<Object, Object> e : p.entrySet()) {
            results.put("" + e.getKey(), "" + e.getValue());
        }

        return results;
    }

    private static void runScriptFile(File tempFile, OutputStream os) throws Exception {
        PrintWriter pw = new PrintWriter(os, true);

        pw.println("--> Results from running " + tempFile.getAbsolutePath());

        ProcessBuilder psBuilder = new ProcessBuilder("/bin/bash", "-x", tempFile.getAbsolutePath()).//
                redirectErrorStream(true);//

        final Process process = psBuilder.start();

        new Thread(() -> copyStream(process.getInputStream(), os)).start();

        process.waitFor();

        int resultCode = process.exitValue();

        pw.println("--> Result Code: " + resultCode);
    }

    private static void copyStream(InputStream in, OutputStream out) {
        try {
            byte[] buf = new byte[64 * 1024];

            int nRead = -1;

            while (-1 != (nRead = in.read(buf)))
                out.write(buf, 0, nRead);
        } catch (IOException exc) {
            throw new RuntimeException(exc);
        }
    }


    public static String asString(InputStream is) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        copyStream(is, baos);

        return new String(baos.toByteArray());
    }
}
