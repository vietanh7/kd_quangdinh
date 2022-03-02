package com.example.android.kdquangdinh.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.example.android.kdquangdinh.util.Constants.Companion.PREFERENCES_NAME
import com.example.android.kdquangdinh.util.Constants.Companion.PREFERENCES_TOKEN
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

private val Context.dataStore by preferencesDataStore(PREFERENCES_NAME)

@ViewModelScoped
class DataStoreRepository @Inject constructor(@ApplicationContext private val context: Context)  {

    private object PreferenceKeys {
        val token = stringPreferencesKey(PREFERENCES_TOKEN)
    }

    private val dataStore: DataStore<Preferences> = context.dataStore

    suspend fun saveToken(
        token: String?
    ) {
        dataStore.edit { preferences ->
            preferences[PreferenceKeys.token] = token ?: ""
        }
    }

    val readToken: Flow<String> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map {preferences ->
            val token = preferences[PreferenceKeys.token] ?: ""
            token
        }
}