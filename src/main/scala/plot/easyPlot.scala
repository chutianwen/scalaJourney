import scalafx.application
import scalafx.application.JFXApp
import scalafx.collections.ObservableBuffer
import scalafx.scene.Scene
import scalafx.scene.chart.{NumberAxis, ScatterChart, XYChart}


object easyPlot extends JFXApp{
	stage = new application.JFXApp.PrimaryStage{
		title = "Temp Plot"
		scene = new Scene(500, 500){
			val xAxix = NumberAxis()
			val yAxis = NumberAxis()
//			val pData = XYChart.Series[Number, Number]("Temps", ObservableBuffer())

		}
		scene
	}
}
