package my.chart.events;
import org.eclipse.birt.chart.factory.GeneratedChartState;
import org.eclipse.birt.chart.model.layout.Block;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl;
import org.eclipse.birt.chart.model.component.Label;
import org.eclipse.birt.chart.script.ChartEventHandlerAdapter;
import org.eclipse.birt.chart.script.IChartEventHandler;
import org.eclipse.birt.chart.computation.LegendEntryRenderingHints;
import org.eclipse.birt.chart.computation.LegendItemHints;
import org.eclipse.birt.chart.computation.LegendLayoutHints;
import org.eclipse.birt.chart.script.IChartScriptContext;
public class BlockScript extends ChartEventHandlerAdapter
{
	@Override
	public void beforeRendering(GeneratedChartState gcs,
			IChartScriptContext icsc) {
		// TODO Auto-generated method stub
		//super.beforeRendering(gcs, icsc);


        try{

            LegendLayoutHints llh = gcs.getRunTimeContext().getLegendLayoutHints();

            LegendItemHints[] liha = llh.getLegendItemHints( );
            
            //liha[2].
            
            LegendItemHints[] nliha = new LegendItemHints[liha.length];
            if ( liha.length > 1 ){
                for( int idx=0; idx < liha.length; idx++ ){

                    LegendItemHints tli = liha[idx];
                    int oldpos = tli.getCategoryIndex();
                    int newpos = (liha.length-1)-idx;
                    LegendItemHints ntli = liha[newpos];
                    //Swap the legend location (this swaps the text) the cat index ties to cat color
                    nliha[newpos] = new LegendItemHints( tli.getType(), ntli.getLocation(), tli.getWidth(), tli.getHeight(), tli.getText(), tli.getCategoryIndex());
                }
            }
            LegendLayoutHints nllh = new LegendLayoutHints( llh.getLegendSize(),llh.getTitleSize(),llh.isMinSliceApplied(),llh.getMinSliceText(),
                    nliha );
            gcs.getRunTimeContext().setLegendLayoutHints(nllh);

        }catch(Exception e){
            e.printStackTrace();
        } 


	}
	@Override
	public void beforeDrawBlock(Block block, IChartScriptContext icsc) {
		// TODO Auto-generated method stub


		if ( block.isLegend( ) )
		{
			block.getOutline( ).setVisible( true );
			block.getOutline( ).getColor( ).set( 0, 0,255);
		}
		else if ( block.isPlot( ) )
		{
			block.getOutline( ).setVisible( true );
			block.getOutline( ).getColor( ).set( 0, 0, 255);
		}  
		else if ( block.isTitle( ) )
		{
			block.getOutline( ).setVisible( true );
			//block.setBackground( ColorDefinitionImpl.BLUE( ) );
			block.getOutline( ).getColor( ).set( 0, 0, 255 );
		}
		else
		{
			block.getOutline( ).setVisible( true );
			block.getOutline( ).setColor(ColorDefinitionImpl.BLUE());

		}


	}

}
