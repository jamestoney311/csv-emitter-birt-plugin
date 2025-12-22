package org.eclipse.birt.report.engine.emitter.csv;

import org.eclipse.birt.report.engine.api.IRenderOption;

/**
 * Interface for CSV render options used by the CSV emitter.
 */
public interface ICSVRenderOption extends IRenderOption{
    /**
     * CSV Emitter Id.
     */
    String OUTPUT_EMITTERID_CSV = "org.eclipse.birt.report.engine.emitter.csv";

    /**
     * CSV Output Format.
     */
    String OUTPUT_FORMAT_CSV = "csv";

    /**
     * The option to decide to show data type in second row of output CSV.
     */
    String SHOW_DATATYPE_IN_SECOND_ROW = "csvRenderOption.showDatatypeInSecondRow";

    /**
     * The option to export a specific table by name in the CSV Output.
     */
    String EXPORT_TABLE_BY_NAME = "csvRenderOption.exportTableByName";

    /**
     * The option to specify the field delimiter, default is comma.
     */
    String DELIMITER = "csvRenderOption.Delimiter";

    /**
     * The option to specify the character with which delimiter should be replaced with
     * if it occurs inside the text, default is blank space.
     */
    String REPLACE_DELIMITER_INSIDE_TEXT_WITH = "csvRenderOption.replaceDelimiterInsideTextWith";

    /**
     * Flag to indicate whether text should be wrapped with quotes.
     */
    String ENABLE_QUOTE_WRAPPING = "csvRenderOption.isQuoteWrappingEnabled";

    /**
     * Sets whether to show datatype in the second row.
     * @param showDatatypeInSecondRow true to show datatype in second row
     */
    void setShowDatatypeInSecondRow(boolean showDatatypeInSecondRow);

    /**
     * Gets whether to show datatype in the second row.
     * @return true if datatype is shown in second row
     */
    boolean getShowDatatypeInSecondRow();

    /**
     * Sets the table name to export.
     * @param tableName name of the table
     */
    void setExportTableByName(String tableName);

    /**
     * Gets the table name to export.
     * @return table name
     */
    String getExportTableByName();

    /**
     * Sets the field delimiter.
     * @param fieldDelimiter delimiter string
     */
    void setDelimiter (String fieldDelimiter);

    /**
     * Gets the field delimiter.
     * @return delimiter string
     */
    String getDelimiter ();

    /**
     * Sets the replacement for delimiter inside text.
     * @param replaceDelimiterInsideTextWith replacement string
     */
    void setReplaceDelimiterInsideTextWith (String replaceDelimiterInsideTextWith);

    /**
     * Gets the replacement for delimiter inside text.
     * @return replacement string
     */
    String getReplaceDelimiterInsideTextWith ();

    /**
     * Enable or disable wrapping text values with quotes.
     * @param isQuoteWrappingEnabled true to wrap text values
     */
    void setQuoteWrappingEnabled(boolean isQuoteWrappingEnabled);

    /**
     * Checks if wrapping text values with quotes is enabled.
     * @return true if quote wrapping is enabled
     */
    boolean isQuoteWrappingEnabled();
}
