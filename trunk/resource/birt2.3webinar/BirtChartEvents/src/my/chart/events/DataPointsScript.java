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

import org.eclipse.birt.chart.computation.DataPointHints;
import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl;

import org.eclipse.birt.chart.model.component.Label;
import org.eclipse.birt.chart.script.ChartEventHandlerAdapter;
import org.eclipse.birt.chart.script.IChartScriptContext;

public class DataPointsScript extends ChartEventHandlerAdapter
{

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.script.IChartItemScriptHandler#beforeDrawDataPointLabel(org.eclipse.birt.chart.computation.DataPointHints,
	 *      org.eclipse.birt.chart.model.component.Label,
	 *      org.eclipse.birt.chart.script.IChartScriptContext)
	 */
	public void beforeDrawDataPointLabel( DataPointHints dph, Label label,
			IChartScriptContext icsc )
	{
		//double x =dph.getLocation();

		double value = ( (Double) dph.getOrthogonalValue( ) ).doubleValue( );
		String seriesdsp = dph.getSeriesDisplayValue();
		//use the following line with DataPointsScriptGrouped.rptdesign
		//label.getCaption().setValue(seriesdsp);
		if ( value <= 79.0 )
		{
			label.setVisible(false);
		}
		else if ( ( value > 79.0 ) & ( value <= 89.0 ) )
		{
			label.getCaption( ).getColor( ).set( 168, 0, 208 );
		}
		else if ( value > 89.0 )
		{
			label.getCaption( ).getColor( ).set( 0, 208, 32 );
		}

	}

	@Override
	public void beforeDrawDataPoint(DataPointHints dph, Fill fill,
			IChartScriptContext icsc) {
		
		Double myval = (Double)dph.getOrthogonalValue();
		if( myval > 95 ){
			ColorDefinitionImpl cdi = (ColorDefinitionImpl)fill;
			cdi.set(0, 255, 0);			
		}
	}

}
