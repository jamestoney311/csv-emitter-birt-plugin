package org.eclipse.birt.report.engine.emitter.csv;


import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.IRenderOption;
import org.eclipse.birt.report.engine.content.*;
import org.eclipse.birt.report.engine.css.engine.value.birt.BIRTConstants;
import org.eclipse.birt.report.engine.emitter.ContentEmitterAdapter;
import org.eclipse.birt.report.engine.emitter.EmitterUtil;
import org.eclipse.birt.report.engine.emitter.IEmitterServices;
import org.eclipse.birt.report.engine.ir.EngineIRConstants;
import org.eclipse.birt.report.engine.presentation.ContentEmitterVisitor;
import org.eclipse.birt.report.model.api.CellHandle;
import org.eclipse.birt.report.model.api.OdaDataSetHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.RowHandle;
import org.eclipse.birt.report.model.api.elements.structures.OdaResultSetColumn;
import org.eclipse.birt.report.model.elements.Cell;
import org.eclipse.birt.report.model.elements.DataItem;
import org.eclipse.birt.report.model.elements.OdaDataSet;
import org.eclipse.birt.report.model.elements.interfaces.IDataItemModel;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CSVReportEmitter extends ContentEmitterAdapter
{		
	protected static Logger logger = Logger.getLogger( CSVReportEmitter.class.getName( ) );
	
	protected static final String OUTPUT_FORMAT_CSV = "csv";

	protected static final String REPORT_FILE = "report.csv";
	
	protected ContentEmitterVisitor contentVisitor;
	
	protected IEmitterServices services;
	
	protected CSVWriter writer; 
	
	protected IReportContent report;
	
	protected IRenderOption renderOption;
	
	protected int totalColumns;
	
	protected int currentColumn;
	
	protected OutputStream out = null;	
	
	protected boolean isFirstPage = false;	
	
	protected long firstTableID = -1;
	
	protected boolean writeData = true;
	
	protected Boolean showDatatypeInSecondRow = false;
	
	protected String tableToOutput;
	
	protected boolean outputCurrentTable;
	
	protected String delimiter = null;
	
	protected String replaceDelimiterInsideTextWith = null;

    protected Boolean isQuoteWrappingEnabled = false;

	protected Boolean debug = false;

	protected int tableNestingLevel = 0;

	protected boolean cellHasContent = false;

	protected int nestedTableColumns = 0;

	protected int nestedCurrentColumn = 0;

	public CSVReportEmitter( )
	{
		contentVisitor = new ContentEmitterVisitor( this );
	}

    private void debug(String message) {
        if (!debug) {
            return;
        }
        System.out.println("[CSVEmitter] " + message);
    }
	
	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.engine.emitter.ContentEmitterAdapter#initialize(org.eclipse.birt.report.engine.emitter.IEmitterServices)
	 */
	public void initialize( IEmitterServices services ) throws EngineException
	{
		this.services = services;		
		this.out = EmitterUtil.getOuputStream( services, REPORT_FILE );			
		
		writer = new CSVWriter( );	
	}
	
	public void start( IReportContent report )
	{
		logger.log( Level.FINE,"Starting CSVReportEmitter." );
		
		this.report = report;
		
		this.renderOption = report.getReportContext().getRenderOption();
		
		this.tableToOutput= (String)renderOption.getOption(ICSVRenderOption.EXPORT_TABLE_BY_NAME);		
		
		// Setting tableToOutput to Default as user has not set any Render Option to Output a specific Table
		if(tableToOutput == null)
		{
			this.tableToOutput="Default";
		}
		
		this.delimiter = (String)renderOption.getOption(ICSVRenderOption.DELIMITER);
		
		// Setting Default Field Delimiter if user has not specified any Delimiter
		if(delimiter == null)
		{
			delimiter = CSVTags.TAG_COMMA;
		}
		
		this.replaceDelimiterInsideTextWith = (String)renderOption.getOption(ICSVRenderOption.REPLACE_DELIMITER_INSIDE_TEXT_WITH);
		
		this.isQuoteWrappingEnabled = (Boolean)renderOption.getOption(ICSVRenderOption.ENABLE_QUOTE_WRAPPING);
        if(isQuoteWrappingEnabled == null)
        {
            isQuoteWrappingEnabled = false;
        }

        this.debug = Boolean.TRUE.equals(renderOption.getOption(ICSVRenderOption.DEBUG));
        if (debug) {
            debug("CSVReportEmitter debug mode enabled.");
        }

		// checking csv render option if set to export data type in second row of the output
		this.showDatatypeInSecondRow = (Boolean)renderOption.getOption(ICSVRenderOption.SHOW_DATATYPE_IN_SECOND_ROW);
		
		// Setting Default value to false if user has not specified aany value
		if(showDatatypeInSecondRow == null)
			showDatatypeInSecondRow = false;

		writer.open( out, "UTF-8" );
		writer.startWriter( );
	}
	
	public void end( IReportContent report )
	{
		logger.log( Level.FINE,"CSVReportEmitter end report." );
		
		writer.endWriter( );
		writer.close( );
		
		// Informing user if Table Name provided in Render Option is not found and Blank Report is getting generated
		if(tableToOutput != "Default" && report.getDesign().getReportDesign().findElement(tableToOutput) == null)
		{
			System.out.println(tableToOutput + " Table not found in Report Design. Blank Report Generated!!");
			logger.log(Level.WARNING, tableToOutput+ " Table not found in Report Design. Blank Report Generated!!");
		}
		if( out != null )
		{
			try
			{
				out.close( );
			}
			catch ( IOException e )
			{
				logger.log( Level.WARNING, e.getMessage( ), e );
			}
		}
	}
	
	public void startPage( IPageContent page ) throws BirtException
	{
		logger.log( Level.FINE,"CSVReportEmitter startPage" );
		
		startContainer( page );
		
		if(page.getPageNumber()>1){
			isFirstPage = false;
		}else{
			isFirstPage = true;			
		}		
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.engine.emitter.ContentEmitterAdapter#startLabel(org.eclipse.birt.report.engine.content.ILabelContent)
	 * To avoid framework to print label for every page.
	 */
	public void startLabel( ILabelContent label ) {
		if(isFirstPage){
            startText( label );
            debug("Label: " + label.getLabelText());
        }
	}
	
	public void startTable( ITableContent table )
	{
		assert table != null;

		tableNestingLevel++;
		debug("startTable - Nesting level: " + tableNestingLevel + ", Name: " + table.getName());

		// If this is a nested table (level > 1), mark the parent cell as having content
		if (tableNestingLevel > 1) {
			cellHasContent = true;
			// Track nested table column count
			nestedTableColumns = table.getColumnCount();
			debug("Nested table columns: " + nestedTableColumns);
		}

		// Only update totalColumns for the main table, not nested ones
		if (tableNestingLevel == 1) {
			totalColumns = table.getColumnCount();
		}

		if(firstTableID == -1)
			firstTableID = table.getInstanceID().getComponentID();		
		
		String currentTableName = table.getName();

        debug("Active table: " + currentTableName);
		
		// Only process table selection logic for the first level table
		// Nested tables inherit the parent's outputCurrentTable state
		if (tableNestingLevel == 1) {
			if(tableToOutput.equals("Default") && table.getInstanceID().getComponentID() == firstTableID)
			{
				this.outputCurrentTable = true;
			}
			else if(currentTableName != null && currentTableName.equals(this.tableToOutput))
			{
				this.outputCurrentTable = true;
			}
			else
			{
				this.outputCurrentTable = false;
			}
		}
		// For nested tables, keep the parent's outputCurrentTable state
		debug("outputCurrentTable after startTable: " + outputCurrentTable);
	}

	public void endTable( ITableContent table )
	{
		tableNestingLevel--;
		debug("endTable - Nesting level: " + tableNestingLevel + ", Name: " + table.getName());
	}

	public void startRow( IRowContent row )
	{
		assert row != null;
		
		debug("startRow - Nesting level: " + tableNestingLevel + ", RowID: " + row.getRowID());

		// Only skip footer/header rows for top-level table (nesting level 1)
		if (tableNestingLevel == 1) {
			if ( isRowInFooter( row ) || isRowInHeaderExceptFirstHeader(row) || outputCurrentTable!=true)
				writeData = false;
		}
		// For nested tables (level > 1), output ALL rows including headers and footers
		// The footer is used for aggregated data in nested tables

        // rowID will be 1 for the first data row, before printing the data row we need to print the datatype
		// printDatatypeInSecondRow will be called only once
		if ( showDatatypeInSecondRow && row.getRowID()==1)			
			printDatatypeInSecondRow(row);				
		
		// Only reset currentColumn for top-level table rows
		// Nested table rows should continue with the parent's column position
		if (tableNestingLevel == 1) {
			currentColumn = 0;
		} else {
			// For nested tables, reset the nested column counter
			nestedCurrentColumn = 0;
		}
	}
	
	public void startText( ITextContent text )
	{				
		if ( isHidden(text.getStyle()) )
		{
			debug("skip text (hidden)");
			logger.log( Level.FINE,"Skipping Hidden text" );
			return;
		}
		
		logger.log( Level.FINE,"Start text" );
		String textValue = text.getText( );

        debug("start text - Value: '" + textValue + "', writeData: " + writeData + ", currentColumn: " + currentColumn + ", nestingLevel: " + tableNestingLevel);
		if ( writeData )
		{
			writer.text( textValue, delimiter, replaceDelimiterInsideTextWith, isQuoteWrappingEnabled );
			cellHasContent = true;
		}
	}

	public void startData( IDataContent data )
	{
		if ( isHidden(data.getStyle()) )
		{
			debug("skip data (hidden)");
			logger.log( Level.FINE,"Skipping Hidden data" );
			return;
		}

		logger.log( Level.FINE,"Start data" );
		Object dataValue = data.getValue( );
		String textValue = dataValue == null ? "" : dataValue.toString();

        debug("start data - Value: '" + textValue + "', writeData: " + writeData + ", currentColumn: " + currentColumn + ", nestingLevel: " + tableNestingLevel);

		if ( writeData )
		{
			writer.text( textValue, delimiter, replaceDelimiterInsideTextWith, isQuoteWrappingEnabled );
			cellHasContent = true;
		}
	}

	public void startForeign( IForeignContent foreign )
	{
		if ( isHidden(foreign.getStyle()) )
		{
			debug("skip foreign (hidden)");
			logger.log( Level.FINE,"Skipping Hidden foreign" );
			return;
		}

		logger.log( Level.FINE,"Start foreign" );
		Object rawValue = foreign.getRawValue();
		String textValue = rawValue == null ? "" : rawValue.toString();

        debug("start foreign - Value: '" + textValue + "', writeData: " + writeData + ", currentColumn: " + currentColumn + ", nestingLevel: " + tableNestingLevel);
		if ( writeData )
		{
			writer.text( textValue, delimiter, replaceDelimiterInsideTextWith, isQuoteWrappingEnabled );
			cellHasContent = true;
		}
	}

	public void startAutoText( IAutoTextContent autoText )
	{
		if ( isHidden(autoText.getStyle()) )
		{
			debug("skip autotext (hidden)");
			logger.log( Level.FINE,"Skipping Hidden autotext" );
			return;
		}

		logger.log( Level.FINE,"Start autotext" );
		String textValue = autoText.getText( );

        debug("start autotext - L" + tableNestingLevel + " val:'" + textValue + "'");
		if ( writeData )
		{
			writer.text( textValue, delimiter, replaceDelimiterInsideTextWith, isQuoteWrappingEnabled );
			cellHasContent = true;
		}
	}

	public void startCell( ICellContent cell )
	{
		cellHasContent = false; // Reset for each cell
		debug("startCell - Column: " + currentColumn + ", writeData: " + writeData + ", colSpan: " + cell.getColSpan() + ", nestingLevel: " + tableNestingLevel);

		if ( isHidden(cell.getStyle()) )
		{
			debug("skip cell (hidden)");
			logger.log( Level.FINE,"Skipping Hidden cell" );
			return;
		}
	}

	public void endCell( ICellContent cell )
	{
		if ( isHidden(cell.getStyle()))
		{
			logger.log( Level.FINE,"Skipping Hidden cell" );
			return;
		}

		debug("endCell - Nesting level: " + tableNestingLevel + ", currentColumn: " + currentColumn + ", totalColumns: " + totalColumns + ", nestedCurrentColumn: " + nestedCurrentColumn + ", nestedTableColumns: " + nestedTableColumns);

		// For top-level table, increment column counter
		if (tableNestingLevel == 1) {
			currentColumn++;
			debug("Top-level: after increment currentColumn=" + currentColumn);
			if ( writeData && currentColumn < totalColumns )
			{
				writer.closeTag( delimiter );
				debug("\t-> insert delimiter");
			} else {
                debug("\t-> no delimiter");
            }
		} else {
			// Nested table cell - increment nested column counter
			nestedCurrentColumn++;
			debug("endCell - L" + tableNestingLevel + " nestCol:" + nestedCurrentColumn + "/" + nestedTableColumns);

			if ( writeData && nestedCurrentColumn < nestedTableColumns )
			{
				writer.closeTag( delimiter );
				debug("\t-> insert delimiter (nested)");
			} else {
                debug("\t-> no delimiter (nested)");
            }
		}
	}
	
	public void endRow( IRowContent row )
	{
		debug("endRow - L" + tableNestingLevel);

		if ( writeData && tableNestingLevel == 1 )
			writer.closeTag( CSVTags.TAG_CR );

		writeData = true;
	}	
	
	private boolean isHidden(IStyle style)
	{		
		String format=style.getVisibleFormat();
		
		if ( format != null && ( format.indexOf( EngineIRConstants.FORMAT_TYPE_VIEWER ) >= 0 || format.indexOf( BIRTConstants.BIRT_ALL_VALUE ) >= 0 ) )
		{
			return true;
		}
		else
		{
			return false;
		}		
	}
	
	private boolean isRowInFooter( IRowContent row )
	{
		IElement parent = row.getParent( );
		if ( !( parent instanceof IBandContent ) )
		{
			return false;
		}
		
		IBandContent band = ( IBandContent )parent;
		if ( band.getBandType( ) == IBandContent.BAND_FOOTER )
		{
			return true;
		}
		return false;
	}
	
	private boolean isRowInHeaderExceptFirstHeader( IRowContent row )
	{
		if(isFirstPage)
			return false;
		
		IElement parent = row.getParent( );
		if ( !( parent instanceof IBandContent ) )
		{
			return false;
		}
		
		IBandContent band = ( IBandContent )parent;
		if ( band.getBandType( ) == IBandContent.BAND_HEADER )
		{
			return true;
		}
		
		return false;
	}
	
	private void printDatatypeInSecondRow(IRowContent row)
	{		
		if(writeData)
		{			
			// ArrayList of columns used in Table in the same order as they appear in Report Design
			ArrayList<String> columnNamesInTableOrder = new ArrayList<String> ();
			
			// HashMap to contain ResultSetMetaData column and its respective DataType
			HashMap<String,String> resultSetMetaDatacolumnsWithDataType = new HashMap<String, String>();
			
			ReportDesignHandle reportDesignHandle=report.getDesign().getReportDesign();
			
			Object obj=reportDesignHandle.getElementByID(row.getInstanceID().getComponentID());
			
			RowHandle rowHandle=null;
			
			if(obj instanceof RowHandle)
			{
				rowHandle=(RowHandle)obj;
			}
			else
			{
				return; //not a row handle, nothing to do
			}
			
			@SuppressWarnings("unchecked")
			ArrayList<CellHandle> cells= (ArrayList<CellHandle>)rowHandle.getCells().getContents();
			
			for(CellHandle cellHandle:cells)
			{				
				Cell cell=(Cell)cellHandle.getElement();			
				
				@SuppressWarnings("rawtypes")
				ArrayList cellContents=(ArrayList)cell.getSlot(0).getContents();
				
				// Currently hard coded to get the first content only
				
				if(cellContents.get(0) instanceof DataItem)
				{
					DataItem cellDataItem=(DataItem)cellContents.get(0);									
					columnNamesInTableOrder.add((String)cellDataItem.getLocalProperty(report.getDesign()
							.getReportDesign().getModule(), IDataItemModel.RESULT_SET_COLUMN_PROP));					
				}
			}
			
			// fetching all data sets in report
			
			@SuppressWarnings("unchecked")
			ArrayList<OdaDataSetHandle> odaDataSetHandles=(ArrayList<OdaDataSetHandle>)report.getDesign()
					.getReportDesign().getAllDataSets();
			
			for(OdaDataSetHandle odaDataSetHandle:odaDataSetHandles)
			{
				OdaDataSet odaDataSet=(OdaDataSet)odaDataSetHandle.getElement();
				@SuppressWarnings("unchecked")
				ArrayList<OdaResultSetColumn> odaResultSetColumns=(ArrayList<OdaResultSetColumn>)odaDataSet
						.getLocalProperty(report.getDesign().getReportDesign().getModule(), "resultSet");
				for(OdaResultSetColumn odaResultSetColumn:odaResultSetColumns)
				{					
					resultSetMetaDatacolumnsWithDataType.put(odaResultSetColumn.getColumnName(), 
							odaResultSetColumn.getDataType());
				}
			}
			
			// printing the datatype in the same order as column appearing in the report
			for(int i=0;i<columnNamesInTableOrder.size();i++)
			{
				String dataType=resultSetMetaDatacolumnsWithDataType.get(columnNamesInTableOrder.get(i));
				
				if(dataType != null)
					writer.text(dataType, delimiter, replaceDelimiterInsideTextWith, isQuoteWrappingEnabled);

				if(i < columnNamesInTableOrder.size()-1)
					writer.closeTag(delimiter);
				else
					writer.closeTag(CSVTags.TAG_CR);
			}

		}
	}
	
}
