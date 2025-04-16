package qupath.ext.template;

import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;


import qupath.lib.gui.QuPathGUI;
import qupath.lib.gui.viewer.GridLines;
import qupath.lib.gui.viewer.OverlayOptions;
import qupath.lib.gui.viewer.QuPathViewer;
import qupath.lib.gui.viewer.ViewerManager;


import qupath.lib.images.servers.ImageServer;
import qupath.lib.images.servers.PixelCalibration;
import qupath.lib.objects.PathObjects;
import qupath.lib.roi.ROIs;
import qupath.lib.objects.classes.PathClassFactory;

import static qupath.lib.scripting.QP.getCurrentHierarchy;


public class TileClickListener {

    QuPathGUI qupath = QuPathGUI.getInstance();
    ViewerManager viewerManager = qupath.getViewerManager();

    OverlayOptions overlayOptions = qupath.getOverlayOptions();
    GridLines gridLines = overlayOptions.getGridLines();

    //double tileHeight = PathPrefs.gridSpacingYProperty().get();
    //double tileWidth = PathPrefs.gridSpacingXProperty().get();

    public void registerClickListener() {
        QuPathViewer activeViewer = viewerManager.getActiveViewer();
        if (activeViewer != null) {
            StackPane stackPane = (StackPane)activeViewer.getView();
            stackPane.addEventFilter(MouseEvent.MOUSE_CLICKED, e -> {

                if (!TileAnnotationExtension.isTileModeActive())
                    return;

                double xPane = e.getX();
                double yPane = e.getY();

                double tileWidth = gridLines.getSpaceX();
                double tileHeight = gridLines.getSpaceY();

                if (gridLines.useMicrons()){
                    ImageServer<?> imageServer = qupath.getImageData().getServer();
                    PixelCalibration cal = imageServer.getPixelCalibration();
                    tileWidth /= cal.getPixelWidthMicrons();
                    tileHeight /= cal.getPixelHeightMicrons();
                }

                // Convert to image coordinates
                Point2D imagePoint = activeViewer.componentPointToImagePoint(xPane, yPane, null, true);
                double xImage = imagePoint.getX();
                double yImage = imagePoint.getY();

                // Get tile indices
                int tileX = (int) (xImage / tileWidth);
                int tileY = (int) (yImage / tileHeight);

                // Tile's upper left corner coordinates
                double tileOriginX = tileX * tileWidth;
                double tileOriginY = tileY * tileHeight;

                var tileRect = new Rectangle2D.Double(tileOriginX, tileOriginY, tileWidth, tileHeight);

                // Check if there already is a bounding box on this tile
                var existing = getCurrentHierarchy().getFlattenedObjectList(null).stream()
                        .filter(anno -> anno.getROI() != null)
                        .filter(anno -> tileRect.intersects(
                                anno.getROI().getBoundsX(),
                                anno.getROI().getBoundsY(),
                                anno.getROI().getBoundsWidth(),
                                anno.getROI().getBoundsHeight()))
                        .findFirst();

                // Select the box if it exists
                if (existing.isPresent()) {
                    getCurrentHierarchy().getSelectionModel().setSelectedObject(existing.get());
                }
                else {
                        var roi = ROIs.createRectangleROI(tileOriginX, tileOriginY, tileWidth, tileHeight, null);

                        var annotation = PathObjects.createAnnotationObject(roi);
                        annotation.setPathClass(PathClassFactory.getPathClass("SelectedTile"));

                        // Add new box to hierarchy
                        getCurrentHierarchy().addPathObject(annotation);
                        getCurrentHierarchy().getSelectionModel().setSelectedObject(annotation);
                    }


                // Repaint the view
                activeViewer.repaint();

            });
        }
    }

}
