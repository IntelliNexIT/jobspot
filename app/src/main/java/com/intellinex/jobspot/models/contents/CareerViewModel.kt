package com.intellinex.jobspot.models.contents

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.intellinex.jobspot.api.resource.content.CareerDetailResponse

class CareerViewModel: ViewModel() {

    val careerData: MutableLiveData<CareerDetailResponse> = MutableLiveData()

}