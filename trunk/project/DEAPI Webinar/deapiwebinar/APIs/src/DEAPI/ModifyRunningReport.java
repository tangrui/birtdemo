package DEAPI;



import java.util.HashMap;
import java.util.logging.Level;

import org.eclipse.birt.core.framework.Platform;
import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.engine.api.EngineConstants;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.HTMLActionHandler;
import org.eclipse.birt.report.engine.api.HTMLRenderOption;
import org.eclipse.birt.report.engine.api.HTMLServerImageHandler;
import org.eclipse.birt.report.engine.api.HTMLCompleteImageHandler;

import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.IReportEngineFactory;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IRunAndRenderTask;
import org.eclipse.birt.report.engine.api.RenderOption;
import org.eclipse.birt.report.model.api.ReportDesignHandle;




public class ModifyRunningReport {

	public void runReport() throws EngineException
	{

		IReportEngine engine=null;
		EngineConfig config = null;

		try{

			config = new EngineConfig( );			
			config.setBIRTHome("C:/birt/birt-runtime-2.3RC3/birt-runtime-2_3_0/ReportEngine");
			config.setLogConfig(null, Level.FINE);
			Platform.startup( config );
			IReportEngineFactory factory = (IReportEngineFactory) Platform
			.createFactoryObject( IReportEngineFactory.EXTENSION_REPORT_ENGINE_FACTORY );
			engine = factory.createReportEngine( config );


			IReportRunnable design = null;
			//Open the report design
			design = engine.openReportDesign("Reports/TopSellingProducts.rptdesign"); 

			ReportDesignHandle report = (ReportDesignHandle) design.getDesignHandle( );
			report.findElement("mychart").drop();


			//Create task to run and render the report,
			IRunAndRenderTask task = engine.createRunAndRenderTask(design); 		
			task.setParameterValue("Top Percentage", new Integer(3));
			task.setParameterValue("Top Count", new Integer(5));
			task.validateParameters();

			HTMLRenderOption options = new HTMLRenderOption();		
			options.setOutputFileName("output/desample/Modified.html");
			options.setOutputFormat("html");
			options.setImageDirectory("images");

			task.setRenderOption(options);


	
			task.run();

			task.close();
			engine.destroy();
			Platform.shutdown();
			System.out.println("Finished");


		}catch( Exception ex){
			ex.printStackTrace();
		}		


	}	


	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try
		{

			ModifyRunningReport ex = new ModifyRunningReport( );
			ex.runReport();

		}
		catch ( Exception e )
		{
			e.printStackTrace();
		}
	}


}


