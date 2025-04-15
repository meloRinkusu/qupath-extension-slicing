package qupath.ext.template;

import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import qupath.lib.gui.QuPathGUI;
import qupath.lib.gui.viewer.QuPathViewer;
import qupath.lib.gui.viewer.ViewerManager;

import java.awt.geom.Point2D;

public class TileClickListener {

    QuPathGUI qupath = QuPathGUI.getInstance();
    ViewerManager viewerManager = qupath.getViewerManager();

    public void registerClickListener() {
        QuPathViewer activeViewer = viewerManager.getActiveViewer();
        if (activeViewer != null) {
            StackPane stackPane = (StackPane)activeViewer.getView();
            stackPane.addEventFilter(MouseEvent.MOUSE_CLICKED, e -> {
                double xPane = e.getX();
                double yPane = e.getY();
                System.out.println("Clicked on Pane at: " + xPane + ", " + yPane);

                // Convertir en coordonnées image
                Point2D imagePoint = activeViewer.componentPointToImagePoint(xPane, yPane, null, true);
                double xImage = imagePoint.getX();
                double yImage = imagePoint.getY();
                System.out.println("Image coordinates: " + xImage + ", " + yImage);
            });
        } else {
            System.out.println("Pas de viewer actif");
        }
    }

}
