package com.decard.facelibrary

import com.decard.facelibrary.utils.DCFaceManager
import com.decard.facelibrary.utils.EIFaceUtils
import com.decard.facelibrary.utils.HRFaceUtils

object DCFaceFactory {


    fun initFace(version: Int): DCFaceManager {
        if (version == 1) {
            return EIFaceUtils.instance
        }
        return HRFaceUtils.instance
    }
}