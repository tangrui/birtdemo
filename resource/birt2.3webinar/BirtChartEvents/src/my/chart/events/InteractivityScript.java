package my.chart.events;

import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.impl.ChartWithAxesImpl;
import org.eclipse.birt.chart.render.ISeriesRenderer;
import org.eclipse.birt.chart.script.ChartEventHandlerAdapter;
import org.eclipse.birt.chart.script.IChartScriptContext;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.attribute.ActionType;
import org.eclipse.birt.chart.model.attribute.LegendItemType;
import org.eclipse.birt.chart.model.attribute.TriggerCondition;
import org.eclipse.birt.chart.model.data.impl.TriggerImpl;
import org.eclipse.birt.chart.model.data.impl.ActionImpl;
import org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl;
import org.eclipse.birt.chart.model.attribute.Position;
import org.eclipse.birt.chart.model.data.Action;
import org.eclipse.birt.chart.model.attribute.impl.TooltipValueImpl;
import org.eclipse.birt.chart.model.attribute.TooltipValue;
import org.eclipse.birt.chart.model.attribute.impl.ScriptValueImpl;
import org.eclipse.birt.chart.model.attribute.ScriptValue;;

public class InteractivityScript extends ChartEventHandlerAdapter {

	@Override
	public void beforeGeneration(Chart cm, IChartScriptContext icsc) {

		// Set color by category
		cm.getLegend().setItemType(LegendItemType.CATEGORIES_LITERAL);
		// Set color by Value Series
		// cm.getLegend().setItemType(LegendItemType.SERIES_LITERAL);
		cm.getPlot().getClientArea().setBackground(ColorDefinitionImpl.GREY());
		if (cm instanceof ChartWithAxesImpl) {
			// x axis
			Axis xaxis = ((ChartWithAxesImpl) cm).getPrimaryBaseAxes()[0];
			xaxis.setLabelPosition(Position.BELOW_LITERAL);
			xaxis.getLabel().getCaption().getFont().setRotation(45);
			Axis yaxis = ((ChartWithAxesImpl) cm)
					.getPrimaryOrthogonalAxis(xaxis);
			yaxis.getMajorGrid().getLineAttributes().setVisible(true);
		}

	}

public void beforeDrawSeries(Series series, ISeriesRenderer isr,
			IChartScriptContext icsc) {
		ScriptValue myscript = ScriptValueImpl.create("alert('I have Been Clicked');");
		Action ac = ActionImpl.create(ActionType.INVOKE_SCRIPT_LITERAL, myscript);
		series.getTriggers().add(
				TriggerImpl.create(TriggerCondition.ONCLICK_LITERAL, ac));
		
		
		
	}

}
