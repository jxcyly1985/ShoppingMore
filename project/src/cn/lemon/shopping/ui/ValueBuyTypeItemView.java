package cn.lemon.shopping.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.lemon.shopping.R;
import cn.lemon.utils.DebugUtil;

/**
 * Created with IntelliJ IDEA.
 * User: leiyong
 * Date: 14-3-22
 * Time: 下午1:15
 * To change this template use File | Settings | File Templates.
 */
public class ValueBuyTypeItemView extends ViewGroup {

    private static final String TAG = "ValueBuyTypeItemView";
    private TextView mTypeName;
    private ImageView mTypeDivider;

    public ValueBuyTypeItemView(Context context) {
        super(context);

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(R.layout.value_buy_type_item_layout, this);
        mTypeName = (TextView) findViewById(R.id.id_value_buy_type_name);
        mTypeDivider = (ImageView) findViewById(R.id.id_value_buy_type_divider);

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        DebugUtil.debug(TAG, "onLayout changed " + changed + " l " + l + " t " + t
                + " r " + r + " b " + b);
        mTypeName.layout(l, t, r - 4, b);
        mTypeDivider.layout(r - 4, t, r, b);


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int widthSpec = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpec = MeasureSpec.getSize(heightMeasureSpec);
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        DebugUtil.debug(TAG, "onMeasure widthSpec " + widthSpec + " heightSpec " + heightSpec
                + " widthSpecMode " + widthSpecMode + " heightSpecMode " + heightSpecMode);

        int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);

        mTypeName.measure(childWidthMeasureSpec, childHeightMeasureSpec);
        mTypeDivider.measure(childWidthMeasureSpec, childHeightMeasureSpec);

        DebugUtil.debug(TAG, "typeName Width " + mTypeName.getMeasuredWidth());
        DebugUtil.debug(TAG, "typeName Height " + mTypeName.getMeasuredHeight());
        DebugUtil.debug(TAG, "TypeDivider Width " + mTypeDivider.getMeasuredWidth());
        DebugUtil.debug(TAG, "TypeDivider Height " + mTypeDivider.getMeasuredHeight());

        int width = mTypeName.getMeasuredWidth() + mTypeDivider.getMeasuredWidth();
        int height = Math.max(mTypeName.getMeasuredHeight(), mTypeDivider.getMeasuredHeight());
        widthMeasureSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void setTypeName(String typeName) {

        mTypeName.setText(typeName);
    }

    public void setDividerVisible(int visible) {

        mTypeDivider.setVisibility(visible);
    }

}
