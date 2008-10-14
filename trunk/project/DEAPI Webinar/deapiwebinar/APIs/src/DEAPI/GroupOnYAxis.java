package DEAPI;

import java.io.IOException;

import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.data.Query;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.data.impl.QueryImpl;
import org.eclipse.birt.chart.model.data.impl.SeriesDefinitionImpl;
import org.eclipse.birt.core.framework.Platform;
import org.eclipse.birt.report.model.api.DesignConfig;
import org.eclipse.birt.report.model.api.DesignEngine;
import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.IDesignEngine;
import org.eclipse.birt.report.model.api.IDesignEngineFactory;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.SessionHandle;
import org.eclipse.birt.report.model.api.extension.ExtendedElementException;

import com.ibm.icu.util.ULocale;


public class GroupOnYAxis
{

	/**
	 * execute application
	 * 
	 * @param args
	 */
	public static void main( String[] args )
	{
		new GroupOnYAxis( ).groupKey( );

	}

	/**
	 * Get the chart instance from the design file and add series grouping key.
	 * 
	 * @return An instance of the simulated runtime chart model (containing
	 *         filled datasets)
	 */
	void groupKey( )
	{
		ReportDesignHandle designHandle = null;
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

		SessionHandle sessionHandle = engine.newSessionHandle( (ULocale) null );
		
		try
		{
			designHandle = sessionHandle.openDesign( "Reports/NonGroupOnYAxis.rptdesign" );//$NON-NLS-1$
		}
		catch ( DesignFileException e )
		{
			// TODO Auto-generated catch block
			e.printStackTrace( );
		}

		ExtendedItemHandle eih = (ExtendedItemHandle) designHandle.getBody( )
				.getContents( ).get( 0 );

		Chart cm = null;
		try
		{
			cm = (Chart) eih.getReportItem( ).getProperty( "chart.instance" ); //$NON-NLS-1$
		}
		catch ( ExtendedElementException e )
		{
			// TODO Auto-generated catch block
			e.printStackTrace( );
		}
		cm.getTitle( ).getLabel( ).getCaption( ).setValue( "Group On Y Axis" );//$NON-NLS-1$

		Axis axisBase = (Axis) ( (ChartWithAxes) cm ).getAxes( ).get( 0 ); // X-Axis
		Axis axisOrth = (Axis) axisBase.getAssociatedAxes( ).get( 0 ); // Y-Axis
		SeriesDefinition sdY = (SeriesDefinition) axisOrth
				.getSeriesDefinitions( ).get( 0 ); // Y-Series

		
		SeriesDefinition sdGroup = SeriesDefinitionImpl.create( );
		Query query = QueryImpl.create( "row[\"Month\"]" );//$NON-NLS-1$
		sdGroup.setQuery( query );

		axisOrth.getSeriesDefinitions( ).clear( ); // Clear the original
													// Y-Series (sdY)
		axisOrth.getSeriesDefinitions( ).add( 0, sdGroup );
		sdGroup.getSeries( ).add( sdY.getSeries( ).get( 0 ) );

		try
		{
			designHandle.saveAs( "output/desample/GroupOnYAxis.rptdesign" );//$NON-NLS-1$
			System.out.println("Finished");
		}
		catch ( IOException e )
		{
			e.printStackTrace( );
		}
		Platform.shutdown();

	}

}
