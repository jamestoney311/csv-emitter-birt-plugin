package org.eclipse.birt.report.engine.emitter.csv;

import org.eclipse.birt.report.engine.emitter.XMLWriter;

import java.io.OutputStream;

/**
 * Writer for CSV output, extends XMLWriter but omits XML tags.
 */
public class CSVWriter extends XMLWriter {

    /**
     * Constructs a CSVWriter.
     */
    public CSVWriter() {
        bImplicitCloseTag = false;
        bPairedFlag = false;
    }

    /**
     * Opens the writer with the given output stream and encoding.
     *
     * @param out    the output stream
     * @param string the encoding
     */
    public void open(OutputStream out, String string) {
        super.open(out, string);
    }

    /**
     * Overridden to avoid writing XML tagging in beginning of file.
     */
    public void startWriter() {
        // do nothing here.
    }

    /**
     * Overridden to avoid flushing, handled in close.
     */
    public void endWriter() {
        // do nothing flushing will happen in close
    }

    /**
     * Closes the writer and flushes output.
     */
    public void close() {
        super.close();
    }

    /**
     * Writes text to the output, replacing delimiter inside cell data.
     *
     * @param textValue                      the text to write
     * @param delimiter                      the delimiter string
     * @param replaceDelimiterInsideTextWith replacement string for delimiter inside text
     */
    public void text(String textValue, String delimiter, String replaceDelimiterInsideTextWith) {
        text(textValue, delimiter, replaceDelimiterInsideTextWith, false, false);
    }

    /**
     * Writes text to the output, replacing delimiter inside cell data.
     *
     * @param textValue                      the text to write
     * @param delimiter                      the delimiter string
     * @param replaceDelimiterInsideTextWith replacement string for delimiter inside text
     * @param isQuoteWrappingEnabled         indicates if text should be wrapped
     */
    public void text(String textValue, String delimiter, String replaceDelimiterInsideTextWith, boolean isQuoteWrappingEnabled, boolean isFixedWidth) {
        if (textValue == null || textValue.isEmpty()) {
            if(isQuoteWrappingEnabled){
                print("\"\"");
            }
            return;
        }

        if (!isFixedWidth) {
            textValue = trim(textValue);
        }

        boolean containsDelimiter = textValue.contains(delimiter);
        boolean isAlreadyQuoted = textValue.startsWith("\"") && textValue.endsWith("\"") && textValue.length() >= 2;
        boolean needsQuoting = isQuoteWrappingEnabled || containsDelimiter;
        if (isAlreadyQuoted) {
            print(textValue);
        } else if (needsQuoting) {
            print("\"" + textValue + "\"");
        } else {
            if (replaceDelimiterInsideTextWith != null) {
                textValue = textValue.replace(delimiter, replaceDelimiterInsideTextWith);
            }
            print(textValue);
        }
    }

    /**
     * Clean the data from new lines and multiple whitespaces.
     *
     * @param textValue                      the text to remove inner whitespaces
     * @return cleaned text
     */
    public String trim(String textValue){
        if(textValue.trim().isEmpty()){
            return "";
        }

        if(!isNumber(textValue)) {
            return textValue;
        }

        textValue = textValue.replace("\r\n", " ").replace("\n", " ").replace("\r", " ");

        textValue = textValue.replaceAll("\\s+$", "").replaceAll("^\\s+", " ");

        return textValue;
    }

    /**
     * Check if textValue is a number.
     *
     * @param textValue                      the text to check if parsable to number format
     * @return true if textValue is a number.
     */
    public boolean isNumber(String textValue) {
        try {
            Double.parseDouble(textValue);
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }

    /**
     * Writes a closing tag or delimiter to the output.
     *
     * @param tagName the tag or delimiter to write
     */
    public void closeTag(String tagName) {
        print(tagName);
    }
}
