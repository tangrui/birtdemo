package DEAPI;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.core.framework.Platform;
import org.eclipse.birt.report.model.api.CellHandle;
import org.eclipse.birt.report.model.api.ColumnHandle;
import org.eclipse.birt.report.model.api.DataItemHandle;
import org.eclipse.birt.report.model.api.DataSetHandle;
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
import org.eclipse.birt.report.model.api.ScriptDataSetHandle;
import org.eclipse.birt.report.model.api.ScriptDataSourceHandle;
import org.eclipse.birt.report.model.api.SessionHandle;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.ContentException;
import org.eclipse.birt.report.model.api.command.NameException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.structures.ComputedColumn;
import org.eclipse.birt.report.model.api.elements.structures.ResultSetColumn;
import org.eclipse.birt.report.model.api.metadata.IMetaDataDictionary;
import org.eclipse.birt.report.model.elements.interfaces.IReportItemModel;
import org.eclipse.birt.report.model.elements.interfaces.IStyleModel;

import com.ibm.icu.util.ULocale;

public class ScriptedDS {

	ReportDesignHandle reportDesignHandle = null;

	ElementFactory elementFactory = null;

	IMetaDataDictionary dict = null;

	ComputedColumn cs1, cs2, cs3, cs4, cs5 = null;

	public static void main(String[] args) throws SemanticException,
			IOException {
		new ScriptedDS().createReport();
	}

	void createReport() throws SemanticException, IOException {
		// Configure the Engine and start the Platform
		DesignConfig config = new DesignConfig();
		config.setBIRTHome("C:/birt/birt-runtime-2.3RC3/birt-runtime-2_3_0/ReportEngine");
		IDesignEngine engine = null;
		try {

			Platform.startup(config);
			IDesignEngineFactory factory = (IDesignEngineFactory) Platform
					.createFactoryObject(IDesignEngineFactory.EXTENSION_DESIGN_ENGINE_FACTORY);
			engine = factory.createDesignEngine(config);

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		SessionHandle session = engine.newSessionHandle(ULocale.ENGLISH);

		// Create a new report
		reportDesignHandle = session.createDesign();

		// Element factory is used to create instances of BIRT elements.
		elementFactory = reportDesignHandle.getElementFactory();

		createMasterPages();
		createDataSources();
		createDataSets();
		createBody();

		reportDesignHandle.saveAs("output/desample/SDS.rptdesign");//$NON-NLS-1$//$NON-NLS-2$
		reportDesignHandle.close();

		System.out.println("finished");

		Platform.shutdown();
	}

	// Scripted Data Set

	private void createDataSources() throws SemanticException {
		ScriptDataSourceHandle dataSourceHandle = elementFactory
				.newScriptDataSource("Data Source");
		reportDesignHandle.getDataSources().add(dataSourceHandle);
	}

	private void createDataSets() throws SemanticException {

		ScriptDataSetHandle dataSetHandle = elementFactory
				.newScriptDataSet("Data Set");//$NON-NLS-1$
		dataSetHandle.setDataSource("Data Source");//$NON-NLS-1$

		// Set open( ) in code
		dataSetHandle.setOpen("i=0;");

		// Set fetch( ) in code
		dataSetHandle.setFetch("if ( i < 20 ){" + "row[\"Month\"] = 1;"
				+ "row[\"Product\"] = 'My Product' + parseInt(i/3);"
				+ "row[\"Amount\"] = i;" + "i++;" + "return true;}"
				+ "else return false;");

		PropertyHandle computedSet = dataSetHandle
				.getPropertyHandle(ScriptDataSetHandle.RESULT_SET_PROP);

		// Since this is a Scripted Data Source you need to tell the
		// DataSet what the columns are. For this example, we will just
		// hard code the known resultColumn values
		ResultSetColumn resultColumn = StructureFactory.createResultSetColumn();
		resultColumn.setPosition(1);
		resultColumn.setColumnName("Month");
		resultColumn.setDataType("integer");
		computedSet.addItem(resultColumn);

		resultColumn = StructureFactory.createResultSetColumn();
		resultColumn.setPosition(2);
		resultColumn.setColumnName("Product");
		resultColumn.setDataType("string");
		computedSet.addItem(resultColumn);

		resultColumn = StructureFactory.createResultSetColumn();
		resultColumn.setPosition(3);
		resultColumn.setColumnName("Amount");
		resultColumn.setDataType("integer");

		computedSet.addItem(resultColumn);

		reportDesignHandle.getDataSets().add(dataSetHandle);

	}

	private void createMasterPages() throws ContentException, NameException {
		DesignElementHandle simpleMasterPage = elementFactory
				.newSimpleMasterPage("Master Page");//$NON-NLS-1$
		reportDesignHandle.getMasterPages().add(simpleMasterPage);
	}

	private void createBody() throws SemanticException {

		TableHandle table = elementFactory.newTableItem(null, 3, 1, 1, 1);
		table.setProperty(IStyleModel.TEXT_ALIGN_PROP,
				DesignChoiceConstants.TEXT_ALIGN_CENTER);
		table.setWidth("80%");//$NON-NLS-1$
		table.setProperty(IReportItemModel.DATA_SET_PROP, "Data Set");//$NON-NLS-1$

		ColumnHandle ch = (ColumnHandle) table.getColumns().get(0);
		// ch.setProperty("Width", "3in");
		ch.getWidth().setStringValue("3in");

		// bind the data set columns to the table
		DataSetHandle dataSetHandle = (DataSetHandle) reportDesignHandle
				.getDataSets().get(0);
		List resultSetCols = dataSetHandle
				.getListProperty(DataSetHandle.RESULT_SET_PROP);
		PropertyHandle boundCols = table.getColumnBindings();
		for (Iterator iterator = resultSetCols.iterator(); iterator.hasNext();) {
			ResultSetColumn rsHandle = (ResultSetColumn) iterator.next();
			ComputedColumn col = StructureFactory.createComputedColumn();
			col.setName(rsHandle.getColumnName());
			col.setExpression("dataSetRow[\"" + rsHandle.getColumnName()
					+ "\"]");
			boundCols.addItem(col);
		}

		// Header
		RowHandle header = (RowHandle) table.getHeader().get(0);

		CellHandle tcell = (CellHandle) header.getCells().get(0);
		tcell.getWidth().setStringValue("3in");
		LabelHandle label = elementFactory.newLabel(null);
		label.setText("Order");//$NON-NLS-1$
		tcell.getContent().add(label);

		tcell = (CellHandle) header.getCells().get(1);
		label = elementFactory.newLabel(null);
		label.setText("Product");//$NON-NLS-1$
		tcell.getContent().add(label);

		tcell = (CellHandle) header.getCells().get(2);
		label = elementFactory.newLabel(null);
		label.setText("Amount");//$NON-NLS-1$
		tcell.getContent().add(label);

		RowHandle detail = (RowHandle) table.getDetail().get(0);
		DataItemHandle data = elementFactory.newDataItem(null);

		tcell = (CellHandle) detail.getCells().get(1);
		data = elementFactory.newDataItem(null);
		data.setResultSetColumn("Product");
		tcell.getContent().add(data);

		tcell = (CellHandle) detail.getCells().get(2);
		data = elementFactory.newDataItem(null);
		data.setResultSetColumn("Amount");
		tcell.getContent().add(data);

		reportDesignHandle.getBody().add(table);

	}

}