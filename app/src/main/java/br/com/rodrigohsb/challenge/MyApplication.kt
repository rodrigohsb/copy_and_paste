package br.com.rodrigohsb.challenge

import android.app.Application
import android.content.Context
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
        resetInjection()
    }

    fun addModule(activityModules: Kodein.Module) {
        kodein.addImport(activityModules, true)
    }

    fun resetInjection() {
        kodein.clear()
        kodein.addImport(appDependencies(), true)
    }

    private fun appDependencies() = Injector(this).dependencies
}
fun Context.asApp() = this.applicationContext as MyApplication