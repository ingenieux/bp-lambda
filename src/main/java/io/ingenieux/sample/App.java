package io.ingenieux.sample;

import com.amazonaws.services.lambda.runtime.Context;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

public class App {
    public static void defaultHandler(InputStream inputStream, OutputStream outputStream, Context ctx) throws Exception {
        PrintWriter pw = new PrintWriter(outputStream);

        pw.println("Hello, World!");
    }
}
