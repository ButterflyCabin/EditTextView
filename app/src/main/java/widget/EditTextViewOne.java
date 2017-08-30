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

import widget.listener.OnCompleteListener;

/**
 * 创建日期：2017/8/29 15:48
 *
 * @author yehu
 *         类说明：
 */
public class EditTextViewOne extends AppCompatEditText {
    private int mWidth;
    private int mHeight;
    private int mItemWidth = 20;  // 支持XML赋值
    private int mItemHeight = 20;  // 支持XML赋值
    private int mItemCount = 4;  // 支持XML赋值
    private int mItemDivider = 10;  // 支持XML赋值
    private Paint mBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mContentPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private SparseArray<RectF> mItemRectF = new SparseArray<>();
    private int mTextLength;
    private char[] mText;
    private int mBackgroundColor = Color.BLUE;  // 支持XML赋值
    private int mContentRegionColor = Color.WHITE;  // 支持XML赋值
    private int mContentTextColor = Color.BLACK;  // 支持XML赋值
    private int mContentTextSize = 30;  // 支持XML赋值
    private int mBorderSize = 10;  // 支持XML赋值
    private OnCompleteListener onCompleteListener;


    public EditTextViewOne(Context context) {
        super(context);
        init(context, null);
    }

    public EditTextViewOne(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public EditTextViewOne(Context context, AttributeSet attrs, int defStyleAttr) {
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
            mContentTextSize = a.getDimensionPixelOffset(R.styleable.EditTextView_edv_ContentTextSize, mContentTextSize);
            mBackgroundColor = a.getColor(R.styleable.EditTextView_edv_BackgroundColor, mBackgroundColor);
            mContentRegionColor = a.getColor(R.styleable.EditTextView_edv_ContentRegionColor, mContentRegionColor);
            mContentTextColor = a.getColor(R.styleable.EditTextView_edv_ContentTextColor, mContentTextColor);
            a.recycle();
        }
        setBackgroundDrawable(null);
        setLongClickable(false);
        setMaxHeight(mItemCount);
        setFilters(new InputFilter[]{new InputFilter.LengthFilter(mItemCount)});

        mBackgroundPaint.setAntiAlias(true);
        mBackgroundPaint.setStyle(Paint.Style.FILL);
        mBackgroundPaint.setStrokeWidth(5);

        mContentPaint.setAntiAlias(true);
        mContentPaint.setStyle(Paint.Style.STROKE);
        mContentPaint.setStrokeWidth(mBorderSize/2);

        mTextPaint.setAntiAlias(true);
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setStrokeWidth(10);
        mTextPaint.setTextSize(mContentTextSize);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = mItemWidth * mItemCount + mItemDivider * (mItemCount - 1) + getPaddingLeft() + getPaddingRight();
        mHeight = mItemHeight + getPaddingTop() + getPaddingBottom();
        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        this.mTextLength = text.toString().trim().length();
        invalidate();
        mText = getText().toString().trim().toCharArray();
        if (mTextLength == mItemCount && null != onCompleteListener) {
            onCompleteListener.onComplete(text);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 绘制背景
        mBackgroundPaint.setColor(mBackgroundColor);
        RectF rectF = new RectF(0, 0, mWidth, mHeight);
        canvas.drawRoundRect(rectF, 5.0f, 5.0f, mBackgroundPaint);// 画背景

        // 绘制内容区域
        RectF itemRect;
        int left;
        int right;

        mContentPaint.setColor(mContentRegionColor);
        for (int i = 0; i < mItemCount; i++) {
            Log.e("getPaddingLeft(): ", getPaddingLeft() + "");
            Log.e("getPaddingRight(): ", getPaddingRight() + "");
            Log.e("getPaddingTop(): ", getPaddingTop() + "");
            Log.e("getPaddingBottom(): ", getPaddingBottom() + "");
            left = mBorderSize / 2 + getPaddingLeft() + mItemWidth * i + mItemDivider * i;
            right = left + mItemWidth - mBorderSize;
            itemRect = new RectF(left, mBorderSize / 2 + getPaddingTop(), right, mHeight - getPaddingBottom() - mBorderSize / 2);
            canvas.drawRoundRect(itemRect, 5.0f, 5.0f, mContentPaint);
            mItemRectF.put(i, itemRect);
        }

        // 绘制内容
        float cx;
        Rect bounds = new Rect();
        mTextPaint.setColor(mContentTextColor);
        Paint.FontMetricsInt fontMetrics = mTextPaint.getFontMetricsInt();
        int baseline = (mHeight - getPaddingBottom() - getPaddingTop() - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;
        if (mTextLength <= mItemCount) {
            for (int i = 0; i < mTextLength; i++) {
                String text = mText[i] + "";
                mTextPaint.getTextBounds(text.toString(), 0, text.toString().length(), bounds);
                RectF rectF1 = mItemRectF.get(i);
                cx = (rectF1.left + rectF1.right) / 2 - bounds.centerX() - mBorderSize/4;
                canvas.drawText(text, cx, baseline, mTextPaint);
            }
        }
    }

    public void setBorderColor(int borderColor) {
        mBackgroundColor = borderColor;
        invalidate();
    }

    public void setOnCompleteListener(OnCompleteListener listener) {
        onCompleteListener = listener;
    }
}
