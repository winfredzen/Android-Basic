package com.example.customradiogroupdemo.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.IdRes;

import com.example.customradiogroupdemo.R;

import java.util.HashMap;

/**
 * create by wangzhen 2021/10/8
 */
public class PresetRadioGroup extends LinearLayout {
    // Attribute Variables
    private int mCheckedId = View.NO_ID; //选中的RadioButton的id
    private boolean mProtectFromCheckedChange = false;

    // Variables
    //Listener that is set by a user in order to listen for check change events.
    private OnCheckedChangeListener mOnCheckedChangeListener;
    private HashMap<Integer, View> mChildViewsMap = new HashMap<>();
    //This listener is used the internal purpose only. The radio group is notified when a child view is added or removed.
    private PassThroughHierarchyChangeListener mPassThroughListener;
    //This listener is assigned to a child radio button when it is added.
    private RadioCheckable.OnCheckedChangeListener mChildOnCheckedChangeListener;


    //================================================================================
    // Constructors
    //================================================================================

    public PresetRadioGroup(Context context) {
        super(context);
        setupView();
    }

    public PresetRadioGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        parseAttributes(attrs);
        setupView();
    }

    public PresetRadioGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        parseAttributes(attrs);
        setupView();
    }

    public PresetRadioGroup(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        parseAttributes(attrs);
        setupView();
    }

    //================================================================================
    // Init & inflate methods
    //================================================================================
    private void parseAttributes(AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.PresetRadioGroup, 0, 0);
        try {
            mCheckedId = a.getResourceId(R.styleable.PresetRadioGroup_presetRadioCheckedId, View.NO_ID);
        } finally {
            a.recycle();
        }
    }

    // Template method
    private void setupView() {
        mChildOnCheckedChangeListener = new CheckedStateTracker();
        //view层级改变回调
        mPassThroughListener = new PassThroughHierarchyChangeListener();
        super.setOnHierarchyChangeListener(mPassThroughListener);
    }


    //================================================================================
    // Overriding default behavior
    //================================================================================
    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if (child instanceof RadioCheckable) {
            final RadioCheckable button = (RadioCheckable) child;
            if (button.isChecked()) {
                mProtectFromCheckedChange = true; //防止下面设置radioButton状态时，引起状态回调调用，引起递归
                if (mCheckedId != View.NO_ID) {
                    setCheckedStateForView(mCheckedId, false);
                }
                mProtectFromCheckedChange = false;
                setCheckedId(child.getId(), true);
            }
        }

        super.addView(child, index, params);
    }

    @Override
    public void setOnHierarchyChangeListener(OnHierarchyChangeListener listener) {
        // the user listener is delegated to our pass-through listener
        mPassThroughListener.mOnHierarchyChangeListener = listener;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        // checks the appropriate radio button as requested in the XML file
        if (mCheckedId != View.NO_ID) {
            mProtectFromCheckedChange = true;
            setCheckedStateForView(mCheckedId, true);
            mProtectFromCheckedChange = false;
            setCheckedId(mCheckedId, true);
        }
    }

    private void setCheckedId(@IdRes int id, boolean isChecked) {
        mCheckedId = id;
        if (mOnCheckedChangeListener != null) {
            mOnCheckedChangeListener.onCheckedChanged(this, mChildViewsMap.get(id), isChecked, mCheckedId);
        }
    }

    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof LayoutParams;
    }

    public void clearCheck() {
        check(View.NO_ID);
    }

    public void check(@IdRes int id) {
        // don't even bother
        if (id != View.NO_ID && (id == mCheckedId)) {
            return;
        }

        if (mCheckedId != View.NO_ID) {
            setCheckedStateForView(mCheckedId, false);
        }

        if (id != View.NO_ID) {
            setCheckedStateForView(id, true);
        }

        setCheckedId(id, true);
    }

    private void setCheckedStateForView(int viewId, boolean checked) {
        View checkedView;
        checkedView = mChildViewsMap.get(viewId);
        if (checkedView == null) {
            checkedView = findViewById(viewId);
            if (checkedView != null) {
                mChildViewsMap.put(viewId, checkedView);
            }
        }
        if (checkedView != null && checkedView instanceof RadioCheckable) {
            ((RadioCheckable) checkedView).setChecked(checked);
        }
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }
    //================================================================================
    // Public methods
    //================================================================================


    public void setOnCheckedChangeListener(OnCheckedChangeListener onCheckedChangeListener) {
        mOnCheckedChangeListener = onCheckedChangeListener;
    }

    public OnCheckedChangeListener getOnCheckedChangeListener() {
        return mOnCheckedChangeListener;
    }


    //================================================================================
    // Nested classes
    //================================================================================
    public static interface OnCheckedChangeListener {
        void onCheckedChanged(View radioGroup, View radioButton, boolean isChecked, int checkedId);
    }

    //================================================================================
    // Inner classes
    //================================================================================
    private class CheckedStateTracker implements RadioCheckable.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(View buttonView, boolean isChecked) {
            // prevents from infinite recursion
            if (mProtectFromCheckedChange) {
                return;
            }

            mProtectFromCheckedChange = true;
            if (mCheckedId != View.NO_ID) {
                setCheckedStateForView(mCheckedId, false); //取消原来的选中
            }
            mProtectFromCheckedChange = false;

            int id = buttonView.getId();
            setCheckedId(id, true); //选中现在选择的
        }
    }

    private class PassThroughHierarchyChangeListener implements ViewGroup.OnHierarchyChangeListener {
        private ViewGroup.OnHierarchyChangeListener mOnHierarchyChangeListener;

        /**
         * {@inheritDoc}
         */
        public void onChildViewAdded(View parent, View child) {
            if (parent == PresetRadioGroup.this && child instanceof RadioCheckable) {
                int id = child.getId();
                // generates an id if it's missing
                if (id == View.NO_ID) {
                    id = ViewUtils.generateViewId();
                    child.setId(id);
                }
                //对新添加进来的RadioButton，添加事件
                ((RadioCheckable) child).addOnCheckChangeListener(mChildOnCheckedChangeListener);
                mChildViewsMap.put(id, child);
            }

            if (mOnHierarchyChangeListener != null) {
                mOnHierarchyChangeListener.onChildViewAdded(parent, child);
            }
        }

        /**
         * {@inheritDoc}
         */
        public void onChildViewRemoved(View parent, View child) {
            if (parent == PresetRadioGroup.this && child instanceof RadioCheckable) {
                ((RadioCheckable) child).removeOnCheckChangeListener(mChildOnCheckedChangeListener);
            }
            mChildViewsMap.remove(child.getId());
            if (mOnHierarchyChangeListener != null) {
                mOnHierarchyChangeListener.onChildViewRemoved(parent, child);
            }
        }
    }
}
