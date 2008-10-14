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
import org.eclipse.birt.report.model.api.DesignConfig;
import org.eclipse.birt.report.model.api.IDesignEngineFactory;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.LibraryHandle;
import org.eclipse.birt.report.model.api.DesignEngine;
import org.eclipse.birt.report.model.api.IDesignEngine;
import org.eclipse.birt.report.model.api.SessionHandle;

import com.ibm.icu.util.ULocale;



public class AddTableFromLib {

	public void runReport() throws EngineException
	{

		IReportEngine engine=null;
		IDesignEngine dengine=null;
		EngineConfig config = null;

		try{

			config = new EngineConfig( );			
			config.setBIRTHome("C:/birt/birt-runtime-2.3RC3/birt-runtime-2_3_0/ReportEngine");
			config.setLogConfig(null, Level.FINE);
			config.setResourcePath("C:/work/workspaces/2.3rc3srcexamples/APIs/Reports");
			Platform.startup( config );
			IReportEngineFactory factory = (IReportEngineFactory) Platform
			.createFactoryObject( IReportEngineFactory.EXTENSION_REPORT_ENGINE_FACTORY );
			engine = factory.createReportEngine( config );

			
			IDesignEngineFactory dfactory = (IDesignEngineFactory) Platform
			.createFactoryObject( IDesignEngineFactory.EXTENSION_DESIGN_ENGINE_FACTORY );
			dengine = dfactory.createDesignEngine( new DesignConfig() );

			

			IReportRunnable design = null;
			//Open the report design
			
			SessionHandle session = dengine.newSessionHandle( ULocale.ENGLISH ); 
			
			design = engine.openReportDesign("Reports/TopNPercent.rptdesign"); 
			
			
			
			ReportDesignHandle report = (ReportDesignHandle) design.getDesignHandle( );

			LibraryHandle libhan =session.openLibrary("C:/work/workspaces/2.3rc3srcexamples/APIs/Reports/test.rptlibrary");
			report.includeLibrary("C:/work/workspaces/2.3rc3srcexamples/APIs/Reports/test.rptlibrary", "mylib");
	
			DesignElementHandle deh1 = libhan.findDataSource("Data Source");
			DesignElementHandle deh2 = libhan.findDataSet("Data Set");
			DesignElementHandle deh3 = libhan.findElement("myTable");
			DesignElementHandle ldeh1 = report.getElementFactory().newElementFrom(deh1, "newds");
			DesignElementHandle ldeh2 = report.getElementFactory().newElementFrom(deh2, "newdt");
			DesignElementHandle ldeh3 = report.getElementFactory().newElementFrom(deh3, "newtable");
			report.getDataSources().add(ldeh1);
			report.getDataSets().add(ldeh2);
			report.getBody().add(ldeh3);
			
			report.saveAs("output/desample/modifiedtopnlib.rptdesign");

			
			//Create task to run and render the report,
			IRunAndRenderTask task = engine.createRunAndRenderTask(design); 		
			task.setParameterValue("Top Percentage", new Integer(3));
			task.setParameterValue("Top Count", new Integer(5));
			task.validateParameters();

			HTMLRenderOption options = new HTMLRenderOption();		
			options.setOutputFileName("output/desample/ModifiedTopNPercent.html");
			options.setOutputFormat("html");
			options.setImageDirectory("images");

			task.setRenderOption(options);


	
			task.run();

			task.close();
			session.closeAll(false);
			
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

			AddTableFromLib ex = new AddTableFromLib( );
			ex.runReport();

		}
		catch ( Exception e )
		{
			e.printStackTrace();
		}
	}


}


