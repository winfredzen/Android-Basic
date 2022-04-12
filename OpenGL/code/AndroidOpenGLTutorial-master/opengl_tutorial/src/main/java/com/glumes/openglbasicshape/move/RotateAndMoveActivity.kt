package com.glumes.openglbasicshape.move

import android.os.Bundle
import com.glumes.openglbasicshape.R
import com.glumes.openglbasicshape.base.BaseFragmentToolbarActivity
import com.glumes.openglbasicshape.move.movefrags.RotateFragment

class RotateAndMoveActivity : BaseFragmentToolbarActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        updateFragment(R.id.rotate)
    }

    override fun setMenuId() {
        setToolbarMenu(R.menu.rotate_and_touch_menu)
    }


    override fun initFragments() {
        mFragSparseArray.put(R.id.rotate, RotateFragment())
    }

}
