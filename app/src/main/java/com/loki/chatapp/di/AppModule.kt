package com.loki.chatapp.di

import android.content.Context
import androidx.room.Room
import com.google.firebase.auth.FirebaseAuth
import com.loki.chatapp.data.local.dao.SettingsDao
import com.loki.chatapp.data.local.database.AppDatabase
import com.loki.chatapp.data.repository.AuthRepositoryImp
import com.loki.chatapp.data.repository.ChatRepository
import com.loki.chatapp.domain.repository.AuthRepository
import com.loki.chatapp.domain.usecase.ListenMessagesUseCase
import com.loki.chatapp.domain.usecase.LoginUseCase
import com.loki.chatapp.domain.usecase.SendMessageUseCase
import com.loki.chatapp.domain.usecase.SignupUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    fun provideAuthRepository(auth: FirebaseAuth): AuthRepository{
        return AuthRepositoryImp(auth)
    }
    @Provides
    fun provideLoginUseCase(repo: AuthRepository) = LoginUseCase(repo)

    @Provides
    fun provideSignupUseCase(repo: AuthRepository) = SignupUseCase(repo)

    @Provides
    fun provideChatRepository()= ChatRepository()

    @Provides
    fun providesSendMessageUseCase(repo: ChatRepository)= SendMessageUseCase(repo)

    @Provides
    fun provideListenMessageUseCase(repo: ChatRepository)= ListenMessagesUseCase(repo)

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): AppDatabase{
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "settings_db"
        ).build()
    }

    @Provides
    fun provideSettingsDao(database: AppDatabase): SettingsDao{
        return database.settingsDao()
    }


}