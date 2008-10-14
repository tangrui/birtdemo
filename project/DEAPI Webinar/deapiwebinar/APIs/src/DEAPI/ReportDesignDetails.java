package DEAPI;



import java.io.IOException;

import org.eclipse.birt.core.framework.Platform;
import org.eclipse.birt.report.model.api.CellHandle;
import org.eclipse.birt.report.model.api.DesignConfig;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ElementFactory;
import org.eclipse.birt.report.model.api.GridHandle;
import org.eclipse.birt.report.model.api.IDesignEngine;
import org.eclipse.birt.report.model.api.IDesignEngineFactory;
import org.eclipse.birt.report.model.api.ImageHandle;
import org.eclipse.birt.report.model.api.LabelHandle;
import org.eclipse.birt.report.model.api.IncludedCssStyleSheetHandle;

import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.RowHandle;
import org.eclipse.birt.report.model.api.SessionHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;

import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.DataItemHandle;
import org.eclipse.birt.report.model.api.ComputedColumnHandle;
import org.eclipse.birt.report.model.api.RowHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.StructureHandle;
import org.eclipse.birt.report.model.api.MemberHandle;
import org.eclipse.birt.report.model.api.metadata.IStructureDefn;
import java.util.*;









import java.util.Iterator;


import com.ibm.icu.util.ULocale;

/**
 * Simple BIRT Design Engine API (DEAPI) demo.
 */

public class ReportDesignDetails
{

	public static void main( String[] args )
	{
		try
		{
			buildReport( );
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


	static void buildReport( ) throws IOException, SemanticException
	{
		// Create a session handle. This is used to manage all open designs.
		// Your app need create the session only once.

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

		ReportDesignHandle design = null;
		try{
			design = session.openDesign("Reports/customers.rptdesign" );

			
	         List cssStyleSheets = design.getAllStyles();//getAllCssStyleSheets();
	         int size = cssStyleSheets.size();
	         Iterator myincludecssiter = design.includeCssesIterator();
	         while ( myincludecssiter.hasNext( ) ){
	        	IncludedCssStyleSheetHandle css = (IncludedCssStyleSheetHandle)myincludecssiter.next();
	        	System.out.println( "Included Style Sheet " + css.getFileName());
	        	
	         }
	        	 
			SlotHandle sh = design.getBody();
			System.out.println( "Body Contents Count: " + sh.getCount());
			Iterator it = sh.iterator();
			while( it.hasNext()){
				DesignElementHandle de =(DesignElementHandle)it.next();
				System.out.println("Body " + de.getName());

			}

			sh = design.getDataSources();
			it = sh.iterator();
			while( it.hasNext()){
				DesignElementHandle de =(DesignElementHandle)it.next();
				System.out.println("Data Source: -- " + de.getName());

			}
			sh = design.getDataSets();
			it = sh.iterator();
			while( it.hasNext()){
				DesignElementHandle de =(DesignElementHandle)it.next();
				System.out.println("Data Set: -- " + de.getName());

			}

			sh = design.getMasterPages();
			it = sh.iterator();
			while( it.hasNext()){
				DesignElementHandle de =(DesignElementHandle)it.next();
				System.out.println("Master Page: " + de.getName());

			}			

			sh = design.getParameters();
			it = sh.iterator();
			while( it.hasNext()){
				DesignElementHandle de =(DesignElementHandle)it.next();
				System.out.println("Parameter: " + de.getName());

			}	


			System.out.println("Slot Count for a report design " + design.getDefn().getSlotCount());
			for( int i=0; i < design.getDefn().getSlotCount(); i++){
				System.out.println(" Slot " + i + " -- " + design.getDefn().getSlot(i).getName());
			}

		


			Iterator pit = design.getPropertyIterator();
			while( pit.hasNext()){
				PropertyHandle ph = (PropertyHandle) pit.next();
	
				IStructureDefn structDefn = ph.getPropertyDefn().getStructDefn( );
				if ( structDefn != null )

				{
					System.out.println("ListProperty " + ph.getPropertyDefn().getDisplayName());

					
					Iterator structIterator = ph.iterator( );
					while ( structIterator.hasNext( ) )

					{

						StructureHandle structHandle = (StructureHandle) structIterator.next( );
						Iterator memberIterator = structHandle.iterator( );
						while ( memberIterator.hasNext( ) )

						{
							MemberHandle memHandle = (MemberHandle) memberIterator.next( );
							System.out.println( " Structure Item " + memHandle.getDefn( ).getDisplayName( ) + "---" + memHandle.getValue() );
						}

					}

				}else{
					
					System.out.println("StandardProperty " + ph.getPropertyDefn().getDisplayName() + "--" +ph.getValue());

				}



			}
			
			design.close( );
			Platform.shutdown();

		}catch(Exception e){
			e.printStackTrace();
		}		
		System.out.println("Finished");

		// We're done!
	}
}

