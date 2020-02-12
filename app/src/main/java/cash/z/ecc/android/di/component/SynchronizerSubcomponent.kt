package cash.z.ecc.android.di.component

import androidx.lifecycle.ViewModelProvider
import cash.z.ecc.android.di.annotation.SynchronizerScope
import cash.z.ecc.android.di.module.SynchronizerModule
import cash.z.wallet.sdk.Initializer
import cash.z.wallet.sdk.Synchronizer
import dagger.BindsInstance
import dagger.Subcomponent
import javax.inject.Named

@SynchronizerScope
@Subcomponent(modules = [SynchronizerModule::class])
interface SynchronizerSubcomponent {

    fun synchronizer(): Synchronizer

    @Named("Synchronizer") fun viewModelFactory(): ViewModelProvider.Factory

    @Subcomponent.Factory
    interface Factory {
        fun create(@BindsInstance initializer: Initializer): SynchronizerSubcomponent
    }
}