package com.example.paging3sampleapp.util

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.paging3sampleapp.app.AppConstants
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class AppConfig @Inject constructor(private val dataStore: DataStore<Preferences>) {

    private val queryPreferenceKey = stringPreferencesKey("query")

    suspend fun getQueryParam(): String {
        return dataStore.data.map {
            it[queryPreferenceKey] ?: AppConstants.DEFAULT_QUERY_PARAM_TYPE
        }.first()
    }

    suspend fun setQueryParam(query: String) {
        dataStore.edit {
            it[queryPreferenceKey] = query
        }
    }

}