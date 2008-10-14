package DEAPI;

import java.io.File;
import java.io.IOException;
import org.eclipse.birt.core.framework.Platform;
import org.eclipse.birt.report.model.api.CellHandle;
import org.eclipse.birt.report.model.api.DataItemHandle;
import org.eclipse.birt.report.model.api.DesignConfig;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.DesignEngine;
import org.eclipse.birt.report.model.api.ElementFactory;
import org.eclipse.birt.report.model.api.IDesignEngine;
import org.eclipse.birt.report.model.api.IDesignEngineFactory;
import org.eclipse.birt.report.model.api.LabelHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.RowHandle;
import org.eclipse.birt.report.model.api.OdaDataSetHandle;
import org.eclipse.birt.report.model.api.OdaDataSourceHandle;
import org.eclipse.birt.report.model.api.SessionHandle;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.ContentException;
import org.eclipse.birt.report.model.api.command.NameException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.structures.ComputedColumn;
import org.eclipse.birt.report.model.api.metadata.IMetaDataDictionary;
import org.eclipse.birt.report.model.elements.interfaces.IReportItemModel;
import org.eclipse.birt.report.model.elements.interfaces.IStyleModel;

import com.ibm.icu.util.ULocale;

public class XMLReport
{

	ReportDesignHandle reportDesignHandle = null;

	ElementFactory elementFactory = null;

	IMetaDataDictionary dict = null;

	ComputedColumn cs1, cs2, cs3 = null;

	public static void main( String[] args ) throws SemanticException,
	IOException
	{
		new XMLReport( ).createReport( );
	}

	void createReport( ) throws SemanticException, IOException
	{
		//Configure the Engine and start the Platform
		DesignConfig config = new DesignConfig( );
		config.setBIRTHome("C:/birt/birt-runtime-2.3RC3/birt-runtime-2_3_0/ReportEngine");
		IDesignEngine engine = null;
		try{


			Platform.startup( config );
			IDesignEngineFactory factory = (IDesignEngineFactory) Platform
			.createFactoryObject( 
					IDesignEngineFactory.EXTENSION_DESIGN_ENGINE_FACTORY );
			engine = factory.createDesignEngine( config );

		}catch( Exception ex){
			ex.printStackTrace();
		}


		SessionHandle session = engine.newSessionHandle( ULocale.ENGLISH ) ;


		// Create a new report
		reportDesignHandle = session.createDesign( );

		// Element factory is used to create instances of BIRT elements.
		elementFactory = reportDesignHandle.getElementFactory( );

		dict = new DesignEngine( null ).getMetaData( );

		createMasterPages( );
		createDataSources( );
		createDataSets( );
		createBody( );

		//OdaDataSourceHandle dataSourceHandle = (OdaDataSourceHandle)reportDesignHandle.findDataSource("myxmldatasource");


		String outputPath = "output";//$NON-NLS-1$
		File outputFolder = new File( outputPath );
		if ( !outputFolder.exists( ) && !outputFolder.mkdir( ) )
		{
			throw new IOException( "Can not create the output folder" );//$NON-NLS-1$
		}
		reportDesignHandle.saveAs( "output/desample/XMLReport.rptdesign" );//$NON-NLS-1$
		System.out.println("finished");
	}





	private void createDataSources( ) throws SemanticException
	{
		OdaDataSourceHandle dataSourceHandle = 
			elementFactory.newOdaDataSource("Data Source", 
			"org.eclipse.birt.report.data.oda.xml");
		dataSourceHandle.setProperty("FILELIST", "C:/temp/xmltest2.xml");
		reportDesignHandle.getDataSources( ).add( dataSourceHandle );
	}

	private void createDataSets( ) throws SemanticException
	{
		// Data Set

		OdaDataSetHandle dsHandle = elementFactory.newOdaDataSet( "Data Set",
		"org.eclipse.birt.report.data.oda.xml.dataSet" );
		dsHandle.setDataSource( "Data Source" );
		dsHandle.setQueryText( 
		"table0#-TNAME-#table0#:#[/people/person/name]#:#{name;String;}" );

		reportDesignHandle.getDataSets( ).add( dsHandle );


	}

	private void createMasterPages( ) throws ContentException, NameException
	{
		DesignElementHandle simpleMasterPage = 
			elementFactory.newSimpleMasterPage( "Master Page" );//$NON-NLS-1$
		reportDesignHandle.getMasterPages( ).add( simpleMasterPage );
	}



	private void createBody( ) throws SemanticException
	{



		TableHandle table = elementFactory.newTableItem( null, 3, 1, 1, 1 );
		table.setProperty( IStyleModel.TEXT_ALIGN_PROP,
				DesignChoiceConstants.TEXT_ALIGN_CENTER );
		table.setWidth( "80%" );//$NON-NLS-1$
		table.setProperty( IReportItemModel.DATA_SET_PROP, "Data Set" );//$NON-NLS-1$

		PropertyHandle computedSet = table.getColumnBindings( );
		cs1 = StructureFactory.createComputedColumn( );

		cs1.setName("Name");
		cs1.setExpression( "dataSetRow[\"name\"]" );//$NON-NLS-1$
		computedSet.addItem( cs1 );



		// Header
		RowHandle header = (RowHandle) table.getHeader( ).get( 0 );

		CellHandle tcell = (CellHandle) header.getCells( ).get( 0 );
		LabelHandle label = elementFactory.newLabel( null );
		label.setText( "Name" );//$NON-NLS-1$
		tcell.getContent( ).add( label );



		DataItemHandle data = null;
		// Detail
		RowHandle detail = (RowHandle) table.getDetail( ).get( 0 );
		tcell = (CellHandle) detail.getCells( ).get( 0 );
		data = elementFactory.newDataItem( null );
		data.setResultSetColumn( cs1.getName( ) );
		tcell.getContent( ).add( data );


		reportDesignHandle.getBody( ).add( table );


	}


}
