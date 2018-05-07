package br.com.rodrigohsb.joao_challenge.robots

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.test.espresso.matcher.ViewMatchers.withId
import br.com.rodrigohsb.challenge.robots.*
import br.com.rodrigohsb.joao_challenge.R

/**
 * @rodrigohsb
 */
fun robot(func: HomeRobot.() -> Unit) = HomeRobot().apply{func()}

class HomeRobot {

    fun withLoading() = onView(withId(R.id.progress)).check(matches(isDisplayed()))

    infix fun withContent(func: RobotContentResult.() -> Unit) =
            RobotContentResult().apply{ func() }

    infix fun withBadRequest(func: RobotBadRequestResult.() -> Unit) =
            RobotBadRequestResult().apply{ func() }

    infix fun withEmptyState(func: RobotEmptyResult.() -> Unit) =
            RobotEmptyResult().apply{ func() }

    infix fun withGenericError(func: RobotGenericErrorResult.() -> Unit) =
            RobotGenericErrorResult().apply{ func() }

    infix fun withClientError(func: RobotClientErrorResult.() -> Unit) =
            RobotClientErrorResult().apply{ func() }

    infix fun withServerError(func: RobotServerErrorResult.() -> Unit) =
            RobotServerErrorResult().apply{ func() }

    infix fun withTimeout(func: RobotTimeoutResult.() -> Unit) =
            RobotTimeoutResult().apply{ func() }

    infix fun withoutConnection(func: RobotNoNetworkResult.() -> Unit) =
            RobotNoNetworkResult().apply{ func() }

}