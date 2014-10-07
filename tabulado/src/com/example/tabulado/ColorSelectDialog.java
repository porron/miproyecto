package com.example.tabulado;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

public class ColorSelectDialog extends Dialog {
  public interface OnColorChangedListener {
    void colorChanged(String key, int color);
  }

  private OnColorChangedListener mListener;
  private int mInitialColor;
  private String mKey;
  
  public static class ColorPickerView extends View {
    private Paint mPaint;
    private float mCurrentHue = 0;
    private final int[] mHueBarColors = new int[258];
    private OnColorChangedListener mListener;
    
    public void setColor(int color) {

    }
    
    public int getColor() {
      return getCurrentMainColor();
    }

    ColorPickerView(Context c, OnColorChangedListener l, int color) {
      super(c);
      mListener = l;
      // Get the current hue from the current color and update the main color field
      float[] hsv = new float[3];
      Color.colorToHSV(color, hsv);
      mCurrentHue = hsv[0];

      // Initialize the colors of the hue slider bar
      int index = 0;
      for (float i=0; i<256; i += 256/42) // Red (#f00) to pink (#f0f)
      {
        mHueBarColors[index] = Color.rgb(255, 0, (int) i);
        index++;
      }
      for (float i=0; i<256; i += 256/42) // Pink (#f0f) to blue (#00f)
      {
        mHueBarColors[index] = Color.rgb(255-(int) i, 0, 255);
        index++;
      }
      for (float i=0; i<256; i += 256/42) // Blue (#00f) to light blue (#0ff)
      {
        mHueBarColors[index] = Color.rgb(0, (int) i, 255);
        index++;
      }
      for (float i=0; i<256; i += 256/42) // Light blue (#0ff) to green (#0f0)
      {
        mHueBarColors[index] = Color.rgb(0, 255, 255-(int) i);
        index++;
      }
      for (float i=0; i<256; i += 256/42) // Green (#0f0) to yellow (#ff0)
      {
        mHueBarColors[index] = Color.rgb((int) i, 255, 0);
        index++;
      }
      for (float i=0; i<256; i += 256/42) // Yellow (#ff0) to red (#f00)
      {
        mHueBarColors[index] = Color.rgb(255, 255-(int) i, 0);
        index++;
      }

      // Initializes the Paint that will draw the View
      mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
      mPaint.setTextAlign(Paint.Align.CENTER);
      mPaint.setTextSize(12);
    }

    // Get the current selected color from the hue bar
    private int getCurrentMainColor()
    {
      int translatedHue = 255-(int)(mCurrentHue*255/360);
      int index = 0;
      for (float i=0; i<256; i += 256/42)
      {
        if (index == translatedHue)
          return Color.rgb(255, 0, (int) i);
        index++;
      }
      for (float i=0; i<256; i += 256/42)
      {
        if (index == translatedHue)
          return Color.rgb(255-(int) i, 0, 255);
        index++;
      }
      for (float i=0; i<256; i += 256/42)
      {
        if (index == translatedHue)
          return Color.rgb(0, (int) i, 255);
        index++;
      }
      for (float i=0; i<256; i += 256/42)
      {
        if (index == translatedHue)
          return Color.rgb(0, 255, 255-(int) i);
        index++;
      }
      for (float i=0; i<256; i += 256/42)
      {
        if (index == translatedHue)
          return Color.rgb((int) i, 255, 0);
        index++;
      }
      for (float i=0; i<256; i += 256/42)
      {
        if (index == translatedHue)
          return Color.rgb(255, 255-(int) i, 0);
        index++;
      }
      return Color.RED;
    }

    @Override
    protected void onDraw(Canvas canvas) {
      int translatedHue = 255-(int)(mCurrentHue*255/360);
      // Display all the colors of the hue bar with lines
      for (int x=0; x<256; x++)
      {
        // If this is not the current selected hue, display the actual color
        if (translatedHue != x)
        {
          mPaint.setColor(mHueBarColors[x]);
          mPaint.setStrokeWidth(2);
        }
        else // else display a slightly larger black line
        {
          mPaint.setColor(Color.BLACK);
          mPaint.setStrokeWidth(3);
        }
        canvas.drawLine((x*2)+10, 0, (x*2)+10, 40, mPaint);
      }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
      //setMeasuredDimension(276, 60);
      setMeasuredDimension(532, 60);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
      if (event.getAction() != MotionEvent.ACTION_DOWN) return true;
      float x = event.getX();
      float y = event.getY();

      //the touch event is located in the hue bar
      if (x > 10 && x < 522 && y > 0 && y < 40)
      {
        // Update the main field colors
        mCurrentHue = (255-(x/2))*360/255;
        mListener.colorChanged("", getCurrentMainColor());

        // Force the redraw of the dialog
        invalidate();
      }

      return true;
    }
  }
  
  public ColorSelectDialog(Context context, OnColorChangedListener listener, String key, int initialColor) {
    super(context);

    mListener = listener;
    mKey = key;
    mInitialColor = initialColor;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    OnColorChangedListener l = new OnColorChangedListener() {
      public void colorChanged(String key, int color) {
        mListener.colorChanged(mKey, color);
        dismiss();
      }
    };

    setContentView(new ColorPickerView(getContext(), l, mInitialColor));
    setTitle("Select Color");
  }
}
