package qupath.ext.template;

import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import qupath.lib.gui.QuPathGUI;
import qupath.lib.gui.viewer.QuPathViewer;
import qupath.lib.gui.viewer.ViewerManager;
import qupath.lib.gui.prefs.PathPrefs;

import qupath.lib.objects.PathObjects;
import qupath.lib.roi.ROIs;
import qupath.lib.objects.classes.PathClassFactory;

import static qupath.lib.scripting.QP.getCurrentHierarchy;


public class TileClickListener {

    QuPathGUI qupath = QuPathGUI.getInstance();
    ViewerManager viewerManager = qupath.getViewerManager();

    //double tileHeight = 224;
    double tileHeight = PathPrefs.gridSpacingYProperty().get();
    //double tileLength = 224;
    double tileWidth = PathPrefs.gridSpacingXProperty().get();



    public void registerClickListener() {
        QuPathViewer activeViewer = viewerManager.getActiveViewer();
        if (activeViewer != null) {
            StackPane stackPane = (StackPane)activeViewer.getView();
            stackPane.addEventFilter(MouseEvent.MOUSE_CLICKED, e -> {
                double xPane = e.getX();
                double yPane = e.getY();

                // Convertir en coordonnées image
                Point2D imagePoint = activeViewer.componentPointToImagePoint(xPane, yPane, null, true);
                double xImage = imagePoint.getX();
                double yImage = imagePoint.getY();

                // Calculer les indices de la tuile
                int tileX = (int) (xImage / tileWidth);
                int tileY = (int) (yImage / tileHeight);

                // Coordonnées du coin supérieur gauche de la tuile
                double tileOriginX = tileX * tileWidth;
                double tileOriginY = tileY * tileHeight;

                //System.out.println("Image point: " + xImage + ", " + yImage);
                //System.out.println("Tile indices: (" + tileX + ", " + tileY + ")");
                //System.out.println("Tile origin in image: " + tileOriginX + ", " + tileOriginY);

                //var tileRect = new Rectangle2D.Double(tileOriginX, tileOriginY, tileWidth, tileHeight);

                var roi = ROIs.createRectangleROI(tileOriginX, tileOriginY, tileWidth, tileHeight, null);

                var annotation = PathObjects.createAnnotationObject(roi);
                annotation.setPathClass(PathClassFactory.getPathClass("SelectedTile"));

                // Ajouter à la hiérarchie
                getCurrentHierarchy().addPathObject(annotation);

                // Redessiner la vue
                activeViewer.repaint();

            });
        } else {
            System.out.println("Pas de viewer actif");
        }
    }

}
