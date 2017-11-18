package me.alextur.matlab.octave;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

/**
 * @author Alex Turchynovich
 */
@Component
public class OctaveExecutor {

    private Logger logger = Logger.getLogger(OctaveExecutor.class);

    private OctaveConfig config;

    public OctaveExecutor(@Autowired OctaveConfig pConfig) {
        config = pConfig;
    }

    private void runOctaveProcess(String input, Writer outputWriter, Writer errorWriter) throws IOException {
        ProcessBuilder builder = new ProcessBuilder();
        builder.command(config.getCommand(), "--eval", input);
        Process p = builder.start();

        boolean finished = false;
        try {
            finished = p.waitFor(config.getProcessTimeout(), TimeUnit.SECONDS);
        } catch (InterruptedException pE) {
            logger.error("timeout for octave is exceed!", pE);
        }

        if(!finished) {
            p.destroyForcibly();

            errorWriter.write("Timed out (possibly an infinite loop or long calculations)\n");

            return;
        }

        IOUtils.copy(p.getInputStream(), outputWriter, Charset.defaultCharset());
        IOUtils.copy(p.getErrorStream(), errorWriter, Charset.defaultCharset());
    }

    public ExecutionResult execute(ExecutionRequest pExecutionRequest){
        ExecutionResult result = new ExecutionResult();
        try {

            StringWriter outputWriter = new StringWriter();
            StringWriter errorWriter = new StringWriter();

            runOctaveProcess(pExecutionRequest.getSourceCode(), outputWriter, errorWriter);

            result.setOutput(outputWriter.toString());
            result.setErrors(errorWriter.toString());

            outputWriter.close();
            errorWriter.close();
        } catch (IOException pE) {
            logger.error("unadlbe to execute octave", pE);
        }

        return result;
    }

}
