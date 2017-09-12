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
public class EditTextView extends AppCompatEditText {
    private static final int TYPE_A = 0;
    private static final int TYPE_B = 1;
    private static final int TYPE_C = 2;
    private static final int TYPE_D = 3;
    private int mWidth;
    private int mHeight;
    private int mItemWidth = 20;  // 支持XML赋值
    private int mItemHeight = 20;  // 支持XML赋值
    private int mItemCount = 4;  // 支持XML赋值
    private int mItemDivider = 10;  // 支持XML赋值
    private int mBackgroundColor = Color.BLUE;  // 支持XML赋值
    private int mBorderColor = Color.GRAY;  // 支持XML赋值
    private int mContentRegionColor = Color.WHITE;  // 支持XML赋值
    private int mContentTextColor = Color.BLACK;  // 支持XML赋值
    private int mContentTextSize = 30;  // 支持XML赋值
    private int mBorderSize = 4;// 支持XML赋值
    private int mBorderRadius = 5;// 支持XML赋值
    private int mDividerSize = 4;  // 支持XML赋值
    private boolean isPassword = false;  // 支持XML赋值
    private Paint mBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mContentPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private SparseArray<RectF> mItemRectF = new SparseArray<>();
    private int mTextLength;
    private char[] mText;
    private int mType = TYPE_A;


    private OnCompleteListener onCompleteListener;


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
//            mItemWidth = a.getDimensionPixelOffset(R.styleable.EditTextView_edv_item_width, mItemWidth);
//            mItemHeight = a.getDimensionPixelOffset(R.styleable.EditTextView_edv_item_height, mItemHeight);
//            mItemDivider = a.getDimensionPixelOffset(R.styleable.EditTextView_edv_item_divider, mItemDivider);
//            mItemCount = a.getInteger(R.styleable.EditTextView_edv_item_count, mItemCount);
//            mContentTextSize = a.getDimensionPixelOffset(R.styleable.EditTextView_edv_content_text_size, mContentTextSize);
//            mBorderSize = a.getDimensionPixelOffset(R.styleable.EditTextView_edv_border_size, mBorderSize);
//            mDividerSize = a.getDimensionPixelOffset(R.styleable.EditTextView_edv_divider_size, mDividerSize);
//            mBorderRadius = a.getDimensionPixelOffset(R.styleable.EditTextView_edv_border_radius, mBorderRadius);
//            mBackgroundColor = a.getColor(R.styleable.EditTextView_edv_background_color, mBackgroundColor);
//            mBorderColor = a.getColor(R.styleable.EditTextView_edv_border_color, mBorderColor);
//            mContentRegionColor = a.getColor(R.styleable.EditTextView_edv_content_region_color, mContentRegionColor);
//            mContentTextColor = a.getColor(R.styleable.EditTextView_edv_content_text_color, mContentTextColor);
//            mType  = a.getInt(R.styleable.EditTextView_edv_type,mType);
//            isPassword  = a.getBoolean(R.styleable.EditTextView_edv_password,isPassword);
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.EditTextView);
            mItemWidth = a.getDimensionPixelOffset(R.styleable.EditTextView_edv_ItemWidth, mItemWidth);
            mItemHeight = a.getDimensionPixelOffset(R.styleable.EditTextView_edv_ItemHeight, mItemHeight);
            mItemDivider = a.getDimensionPixelOffset(R.styleable.EditTextView_edv_ItemDivider, mItemDivider);
            mItemCount = a.getInteger(R.styleable.EditTextView_edv_ItemCount, mItemCount);
            mContentTextSize = a.getDimensionPixelOffset(R.styleable.EditTextView_edv_ContentTextSize, mContentTextSize);
            mBorderSize = a.getDimensionPixelOffset(R.styleable.EditTextView_edv_BorderSize, mBorderSize);
            mDividerSize = a.getDimensionPixelOffset(R.styleable.EditTextView_edv_DividerSize, mDividerSize);
            mBorderRadius = a.getDimensionPixelOffset(R.styleable.EditTextView_edv_BorderRadius, mBorderRadius);
            mBackgroundColor = a.getColor(R.styleable.EditTextView_edv_BackgroundColor, mBackgroundColor);
            mBorderColor = a.getColor(R.styleable.EditTextView_edv_BorderColor, mBorderColor);
            mContentRegionColor = a.getColor(R.styleable.EditTextView_edv_ContentRegionColor, mContentRegionColor);
            mContentTextColor = a.getColor(R.styleable.EditTextView_edv_ContentTextColor, mContentTextColor);
            mType  = a.getInt(R.styleable.EditTextView_edv_Type,mType);
