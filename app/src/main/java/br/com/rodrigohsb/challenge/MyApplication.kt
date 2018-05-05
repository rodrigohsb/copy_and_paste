package br.com.rodrigohsb.challenge

import android.app.Application
import br.com.rodrigohsb.challenge.di.Injector
import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.KodeinAware
import com.github.salomonbrys.kodein.conf.ConfigurableKodein

/**
 * @rodrigohsb
 */
class MyApplication: Application(), KodeinAware {

    override val kodein = ConfigurableKodein(mutable = true)

    override fun onCreate() {
        super.onCreate()
        kodein.addImport(appDependencies(), true)
    }

    private fun appDependencies(): Kodein.Module {
        return Injector(this).dependencies
    }
}