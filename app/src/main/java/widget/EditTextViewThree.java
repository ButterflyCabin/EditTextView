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
 */
public class EditTextViewThree extends AppCompatEditText {
    private int mWidth;
    private int mHeight;
    private int mItemWidth = 20;  // 支持XML赋值
    private int mItemHeight = 20;  // 支持XML赋值
    private int mTextCount = 4;  // 支持XML赋值
    private int mTextSize = 30;  // 支持XML赋值
    private int mBorderSize = 4;// 支持XML赋值
    private int mDividerSize = 4;  // 支持XML赋值
    private int mBackgroundColor = Color.TRANSPARENT;  // 支持XML赋值
    private int mTextColor = Color.BLACK;  // 支持XML赋值
    private int mBorderColor = Color.GRAY;  // 支持XML赋值
    private boolean isPassword = false;  // 支持XML赋值
    private Paint mBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mBorderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private SparseArray<RectF> mItemRectF = new SparseArray<>();
    private char[] mText;
    private int mTextLength;

    private OnCompleteListener onCompleteListener;


    public EditTextViewThree(Context context) {
        super(context);
        init(context, null);
    }

    public EditTextViewThree(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public EditTextViewThree(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.EditTextView);
            mItemWidth = a.getDimensionPixelOffset(R.styleable.EditTextView_edv_ItemWidth, mItemWidth);
            mItemHeight = a.getDimensionPixelOffset(R.styleable.EditTextView_edv_ItemHeight, mItemHeight);
            mTextCount = a.getInt(R.styleable.EditTextView_edv_ItemCount, mTextCount);
            mTextSize = a.getDimensionPixelOffset(R.styleable.EditTextView_edv_ContentTextSize, mTextSize);
            mBorderSize = a.getDimensionPixelOffset(R.styleable.EditTextView_edv_BorderSize, mBorderSize);
            mDividerSize = a.getDimensionPixelOffset(R.styleable.EditTextView_edv_DividerSize, mDividerSize);
            mBackgroundColor = a.getColor(R.styleable.EditTextView_edv_BackgroundColor, mBackgroundColor);
            mBorderColor = a.getColor(R.styleable.EditTextView_edv_BorderColor, mBorderColor);
            mTextColor = a.getColor(R.styleable.EditTextView_edv_ContentTextColor, mTextColor);
            isPassword = a.getBoolean(R.styleable.EditTextView_edv_password, isPassword);
            a.recycle();
        }
        setTextColor(Color.TRANSPARENT);
        setBackgroundDrawable(null);
        setLongClickable(false);
        setMaxHeight(mTextCount);
        setFilters(new InputFilter[]{new InputFilter.LengthFilter(mTextCount)});

        mBackgroundPaint.setAntiAlias(true);
        mBorderPaint.setAntiAlias(true);
        mTextPaint.setAntiAlias(true);
        mBackgroundPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setStyle(Paint.Style.FILL);

        mBackgroundPaint.setStrokeWidth(mBorderSize);
        mTextPaint.setTextSize(mTextSize);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int mode = MeasureSpec.getMode(widthMeasureSpec);
        if (mode == MeasureSpec.EXACTLY) {// match_parent fill_parent 50dp
            mWidth = MeasureSpec.getSize(widthMeasureSpec);
            mItemWidth = (mWidth - (mDividerSize * (mTextCount - 1))) / mTextCount;
        } else {
            mWidth = mItemWidth * mTextCount + mDividerSize * (mTextCount + 1) + getPaddingLeft() + getPaddingRight();
        }
        mode = MeasureSpec.getMode(heightMeasureSpec);
        if (mode == MeasureSpec.EXACTLY) {// match_parent fill_parent 50dp
            mHeight = MeasureSpec.getSize(heightMeasureSpec);
            mItemHeight = mHeight - getPaddingTop() - getPaddingBottom();
        } else {
            mHeight = mItemHeight + getPaddingTop() + getPaddingBottom();
        }

        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        this.mTextLength = text.toString().trim().length();
        invalidate();
        mText = getText().toString().trim().toCharArray();
        if (mTextLength == mTextCount && null != onCompleteListener) {
            onCompleteListener.onComplete(text);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 绘制背景
        drawBackground(canvas, mBackgroundColor, mBackgroundPaint);
        // 绘制边框
        drawContentBorder(canvas, mBorderSize, mBorderPaint, mBorderColor);
        // 绘制内容
        drawContentText(canvas, mTextColor, mTextPaint, isPassword);
    }

    private void drawBackground(Canvas canvas, int backgroundColor, Paint backgroundPaint) {
        mBackgroundPaint.setColor(backgroundColor);
        RectF rectF = new RectF(0, 0, mWidth, mHeight);
        canvas.drawRoundRect(rectF, 0, 0, backgroundPaint);// 画背景
    }

    private void drawContentBorder(Canvas canvas, int mBorderSize, Paint mContentPaint, int mContentRegionColor) {
        RectF itemRect;
        int left;
        int right;
        mContentPaint.setColor(mContentRegionColor);
        for (int i = 0; i < mTextCount; i++) {
            left = getPaddingLeft() + mItemWidth * i + mDividerSize * i;
            right = left + mItemWidth;
            itemRect = new RectF(left, getPaddingTop(), right, mHeight - getPaddingBottom() - mBorderSize);
            canvas.drawLine(left, mHeight - getPaddingBottom() - mBorderSize, right, mHeight - getPaddingBottom() - mBorderSize, mContentPaint);
            mItemRectF.put(i, itemRect);
        }
    }

    private void drawContentText(Canvas canvas, int mContentTextColor, Paint mTextPaint, boolean isPassword) {
        float cx;
        Rect bounds = new Rect();
        mTextPaint.setColor(mContentTextColor);
        Paint.FontMetricsInt fontMetrics = mTextPaint.getFontMetricsInt();
        int baseline = isPassword ? mHeight/2 : (mHeight - mBorderSize - getPaddingBottom() - getPaddingTop() - fontMetrics.bottom - fontMetrics.top) / 2;
        if (mTextLength <= mTextCount) {
            String text;
            for (int i = 0; i < mTextLength; i++) {
                RectF rectF1 = mItemRectF.get(i);
                if (isPassword) {
                    text = "*";
                } else {
                    text = mText[i] + "";
                }
                mTextPaint.getTextBounds(text.toString(), 0, text.toString().length(), bounds);
                cx = (rectF1.left + rectF1.right) / 2 - bounds.centerX();
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
