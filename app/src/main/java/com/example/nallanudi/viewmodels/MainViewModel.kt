package com.example.nallanudi.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nallanudi.database.WordDao
import com.example.nallanudi.models.SavedWord
import com.example.nallanudi.models.Word
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MainViewModel(
    private val wordDao: WordDao
) : ViewModel() {

    private val _userName = MutableStateFlow("Learner")
    val userName: StateFlow<String> = _userName.asStateFlow()

    private val _userAvatar = MutableStateFlow("Student")
    val userAvatar: StateFlow<String> = _userAvatar.asStateFlow()

    private val _isDarkMode = MutableStateFlow<Boolean?>(null) // null means follow system
    val isDarkMode: StateFlow<Boolean?> = _isDarkMode.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedSubject = MutableStateFlow("Science")
    val selectedSubject: StateFlow<String> = _selectedSubject.asStateFlow()

    private val _searchResults = MutableStateFlow<List<Word>>(emptyList())
    val searchResults: StateFlow<List<Word>> = _searchResults.asStateFlow()

    private val _filteredWords = MutableStateFlow<List<Word>>(emptyList())
    val filteredWords: StateFlow<List<Word>> = _filteredWords.asStateFlow()

    val savedWords = wordDao.getSavedWords().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    private val _wordOfTheDay = MutableStateFlow<Word?>(null)
    val wordOfTheDay: StateFlow<Word?> = _wordOfTheDay.asStateFlow()

    init {
        loadWordOfTheDay()
        updateFilteredWords()
    }

    fun updateProfile(name: String, avatar: String) {
        _userName.value = name
        _userAvatar.value = avatar
    }

    fun toggleDarkMode(enabled: Boolean?) {
        _isDarkMode.value = enabled
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
        performGlobalSearch()
    }

    fun updateSubject(subject: String) {
        _selectedSubject.value = subject
        updateFilteredWords()
    }

    private fun performGlobalSearch() {
        viewModelScope.launch {
            val query = _searchQuery.value
            if (query.isEmpty()) {
                _searchResults.value = emptyList()
            } else {
                _searchResults.value = wordDao.searchWords(query).first()
            }
        }
    }

    private fun updateFilteredWords() {
        viewModelScope.launch {
            val subject = _selectedSubject.value
            _filteredWords.value = wordDao.getWordsBySubject(subject).first()
        }
    }

    fun saveWord(word: Word) {
        viewModelScope.launch {
            wordDao.saveWord(SavedWord(word.id))
        }
    }

    fun removeSavedWord(word: Word) {
        viewModelScope.launch {
            wordDao.removeSavedWord(SavedWord(word.id))
        }
    }

    private fun loadWordOfTheDay() {
        viewModelScope.launch {
            _wordOfTheDay.value = wordDao.getRandomWord()
        }
    }
}