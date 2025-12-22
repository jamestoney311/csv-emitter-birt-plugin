package org.eclipse.birt.report.engine.emitter.csv;

import org.eclipse.birt.report.engine.api.IRenderOption;
import org.eclipse.birt.report.engine.api.RenderOption;

/**
 * Provides options for CSV report rendering.
 */
public class CSVRenderOption extends RenderOption implements ICSVRenderOption{

    /**
     * Constant for CSV format.
     */
    public static final String CSV = "CSV";

    /**
     * Default constructor.
     */
    public CSVRenderOption( )
    {
        super();
    }

    /**
     * Constructor with options.
     * @param options render options
     */
    public CSVRenderOption( IRenderOption options )
    {
        super( options );
    }

    /**
     * Sets whether to show datatype in the second row.
     * @param showDatatypeInSecondRow true to show datatype in second row
     */
    public void setShowDatatypeInSecondRow(boolean showDatatypeInSecondRow)
    {
        setOption( SHOW_DATATYPE_IN_SECOND_ROW, showDatatypeInSecondRow );
    }

    /**
     * Gets whether to show datatype in the second row.
     * @return true if datatype is shown in second row
     */
    public boolean getShowDatatypeInSecondRow()
    {
        return getBooleanOption(SHOW_DATATYPE_IN_SECOND_ROW, true);
    }

    /**
     * Sets the table name to export.
     * @param tableName name of the table
     */
    public void setExportTableByName(String tableName)
    {
        setOption(EXPORT_TABLE_BY_NAME,tableName);
    }

    /**
     * Gets the table name to export.
     * @return table name
     */
    public String getExportTableByName()
    {
        return getStringOption(EXPORT_TABLE_BY_NAME);
    }

    /**
     * Sets the field delimiter.
     * @param fieldDelimiter delimiter string
     */
    public void setDelimiter(String fieldDelimiter)
    {
        setOption(DELIMITER,fieldDelimiter);
    }

    /**
     * Gets the field delimiter.
     * @return delimiter string
     */
    public String getDelimiter()
    {
        return getStringOption(DELIMITER);
    }

    /**
     * Sets the replacement for delimiter inside text.
     * @param replaceDelimiterInsideTextWith replacement string
     */
    public void setReplaceDelimiterInsideTextWith(String replaceDelimiterInsideTextWith)
    {
        setOption(REPLACE_DELIMITER_INSIDE_TEXT_WITH, replaceDelimiterInsideTextWith);
    }

    /**
     * Gets the replacement for delimiter inside text.
     * @return replacement string
     */
    public String getReplaceDelimiterInsideTextWith()
    {
        return getStringOption(REPLACE_DELIMITER_INSIDE_TEXT_WITH);
    }

    /**
     * Sets the text wrapper.
     * @param enableQuoteWrapping true to wrap text values with quotes
     */
    public void setQuoteWrappingEnabled(boolean enableQuoteWrapping)
    {
        setOption(ENABLE_QUOTE_WRAPPING, enableQuoteWrapping);
    }

    /**
     * Gets whether text is wrapped.
     * @return true if wrapping is enabled
     */
    public boolean isQuoteWrappingEnabled()
    {
        return getBooleanOption(ENABLE_QUOTE_WRAPPING, false);
    }
}
