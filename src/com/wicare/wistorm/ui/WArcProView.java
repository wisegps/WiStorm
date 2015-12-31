package com.wicare.wistorm.ui;

import android.content.Context;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;


/**
 * @author Wu 圆弧形进度UI
 *
 */
public class WArcProView  extends View {

	static final String TAG = WArcProView.class.getSimpleName();
	private int MaxInsideProgress = 270;
	private int MaxProgress = MaxInsideProgress;
	
	private boolean enableInsideRing = false;
	
	private int colorBg = Color.WHITE; //背景颜色
	private int colorBgRing = 0x80ffffff;//内环颜色
	private int colorScaleRing = 0xccdddddd;//外环的颜色
	private int colorScaleRingPoint = 0xff20B2AA;//外环上圆点的颜色
	
	private int startProgressColor = 0xff40E0D0;//开始渐变的颜色
	private int endProgressColor = 0xff7FFFAA;//渐变结束的颜色
	private int colorProgressBgRing = 0xff7FFFAA;//环形进度的颜色
	
	private int scaleRingPointSize = 8;

	private Paint paintBgRing; //环形画笔
	private Paint paintProgressRing;//环形进度画笔
	private Paint paintScaleRing;//最外圆环画笔
	private Paint paintCiecle; //最外环的圆点
	
	private RectF boundScaleRing;
	private RectF boundTimeRing;

	private int centerX;
	private int centerY;
	private float radius;//半径
	private float radiusScaleRing;//外环半径
	
	private float strokeWidthRing = 30;//内圆环大小
	private float strokeWidthScaleRing = 2;//外圆环大小
	private float zDepthScaleRing = 180;//调整外环的距离
	private float zDepthDashRing = 100;//调整内环距离
	
	private float rotateOutPointer = 0;
	private float rotateInPointer = 0;
	private float progressInitInsideRotateDegree = 135;//内环调整这个可以调整起始的位置
	private float progressInitOutsideRotateDegree = progressInitInsideRotateDegree +90;//内环调整这个可以调整起始的位置
	
	private float[] ringDashIntervals = new float[]{3f, 6f};
	private float[] sweepGradientColorPos = new float[]{0f, 300f / 360f, 330f / 360f, 1f};//颜色渐变的范围长度
	private int[] sweepGradientColors = new int[]{Color.TRANSPARENT, startProgressColor, endProgressColor, colorProgressBgRing};//渐变颜色

	private Matrix matrixCanvas = new Matrix();
	private Matrix matrixSweepGradient = new Matrix();
	private Shader sweepGradient;
	private Camera camera = new Camera();

	

	public WArcProView(Context context) {
		super(context);
	}

