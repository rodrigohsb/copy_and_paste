package br.com.rodrigohsb.joao_challenge

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.test.InstrumentationRegistry
import android.support.test.espresso.intent.Intents
import android.support.test.filters.LargeTest
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import br.com.rodrigohsb.challenge.asApp
import com.github.salomonbrys.kodein.Kodein
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith

/**
 * @rodrigohsb
 */
@LargeTest
@RunWith(AndroidJUnit4::class)
abstract class AcceptanceTest<T : Activity>(clazz: Class<T>){

    val server = MockWebServer()

    @Rule
    @JvmField
    val testRule: ActivityTestRule<T> = ActivityTestRule(clazz, true, false)

    @Before
    fun setup() {
        val app = InstrumentationRegistry.getInstrumentation().targetContext.asApp()
        app.resetInjection()
        app.addModule(testDependencies)
        Intents.init()
        server.start()
    }

    protected fun startActivity(args: Bundle = Bundle()): T {
        val intent = Intent()
        intent.putExtras(args)
        return testRule.launchActivity(intent)
    }

    fun getJson(fileName : String) = FileHandler().invoke(fileName)

    abstract val testDependencies: Kodein.Module

    @After
    fun tearDown(){
        Intents.release()
        server.shutdown()
    }

}