package me.alextur.octave.octave;

/**
 * @author Alex Turchynovich
 */
public class ExecutionResult {

    private String output;
    private String errors;

    public String getErrors() {
        return errors;
    }

    public void setErrors(String pErrors) {
        errors = pErrors;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String pOutput) {
        output = pOutput;
    }
}
