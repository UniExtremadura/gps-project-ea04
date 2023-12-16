package es.unex.nbafantasy.data.api

import com.google.gson.annotations.SerializedName


data class Meta (

  @SerializedName("total_pages"  ) var totalPages  : Int? = null,
  @SerializedName("current_page" ) var currentPage : Int? = null,
  @SerializedName("next_page"    ) var nextPage    : Int? = null,
  @SerializedName("per_page"     ) var perPage     : Int? = null,
  @SerializedName("total_count"  ) var totalCount  : Int? = null

)