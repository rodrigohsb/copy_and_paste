package br.com.rodrigohsb.joao_challenge.home

import br.com.rodrigohsb.challenge.AcceptanceTest
import br.com.rodrigohsb.challenge.RequestInterceptorMock
import br.com.rodrigohsb.challenge.di.Injector
import br.com.rodrigohsb.challenge.ui.MainActivity
import br.com.rodrigohsb.challenge.webservice.exceptions.BadRequestException
import br.com.rodrigohsb.joao_challenge.robots.robot
import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.bind
import com.github.salomonbrys.kodein.provider
import okhttp3.Interceptor
import org.junit.Test

/**
 * @rodrigohsb
 */
class HomeActivityBadRequestTest : AcceptanceTest<MainActivity>(MainActivity::class.java) {

    @Test
    fun testWithBadRequestState() {

        startActivity()

        robot {
            withLoading()
        } withBadRequest {
            errorHasBeenShown()
        }
    }

    override val testDependencies = Kodein.Module(allowSilentOverride = true) {
        bind<Interceptor>(tag = Injector.REQUEST_INTERCEPTOR,overrides = true) with provider {
            RequestInterceptorMock(BadRequestException())
        }
    }
}