//            isPassword  = a.getBoolean(R.styleable.EditTextView_edv_Password,isPassword);
            a.recycle();
        }
        setBackgroundDrawable(null);
        setLongClickable(false);
        setMaxHeight(mItemCount);
        setFilters(new InputFilter[]{new InputFilter.LengthFilter(mItemCount)});

        mBackgroundPaint.setAntiAlias(true);
        mContentPaint.setAntiAlias(true);
        mTextPaint.setAntiAlias(true);
        mBackgroundPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setStyle(Paint.Style.FILL);

       switch (mType){
           case TYPE_A:
               initByType(mBorderSize,10,mContentTextSize,Paint.Style.FILL);
               break;
           case TYPE_B:
               initByType(mBorderSize,10,mContentTextSize,Paint.Style.STROKE);
               break;
           case TYPE_C:
               initByType(mBorderSize,10,mContentTextSize,Paint.Style.FILL);
               break;
           case TYPE_D:
               initByType(mBorderSize,10,mContentTextSize,Paint.Style.FILL);
               break;
           default:
       }
    }

    private void initByType(int borderSize,int contentSize,int textSize,Paint.Style style ){
        mBackgroundPaint.setStrokeWidth(borderSize);
        mContentPaint.setStyle(style);
        mContentPaint.setStrokeWidth(contentSize);
        mTextPaint.setTextSize(textSize);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        switch (mType) {
            case TYPE_A:
                mWidth = mItemWidth * mItemCount + mBorderSize * (mItemCount + 1) + getPaddingLeft() + getPaddingRight();
                mHeight = mItemHeight + getPaddingTop() + getPaddingBottom() + mBorderSize * 2;
                break;
            case TYPE_B:
                mWidth = mItemWidth * mItemCount + mItemDivider * (mItemCount - 1) + getPaddingLeft() + getPaddingRight();
                mHeight = mItemHeight + getPaddingTop() + getPaddingBottom();
                break;
            case TYPE_C:
                break;
            case TYPE_D:
                break;
            default:
        }
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
        // 绘制内容区域
        // 绘制内容
        switch (mType) {
            case TYPE_A:
                drawBackground(canvas,mBackgroundColor,mBackgroundPaint);
                drawContentRegion(canvas,mBorderSize,mContentPaint,mContentRegionColor);
                drawContentText(canvas, mContentTextColor, mTextPaint, false);
                break;
            case TYPE_B:
                drawBackground(canvas,mBackgroundColor,mBackgroundPaint);
                drawContentRegion(canvas,mBorderSize,mContentPaint,mContentRegionColor);
                drawContentText(canvas, mContentTextColor, mTextPaint, false);
                break;
            case TYPE_C:
                break;
            case TYPE_D:
                break;
            default:
        }
    }

    private void drawBackground(Canvas canvas,int backgroundColor,Paint backgroundPaint){
        mBackgroundPaint.setColor(backgroundColor);
        RectF rectF = new RectF(0, 0, mWidth, mHeight);
        canvas.drawRoundRect(rectF, mBorderRadius, mBorderRadius, backgroundPaint);// 画背景
    }

    private void drawContentRegion(Canvas canvas,int mBorderSize,Paint mContentPaint,int mContentRegionColor){
        RectF itemRect;
        int left;
        int right;
        mContentPaint.setColor(mContentRegionColor);
        for (int i = 0; i < mItemCount; i++) {
            left = getPaddingLeft() + mItemWidth * i + mBorderSize * (i + 1);
            right = left + mItemWidth;
            itemRect = new RectF(left, getPaddingTop() + mBorderSize, right, mHeight - getPaddingBottom() - mBorderSize);
//            Log.e("ItemRect ", i + " : " + itemRect.left + " : " + itemRect.right + " : " + (itemRect.right - itemRect.left));
            canvas.drawRoundRect(itemRect, mBorderRadius, mBorderRadius, mContentPaint);
            mItemRectF.put(i, itemRect);
        }
    }

    private void drawContentBorder(Canvas canvas,int mBorderSize, Paint mContentPaint,int mContentRegionColor){
        RectF itemRect;
        int left;
        int right;
        mContentPaint.setColor(mContentRegionColor);
        for (int i = 0; i < mItemCount; i++) {
            left = getPaddingLeft() + mItemWidth * i + mBorderSize * (i + 1);
            right = left + mItemWidth;
            itemRect = new RectF(left, getPaddingTop() + mBorderSize, right, mHeight - getPaddingBottom() - mBorderSize);
//            Log.e("ItemRect ", i + " : " + itemRect.left + " : " + itemRect.right + " : " + (itemRect.right - itemRect.left));
            canvas.drawRoundRect(itemRect, mBorderRadius, mBorderRadius, mContentPaint);
            mItemRectF.put(i, itemRect);
        }
    }

    private void drawContentText(Canvas canvas,int mContentTextColor,Paint mTextPaint, boolean isPassword){
        float cx;
        Rect bounds = new Rect();
        mTextPaint.setColor(mContentTextColor);
        Paint.FontMetricsInt fontMetrics = mTextPaint.getFontMetricsInt();
        int baseline = isPassword ? 0:(mHeight - getPaddingBottom() - getPaddingTop() - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;
        if (mTextLength <= mItemCount) {
            String text;
            for (int i = 0; i < mTextLength; i++) {
                RectF rectF1 = mItemRectF.get(i);
                if (isPassword){
                    text = mText[i] + "";
                    cx = (rectF1.left + rectF1.right) / 2;
                }else {
                    text = "*";
                    mTextPaint.getTextBounds(text.toString(), 0, text.toString().length(), bounds);
                    cx = (rectF1.left + rectF1.right) / 2 - bounds.centerX();
                }
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
