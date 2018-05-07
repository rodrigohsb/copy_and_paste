package br.com.rodrigohsb.joao_challenge.robots

import android.support.test.espresso.Espresso.onView

import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.*
import org.hamcrest.Matchers.not
import br.com.rodrigohsb.joao_challenge.R

/**
 * @rodrigohsb
 */
class RobotServerErrorResult {

    fun errorHasBeenShown(): Boolean{
        onView(withText(R.string.server_error_message)).check(matches(isDisplayed()))

        onView(withId(R.id.errorButton)).check(matches(not((isDisplayed()))))
        onView(withText(R.string.try_again)).check(matches(not((isDisplayed()))))

        return true
    }
}