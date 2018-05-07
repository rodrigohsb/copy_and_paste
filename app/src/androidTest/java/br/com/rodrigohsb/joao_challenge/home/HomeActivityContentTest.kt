package br.com.rodrigohsb.joao_challenge.home

import br.com.rodrigohsb.challenge.ui.MainActivity
import br.com.rodrigohsb.joao_challenge.robots.robot
import br.com.rodrigohsb.joao_challenge.AcceptanceTest
import com.github.salomonbrys.kodein.Kodein
import okhttp3.mockwebserver.MockResponse
import org.junit.Test

/**
 * @rodrigohsb
 */
class HomeActivityContentTest : AcceptanceTest<MainActivity>(MainActivity::class.java) {

    @Test
    fun testWithContentState() {

        server.enqueue(MockResponse()
                .setResponseCode(200)
                .setBody(getJson("success.json")))

        startActivity()

        robot{
            withLoading()
        } withContent {
            isSuccess()
        }
    }
    override val testDependencies = Kodein.Module(allowSilentOverride = true) {}
}