package widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.v7.widget.AppCompatEditText;
import android.text.InputFilter;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;

import com.yehu.edittextview.R;

/**
 * 创建日期：2017/8/29 15:48
 *
 * @author yehu
 *         类说明：
 */
public class EditTextView extends AppCompatEditText {
    int mWidth;
    int mHeight;
    int mItemWidth = 20;
    int mItemHeight = 20;
    int mItemCount = 4;
    int mItemDivider = 10;
    Paint mBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    Paint mBorderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    Paint mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    SparseArray<RectF> mItemRectF = new SparseArray<>();
    int mTextLength ;
    private char[] mText;


    public EditTextView(Context context) {
        super(context);
        init(context, null);
    }

    public EditTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public EditTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.EditTextView);
            mItemWidth = a.getDimensionPixelOffset(R.styleable.EditTextView_edv_ItemWidth, mItemWidth);
            mItemHeight = a.getDimensionPixelOffset(R.styleable.EditTextView_edv_ItemHeight, mItemHeight);
            mItemDivider = a.getDimensionPixelOffset(R.styleable.EditTextView_edv_ItemDivider, mItemDivider);
            mItemCount = a.getInteger(R.styleable.EditTextView_edv_ItemCount, mItemCount);
            a.recycle();
        }
        setBackgroundDrawable(null);
        setMaxHeight(mItemCount);
        setFilters(new InputFilter[]{new InputFilter.LengthFilter(mItemCount)});

        mBackgroundPaint.setColor(Color.GRAY);
        mBackgroundPaint.setAntiAlias(true);
        mBackgroundPaint.setStyle(Paint.Style.FILL);
        mBackgroundPaint.setStrokeWidth(5);

        mBorderPaint.setColor(Color.GREEN);
        mBorderPaint.setAntiAlias(true);
        mBorderPaint.setStyle(Paint.Style.FILL);
        mBorderPaint.setStrokeWidth(10);

        mTextPaint.setColor(Color.BLACK);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setStrokeWidth(10);
        mTextPaint.setTextSize(40);


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        /**
         * 设置宽度
         */
//        int specMode = MeasureSpec.getMode(widthMeasureSpec);
//        int specSize = MeasureSpec.getSize(widthMeasureSpec);

//        if (specMode == MeasureSpec.EXACTLY){// match_parent , accurate
//            Log.e("xxx", "EXACTLY");
//            mWidth = specSize;
//        } else  if (specMode == MeasureSpec.AT_MOST){// wrap_content
//                // 由字体决定的宽
////                int desireByTitle = getPaddingLeft() + getPaddingRight() ;
////                mWidth = Math.min(desire, specSize);
//                Log.e("xxx", "AT_MOST");
//        }else {
//            mWidth = widthMeasureSpec;
//        }


        /***
         * 设置高度
         */

//        specMode = MeasureSpec.getMode(heightMeasureSpec);
//        specSize = MeasureSpec.getSize(heightMeasureSpec);
//        if (specMode == MeasureSpec.EXACTLY){// match_parent , accurate
//            mHeight = specSize;
//        } else if (specMode == MeasureSpec.AT_MOST){// wrap_content
//
//        }else {
//            mHeight = heightMeasureSpec;
//        }

        mWidth = mItemWidth * mItemCount + mItemDivider * (mItemCount + 1) + getPaddingLeft() + getPaddingRight();
        mHeight = mItemHeight + getPaddingTop() + getPaddingBottom() + mItemDivider * 2;
        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        this.mTextLength = text.toString().length();
        invalidate();
        mText = getText().toString().trim().toCharArray();
//        if (textLength == mItemCount && null != onCompleteListener) {
//            onCompleteListener.onComplete(text);
//        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        if (isDraw)
        RectF rectF = new RectF(0, 0, mWidth, mHeight);
        canvas.drawRoundRect(rectF, 5.0f, 5.0f, mBackgroundPaint);// 画背景
        RectF itemRect;
        int left;
        int right;
        for (int i = 0; i < mItemCount; i++) {
            left = getPaddingLeft() + mItemWidth * i + mItemDivider * (i + 1);
//           right = getPaddingLeft() + mItemWidth * ( i+1) + mItemDivider * (i+1) ;
            right = left + mItemWidth;
            itemRect = new RectF(left, getPaddingTop() + mItemDivider, right, mHeight - getPaddingBottom() - mItemDivider);
//            Log.e("ItemRect ", i + " : " + itemRect.left + " : " + itemRect.right + " : " + (itemRect.right - itemRect.left));
            canvas.drawRoundRect(itemRect, 5.0f, 5.0f, mBorderPaint);
            mItemRectF.put(i, itemRect);
        }

        float cx;
        Rect bounds = new Rect();
        Paint.FontMetricsInt fontMetrics = mTextPaint.getFontMetricsInt();
        int baseline = (mHeight - getPaddingBottom() - getPaddingTop() - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;
        if (mTextLength <= mItemCount){
            for (int i = 0; i < mTextLength; i++) {
                String text = mText[i]+"";
                mTextPaint.getTextBounds(text.toString(), 0, text.toString().length(), bounds);
                RectF rectF1 = mItemRectF.get(i);
                cx = (rectF1.left + rectF1.right ) / 2 - bounds.centerX();//(bounds.right -bounds.left)
                canvas.drawText(text, cx, baseline, mTextPaint);
            }
        }
    }
}
