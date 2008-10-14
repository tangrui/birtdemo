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
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.RowHandle;
import org.eclipse.birt.report.model.api.SessionHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.DataItemHandle;
import org.eclipse.birt.report.model.api.ComputedColumnHandle;
import org.eclipse.birt.report.model.api.RowHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.StructureHandle;
import org.eclipse.birt.report.model.api.MemberHandle;
import org.eclipse.birt.report.model.api.HighlightRuleHandle;

import org.eclipse.birt.report.model.api.metadata.IStructureDefn;









import java.util.Iterator;


import com.ibm.icu.util.ULocale;

/**
 * Simple BIRT Design Engine API (DEAPI) demo.
 */

public class ReportItemDetails
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

			//Find an existing Table that is named
			TableHandle te = (TableHandle)design.findElement("MyCustomerTable");
			System.out.println( "Table has " + te.getColumnCount() + " Columns");
			SlotHandle detail = te.getDetail();
			CellHandle ch = (CellHandle)((RowHandle)detail.get(0)).getCells().get(0);
			SlotHandle csh =ch.getContent();
			DataItemHandle cshde = (DataItemHandle)csh.getContents().get(0);
			System.out.println( "Detail cell 1, 1 = " + cshde.getResultSetColumn());
			for( Iterator itr = te.getColumnBindings().iterator(); itr.hasNext(); ){
				ComputedColumnHandle cch = (ComputedColumnHandle) itr.next();
				System.out.println("Table Bindings: " + cch.getName() + " Expression " + cch.getExpression());

			}

			System.out.println("Slot Count for a table design " + te.getDefn().getSlotCount());
			for( int i=0; i < te.getDefn().getSlotCount(); i++){
				System.out.println(" Slot " + i + " -- " + te.getDefn().getSlot(i).getName());
			}			

	
			Iterator pit = te.getPropertyIterator();
			while( pit.hasNext()){
				PropertyHandle ph = (PropertyHandle) pit.next();
	
				//If this is a simple value it will return null
				IStructureDefn structDefn = ph.getPropertyDefn().getStructDefn( );
				if ( structDefn != null )

				{
					System.out.println("Structure Name " + structDefn.getDisplayName());
					
					
					Iterator structIterator = ph.iterator( );
					while ( structIterator.hasNext( ) )

					{

						StructureHandle structHandle = (StructureHandle) structIterator.next( );
						if( structHandle instanceof HighlightRuleHandle){
							System.out.println( "We have a highlight Rule");
						}
						Iterator memberIterator = structHandle.iterator( );
						while ( memberIterator.hasNext( ) )

						{
							MemberHandle memHandle = (MemberHandle) memberIterator.next( );
							System.out.println( "--Structure Item " + memHandle.getDefn( ).getDisplayName( ) + " --- " + memHandle.getValue() );
						}

					}

				}else{
					
					System.out.println("Simple Property " + ph.getPropertyDefn().getDisplayName() + " -- " + ph.getValue());

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

