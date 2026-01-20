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
        text(textValue, delimiter, replaceDelimiterInsideTextWith, false);
    }

    /**
     * Clean the data from new lines and multiple whitespaces.
     *
     * @param textValue                      the text to remove inner whitespaces
     * @return cleaned text
     */
    public String removeInnerWhiteSpaces(String textValue){
        textValue = textValue.replace("\r\n", " ").replace("\n", " ").replace("\r", " ");

        int leadingSpaces = 0;
        int trailingSpaces = 0;

        while (leadingSpaces < textValue.length() && textValue.charAt(leadingSpaces) == ' ') {
            leadingSpaces++;
        }

        while (trailingSpaces < textValue.length() &&
                textValue.charAt(textValue.length() - 1 - trailingSpaces) == ' ') {
            trailingSpaces++;
        }

        if (leadingSpaces + trailingSpaces < textValue.length()) {
            String middle = textValue.substring(leadingSpaces, textValue.length() - trailingSpaces);
            middle = middle.replaceAll("\\s+", " ");

            textValue = textValue.substring(0, leadingSpaces) + middle + textValue.substring(textValue.length() - trailingSpaces);
        }

        return textValue;
    }

    /**
     * Writes text to the output, replacing delimiter inside cell data.
     *
     * @param textValue                      the text to write
     * @param delimiter                      the delimiter string
     * @param replaceDelimiterInsideTextWith replacement string for delimiter inside text
     * @param isQuoteWrappingEnabled         indicates if text should be wrapped
     */
    public void text(String textValue, String delimiter, String replaceDelimiterInsideTextWith, boolean isQuoteWrappingEnabled) {
        if (textValue == null || textValue.isEmpty()) {
            if(isQuoteWrappingEnabled){
                print("\"\"");
            }
            return;
        }

        textValue = removeInnerWhiteSpaces(textValue);

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
     * Writes a closing tag or delimiter to the output.
     *
     * @param tagName the tag or delimiter to write
     */
    public void closeTag(String tagName) {
        print(tagName);
    }
}