	//下面这个构造可以在xml 中引用
	public WArcProView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		resetInit();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawColor(colorBg);//画背景图
		drawContent(canvas);
	}
	
	/**
	 * @param color 背景颜色
	 */
	public void setBackGroundColor(int color){
		this.colorBg = color;
	}
	
	
	/**
	 * @param color 外环的圆点颜色
	 */
	public void setScaleRingPointColor(int color){
		this.colorScaleRingPoint = color;
	}
	
	/**
	 * @param size 外环上圆点的大小
	 */
	public void setScaleRingPointSize(int size){
		this.scaleRingPointSize = size;
	}
	
	
	/**
	 * @param color 外环形的颜色
	 */
	public void setScaleRingBackground(int color){
		this.colorScaleRing = color;
	}
	
	/**
	 * @param startColor 
	 * @param endColor
	 * @param color
	 */
	public void setSweepGradientColors(int startColor,int endColor,int color){
		this.sweepGradientColors = new int[]{Color.TRANSPARENT, startColor, endColor, color};
	}
	
	
	
	public void enableInsideScaleRing(boolean enable){
		this.enableInsideRing = enable;
		if(enableInsideRing){
			MaxProgress = 0;
		}
	}
	
	private void translateCanvas(Canvas canvas, float x, float y, float z) {
		matrixCanvas.reset();
		camera.save();
		camera.translate(x, y, z);
		camera.getMatrix(matrixCanvas);
		camera.restore();

		int matrixCenterX = centerX;
		int matrixCenterY = centerY;
		matrixCanvas.preTranslate(-matrixCenterX, -matrixCenterY);
		matrixCanvas.postTranslate(matrixCenterX, matrixCenterY);
		canvas.concat(matrixCanvas);
	}

	/**
	 * @param canvas 画所有的东西
	 */
	private void drawContent(Canvas canvas) {
		// Check rotate bound
		if (rotateOutPointer >= 270f) {
			rotateOutPointer= 270f;
		}

		// Rotate ring sweep gradient
		matrixSweepGradient.setRotate(getProgressInitInsideRingRotateDegree(), centerX, centerY);
		sweepGradient.setLocalMatrix(matrixSweepGradient);
		paintProgressRing.setShader(sweepGradient);		

		// Draw scale ring
		canvas.save();
		translateCanvas(canvas, 0f, 0f, zDepthScaleRing);
//		canvas.drawCircle(centerX, centerY, radiusScaleRing, paintScaleRing);
		canvas.drawArc(boundScaleRing, 135f, 270f , false, paintScaleRing);//画圆弧
		canvas.restore();

		// Draw dash ring and second pointer
		canvas.save();
		translateCanvas(canvas, 0f, 0f, zDepthDashRing);
		canvas.drawArc(boundTimeRing, 0f, 359.0f, false, paintBgRing);//画底层圆环
		canvas.drawArc(boundTimeRing ,135f, 270f, false, paintProgressRing);//画进度圆环
		canvas.restore();

		//draw circle point
		canvas.save();
		translateCanvas(canvas, 0f, 0f, zDepthScaleRing);
		canvas.rotate(getProgressOutsideRingRotateDegree(), centerX, centerY);
		canvas.drawCircle(centerX, centerY - radiusScaleRing, scaleRingPointSize, paintCiecle);
		canvas.restore();
	}

	/**
	 * 外层圆弧上的圆点
	 */
	private void initArcPoint() {
		paintCiecle = new Paint();
		paintCiecle.setColor(colorScaleRingPoint);
		paintCiecle.setStyle(Paint.Style.FILL);
		paintCiecle.setAntiAlias(true);
	}

	/**
	 * 初始化
	 */
	private void resetInit() {
		initBound();
		initBgRing();
		initProgressRing();
		initScaleRing();
		initArcPoint();
	}
	
	/**
	 * 初始化画布的一下位置大小参数
	 */
	private void initBound() {
		radius = getWidth() * 3 / 7;
		radiusScaleRing = radius * 4 / 3;
		centerX = getWidth() / 2;
		centerY = getHeight() / 2;
		boundTimeRing = new RectF(centerX - radius, centerY - radius, centerX + radius, centerY + radius);
		boundScaleRing = new RectF(centerX - radiusScaleRing, centerY - radiusScaleRing, centerX + radiusScaleRing, centerY + radiusScaleRing);	
	}

	/**
	 * 外的圆环
	 */
	private void initScaleRing() {
		// Scale ring paint
		paintScaleRing = new Paint();
		paintScaleRing.setAntiAlias(true);
		paintScaleRing.setStyle(Paint.Style.STROKE);
		paintScaleRing.setStrokeWidth(strokeWidthScaleRing);
		paintScaleRing.setColor(colorScaleRing);
	}


	/**
	 * 内圆环
	 */
	private void initBgRing() {
		paintBgRing = new Paint();
		paintBgRing.setStyle(Paint.Style.STROKE);
		paintBgRing.setStrokeWidth(strokeWidthRing);
		paintBgRing.setAntiAlias(true);
		paintBgRing.setPathEffect(new DashPathEffect(ringDashIntervals, 0));
		paintBgRing.setColor(colorBgRing);
	}

	/**
	 * 进度绘制
	 */
	private void initProgressRing() {
		paintProgressRing = new Paint(paintBgRing);
		paintProgressRing.setStyle(Paint.Style.STROKE);
		paintProgressRing.setColor(Color.WHITE);
		sweepGradient = new SweepGradient(centerX, centerY, sweepGradientColors, sweepGradientColorPos);
		paintProgressRing.setShader(sweepGradient);
	}



	
	
	/**
	 * @return 返回内环初始角度
	 */
	public float getProgressInitInsideRingRotateDegree() {
		float degree = 0f;
		if(enableInsideRing){
			degree = (rotateInPointer + progressInitInsideRotateDegree) % 360;
		}else{
			degree = (MaxProgress + progressInitInsideRotateDegree) % 360;
		}
		return degree;
	}
	
	
	public void setProgressInitInsideRingRotateDegree(int rotateInsidePointer){
		if(enableInsideRing){
			this.rotateInPointer = rotateInsidePointer * (MaxInsideProgress/10) / 10;
		}else{
			this.rotateInPointer = rotateInsidePointer * (MaxProgress/10) / 10;
		}
		postInvalidate();
	}
	
	/**
	 * @return 返回外环角度
	 */
	public float getProgressOutsideRingRotateDegree() {
		float degree = (rotateOutPointer + progressInitOutsideRotateDegree) % 360;
		return degree;
	}
	
	
	/**
	 * @param rotateSecondPointer  进度 
	 */
	public void setProgressOutsideRingRotateDegree(int rotateOutsidePointer){
		if(enableInsideRing){
			this.rotateOutPointer = rotateOutsidePointer * (MaxInsideProgress/10) / 10;
		}else{
			this.rotateOutPointer = rotateOutsidePointer * (MaxProgress/10) / 10;
		}
		postInvalidate();
	}
	

	public float dp2px(float dpValue) {
		return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, getResources().getDisplayMetrics());
	}

	public float sp2px(float spValue) {
		return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spValue, getResources().getDisplayMetrics());
	}

}
