package com.google.firebase.samples.apps.mlkit.java.facedetection;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;

import com.google.firebase.ml.vision.common.FirebaseVisionPoint;
import com.google.firebase.ml.vision.face.FirebaseVisionFace;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceContour;
import com.google.firebase.samples.apps.mlkit.R;
import com.google.firebase.samples.apps.mlkit.common.GraphicOverlay;
import com.google.firebase.samples.apps.mlkit.common.GraphicOverlay.Graphic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Graphic instance for rendering face contours graphic overlay view.
 */
public class FaceContourGraphic extends Graphic {

    private volatile FirebaseVisionFace firebaseVisionFace;

    FaceContourGraphic(GraphicOverlay overlay, FirebaseVisionFace face, int selectedColor) {
        super(overlay);

        this.firebaseVisionFace = face;

        Paint facePositionPaint = new Paint();
        facePositionPaint.setColor(selectedColor);
    }

    /**
     * Draws the face annotations for position on the supplied canvas.
     */
    @Override
    public void draw(Canvas canvas) {
        FirebaseVisionFace face = firebaseVisionFace;
        if (face == null) {
            return;
        }
        drawLip(face, canvas);
    }

    private void drawLip(FirebaseVisionFace face, Canvas canvas) {
        drawLip(face, canvas, FirebaseVisionFaceContour.UPPER_LIP_TOP, FirebaseVisionFaceContour.UPPER_LIP_BOTTOM);
        drawLip(face, canvas, FirebaseVisionFaceContour.LOWER_LIP_TOP, FirebaseVisionFaceContour.LOWER_LIP_BOTTOM);
    }

    private void drawLip(FirebaseVisionFace face, Canvas canvas, int lipTop, int lipBottom) {
        FirebaseVisionFaceContour contour = face.getContour(lipTop);
        ArrayList<FirebaseVisionPoint> upperPoints = new ArrayList<>(contour.getPoints());
        contour = face.getContour(lipBottom);
        List<FirebaseVisionPoint> bottomPoints = contour.getPoints();
        Collections.reverse(bottomPoints);
        upperPoints.addAll(bottomPoints);

        Paint wallpaint = new Paint();
        wallpaint.setColor(getApplicationContext().getResources().getColor(R.color.over));
        wallpaint.setStyle(Paint.Style.FILL_AND_STROKE);

        Path wallpath = new Path();
        wallpath.reset(); // only needed when reusing this path for a new build
        float px = translateX(upperPoints.get(0).getX());
        float py = translateY(upperPoints.get(0).getY());
        wallpath.moveTo(px, py); // used for first point
        for (int i = 1; i < upperPoints.size() + 1; i++) {
            if (i == upperPoints.size()) {
                px = translateX(upperPoints.get(0).getX());
                py = translateY(upperPoints.get(0).getY());
            } else {
                px = translateX(upperPoints.get(i).getX());
                py = translateY(upperPoints.get(i).getY());
            }
            wallpath.lineTo(px, py);
        }
        canvas.drawPath(wallpath, wallpaint);
    }
}
