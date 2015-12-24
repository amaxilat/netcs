package org.netcs.service;

import org.apache.log4j.Logger;
import org.netcs.model.sql.TerminationCondition;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.*;

/**
 * Functions executed asynchronously.
 *
 * @author ichatz@gmail.com.
 */
@Service
public class ExperimentPrintService {

    /**
     * a log4j logger to print messages.
     */
    protected static final Logger LOGGER = Logger.getLogger(ExperimentPrintService.class);

    @PostConstruct
    public void init() {
    }

    /**
     * Log a message for a specific user.
     */
    public String createGvFile(final TerminationCondition condition) {
        final StringBuilder label = new StringBuilder();
        label.append("label=\"Experiment ").
                append(condition.getExperiment().getId())
                .append("\\nExtracted from netcs.info and layed out by Graphviz \"\n");


        final StringBuilder sb = new StringBuilder();
        sb.append("digraph PhiloDilemma {\n" +
                "node [shape=circle,fixedsize=true,width=0.9];  " + condition.getNodes() + "\n");
        sb.append(condition.getEdges());
        sb.append("\n").append("overlap=false\n")
                .append(label.toString())
                .append("fontsize=12;\n").append("}\n").append("\n");

        return sb.toString();
    }

    public String write2GvFile(String gvFileText) {
        try {
            //create a temporary file
            final String fileName = System.currentTimeMillis() + ".gv";
            final File logFile = new File(fileName);

            final BufferedWriter writer = new BufferedWriter(new FileWriter(logFile));
            writer.write(gvFileText);

            //Close writer
            writer.close();
            return fileName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String generatePng(String gvFileName) throws IOException {
        final String command = "neato -Tsvg " + gvFileName;
        final String pngFile = gvFileName + ".png";

        final Runtime runtime = Runtime.getRuntime();
        final Process process = runtime.exec(command);
        try {
            process.waitFor();
            InputStream is = process.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            int line;
            StringBuilder png = new StringBuilder();

            final File logFile = new File(pngFile);

            final BufferedWriter writer = new BufferedWriter(new FileWriter(logFile));
            while ((line = br.read()) != -1) {
                writer.write(line);
            }
            //Close writer
            writer.close();
            return pngFile;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
}


