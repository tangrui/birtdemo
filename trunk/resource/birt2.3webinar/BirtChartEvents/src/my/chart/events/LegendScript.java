/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package my.chart.events;

import org.eclipse.birt.chart.computation.LegendEntryRenderingHints;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.model.component.Label;
import org.eclipse.birt.chart.script.ChartEventHandlerAdapter;
import org.eclipse.birt.chart.script.IChartScriptContext;

public class LegendScript extends ChartEventHandlerAdapter
{

	@Override
	public void beforeDrawLegendItem(LegendEntryRenderingHints lerh, Bounds bo,
			IChartScriptContext icsc) {
		float fsize = (float)8.0;
		//lerh contains 
		//fill, data index, label and value label

		Label label = lerh.getLabel();
		String newlbl = label.getCaption().getValue() + "-" + lerh.getDataIndex();
		label.getCaption().setValue(newlbl);
		label.getCaption( ).getColor( ).set( 35, 184, 245 );
		label.getCaption( ).getFont( ).setBold( true );
		label.getCaption( ).getFont( ).setItalic( true );
		label.getCaption().getFont().setSize(fsize);
		label.getOutline( ).setVisible( true );
		label.getOutline( ).getColor( ).set( 177, 12, 187 );

	}


}
