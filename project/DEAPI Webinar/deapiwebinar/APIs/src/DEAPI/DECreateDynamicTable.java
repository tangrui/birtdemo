package DEAPI;

import java.io.IOException;
import java.util.ArrayList;

import org.eclipse.birt.core.framework.Platform;
import org.eclipse.birt.report.model.api.CellHandle;
import org.eclipse.birt.report.model.api.DataItemHandle;
import org.eclipse.birt.report.model.api.DesignConfig;
import org.eclipse.birt.report.model.api.ElementFactory;
import org.eclipse.birt.report.model.api.IDesignEngine;
import org.eclipse.birt.report.model.api.IDesignEngineFactory;
import org.eclipse.birt.report.model.api.LabelHandle;
import org.eclipse.birt.report.model.api.OdaDataSetHandle;
import org.eclipse.birt.report.model.api.OdaDataSourceHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.RowHandle;
import org.eclipse.birt.report.model.api.SessionHandle;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.structures.ComputedColumn;

import com.ibm.icu.util.ULocale;

/**
 * Simple BIRT Design Engine API (DEAPI) demo.
 */

public class DECreateDynamicTable
{
	ReportDesignHandle designHandle = null;
	ElementFactory designFactory = null;
	StructureFactory structFactory = null;	

	public static void main( String[] args )
	{
		try
		{
			DECreateDynamicTable de = new DECreateDynamicTable();
			ArrayList al = new ArrayList();
			al.add("OFFICECODE");
			al.add("CITY");
			al.add("COUNTRY");
			
			de.buildReport(al, "From Offices" );
		}
		catch ( IOException e )
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch ( SemanticException e )
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	void buildDataSource( ) throws SemanticException
	{

		OdaDataSourceHandle dsHandle = designFactory.newOdaDataSource(
				"Data Source", "org.eclipse.birt.report.data.oda.jdbc" );
		dsHandle.setProperty( "odaDriverClass",
				"org.eclipse.birt.report.data.oda.sampledb.Driver" );
		dsHandle.setProperty( "odaURL", "jdbc:classicmodels:sampledb" );
		dsHandle.setProperty( "odaUser", "ClassicModels" );
		dsHandle.setProperty( "odaPassword", "" );

		designHandle.getDataSources( ).add( dsHandle );

	}

	void buildDataSet(ArrayList cols, String fromClause ) throws SemanticException
	{

		OdaDataSetHandle dsHandle = designFactory.newOdaDataSet( "ds",
				"org.eclipse.birt.report.data.oda.jdbc.JdbcSelectDataSet" );
		dsHandle.setDataSource( "Data Source" );
		String qry = "Select ";
		for( int i=0; i < cols.size(); i++){
			qry += " " + cols.get(i);
			if( i != (cols.size() -1) ){
				qry += ",";
			}
			
		}
		qry += " " + fromClause;
		
		dsHandle.setQueryText( qry );

		designHandle.getDataSets( ).add( dsHandle );
		
		
		
	}
	void buildReport(ArrayList cols, String fromClause ) throws IOException, SemanticException
	{

		DesignConfig config = new DesignConfig( );
		config.setBIRTHome("C:/birt/birt-runtime-2.3RC3/birt-runtime-2_3_0/ReportEngine");
		IDesignEngine engine = null;
		try{

			Platform.startup( config );
			IDesignEngineFactory factory = (IDesignEngineFactory) Platform
			.createFactoryObject( IDesignEngineFactory.EXTENSION_DESIGN_ENGINE_FACTORY );
			engine = factory.createDesignEngine( config );

		}catch( Exception ex){
			ex.printStackTrace();
		}

		SessionHandle session = engine.newSessionHandle( ULocale.ENGLISH ) ;

	

		try{
			//open a design or a template
			designHandle = session.createDesign();

			designFactory = designHandle.getElementFactory( );

			buildDataSource();
			buildDataSet(cols, fromClause);

			TableHandle table = designFactory.newTableItem( "table", cols.size() );
			table.setWidth( "100%" );
			table.setDataSet( designHandle.findDataSet( "ds" ) );

			 
			PropertyHandle computedSet = table.getColumnBindings( ); 
			ComputedColumn  cs1 = null;

			for( int i=0; i < cols.size(); i++){
				cs1 = StructureFactory.createComputedColumn();
				cs1.setName((String)cols.get(i));
				cs1.setExpression("dataSetRow[\"" + (String)cols.get(i) + "\"]");
				computedSet.addItem(cs1);
			}
		
			
			// table header
			RowHandle tableheader = (RowHandle) table.getHeader( ).get( 0 );

			
			for( int i=0; i < cols.size(); i++){
				LabelHandle label1 = designFactory.newLabel( (String)cols.get(i) );	
				label1.setText((String)cols.get(i));
				CellHandle cell = (CellHandle) tableheader.getCells( ).get( i );
				cell.getContent( ).add( label1 );
			}							
	
			// table detail
			RowHandle tabledetail = (RowHandle) table.getDetail( ).get( 0 );
			
			for( int i=0; i < cols.size(); i++){
				CellHandle cell = (CellHandle) tabledetail.getCells( ).get( i );
				DataItemHandle data = designFactory.newDataItem( "data_"+(String)cols.get(i) );
				data.setResultSetColumn( (String)cols.get(i));
				cell.getContent( ).add( data );
				
			}

			designHandle.getBody( ).add( table );

			// Save the design and close it.

			designHandle.saveAs( "output/desample/dynamic.rptdesign" ); //$NON-NLS-1$
			designHandle.close( );
			Platform.shutdown();
			System.out.println("Finished");
		}catch (Exception e){
			e.printStackTrace();
		}		

	}
}

