package com.valter.marvelcomics.ui.details.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.valter.marvelcomics.data.database.entity.Comic
import com.valter.marvelcomics.data.repository.MarvelRepository
import com.valter.marvelcomics.dispatchers.DispatchersContainer
import com.valter.marvelcomics.ui.details.base.ComicDetailAction
import com.valter.marvelcomics.utils.Outcome
import com.valter.marvelcomics.utils.launchSafely

class ComicDetailsViewModel(
        private val repository: MarvelRepository,
        private val dispatchersContainer: DispatchersContainer,
        private val comicId: String
) : ViewModel() {

    private val _action = MutableLiveData<Outcome<ComicDetailAction>>()
    internal val action: LiveData<Outcome<ComicDetailAction>> = _action

    private val _selectedComic = MutableLiveData<Outcome<Comic>>()
    internal val selectedComic: LiveData<Outcome<Comic>> = _selectedComic

    init {
        fetchComicData(comicId)
    }

    private fun fetchComicData(comicId: String) {
        viewModelScope.launchSafely(
                _selectedComic,
                loading = true,
                context = dispatchersContainer.IO
        ) {
            repository.fetchComic(comicId)
        }
    }

    /**
     * Action to move to the next page in the Comic Details.
     */
    fun openNext() {
        _action.value = Outcome.success(ComicDetailAction.Next)
    }

    fun retry() {
        fetchComicData(comicId)
    }
}