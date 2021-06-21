package com.onething.a2appstudiotest.features.links

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.onething.a2appstudiotest.model.Links
import com.onething.a2appstudiotest.repository.LinksRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class LinksStatus { LOADING, ERROR, DONE }

enum class LinksSorting { ASC, DESC }

@HiltViewModel
class LinksViewModel @Inject constructor(
    private val linksRepository: LinksRepository
) : ViewModel() {

    private var sorting: LinksSorting? = null

    private val _status = MutableLiveData<LinksStatus>()
    val status: LiveData<LinksStatus>
        get() = _status

    private val _list = MutableLiveData<List<Links>>()
    val list : LiveData<List<Links>>
        get() = _list

    fun processLinks(url: String) {
        viewModelScope.launch {
            _status.value = LinksStatus.LOADING
            linksRepository.getLinks(url)
                .either({
                    _status.value = LinksStatus.ERROR
                }, {
                    _status.value = LinksStatus.DONE
                    addItem(it)
                })
        }
    }

    private fun addItem(links: Links) {
        val newList = _list.value?.toMutableList() ?: mutableListOf()
        if (!newList.contains(links)) {
            newList.add(links)
            resetList(newList)
        }
    }

    private fun resetList(list: List<Links>) {
        _list.postValue(list)
    }

    fun shuffle() {
        sorting = null
        val oldList: List<Links> = _list.value?.toMutableList() ?: emptyList()
        val newList = oldList.shuffled()
        resetList(newList)
    }

    fun sort() {
        val oldList: List<Links> = _list.value?.toMutableList() ?: emptyList()
        val newList: List<Links>
        if (sorting == null || sorting == LinksSorting.DESC) {
            newList = oldList.sortedBy { it.title }
            sorting = LinksSorting.ASC
        } else {
            newList = oldList.sortedByDescending { it.title }
            sorting = LinksSorting.DESC
        }
        resetList(newList)
    }

    fun multipleDelete(deleteList: MutableList<Links>) {
        val list: MutableList<Links> = _list.value?.toMutableList() ?: mutableListOf()
        if (list.removeAll(deleteList)){
            resetList(list)
        }
    }

    fun delete(position: Int) {
        val list: MutableList<Links> = _list.value?.toMutableList() ?: mutableListOf()
        list.remove(list[position])
        resetList(list)
    }

}