package org.eclipse.birt.report.engine.emitter.csv;

import java.io.OutputStream;

import org.eclipse.birt.report.engine.emitter.XMLWriter;

/**
 * Writer for CSV output, extends XMLWriter but omits XML tags.
 */
public class CSVWriter extends XMLWriter{

    /**
     * Constructs a CSVWriter.
     */
    public CSVWriter(){
        bImplicitCloseTag = false;
        bPairedFlag = false;
    }

    /**
     * Opens the writer with the given output stream and encoding.
     * @param out the output stream
     * @param string the encoding
     */
    public void open(OutputStream out, String string) {
        super.open(out,string);
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
     * @param textValue the text to write
     * @param delimiter the delimiter string
     * @param replaceDelimiterInsideTextWith replacement string for delimiter inside text
     */
    public void text(String textValue, String delimiter, String replaceDelimiterInsideTextWith) {
        if ( textValue == null || textValue.isEmpty( ) )
        {
            return;
        }
        // Replacing delimiter in Cell Data with user defined character for CSVWriter
        textValue = textValue.replace(delimiter,replaceDelimiterInsideTextWith);
        print( textValue );
    }

    /**
     * Writes a closing tag or delimiter to the output.
     * @param tagName the tag or delimiter to write
     */
    public void closeTag( String tagName )
    {
        print(tagName);
    }
}
