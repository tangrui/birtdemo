import java.io.IOException;
import java.util.Iterator;

import org.eclipse.birt.report.model.api.CellHandle;
import org.eclipse.birt.report.model.api.DataItemHandle;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DesignConfig;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.DesignEngine;
import org.eclipse.birt.report.model.api.ElementFactory;
import org.eclipse.birt.report.model.api.GridHandle;
import org.eclipse.birt.report.model.api.ImageHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.RowHandle;
import org.eclipse.birt.report.model.api.SessionHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.core.Module;


public class ChangeReportDesign {

	public ChangeReportDesign(){
	}
	
	/**
	 * This function reads a BIRT design file and changes the data
	 * field binding to replace it with am image binding
	 */
	public boolean replaceDataBindingWithImage(){
		// Create a design engine configuration object.
		DesignConfig dConfig = new DesignConfig( );
		dConfig.setProperty("BIRT_HOME", "C:\\Program Files\\Actuate9\\BRDPro\\Desktop Application SDK\\ReportEngine");
		DesignEngine dEngine = new DesignEngine( dConfig );
		
		// Create a session handle, using the system locale.
		SessionHandle session = dEngine.newSessionHandle( null );
		
		// Create a handle for an existing report design.
		String name = "CreditRating.rptdesign";
		ReportDesignHandle design = null;
		try {
		     design = session.openDesign( name );
		} catch (Exception e) {
		     System.err.println
		          ( "Report " + name + " not opened!\nReason is " + 
		               e.toString( ) );
		     return false;
		}
		
		// The element factory creates instances of the various BIRT elements.
		ElementFactory efactory = design.getElementFactory( );

		// Find the design element handle
		DesignElementHandle element = findCreditRatingElement(design);
		//DesignElementHandle element = design.findElement("Picture"); 
		DesignElementHandle cell = element.getContainer();

		try {
			element.dropAndClear();
		} catch (SemanticException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//Add the picture element
		ImageHandle ih  = efactory.newImage("Image");
		try {
			cell.addElement(ih, CellHandle.CONTENT_SLOT);
			//ih.setSource(DesignChoiceConstants.IMAGE_REF_TYPE_EXPR);
			//ih.setValueExpression( "row[\"Picture\"]" );
			ih.setSource(DesignChoiceConstants. IMAGE_REF_TYPE_URL);
			ih.setURL("row[\"CreditRating\"]");
		} catch (SemanticException e) {
			e.printStackTrace();
		}
		
		// Save the design and close it.
		try {
			design.saveAs( "CreditRating-Modified.rptdesign" );
		} catch (IOException e) {
			e.printStackTrace();
		} 
		design.close( );
		System.out.println("Finished");

		return true;		
	}
	
	public DesignElementHandle findCreditRatingElement(ReportDesignHandle design){
		TableHandle table = (TableHandle) design.getBody().get(0);
		RowHandle detailrow = (RowHandle) table.getDetail().get(0);
		Iterator rowIterator = detailrow.getCells().iterator();
		while(rowIterator.hasNext()){
			CellHandle cell = (CellHandle)rowIterator.next();
			DataItemHandle dataItem = (DataItemHandle)cell.getContent().get(0);
			String resultSetColumn = dataItem.getResultSetColumn();
			if("CreditRating".compareTo(resultSetColumn)==0)
				return dataItem;
		}
		return null;
	}

	public static void main(String[] args) {
		ChangeReportDesign changeDesign = new ChangeReportDesign();
		changeDesign.replaceDataBindingWithImage();

	}

}
