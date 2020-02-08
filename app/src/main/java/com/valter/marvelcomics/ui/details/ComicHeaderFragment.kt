package com.valter.marvelcomics.ui.details

import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.valter.marvelcomics.R
import com.valter.marvelcomics.data.database.entity.Comic
import com.valter.marvelcomics.utils.Outcome
import com.valter.marvelcomics.utils.buildHighResImageUrl
import kotlinx.android.synthetic.main.fragment_comic_details_header.*

class ComicHeaderFragment : ComicDetailsBaseFragment() {
    override val layout: Int
        get() = R.layout.fragment_comic_details_header

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.selectedComic.observe(viewLifecycleOwner, Observer { outcome ->
            when (outcome) {
                is Outcome.Success -> setData(outcome.data)
            }
        })
    }

    private fun setData(comic: Comic) {
        showContent()
        with(comic) {
            context?.let {
                Glide.with(it)
                        .load(thumbnail.buildHighResImageUrl())
                        .centerCrop()
                        .into(imgBackground)
            }

            txtName.text = title
            val creatorList = creators.items.joinToString { "${it.name} (${it.role})" }
            txtCreators.text = getString(R.string.by_creator_list, creatorList)
        }
    }

    override fun onApplyWindowInsets(p0: View?, insets: WindowInsets) = insets
}