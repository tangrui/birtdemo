package my.chart.events;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.MarkerLine;
import org.eclipse.birt.chart.model.component.MarkerRange;
import org.eclipse.birt.chart.script.ChartEventHandlerAdapter;
import org.eclipse.birt.chart.script.IChartScriptContext;
import org.eclipse.birt.chart.model.data.impl.NumberDataElementImpl;
import org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl;
import org.eclipse.birt.report.engine.api.script.IReportContext;

public class MarkerScript extends ChartEventHandlerAdapter
{
 public void beforeDrawMarkerLine( Axis axis, MarkerLine mLine,
   IChartScriptContext icsc )
 {
  mLine.getLabel( ).getCaption( ).getColor( ).set( 165, 184, 55 );
  mLine.getLineAttributes( ).getColor( ).set( 165, 184, 55 );
  //change location of marker line
  mLine.setValue(NumberDataElementImpl.create(0));
 }
 /*
  * (non-Javadoc)
  * 
  * @see org.eclipse.birt.chart.script.IChartItemScriptHandler#beforeDrawMarkerRange(org.eclipse.birt.chart.model.component.Axis,
  *      org.eclipse.birt.chart.model.component.MarkerRange,
  *      org.eclipse.birt.chart.script.IChartScriptContext)
  */
 public void beforeDrawMarkerRange( Axis axis, MarkerRange mRange,
   IChartScriptContext icsc )
 {
  
  NumberDataElementImpl sv = (NumberDataElementImpl)mRange.getStartValue();
  if( sv.getValue() == -1){
   Object lc = (Object)((IReportContext)(icsc.getExternalContext().getObject())).getParameterValue("lowContact");
   if( lc instanceof String){
    mRange.setStartValue(NumberDataElementImpl.create(new Integer((String)lc)));
   }else if( lc instanceof Integer){
    mRange.setStartValue(NumberDataElementImpl.create((Integer)lc));    
   }
   mRange.setEndValue(NumberDataElementImpl.create(95));
   mRange.setFill(ColorDefinitionImpl.GREEN());
   mRange.getLabel( ).getCaption( ).getColor( ).set( 225, 104, 105 );
  }
 }
}