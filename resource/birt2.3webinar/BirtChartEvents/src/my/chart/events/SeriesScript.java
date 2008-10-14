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

import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.component.impl.CurveFittingImpl;
import org.eclipse.birt.chart.model.attribute.MarkerType;
import org.eclipse.birt.chart.model.attribute.Marker;
import org.eclipse.birt.chart.model.type.LineSeries;
import org.eclipse.birt.chart.model.type.BarSeries;
import org.eclipse.birt.chart.model.type.impl.*;
import org.eclipse.birt.chart.exception.ChartException;

import org.eclipse.birt.chart.render.ISeriesRenderer;
import org.eclipse.birt.chart.script.ChartEventHandlerAdapter;
import org.eclipse.birt.chart.script.IChartScriptContext;
import org.eclipse.birt.chart.render.AxesRenderer;
import org.eclipse.birt.chart.computation.DataPointHints;
import org.eclipse.birt.chart.render.ISeriesRenderingHints;
import org.eclipse.birt.chart.factory.GeneratedChartState;
import org.eclipse.birt.chart.factory.RunTimeContext;
import org.eclipse.birt.core.exception.BirtException;

public class SeriesScript extends ChartEventHandlerAdapter 
{
	
	private RunTimeContext rtc;

	public SeriesScript() 
	{
	}
	
	public void beforeDrawSeries( Series series, ISeriesRenderer isr,
			IChartScriptContext icsc )
	{
		if( series instanceof LineSeries){

		
			Marker mk = ((Marker)((LineSeries)series).getMarkers().get(0));
			mk.setType( MarkerType.TRIANGLE_LITERAL );
			//series.setCurveFitting( CurveFittingImpl.create( ) );
			series.getLabel( ).getCaption( ).getColor( ).set( 255, 0, 0 );
		}
		if( series instanceof BarSeriesImpl){
			AxesRenderer axr = (AxesRenderer)isr;
			ISeriesRenderingHints isrh =axr.getSeriesRenderingHints();
			DataPointHints[] dpha =isrh.getDataPoints();
			
			try{
			DataPointHints dphn = new DataPointHints(
			dpha[4].getBaseValue(),
			dpha[4].getOrthogonalValue(),
			dpha[4].getSeriesValue(),
			dpha[4].getPercentileOrthogonalValue(),
			series.getDataPoint(), null, null, null, null,
			dpha[4].getIndex(),
			dpha[4].getLocation(),
			dpha[4].getSize()*.3,
			rtc);
			dpha[4] = dphn;
			}catch(Exception e){
			
				e.printStackTrace();
			}
		}
	}
	public void beforeRendering(GeneratedChartState gcs,
			IChartScriptContext icsc) {
		rtc =gcs.getRunTimeContext();
		gcs.getComputations();
	}
}
