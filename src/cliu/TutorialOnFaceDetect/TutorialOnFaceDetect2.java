package cliu.TutorialOnFaceDetect;

/*
 * TutorialOnFaceDetect1
 * 
 * [AUTHOR]: Chunyen Liu
 * [SDK   ]: Android SDK 2.1 and up
 * [NOTE  ]: developer.com tutorial, "Face Detection with Android APIs"
 */

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.media.FaceDetector;
import android.media.FaceDetector.Face;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout.LayoutParams;

public class TutorialOnFaceDetect2 extends Activity {
	private MyImageView mIV;
	private Bitmap mFaceBitmap;
	private int mFaceWidth = 200;
	private int mFaceHeight = 200;
	private static final int MAX_FACES = 10;
	private static String TAG = "TutorialOnFaceDetect";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mIV = new MyImageView(this);
		setContentView(mIV, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

		// load the photo
		Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.face1);
		mFaceBitmap = b.copy(Bitmap.Config.RGB_565, true);
		b.recycle();

		mFaceWidth = mFaceBitmap.getWidth();
		mFaceHeight = mFaceBitmap.getHeight();
		mIV.setImageBitmap(mFaceBitmap);

		// perform face detection and set the feature points
		setFace();

		mIV.invalidate();
	}

	public void setFace() {
		FaceDetector fd;
		FaceDetector.Face[] faces = new FaceDetector.Face[1];
		PointF midpoint = new PointF();
		int[] fpx = null;
		int[] fpy = null;
		Rect[] faceRects = null;
		int count = 0;

		try {
			fd = new FaceDetector(mFaceWidth, mFaceHeight, 1);
			long startTime = System.currentTimeMillis();
			count = fd.findFaces(mFaceBitmap, faces);
			long end = System.currentTimeMillis();
			Log.e("TutorialOnFaceDetect1", "TutorialOnFaceDetect  time:" + (end - startTime));
		} catch (Exception e) {
			Log.e(TAG, "setFace(): " + e.toString());
			return;
		}
		// check if we detect any faces
		if (count > 0) {
			fpx = new int[count];
			fpy = new int[count];
			faceRects = new Rect[count];
			for (int i = 0; i < count; i++) {
				Face f = faces[i];
				PointF midPoint = new PointF();
				float dis = f.eyesDistance();
				f.getMidPoint(midPoint);
				int dd = (int) (dis);
				Rect faceRect = new Rect((int) (midPoint.x - dd), (int) (midPoint.y - dd), (int) (midPoint.x + dd),
						(int) (midPoint.y + dd));
				if (checkFace(faceRect)) {
					faceRects[i] = faceRect;
				}
			}
		}
		mIV.setFaceRects(faceRects, 2);
	}

	public boolean checkFace(Rect rect) {
		int w = rect.width();
		int h = rect.height();
		int s = w * h;
		Log.e("TutorialOnFaceDetect1", "TutorialOnFaceDetect   人脸 宽w : " + w + " 高h : " + h + " 人脸面积 s : " + s);
		if (s < 10000) {
			Log.e("TutorialOnFaceDetect1", "TutorialOnFaceDetect   无效人脸，舍弃.");
			return false;
		} else {
			Log.e("TutorialOnFaceDetect1", "TutorialOnFaceDetect   有效人脸，保存.");
			return true;
		}
	}
}